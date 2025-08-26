package com.moviebooking.moviebooking.controller;

import static com.moviebooking.moviebooking.urlpaths.AuthPaths.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

import com.moviebooking.moviebooking.model.dto.userdto.AdminCreateDto;
import com.moviebooking.moviebooking.services.AdminService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(AUTH_BASE)
@Slf4j
public class AuthController {

    private AdminService adminService;

    // Constructor injection
    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    // It will show login page so user can login
    @GetMapping(LOGIN_PAGE_URL)
    public String viewLogin() {
        log.info("Viewing login page");
        return LOGIN_PAGE;
    }

    // It will show register page so user can register
    @GetMapping(REGISTER_PAGE_URL)
    public String viewRegister(Model model) {
        model.addAttribute("admin", new AdminCreateDto());
        return REGISTER_PAGE;
    }

    // It will register admin to database
    @PostMapping(REGISTER_URL)
    public String registerAdmin(@Valid @ModelAttribute("admin") AdminCreateDto admin, BindingResult bindingResult,
            Model model) {

        // If i dont write this then if i will have validation error like username
        // should be more than 2 characters then it will not show the error message
        if (bindingResult.hasErrors()) {
            return REGISTER_PAGE;
        }

        log.info("Admin role : {}", admin.getRole());
        // Save admin to database
        boolean isSaved = adminService.saveAdmin(admin, bindingResult);

        if (bindingResult.hasErrors()) {
            // Keep the admin object with validation errors so they can be displayed
            return REGISTER_PAGE;
        }

        // If save is successful then redirect to login page
        if (isSaved) {
            return "redirect:" + AUTH_BASE + LOGIN_PAGE_URL;
        }

        // If save failed, add error message and return to register page
        model.addAttribute("error", "Registration failed. Please try again.");
        return REGISTER_PAGE;
    }

    // logout
    @GetMapping(LOGOUT_URL)
    public String logout(Model model) {
        return "redirect:" + AUTH_BASE + LOGIN_PAGE_URL;
    }
}
