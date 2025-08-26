package com.moviebooking.moviebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    //This is a page that will be shown when the user tries to access a page that they are not authorized to access
    @GetMapping("/accessdenied")
    public String accessDenied() {
        return "access-denied-page";
    }
}
