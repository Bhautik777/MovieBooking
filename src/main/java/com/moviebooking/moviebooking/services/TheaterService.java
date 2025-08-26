package com.moviebooking.moviebooking.services;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.moviebooking.moviebooking.model.Movie;
import com.moviebooking.moviebooking.model.Admin;
import com.moviebooking.moviebooking.model.Screen;
import com.moviebooking.moviebooking.model.Show;
import com.moviebooking.moviebooking.model.Theater;
import com.moviebooking.moviebooking.services.cachefiles.TheaterCacheService;

@Service
@Slf4j
public class TheaterService {

    private final AdminService adminService;
    private final MovieService movieService;
    private final TheaterCacheService theaterCacheService;

    public TheaterService(AdminService adminService,
            MovieService movieService, TheaterCacheService theaterCacheService) {
        this.adminService = adminService;
        this.movieService = movieService;
        this.theaterCacheService = theaterCacheService;
    }

    public void createTheater(Theater theater) {
        //get current logged in admin
        Admin loggedInUser = adminService.getLoggedInAdmin();
        log.info("Theater name: {}", theater.getName());
        log.info("Theater city: {}", theater.getCity());
        log.info("Theater screens: {}", theater.getScreens().size());
        log.info("Theater screens seats: {}", theater.getScreens().get(0).getSeats().size());

        //set the theater to the screens
        //set the screen to the seats
        //set the show to the movie
        for (Screen screen : theater.getScreens()) {
            screen.setTotalSeats(screen.getSeats().size());
            screen.getSeats().forEach(seat -> seat.setScreen(screen));
            screen.setTheater(theater);

            //set the movie to the show
            //set the screen to the show
            //set the theater to the show
            for (Show show : screen.getShows()) {
                Movie movie = movieService.getMovieByName(show.getMovie().getTitle());
                log.info("Theater Movie: {}", movie.getTitle());
                show.setMovie(movie);
                show.setScreen(screen);
                show.setTheater(theater);
            }
        }

        //set the admin to the theater
        theater.setAdmin(loggedInUser);
        theaterCacheService.saveTheater(theater);
    }

    public List<Theater> getTheatersByAdmin() {
        //get current logged in admin
        Admin loggedInUser = adminService.getLoggedInAdmin();
        //get all the theaters from the database by admin id
        return theaterCacheService.getTheatersByAdminId(loggedInUser.getId());
    }

    /*
     * public void updateTheater(Theater theater) {
     * log.info("Theater Name: " + theater.getCity());
     * log.info("Screen name: " + theater.getScreens().get(0).getName());
     * log.info("Seat size " + theater.getScreens().get(0).getSeats().size());
     * Theater existingTheater = getTheaterById(theater.getId());
     * if (existingTheater != null) {
     * theaterRepository.save(theater);
     * }
     * }
     */
}