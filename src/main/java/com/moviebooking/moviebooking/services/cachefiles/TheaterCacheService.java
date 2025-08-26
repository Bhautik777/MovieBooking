package com.moviebooking.moviebooking.services.cachefiles;

import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import com.moviebooking.moviebooking.model.Theater;
import com.moviebooking.moviebooking.repository.TheaterRepository;

@Service
public class TheaterCacheService {

    private final TheaterRepository theaterRepository;

    public TheaterCacheService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    // Get theaters by admin ID with caching
    @Cacheable(value = "theatersByAdmin", key = "#adminId")
    public List<Theater> getTheatersByAdminId(Long adminId) {
        return theaterRepository.findTheatersByAdminId(adminId);
    }

    // Save theater and evict related caches
    @Caching(evict = {
            @CacheEvict(value = "theatersByAdmin", allEntries = true),
    })
    public void saveTheater(Theater theater) {
        theaterRepository.save(theater);
    }
}
