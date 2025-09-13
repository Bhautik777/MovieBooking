package com.moviebooking.moviebooking.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.moviebooking.moviebooking.exceptionhandler.showexception.ShowException;
import com.moviebooking.moviebooking.model.Movie;
import com.moviebooking.moviebooking.model.Show;
import com.moviebooking.moviebooking.model.Theater;
import com.moviebooking.moviebooking.model.dto.moviedto.MovieCreateDto;
import com.moviebooking.moviebooking.model.dto.moviedto.MovieResponseDto;
import com.moviebooking.moviebooking.model.dto.moviedto.MovieResponseOnShowAndTheaterDto;
import com.moviebooking.moviebooking.model.dto.moviedto.MovieUpdateDto;
import com.moviebooking.moviebooking.repository.ShowRepository;
import com.moviebooking.moviebooking.services.cachefiles.MovieCacheService;

@Service
@Slf4j
public class MovieService {

    private final ShowRepository showRepository;
    private final MovieCacheService movieCacheService;

    public MovieService(ShowRepository showRepository,
            MovieCacheService movieCacheService) {
        this.showRepository = showRepository;
        this.movieCacheService = movieCacheService;
    }

    public void createMovie(MovieCreateDto movie) {
        // Convert MovieCreateDto to Movie
        Movie movieEntity = convertMovieCreateDtoToMovie(movie);
        // Save Movie
        movieCacheService.saveMovie(movieEntity);
    }

    public List<MovieResponseDto> getMovies() {
        // Get all movies from database
        List<Movie> movies = movieCacheService.getAllMovies();
        log.info("Movies from cache: {}", movies);

        List<MovieResponseDto> movieResponseDtos = new ArrayList<>();
        // Convert Movie to MovieResponseDto
        movies.forEach(movie -> {
            MovieResponseDto movieResponseDto = convertMovieToMovieResponseDto(movie);
            movieResponseDtos.add(movieResponseDto);
        });

        return movieResponseDtos;
    }

    public List<MovieResponseDto> getMoviesByCity(String city) {
        // get all the movies from the database by city
        List<Movie> movies = movieCacheService.getMoviesByCity(city);
        log.info("Movies from cache: {}", movies);
        log.info("Movies from database: {}", movies.size());
        List<MovieResponseDto> movieResponseDtos = new ArrayList<>();
        // convert the movies to MovieResponseDto
        movies.forEach(movie -> {
            MovieResponseDto movieResponseDto = convertMovieToMovieResponseDto(movie);
            movieResponseDtos.add(movieResponseDto);
        });

        return movieResponseDtos;
    }

    public MovieResponseDto getMovieById(Long movieId) {
        // try to find movie by id
        Movie movie = movieCacheService.getMovieById(movieId);
        // if movie is found, convert it to MovieResponseDto and return it
        return convertMovieToMovieResponseDto(movie);
    }

    // Here we are getting the theater and show by movie and city and date
    public Map<Theater, List<Show>> getTheaterAndShowByMovieAndCity(String city, Long movieId, LocalDate date) {
        log.info("Getting theater and show by movie and city and date: {}, {}, {}", city, movieId, date);
        // Here i have used LinkedHashMap to maintain the order of the theaters and
        // shows
        Map<Theater, List<Show>> theatersAndShows = new LinkedHashMap<>();
        // Get shows by movie and city and date
        List<Show> shows = showRepository.findTheaterAndShowByMovieAndCityAndDate(city, movieId, date);
        // if shows is empty, throw exception
        if (shows.isEmpty()) {
            throw new ShowException.NoShowsFoundForMovieException("No shows found for movie");
        }
        List<Show> topTheaterShow = new ArrayList<>();
        Theater topTheater = null;

        // Here we are getting the top theater and show
        for (Show show : shows) {
            if (show.getTheater().getName().contains("INOX")) {
                topTheaterShow.add(show);
                topTheater = show.getTheater();
            }
        }

        if (topTheater != null)
            theatersAndShows.put(topTheater, topTheaterShow);

        // Here we are getting the other theaters and shows
        for (Show show : shows) {
            Theater currentTheater = show.getTheater();

            if (topTheater != null && currentTheater.getId().equals(topTheater.getId())) {
                continue;
            }

            theatersAndShows.computeIfAbsent(currentTheater, k -> new ArrayList<>()).add(show);
        }

        return theatersAndShows;
    }

    public Movie getMovieByName(String movieName) {
        return movieCacheService.getMovieByName(movieName);
    }

    public void updateMovie(MovieUpdateDto movie) {
        // Get existing movie from database
        Movie existingMovie = movieCacheService.getMovieById(movie.getId());

        // Convert MovieUpdateDto to Movie
        convertMovieUpdateDtoToMovie(movie, existingMovie);
        // Save Movie
        movieCacheService.updateMovie(existingMovie, existingMovie.getShows().get(0).getTheater().getCity());

    }

    public Movie convertMovieCreateDtoToMovie(MovieCreateDto movieCreateDto) {
        Movie movie = new Movie();
        movie.setTitle(movieCreateDto.getTitle());
        movie.setDescription(movieCreateDto.getDescription());
        movie.setDurationInMinutes(movieCreateDto.getDurationInMinutes());
        movie.setLanguage(movieCreateDto.getLanguage());
        movie.setGenre(movieCreateDto.getGenre());
        movie.setFormat(movieCreateDto.getFormat());
        movie.setImageUrl(movieCreateDto.getImageUrl());

        return movie;
    }

    public MovieResponseDto convertMovieToMovieResponseDto(Movie movie) {
        MovieResponseDto movieResponseDto = new MovieResponseDto();
        movieResponseDto.setId(movie.getId());
        movieResponseDto.setTitle(movie.getTitle());
        movieResponseDto.setDescription(movie.getDescription());
        movieResponseDto.setDurationInMinutes(movie.getDurationInMinutes());
        movieResponseDto.setLanguage(movie.getLanguage());
        movieResponseDto.setGenre(movie.getGenre());
        movieResponseDto.setFormat(movie.getFormat());
        movieResponseDto.setImageUrl(movie.getImageUrl());
        return movieResponseDto;
    }

    private void convertMovieUpdateDtoToMovie(MovieUpdateDto movieUpdateDto, Movie movie) {
        movie.setTitle(movieUpdateDto.getTitle());
        movie.setDescription(movieUpdateDto.getDescription());
        movie.setDurationInMinutes(movieUpdateDto.getDurationInMinutes());
        movie.setLanguage(movieUpdateDto.getLanguage());
        movie.setGenre(movieUpdateDto.getGenre());
        movie.setFormat(movieUpdateDto.getFormat());
        movie.setImageUrl(movieUpdateDto.getImageUrl());
    }

    public MovieResponseOnShowAndTheaterDto convertMovieToMovieResponseOnShowAndTheaterDto(Movie movie) {
        MovieResponseOnShowAndTheaterDto movieResponseDto = new MovieResponseOnShowAndTheaterDto();
        movieResponseDto.setId(movie.getId());
        movieResponseDto.setTitle(movie.getTitle());
        return movieResponseDto;
    }
}
