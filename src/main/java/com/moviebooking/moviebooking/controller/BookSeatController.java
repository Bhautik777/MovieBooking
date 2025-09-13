package com.moviebooking.moviebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.scheduling.annotation.Scheduled;

import com.moviebooking.moviebooking.constant.AppConstants;
import com.moviebooking.moviebooking.services.BookingSeatsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/bookSeat")
public class BookSeatController {

    private final BookingSeatsService bookingSeatsService;

    public BookSeatController(BookingSeatsService bookingSeatsService) {
        this.bookingSeatsService = bookingSeatsService;
    }

   /*  //This is a scheduled task that will cancel the booking of pending seats after 10 minutes
    @Scheduled(fixedDelay = AppConstants.CANCEL_BOOKING_EXPIRY_TIME)
    public void cancelBookingOfPending() {
        log.info("Cancelling booking");
        bookingSeatsService.cancelBookingOfPending();
    } */
}
