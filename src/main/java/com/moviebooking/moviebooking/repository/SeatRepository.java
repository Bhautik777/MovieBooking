package com.moviebooking.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moviebooking.moviebooking.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}
