package com.moviebooking.moviebooking.services;

import org.springframework.stereotype.Service;
import com.moviebooking.moviebooking.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PhoneAuthService {

    private final UserService userService;

    public PhoneAuthService(UserService userService) {
        this.userService = userService;
    }

    // This method is used to login with phone number
    public SecurityContext loginWithPhone(String phoneNumber) {

        // if user is not found then create a new user and save it to the database
        User user = userService.getUserByPhoneNumber(phoneNumber);
        if (user == null) {
            user = userService.saveUser(phoneNumber);
        }

        // here we are storing the user phone number in principal
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getPhoneNumber(), // principal stored in session
                null, // no password
                Collections.emptyList());// no

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        return context;
    }

    public String getCurrentLoggedInPhoneNumber() {
        // get the authentication from the security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // if the authentication is not null and is authenticated and not anonymous user
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            // get the principal from the authentication
            Object principal = auth.getPrincipal();

            if (principal instanceof String) {
                // return the principal as string
                return (String) principal;
            }
        }
        return "";
    }
}
