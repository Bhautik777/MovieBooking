package com.moviebooking.moviebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import static com.moviebooking.moviebooking.urlpaths.ShowsPaths.*;

import java.util.List;

import com.moviebooking.moviebooking.model.Screen;
import com.moviebooking.moviebooking.model.Show;
import com.moviebooking.moviebooking.model.dto.seatdto.SeatAvailabilityDto;
import com.moviebooking.moviebooking.services.ShowService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(SHOW_BASE_PATH)
@Slf4j
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    //Here we are showing seats to user
    @GetMapping(SHOW_SEAT_SELECTION_PAGE_URL)
    public String showSeatSelectionPage(@PathVariable long showId, Model model) {
        //get the show by id
        Show show = showService.getShowById(showId);
        Screen screen = show.getScreen();

        //Here we are getting the seats status for the show and we want to show booked seats to user so that he can't book those seats
        List<SeatAvailabilityDto> seatsStatus = showService.getSeatsStatus(showId, screen.getId());
        log.info("Show ID: {} | Screen ID: {} | Unavailable seats: {}",
                showId, screen.getId(), seatsStatus.size());

        model.addAttribute("screen", screen);
        model.addAttribute("seats", screen.getSeats());
        model.addAttribute("show", show);
        model.addAttribute("theater", show.getTheater());
        model.addAttribute("movie", show.getMovie());
        model.addAttribute("seatsStatus", seatsStatus);
        return SHOW_SEAT_SELECTION_PAGE;
    }
}
