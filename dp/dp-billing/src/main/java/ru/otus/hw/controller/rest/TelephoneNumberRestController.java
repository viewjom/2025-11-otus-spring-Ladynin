package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.TelephoneNumberDto;
import ru.otus.hw.services.TelephoneNumberService;

@RestController
@RequiredArgsConstructor
public class TelephoneNumberRestController {

    private final TelephoneNumberService telephoneNumberService;

    @GetMapping("/api/tels/{id}")
    public TelephoneNumberDto findAllByContractId(@PathVariable long id) {
        return telephoneNumberService.findById(id);
    }

    @PostMapping("/api/tels")
    public ResponseEntity<TelephoneNumberDto> addTelephoneNumber(
            @Valid @RequestBody TelephoneNumberDto telephoneNumberDto) {
        var savedTelephoneNumberDto = telephoneNumberService.update(telephoneNumberDto.getId(),
                telephoneNumberDto.getTariffPlan().getId(),
                telephoneNumberDto.getNumber(),
                telephoneNumberDto.getContractId());
        return ResponseEntity.ok(savedTelephoneNumberDto);
    }

    @DeleteMapping("/api/tels/{id}")
    public void delete(@PathVariable long id) {
        telephoneNumberService.deleteById(id);
    }
}
