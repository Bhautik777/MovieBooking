package com.moviebooking.moviebooking.controller;

import static com.moviebooking.moviebooking.urlpaths.AdminMoviePaths.*;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.moviebooking.moviebooking.model.dto.moviedto.MovieCreateDto;
import com.moviebooking.moviebooking.model.dto.moviedto.MovieResponseDto;
import com.moviebooking.moviebooking.model.dto.moviedto.MovieUpdateDto;
import com.moviebooking.moviebooking.services.MovieService;

@Controller
@RequestMapping(ADMIN_MOVIE_CREATOR_BASE_PATH)
public class AdminMovieCreatorController {

    private final MovieService movieService;

    public AdminMovieCreatorController(MovieService movieService) {
        this.movieService = movieService;
    }

    //Here we are getting all the movies from the database for the admin movie creator
    @GetMapping(ADMIN_MOVIES_LIST_PAGE_URL)
    public String viewListedMovies(Model model) {
        //get all the movies from the database
        List<MovieResponseDto> movies = movieService.getMovies();
        model.addAttribute("movies", movies);
        return ADMIN_MOVIE_LIST_PAGE;
    }

    @GetMapping(ADMIN_CREATE_MOVIE_PAGE_URL)
    public String viewCreateMoviePage() {
        return ADMIN_MOVIE_CREATE_PAGE;
    }

    //admin movie creator can create a new movie
    @PostMapping(ADMIN_CREATE_MOVIE_URL)
    public String createMovie(@ModelAttribute MovieCreateDto movie, Model model) {
        movieService.createMovie(movie);
        return "redirect:" + ADMIN_MOVIE_CREATOR_BASE_PATH + ADMIN_MOVIES_LIST_PAGE_URL;
    }

    //Here we are getting the movie edit page for the admin movie creator
    @GetMapping(ADMIN_MOVIE_EDIT_PAGE_URL)
    public String viewMovieEditPage(@PathVariable Long movieId, Model model) {
        //get the movie from the database by movie id
        MovieResponseDto movie = movieService.getMovieById(movieId);
        model.addAttribute("movie", movie);
        return ADMIN_MOVIE_EDIT_PAGE;
    }

    //admin movie creator can update the movie
    @PostMapping(ADMIN_UPDATE_MOVIE_URL)
    public String updateMovie(@ModelAttribute MovieUpdateDto movie, Model model) {
        movieService.updateMovie(movie);
        return "redirect:" + ADMIN_MOVIE_CREATOR_BASE_PATH + ADMIN_MOVIES_LIST_PAGE_URL;
    }
}
