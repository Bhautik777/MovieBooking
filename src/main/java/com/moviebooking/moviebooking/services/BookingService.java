package com.moviebooking.moviebooking.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import com.moviebooking.moviebooking.model.Booking;
import com.moviebooking.moviebooking.model.BookingSeat;
import com.moviebooking.moviebooking.model.Seat;
import com.moviebooking.moviebooking.model.Show;
import com.moviebooking.moviebooking.model.Booking.BookingStatus;
import com.moviebooking.moviebooking.model.dto.bookingdto.BookingRequestDto;
import com.moviebooking.moviebooking.model.dto.bookingdto.BookingResponseDto;
import com.moviebooking.moviebooking.repository.BookingRepository;
import com.moviebooking.moviebooking.services.cachefiles.BookingCacheService;
import com.moviebooking.moviebooking.exceptionhandler.bookingexception.BookingException;

@Service
@Slf4j
public class BookingService {

        private ShowService showService;
        private BookingSeatsService bookingSeatsService;
        private BookingRepository bookingRepository;
        private BookingCacheService bookingCacheService;

        private final ConcurrentHashMap<String, ReentrantLock> bookingSameSeatsLock = new ConcurrentHashMap<>();
        private static final long LOCK_WAIT_SECONDS = 3;

        public BookingService(ShowService showService, BookingSeatsService bookingSeatsService,
                        BookingRepository bookingRepository, BookingCacheService bookingCacheService) {
                this.showService = showService;
                this.bookingSeatsService = bookingSeatsService;
                this.bookingRepository = bookingRepository;
                this.bookingCacheService = bookingCacheService;
        }

        @Transactional
        public BookingResponseDto bookSeats(BookingRequestDto bookingRequestDto) {

                // get the seat id list from the booking request dto
                // we have to split the seat ids string by comma and then parse it to long (by
                // deffault 1,2,3,4,5,6,7,8,9,10)
                List<Long> seatIdList = Arrays.stream(bookingRequestDto.getSeatIdsString().split(","))
                                .map(String::trim)
                                .map(Long::parseLong)
                                .collect(Collectors.toList());
                bookingRequestDto.setSeatIds(seatIdList);

                // get the seat identifier list from the booking request dto
                // we have to split the seat identifiers string by comma and then parse it to
                // string (by deffault A1,A2,A3,A4,A5,A6,A7,A8,A9,A10)
                List<String> seatIdentifierList = Arrays.stream(bookingRequestDto.getSeatIdentifiersString().split(","))
                                .map(String::trim)
                                .collect(Collectors.toList());
                bookingRequestDto.setSeatIdentifiers(seatIdentifierList);

                try {
                        // acquire lock for same seats
                        // if the seats are already booked by another user then it will throw an
                        // exception
                        acquireLockForSameSeats(bookingRequestDto, seatIdList);

                        // Here we are checking that user selected seats are already booked or not
                        List<BookingSeat> alreadyBookedSeats = bookingSeatsService.findBookingSeatsByIds(
                                        bookingRequestDto.getShowId(),
                                        bookingRequestDto.getSeatIds());

                        // if the seats are already booked by another user then it will throw an
                        // exception
                        if (alreadyBookedSeats.size() > 0) {
                                throw new BookingException.SeatsAlreadyBookedException(
                                                "Seats are already booked by another user",
                                                bookingRequestDto.getShowId());
                        }

                        // get the show by required field
                        Show show = showService.getShowByRequiredField(bookingRequestDto);

                        // here we are getting the selected seats using real seat ids from
                        // frontend bcoz we want to add in booking seats table
                        List<Seat> selectedSeats = show.getScreen().getSeats().stream()
                                        .filter(seat -> bookingRequestDto.getSeatIds().contains(seat.getId()))
                                        .collect(Collectors.toList());

                        // create a new booking
                        Booking createBooking = new Booking();
                        createBooking.setBookingTime(LocalDateTime.now());
                        createBooking.setTotalAmount((double) (selectedSeats.size() * show.getPrice()));
                        createBooking.setBookingStatus(BookingStatus.PENDING);
                        createBooking.setShow(show);
                        // set the payment to null later we will add payment
                        createBooking.setPayment(null);
                        // set the user to null later we will add user
                        createBooking.setUser(null);

                        // create a new booking seats
                        List<BookingSeat> bookingSeats = bookingSeatsService.createBookingSeats(createBooking,
                                        selectedSeats);
                        // set the booking seats to the booking
                        createBooking.setBookingSeats(bookingSeats);
                        // save the booking to the database
                        bookingCacheService.saveBooking(createBooking);
                        // save the booking seats to the database
                        bookingSeatsService.saveBookingSeats(bookingSeats);
                        
                        //convert the booking to booking response dto
                        BookingResponseDto bookingResponseDto = new BookingResponseDto();
                        bookingResponseDto.setId(createBooking.getId());
                        bookingResponseDto.setTotalPrice((double) (selectedSeats.size() * show.getPrice()));
                        bookingResponseDto.setTheaterName(show.getTheater().getName());
                        bookingResponseDto.setMovieName(show.getMovie().getTitle());
                        bookingResponseDto.setShowDate(show.getShowDate().toString());
                        bookingResponseDto.setShowTime(show.getShowTime().toString());
                        bookingResponseDto.setScreenName(show.getScreen().getName());
                        bookingResponseDto.setSeats(bookingRequestDto.getSeatIdentifiers());

                        return bookingResponseDto;
                } catch (BookingException.SeatsAlreadyBookedException e) {
                        throw e;
                } finally {
                        releaseLockForSameSeats(bookingRequestDto, seatIdList);
                }
        }

        private void acquireLockForSameSeats(BookingRequestDto bookingRequestDto, List<Long> seatIdList) {
                try {
                        for (Long seatId : seatIdList) {
                                String key = bookingRequestDto.getShowId() + "-" + seatId;
                                ReentrantLock lock = bookingSameSeatsLock.computeIfAbsent(key,
                                                k -> new ReentrantLock(true));
                                boolean lockAcquired = false;

                                lockAcquired = lock.tryLock(LOCK_WAIT_SECONDS, TimeUnit.SECONDS);
                                if (!lockAcquired) {
                                        releaseLockForSameSeats(bookingRequestDto, seatIdList);
                                        throw new BookingException.SeatsAlreadyBookedException(
                                                        "Seats are already booked by another user",
                                                        bookingRequestDto.getShowId());

                                }
                        }
                } catch (InterruptedException e) {
                        releaseLockForSameSeats(bookingRequestDto, seatIdList);
                        throw new BookingException.SeatsAlreadyBookedException(
                                        "Seats are already booked by another user",
                                        bookingRequestDto.getShowId());
                }
        }

        private void releaseLockForSameSeats(BookingRequestDto bookingRequestDto, List<Long> seatIdList) {
                for (Long seatId : seatIdList) {
                        String key = bookingRequestDto.getShowId() + "-" + seatId;
                        ReentrantLock lock = bookingSameSeatsLock.get(key);
                        if (lock != null) {
                                lock.unlock();
                        }
                }
        }

        public List<Booking> getConfirmedBookingsByUserId(long userId) {
                return bookingCacheService.getConfirmedBookingsByUserId(userId);
        }

}
