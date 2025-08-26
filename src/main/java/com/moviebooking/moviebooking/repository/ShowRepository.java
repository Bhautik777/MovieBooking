package com.moviebooking.moviebooking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moviebooking.moviebooking.model.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

        @Query("SELECT s FROM Show s JOIN s.theater t JOIN s.movie m WHERE t.city=:city and m.id=:movieId")
        List<Show> findTheaterAndShowByMovieAndCity(@Param("city") String city, @Param("movieId") Long movieId);

        @Query("SELECT s FROM Show s JOIN s.theater t JOIN s.movie m WHERE t.city=:city and m.id=:movieId and s.showDate=:showDate")
        List<Show> findTheaterAndShowByMovieAndCityAndDate(@Param("city") String city, @Param("movieId") Long movieId,
                        @Param("showDate") LocalDate showDate);

        @Query("SELECT s FROM Show s JOIN s.theater t JOIN s.movie m WHERE t.city=:city and m.id=:movieId and s.showDate=:showDate and s.id=:showId and s.screen.id=:screenId")
        Show findShowByRequiredField(@Param("city") String city, @Param("movieId") Long movieId,
                        @Param("showDate") LocalDate showDate, @Param("showId") Long showId,
                        @Param("screenId") Long screenId);

}
