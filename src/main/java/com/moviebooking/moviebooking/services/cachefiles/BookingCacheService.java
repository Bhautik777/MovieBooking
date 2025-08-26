package com.moviebooking.moviebooking.services.cachefiles;

import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import com.moviebooking.moviebooking.model.Booking;
import com.moviebooking.moviebooking.repository.BookingRepository;

@Service
public class BookingCacheService {

    private final BookingRepository bookingRepository;

    public BookingCacheService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Get confirmed bookings by user ID with caching
    @Cacheable(value = "confirmedBookingsByUser", key = "#userId")
    public List<Booking> getConfirmedBookingsByUserId(Long userId) {
        return bookingRepository.findConfirmedBookingByUserId(userId);
    }

    // Save booking and evict related caches
    @Caching(evict = {
            @CacheEvict(value = "confirmedBookingsByUser", allEntries = true)
    })
    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

  
}
