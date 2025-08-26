package com.moviebooking.moviebooking.services.cachefiles;

import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.moviebooking.moviebooking.model.BookingSeat;
import com.moviebooking.moviebooking.model.dto.seatdto.SeatAvailabilityDto;
import com.moviebooking.moviebooking.repository.BookingSeatsRepository;

@Service
public class BookingSeatsCacheService {

    private final BookingSeatsRepository bookingSeatsRepository;

    public BookingSeatsCacheService(BookingSeatsRepository bookingSeatsRepository) {
        this.bookingSeatsRepository = bookingSeatsRepository;
    }

    // Save multiple booking seats and evict related caches
    @Caching(evict = {
            @CacheEvict(value = "confirmedBookingsByUser", allEntries = true),
    })
    public void saveAllBookingSeats(List<BookingSeat> bookingSeats) {
        bookingSeatsRepository.saveAll(bookingSeats);
    }

}
