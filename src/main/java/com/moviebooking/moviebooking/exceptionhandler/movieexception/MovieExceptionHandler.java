package com.moviebooking.moviebooking.exceptionhandler.movieexception;

import jakarta.servlet.http.HttpServletRequest;

import static com.moviebooking.moviebooking.urlpaths.AuthPaths.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moviebooking.moviebooking.registry.AdminMovieHandlerRegistry;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class MovieExceptionHandler {

    private final AdminMovieHandlerRegistry adminMovieHandlerRegistry;

    public MovieExceptionHandler(AdminMovieHandlerRegistry adminMovieHandlerRegistry) {
        this.adminMovieHandlerRegistry = adminMovieHandlerRegistry;
    }

    @ExceptionHandler(MovieException.AdminMovieNotFoundException.class)
    public String handleAdminMovieNotFoundException(MovieException.AdminMovieNotFoundException ex,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        String redirectURI = adminMovieHandlerRegistry.checkMatchingURI(request.getRequestURI(), redirectAttributes);

        if (redirectURI != null) {
            return redirectURI;
        }

        return "redirect:" + AUTH_BASE + LOGIN_PAGE_URL;
    }


}
