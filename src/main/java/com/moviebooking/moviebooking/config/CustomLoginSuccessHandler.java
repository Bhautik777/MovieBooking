package com.moviebooking.moviebooking.config;

import static com.moviebooking.moviebooking.urlpaths.AdminMoviePaths.*;
import static com.moviebooking.moviebooking.urlpaths.AdminTheaterPaths.*;
import static com.moviebooking.moviebooking.urlpaths.CitySelectionPaths.*;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//If user is logged in and after you want to perform some action then you can use this class
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// Get roles of authenticated user
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		List<String> roles = authorities.stream().map(p -> p.getAuthority()).collect(Collectors.toList());

		// Redirect based on role
		if (roles.contains("ROLE_ADMIN_MOVIE_CREATOR")) {
			response.sendRedirect(ADMIN_MOVIE_CREATOR_BASE_PATH + ADMIN_MOVIES_LIST_PAGE_URL);
			return;
		} else if (roles.contains("ROLE_ADMIN_THEATER")) {
			// response.sendRedirect(ADMIN_THEATER_BASE_PATH + ADMIN_THEATER_LIST_PAGE);
			response.sendRedirect(ADMIN_THEATER_BASE_PATH + ADMIN_THEATER_LIST_PAGE_URL);
			return;
		} else if (roles.contains("ROLE_USER")) {
			response.sendRedirect(CITY_BASE_PATH + CITY_SELECTION_PAGE_URL);
			return;
		}

		// After that redirect to home page
		response.sendRedirect(CITY_BASE_PATH + MOVIE_LIST_PAGE_URL);
	}
}