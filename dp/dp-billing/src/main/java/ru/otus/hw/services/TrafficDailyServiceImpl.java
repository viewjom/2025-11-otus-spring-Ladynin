package ru.otus.hw.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.TrafficDailyDtoConverter;
import ru.otus.hw.dto.TrafficDailyDto;
import ru.otus.hw.exceptions.NotFoundCostException;
import ru.otus.hw.exceptions.NotFoundTelephoneNumberException;
import ru.otus.hw.models.Cost;
import ru.otus.hw.models.TelephoneNumber;
import ru.otus.hw.models.TrafficDaily;
import ru.otus.hw.repositories.ServiceRepository;
import ru.otus.hw.repositories.TelephoneNumberRepository;
import ru.otus.hw.repositories.TrafficDailyRepository;

@RequiredArgsConstructor
@Service
public class TrafficDailyServiceImpl implements TrafficDailyService {

    private final TrafficDailyRepository trafficDailyRepository;

    private final TelephoneNumberRepository telephoneNumberRepository;

    private final ServiceRepository serviceRepository;

    private final TrafficDailyDtoConverter trafficDailyDtoDtoConverter;

    @Transactional(readOnly = true)
    @Override
    public List<TrafficDailyDto> findAllByContractId(long contractId) {
        return trafficDailyRepository.findAllByContractId(contractId)
                .stream().map(trafficDailyDtoDtoConverter::getDto).toList();
    }

    @Transactional
    @Override
    public TrafficDailyDto create(LocalDateTime cdate, String anumber, String bnumber, double duration) {
        TelephoneNumber telephoneNumber = telephoneNumberRepository.findByNumber(anumber)
                .orElseThrow(() -> new NotFoundTelephoneNumberException("1"));
        Cost cost = getCost(telephoneNumber, bnumber);
        var trafficDaily = new TrafficDaily(0L,
                cdate,
                cost.getServices(),
                telephoneNumber,
                bnumber,
                duration,
                (duration / 60) * cost.getCost());
        trafficDailyRepository.save(trafficDaily);
        return trafficDailyDtoDtoConverter.getDto(trafficDaily);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        trafficDailyRepository.deleteById(id);
    }

    private Cost getCost(TelephoneNumber telephoneNumber, String bnumber) {
        try {
            Comparator<Cost> compareByTpt = Comparator
                    .comparing(Cost::getTpt)
                    .thenComparing(Cost::getTpt);
            List<Cost> costList = telephoneNumber.getTariffPlan()
                    .getCosts()
                    .stream()
                    .sorted(compareByTpt.reversed())
                    .collect(Collectors.toList());
            Cost cost = costList
                    .stream()
                    .filter(v -> bnumber.startsWith(v.getTpt())).collect(Collectors.toList())
                    .get(0);
            return cost;
        } catch (NotFoundTelephoneNumberException e) {
            throw new NotFoundCostException("2");
        }
    }
}