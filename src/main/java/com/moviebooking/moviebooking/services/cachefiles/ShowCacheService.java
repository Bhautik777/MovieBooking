package com.moviebooking.moviebooking.services.cachefiles;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.moviebooking.moviebooking.model.Show;
import com.moviebooking.moviebooking.repository.ShowRepository;
import com.moviebooking.moviebooking.exceptionhandler.showexception.ShowException;

@Service
public class ShowCacheService {

    private final ShowRepository showRepository;

    public ShowCacheService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    // Get show by ID with caching
    @Cacheable(value = "showById", key = "#showId")
    public Show getShowById(Long showId) {
        Optional<Show> show = showRepository.findById(showId);
        // If show is not found, throw exception
        if (show.isEmpty()) {
            throw new ShowException.NoShowsFoundForMovieException("No shows found for movie");
        }
        return show.get();
    }

}
