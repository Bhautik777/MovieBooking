package com.moviebooking.moviebooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moviebooking.moviebooking.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b JOIN BookingSeat bs ON b.id = bs.booking.id WHERE b.id = :bookingId")
    Booking findBookingAndSeatsByBookingId(@Param("bookingId") Long bookingId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") long userId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.bookingStatus = 'CONFIRMED'")
    List<Booking> findConfirmedBookingByUserId(@Param("userId") long userId);

}
