package ru.otus.hw.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.ContractService;

@RequiredArgsConstructor
@Controller
public class ContractController {

    private final ContractService contractService;


    //http://localhost:8080/book
    @GetMapping({"/", "/contracts"})
    public String findAllContracts(Model model, HttpSession session) {
        User object = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean roleAdmin = object.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("roleAdmin", roleAdmin);
        return "contractList";
    }

    @GetMapping("/contractEdit")
    public String editContract(Model model, HttpSession session) {
        User object = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean roleAdmin = object.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("roleAdmin", roleAdmin);
        return "contractEdit";
    }

    @GetMapping("/contractAdd")
    public String addContract(Model model) {
        return "contractAdd";
    }

}