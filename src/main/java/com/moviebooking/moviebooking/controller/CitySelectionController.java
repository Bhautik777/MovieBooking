package com.moviebooking.moviebooking.controller;

import static com.moviebooking.moviebooking.urlpaths.CitySelectionPaths.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(CITY_BASE_PATH)
@Slf4j
public class CitySelectionController {

    @GetMapping(CITY_SELECTION_PAGE_URL)
    public String showSelectCityPage() {
        return CITY_SELECTION_PAGE;
    }
}
