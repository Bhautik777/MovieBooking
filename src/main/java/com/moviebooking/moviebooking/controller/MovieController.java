package com.moviebooking.moviebooking.controller;

import java.util.List;
import java.util.Map;

import static com.moviebooking.moviebooking.urlpaths.MoviePaths.*;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.moviebooking.moviebooking.model.Movie;
import com.moviebooking.moviebooking.model.Show;
import com.moviebooking.moviebooking.model.Theater;
import com.moviebooking.moviebooking.model.dto.moviedto.MovieResponseDto;
import com.moviebooking.moviebooking.model.dto.moviedto.MovieResponseOnShowAndTheaterDto;
import com.moviebooking.moviebooking.services.MovieService;
import com.moviebooking.moviebooking.services.PhoneAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(MOVIE_BASE_PATH)
@Slf4j
public class MovieController {

    private final MovieService movieService;
    private final PhoneAuthService phoneAuthService;

    public MovieController(MovieService movieService, PhoneAuthService phoneAuthService) {
        this.movieService = movieService;
        this.phoneAuthService = phoneAuthService;
    }

    // Helper method to add user information to model
    private void setLoggedInUserData(Model model) {
        // get the current logged in phone number
        String phoneNumber = phoneAuthService.getCurrentLoggedInPhoneNumber();
        // if current user is logged in then add the user information to the model
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("userName", "Guest");
            log.info("User is logged in with phone: {}", phoneNumber);
        } else {
            model.addAttribute("isLoggedIn", false);
            log.info("User is not logged in");
        }
    }

    // Here we are getting the movies by city and showing the movies in the
    // movie-list page
    @PostMapping(MOVIE_LIST_BY_CITY_PAGE_URL)
    public String viewMoviesListByCity(@RequestParam String city, Model model, HttpSession session) {
        log.info("City selected: {}", city);
        List<MovieResponseDto> movies = movieService.getMoviesByCity(city);
        log.info("Movies found: {}", movies.size());
        model.addAttribute("movies", movies);
        model.addAttribute("selectedCity", city);
        session.setAttribute("selectedCity", city);
        // Here we are setting the logged in user data so that we can show the user name
        // in the header and can show his booking history
        setLoggedInUserData(model);
        return MOVIE_CITY_LIST_PAGE;
    }

    @GetMapping(MOVIE_LIST_BY_CITY_PAGE_URL)
    public String viewMoviesListByCityGet(Model model, HttpSession session) {
        // get the city from the session
        String city = (String) session.getAttribute("selectedCity");
        log.info("City selected (GET): {}", city);

        if (city == null || city.isEmpty()) {
            session.setAttribute("selectedCity", "Ahmedabad");
            city = "Ahmedabad";
        }

        List<MovieResponseDto> movies = movieService.getMoviesByCity(city);
        log.info("Movies found: {}", movies.size());
        model.addAttribute("movies", movies);
        model.addAttribute("selectedCity", city);
        // Here we are setting the logged in user data so that we can show the user name
        // in the header and can show his booking history
        setLoggedInUserData(model);
        return MOVIE_CITY_LIST_PAGE;
    }

    @GetMapping(MOVIE_DETAILS_PAGE_URL)
    public String showMovieDetails(@RequestParam Long movieId, @RequestParam LocalDate date,
            Model model, HttpSession session) {
        // get city from session
        String city = (String) session.getAttribute("selectedCity");
        // get theater and show map by movie and city and date
        Map<Theater, List<Show>> theaterAndShowMap = movieService.getTheaterAndShowByMovieAndCity(city, movieId, date);

        // get the movie from theater inside show inside movie
        Movie movie = theaterAndShowMap.get(theaterAndShowMap.keySet().iterator().next()).get(0).getMovie();
        // convert movie to movie response on show and theater dto
        MovieResponseOnShowAndTheaterDto movieResponseDto = movieService
                .convertMovieToMovieResponseOnShowAndTheaterDto(movie);

        model.addAttribute("theaterAndShowMap", theaterAndShowMap);
        model.addAttribute("movie", movieResponseDto);
        model.addAttribute("city", city);
        return MOVIE_THEATER_AND_SHOW_PAGE;
    }
}