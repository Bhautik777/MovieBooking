package com.moviebooking.moviebooking.repository;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moviebooking.moviebooking.model.BookingSeat;
import com.moviebooking.moviebooking.model.dto.seatdto.SeatAvailabilityDto;

public interface BookingSeatsRepository extends JpaRepository<BookingSeat, Long> {

        @Query("SELECT new com.moviebooking.moviebooking.model.dto.seatdto.SeatAvailabilityDto(bs.seat.id, bs.status) "
                        +
                        "FROM BookingSeat bs " +
                        "JOIN bs.booking b " +
                        "JOIN bs.seat s " +
                        "WHERE b.show.id = :showId " +
                        "AND s.screen.id = :screenId " +
                        "AND (bs.status = 'CONFIRMED' OR bs.status = 'PENDING')")
        List<SeatAvailabilityDto> findUnavailableSeats(@Param("showId") Long showId,
                        @Param("screenId") Long screenId);

        // Get booking seats by show ID and seat IDs
        @Query("SELECT bs FROM BookingSeat bs WHERE bs.booking.show.id = :showId AND bs.seat.id IN :seatIds")
        List<BookingSeat> findBookingSeatsByIds(@Param("showId") Long showId,
                        @Param("seatIds") List<Long> seatIds);

        // Get expired pending seats
        @Query("SELECT bs FROM BookingSeat bs WHERE bs.status = 'PENDING' AND bs.lockedAt < :lockedAt")
        List<BookingSeat> findExpiredPendingSeats(@Param("lockedAt") LocalDateTime lockedAt);
}
