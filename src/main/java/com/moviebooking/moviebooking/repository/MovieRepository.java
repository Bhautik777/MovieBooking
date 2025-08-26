package com.moviebooking.moviebooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moviebooking.moviebooking.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

        @Query("SELECT m FROM Movie m JOIN m.shows s JOIN s.theater t WHERE t.city=:city")
        List<Movie> findMoviesByCity(@Param("city") String city);

        @Query("SELECT m FROM Movie m WHERE m.title=:movieName")
        Movie findMovieByName(@Param("movieName") String movieName);
}
