package com.moviebooking.moviebooking.exceptionhandler.bookingexception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.moviebooking.moviebooking.urlpaths.MoviePaths.*;
import static com.moviebooking.moviebooking.urlpaths.ShowsPaths.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class BookingExceptionHandler {

    // This exception is thrown when booking is not found
    @ExceptionHandler(BookingException.BookingNotFoundException.class)
    public String handleBookingNotFoundException(BookingException.BookingNotFoundException ex,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:" + MOVIE_BASE_PATH + MOVIE_LIST_BY_CITY_PAGE_URL;
    }

    // This exception is thrown when seats are already booked
    @ExceptionHandler(BookingException.SeatsAlreadyBookedException.class)
    public String handleSeatsAlreadyBookedException(BookingException.SeatsAlreadyBookedException ex,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:" + SHOW_BASE_PATH + SHOW_SEAT_SELECTION_PAGE_URL_WITHOUT_SHOW_ID + "/"
                + BookingException.getShowId();
    }

}
