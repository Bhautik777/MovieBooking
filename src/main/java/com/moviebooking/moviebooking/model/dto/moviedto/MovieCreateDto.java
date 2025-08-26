package com.moviebooking.moviebooking.model.dto.moviedto;

import lombok.Getter;
import lombok.Setter;
import com.moviebooking.moviebooking.model.Movie;
import com.moviebooking.moviebooking.model.Show;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class MovieCreateDto {

    private String title;
    private String description;
    private String imageUrl;
    private int durationInMinutes;

    private Movie.MovieLanguage language;
    private Movie.MovieGenre genre;
    private Movie.MovieFormat format;

    private List<Show> shows = new ArrayList<>();

}
