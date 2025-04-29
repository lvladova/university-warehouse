package com.warehouse.universitywarehouse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isViceChancellor = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_VICE_CHANCELLOR"));
            boolean isAcademicHead = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ACADEMIC_HEAD"));
            boolean isAdmissionsDirector = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMISSIONS_DIRECTOR"));
            boolean isFinanceDirector = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_FINANCE_DIRECTOR"));
            boolean isRetentionOfficer = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_RETENTION_OFFICER"));
            boolean isAdmin = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            model.addAttribute("isViceChancellor", isViceChancellor);
            model.addAttribute("isAcademicHead", isAcademicHead);
            model.addAttribute("isAdmissionsDirector", isAdmissionsDirector);
            model.addAttribute("isFinanceDirector", isFinanceDirector);
            model.addAttribute("isRetentionOfficer", isRetentionOfficer);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("username", authentication.getName());
        }

        return "dashboard";
    }

    @GetMapping("/enrollment-report")
    public String enrollmentReport(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "enrollment-report";
    }

    @GetMapping("/performance-report")
    public String performanceReport(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "performance-report";
    }

    @GetMapping("/admissions-report")
    public String admissionsReport(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "admissions-report";
    }

    @GetMapping("/finance-report")
    public String financeReport(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "finance-report";
    }

    @GetMapping("/etl-management")
    public String etlManagement(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "etl-management";
    }
}