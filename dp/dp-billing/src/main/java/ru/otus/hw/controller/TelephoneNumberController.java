package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.TariffPlanService;

@RequiredArgsConstructor
@Controller
public class TelephoneNumberController {

    private final TariffPlanService tariffPlanService;

    //http://localhost:8080/telephoneNumberEdit
    //http://localhost:8080/telephoneNumberAdd

    @GetMapping("/telephoneNumberEdit")
    public String editTelephoneNumber(Model model) {
        User object = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean roleAdmin = object.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("roleAdmin", roleAdmin);
        model.addAttribute("tariffPlans", tariffPlanService.findAll());
        return "telephoneNumberEdit";
    }

    @GetMapping("/telephoneNumberAdd")
    public String addTelephoneNumber(Model model) {
        model.addAttribute("tariffPlans", tariffPlanService.findAll());
        return "telephoneNumberAdd";
    }
}