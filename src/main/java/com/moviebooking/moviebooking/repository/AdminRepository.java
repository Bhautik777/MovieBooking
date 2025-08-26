package com.moviebooking.moviebooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moviebooking.moviebooking.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<List<Admin>> findByEmailOrAdminName(String email, String adminName);

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByAdminName(String adminName);
}
