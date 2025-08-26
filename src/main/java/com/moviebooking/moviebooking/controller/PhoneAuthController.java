package com.moviebooking.moviebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

import static com.moviebooking.moviebooking.urlpaths.AuthPaths.*;
import static com.moviebooking.moviebooking.urlpaths.CitySelectionPaths.*;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.moviebooking.moviebooking.services.PhoneAuthService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(AUTH_BASE)
@Slf4j
public class PhoneAuthController {

    private final PhoneAuthService phoneAuthService;

    public PhoneAuthController(PhoneAuthService phoneAuthService) {
        this.phoneAuthService = phoneAuthService;
    }

    @GetMapping(PHONE_LOGIN_PAGE_URL)
    public String showPhoneLoginEntryPage() {
        log.info("Showing phone entry page");
        return PHONE_LOGIN_ENTRY_PAGE;
    }

    @PostMapping(PHONE_LOGIN_URL)
    public String loginWithPhone(@RequestParam String phoneNumber, HttpServletRequest request) {
        SecurityContext context = phoneAuthService.loginWithPhone(phoneNumber);

        // store in session
        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context);

        return "redirect:" + CITY_BASE_PATH + CITY_SELECTION_PAGE_URL;
    }
}
