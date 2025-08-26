package com.moviebooking.moviebooking.controller;

import static com.moviebooking.moviebooking.urlpaths.BookingPaths.*;
import static com.moviebooking.moviebooking.urlpaths.MoviePaths.*;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.moviebooking.moviebooking.model.Booking;
import com.moviebooking.moviebooking.model.User;
import com.moviebooking.moviebooking.model.dto.bookingdto.BookingRequestDto;
import com.moviebooking.moviebooking.model.dto.bookingdto.BookingResponseDto;
import com.moviebooking.moviebooking.services.BookingService;
import com.moviebooking.moviebooking.services.PhoneAuthService;
import com.moviebooking.moviebooking.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(BOOKING_BASE_PATH)
@Slf4j
public class BookingController {

    private final BookingService bookingService;
    private final PhoneAuthService phoneAuthService;
    private final UserService userService;

    public BookingController(BookingService bookingService, PhoneAuthService phoneAuthService,
            UserService userService) {
        this.bookingService = bookingService;
        this.phoneAuthService = phoneAuthService;
        this.userService = userService;
    }

    @PostMapping(BOOK_SEATS_URL)
    public String bookSeats(@ModelAttribute BookingRequestDto bookingRequestDto, Model model)
            throws InterruptedException {

        // Process the booking details not a payment
        BookingResponseDto bookingResponse = bookingService.bookSeats(bookingRequestDto);
        // Add the response to the model
        model.addAttribute("booking", bookingResponse);
        model.addAttribute("phoneNumber", phoneAuthService.getCurrentLoggedInPhoneNumber());
        log.info("Booking processed successfully: {}", bookingResponse);

        // Return the booking details page
        return BOOKING_DETAILS_PAGE;
    }

    @GetMapping(BOOKING_HISTORY_PAGE_URL)
    public String getBookingHistory(Model model) {
        log.info("Getting booking history");
        // Get the curretn logged in phone number
        String phoneNumber = phoneAuthService.getCurrentLoggedInPhoneNumber();
        // If phone number is empty, redirect to movie list page means something went

        if (phoneNumber.isEmpty()) {
            return "redirect:" + MOVIE_BASE_PATH + MOVIE_LIST_BY_CITY_PAGE_URL;
        }
        // Get the user by phone number
        User user = userService.getUserByPhoneNumber(phoneNumber);

        // Get the bookings by user id
        List<Booking> bookings = bookingService.getConfirmedBookingsByUserId(user.getId());
        if (bookings.isEmpty()) {
            model.addAttribute("message", "No bookings found for this phone number");
        } else {
            // Add the bookings to the model
            model.addAttribute("bookings", bookings);
        }

        model.addAttribute("phoneNumber", phoneNumber);

        return BOOKING_HISTORY_PAGE;
    }
}
