package com.moviebooking.moviebooking.services;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.moviebooking.moviebooking.model.Payment;
import com.moviebooking.moviebooking.constant.AppConstants;
import com.moviebooking.moviebooking.model.Booking;
import com.moviebooking.moviebooking.model.BookingSeat;

import java.util.List;
import java.util.stream.Collectors;

import com.moviebooking.moviebooking.repository.BookingRepository;
import com.moviebooking.moviebooking.model.User;

import com.moviebooking.moviebooking.model.Booking.BookingStatus;
import com.moviebooking.moviebooking.model.dto.paymentdto.PaymentRequestDto;
import com.moviebooking.moviebooking.model.dto.paymentdto.PaymentResponseDto;
import com.moviebooking.moviebooking.model.PaymentResult;
import com.moviebooking.moviebooking.exceptionhandler.bookingexception.BookingException;
import com.moviebooking.moviebooking.services.cachefiles.BookingCacheService;
import com.moviebooking.moviebooking.services.cachefiles.PaymentCacheService;

@Service
@Slf4j
public class PaymentService {

    private final BookingRepository bookingRepository;
    private final BookingCacheService bookingCacheService;
    private final UserService userService;
    private final PaymentCacheService paymentCacheService;

    public PaymentService(BookingRepository bookingRepository,
            UserService userService, BookingCacheService bookingCacheService,
            PaymentCacheService paymentCacheService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.bookingCacheService = bookingCacheService;
        this.paymentCacheService = paymentCacheService;
    }

    @Transactional
    public PaymentResult processPayment(PaymentRequestDto paymentRequest) {
        log.info("Processing payment for booking ID: {}", paymentRequest.getBookingId());

        // Find the booking by ID
        Booking booking = bookingRepository.findBookingAndSeatsByBookingId(paymentRequest.getBookingId());
        // If booking is not found, throw exception
        if (booking == null) {
            throw new BookingException.BookingNotFoundException("Booking not found");
        }

        // if the booking is cancelled then return the payment result with null payment
        // response
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            log.info("Booking cancelled: {}", booking.getBookingStatus());
            return new PaymentResult(null, booking.getShow().getId());
        }

        // if the booking time is expired then set the booking status to cancelled and
        // return the payment result with null payment response
        if (booking.getBookingTime().isBefore(LocalDateTime.now().minusSeconds(AppConstants.BOOKING_EXPIRY_TIME))) {
            log.info("Booking expired: {}", booking.getBookingStatus());
            booking.setBookingStatus(BookingStatus.CANCELLED);
            bookingCacheService.saveBooking(booking);
            return new PaymentResult(null, booking.getShow().getId());
        }

        String phoneNumber = paymentRequest.getUserPhoneNumber();

        // Update user's phone number if provided
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            User user = userService.getUserByPhoneNumber(phoneNumber);
            if (user != null) {
                log.info("User found: {}", user.getPhoneNumber());
                // set the user to the booking
                booking.setUser(user);
            } else {
                userService.saveUser(phoneNumber);
                booking.setUser(userService.getUserByPhoneNumber(phoneNumber)); // This will create a new user if not
                                                                                // exists, or update if exists
            }
        }

        // Update booking status to CONFIRMED after payment
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        // Create response DTO
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setPaymentMode(paymentRequest.getPaymentMode());
        paymentResponseDto.setTotalPrice(booking.getTotalAmount());
        paymentResponseDto.setTheaterName(booking.getShow().getTheater().getName());
        paymentResponseDto.setMovieName(booking.getShow().getMovie().getTitle());
        paymentResponseDto.setShowDate(booking.getShow().getShowDate());
        paymentResponseDto.setShowTime(booking.getShow().getShowTime());
        paymentResponseDto.setScreenName(booking.getShow().getScreen().getName());

        List<String> seatName = booking.getBookingSeats().stream().map(BookingSeat::getSeat)
                .map(seat -> seat.getRow() + seat.getSeatNumber()).collect(Collectors.toList());

        paymentResponseDto.setSeats(seatName);

        Payment payment = createPayment(booking, paymentRequest);
        booking.setPayment(payment);

        // set the status of the booking seats to confirmed
        booking.getBookingSeats().stream().forEach(s -> s.setStatus(BookingSeat.SeatStatus.CONFIRMED));
        paymentCacheService.savePayment(payment);

        log.info("Payment processed successfully for booking: {}", booking.getBookingStatus());
        return new PaymentResult(paymentResponseDto, booking.getShow().getId());
    }

    private Payment createPayment(Booking booking, PaymentRequestDto paymentRequest) {
        Payment payment = new Payment();
        payment.setPaymentMode(Payment.PaymentMode.valueOf(paymentRequest.getPaymentMode().toUpperCase()));
        payment.setPaymentAmount(booking.getTotalAmount());
        payment.setBooking(booking);
        return payment;
    }

}
