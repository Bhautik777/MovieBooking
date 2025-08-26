package com.moviebooking.moviebooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moviebooking.moviebooking.model.Theater;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

    @Query("SELECT t FROM Theater t WHERE t.admin.id = :adminId")
    List<Theater> findTheatersByAdminId(@Param("adminId") Long adminId);

    @Query("SELECT t FROM Theater t WHERE t.name = :theaterName")
    Theater findTheatersByTheaterName(@Param("theaterName") String theaterName);
}
