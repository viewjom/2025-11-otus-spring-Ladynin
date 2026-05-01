package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.ContractDto;

import java.util.List;
import ru.otus.hw.dto.ContractEditDto;
import ru.otus.hw.services.ContractService;

@RestController
@RequiredArgsConstructor
public class ContractRestController {

    private final ContractService contractService;

    @GetMapping("/api/contracts")
    public List<ContractDto> getAll() {
            return contractService.findAll();
    }

    @GetMapping("/api/contracts/{id}")
    public ContractEditDto getById(@PathVariable long id) {
        return contractService.findById(id);
    }

    @PostMapping("/api/contracts")
    public ResponseEntity<ContractDto> addContract(@Valid @RequestBody ContractDto contractDto) {
        var savedContractDto = contractService.update(contractDto.getId(),
                contractDto.getContractNumber(),
                contractDto.getName(),
                contractDto.getAddress());
        return ResponseEntity.ok(savedContractDto);
    }

    @DeleteMapping("/api/contracts/{id}")
    public void delete(@PathVariable long id) {
        contractService.deleteById(id);
    }
}