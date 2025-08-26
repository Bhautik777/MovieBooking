package com.moviebooking.moviebooking.exceptionhandler.showexception;

import jakarta.servlet.http.HttpServletRequest;

import static com.moviebooking.moviebooking.urlpaths.MoviePaths.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ShowExceptionHandler {

    // This exception is thrown when no shows are found for a movie
    @ExceptionHandler(ShowException.NoShowsFoundForMovieException.class)
    public String handleNoShowsFoundForMovieException(ShowException.NoShowsFoundForMovieException ex,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:" + MOVIE_BASE_PATH + MOVIE_LIST_BY_CITY_PAGE_URL;
    }

}
