package com.moviebooking.moviebooking.services.cachefiles;

import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.moviebooking.moviebooking.exceptionhandler.movieexception.MovieException;

import com.moviebooking.moviebooking.model.Movie;
import com.moviebooking.moviebooking.repository.MovieRepository;

@Service
public class MovieCacheService {

    private final MovieRepository movieRepository;

    public MovieCacheService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // get all movies from cache if available else fetch from database and cache the
    @Cacheable(value = "allMovies")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // If movie is found in cache, return the cached value
    // If movie is not found, fetch from database and cache the value
    @Cacheable(value = "moviesByCity", key = "#city")
    public List<Movie> getMoviesByCity(String city) {
        // directly fetch from database
        return movieRepository.findMoviesByCity(city);
    }

    // If movie is found in cache, return the cached value
    // If movie is not found, fetch from database and cache the value
    @Cacheable(value = "movieById", key = "#movieId")
    public Movie getMovieById(Long movieId) {
        // directly fetch from database
        Optional<Movie> movie = movieRepository.findById(movieId);
        // if movie is not found, throw exception
        if (movie.isEmpty()) {
            throw new MovieException.AdminMovieNotFoundException();
        }
        return movie.get();
    }

    @Cacheable(value = "movieByName", key = "#movieName")
    public Movie getMovieByName(String movieName) {
        // directly fetch from database
        return movieRepository.findMovieByName(movieName);
    }

    // It will save movie to database and delete movie from cache
    @Caching(evict = {
            @CacheEvict(value = "allMovies", allEntries = true), // list may be stale
            @CacheEvict(value = "moviesByCity", key = "#city"), // only this city
            @CacheEvict(value = "theatersByAdmin", allEntries = true),
    })
    public void saveMovie(Movie movie, String city) {
        movieRepository.save(movie);
    }

    // It will update movie in database and delete movie from cache
    @Caching(evict = {
            @CacheEvict(value = "allMovies", allEntries = true), // list may be stale
            @CacheEvict(value = "movieById", key = "#movie.id"), // only this movie
            @CacheEvict(value = "movieByName", allEntries = true), // only this movie
            @CacheEvict(value = "moviesByCity", key = "#city"), // only this city
            @CacheEvict(value = "theatersByAdmin", allEntries = true),
            @CacheEvict(value = "showById", allEntries = true),
            @CacheEvict(value = "confirmedBookingsByUser", allEntries = true),
    })
    public void updateMovie(Movie movie, String city) {
        movieRepository.save(movie);
    }
}
