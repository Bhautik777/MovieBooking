package com.moviebooking.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moviebooking.moviebooking.model.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
}
