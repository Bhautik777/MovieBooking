package com.moviebooking.moviebooking.controller;

import static com.moviebooking.moviebooking.urlpaths.AdminTheaterPaths.*;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.moviebooking.moviebooking.services.TheaterService;
import com.moviebooking.moviebooking.model.Theater;

@Controller
@RequestMapping(ADMIN_THEATER_BASE_PATH)
public class AdminTheaterController {

    private final TheaterService theaterService;

    public AdminTheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    //admin theater can create a new theater
    @PostMapping(ADMIN_CREATE_THEATER_URL)
    public String createTheater(@ModelAttribute Theater theater) {
        //create a new theater
        theaterService.createTheater(theater);
        return "redirect:" + ADMIN_THEATER_BASE_PATH + ADMIN_THEATER_LIST_PAGE_URL;
    }

    @GetMapping(ADMIN_THEATER_CREATE_PAGE_URL)
    public String viewAdminTheaterCreatePage() {
        return ADMIN_THEATER_CREATE_PAGE;
    }

    //Here we are getting all the theaters from the database for the admin theater
    @GetMapping(ADMIN_THEATER_LIST_PAGE_URL)
    public String viewAdminTheaterListPage(Model model) {
        List<Theater> theaters = theaterService.getTheatersByAdmin();
        model.addAttribute("theaters", theaters);
        return ADMIN_THEATER_LIST_PAGE;
    }

    /*
     * @GetMapping(ADMIN_THEATER_EDIT_PAGE_URL)
     * public String viewAdminTheaterEditPage(@PathVariable Long theaterId, Model
     * model) {
     * Theater theater = theaterService.getTheaterById(theaterId);
     * model.addAttribute("theater", theater);
     * return ADMIN_THEATER_EDIT_PAGE;
     * }
     * 
     * @PostMapping(ADMIN_UPDATE_THEATER)
     * public String updateTheater(@ModelAttribute Theater theater) {
     * theaterService.updateTheater(theater);
     * return "redirect:" + ADMIN_THEATER_BASE_PATH + ADMIN_THEATER_LIST_PAGE_URL;
     * }
     */
}
