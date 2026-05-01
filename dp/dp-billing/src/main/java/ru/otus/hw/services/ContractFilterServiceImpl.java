package ru.otus.hw.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Contract;
import ru.otus.hw.repositories.ContractRepository;

@RequiredArgsConstructor
@Service
public class ContractFilterServiceImpl implements ContractFilterService {

    private final ContractRepository contractRepository;

    /** Для включения @PostFilter добавить сервис фильтрации  ContractFilterServiceImpl
     * в SecurityConfiguration добавить Аннотации
     * @Configuration
     * @EnableGlobalMethodSecurity(prePostEnabled = true)
     */
    @Override
    @PostFilter("filterObject.contractNumber.equals(authentication.name)  || hasRole('ADMIN')")
    public List<Contract> findAll() {
        return contractRepository.findAll();
    }
}
