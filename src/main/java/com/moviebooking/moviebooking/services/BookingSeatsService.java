package com.moviebooking.moviebooking.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.moviebooking.moviebooking.constant.AppConstants;
import com.moviebooking.moviebooking.model.Booking;
import com.moviebooking.moviebooking.model.BookingSeat;
import com.moviebooking.moviebooking.model.Seat;
import com.moviebooking.moviebooking.model.BookingSeat.SeatStatus;
import com.moviebooking.moviebooking.repository.BookingSeatsRepository;
import com.moviebooking.moviebooking.services.cachefiles.BookingSeatsCacheService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingSeatsService {

    private final BookingSeatsRepository bookingSeatsRepository;
    private final BookingSeatsCacheService bookingSeatsCacheService;

    public BookingSeatsService(BookingSeatsRepository bookingSeatsRepository,
            BookingSeatsCacheService bookingSeatsCacheService) {
        this.bookingSeatsRepository = bookingSeatsRepository;
        this.bookingSeatsCacheService = bookingSeatsCacheService;
    }

    public List<BookingSeat> createBookingSeats(Booking booking, List<Seat> selectedSeats) {

        List<BookingSeat> bookingSeats = new ArrayList<>();
        for (Seat seat : selectedSeats) {
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(booking);
            bookingSeat.setSeat(seat);
            bookingSeat.setStatus(SeatStatus.PENDING);
            bookingSeat.setLockedAt(LocalDateTime.now());
            bookingSeats.add(bookingSeat);
        }
        return bookingSeats;
    }

    public void saveBookingSeats(List<BookingSeat> bookingSeats) {
        bookingSeatsCacheService.saveAllBookingSeats(bookingSeats);
    }

    public void cancelBookingOfPending() {
        // get the expired pending seats from the database
        List<BookingSeat> bookingSeats = bookingSeatsRepository
                .findExpiredPendingSeats(LocalDateTime.now().minusSeconds(AppConstants.BOOKING_EXPIRY_TIME));

        // set the status of the booking seats to cancelled
        bookingSeats.stream().forEach(bookingSeat -> {
            bookingSeat.setStatus(SeatStatus.CANCELLED);
            log.info("Booking seat cancelled: {}", bookingSeat.getSeat().getSeatNumber());
        });
        // save the booking seats to the database
        bookingSeatsCacheService.saveAllBookingSeats(bookingSeats);
        log.info("Booking seats cancelled successfully: {}", bookingSeats.size());
    }

    public List<BookingSeat> findBookingSeatsByIds(Long showId, List<Long> seatIds) {
        return bookingSeatsRepository.findBookingSeatsByIds(showId, seatIds);
    }
}
