package com.moviebooking.moviebooking.model.dto.moviedto;

import lombok.Getter;
import lombok.Setter;

import com.moviebooking.moviebooking.model.Movie;

@Getter
@Setter
public class MovieResponseDto {
    private Long id;
    private String title;
    private String description;
    private int durationInMinutes;
    private Movie.MovieLanguage language;
    private Movie.MovieGenre genre;
    private Movie.MovieFormat format;
    private String imageUrl;
}
