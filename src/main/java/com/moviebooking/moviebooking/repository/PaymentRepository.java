package com.moviebooking.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviebooking.moviebooking.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
