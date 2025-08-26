package com.moviebooking.moviebooking.registry;

import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.moviebooking.moviebooking.urlpaths.AdminMoviePaths.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminMovieHandlerRegistry {

    private static final AntPathMatcher matcher = new AntPathMatcher();

    private final Map<String, Function<RedirectAttributes, String>> handlers;

    public AdminMovieHandlerRegistry() {
        this.handlers = Map.of(
                ADMIN_MOVIE_CREATOR_BASE_PATH + ADMIN_MOVIE_EDIT_PAGE_URL,
                (attrs) -> {
                    log.info("Movie edit page matched");
                    attrs.addFlashAttribute("errorMessage", "Movie not found on edit page!");
                    return "redirect:" + ADMIN_MOVIE_CREATOR_BASE_PATH + ADMIN_MOVIES_LIST_PAGE_URL;
                },
                ADMIN_MOVIE_CREATOR_BASE_PATH + ADMIN_UPDATE_MOVIE_URL,
                (attrs) -> {
                    log.info("Movie update page matched");
                    attrs.addFlashAttribute("errorMessage", "Movie not found on update page!");
                    return "redirect:" + ADMIN_MOVIE_CREATOR_BASE_PATH + ADMIN_MOVIES_LIST_PAGE_URL;
                });
    }

    public String checkMatchingURI(String requestURI, RedirectAttributes attrs) {
        for (var entry : handlers.entrySet()) {
            if (matcher.match(entry.getKey(), requestURI)) {
                return entry.getValue().apply(attrs);
            }
        }
        return null;
    }
}
