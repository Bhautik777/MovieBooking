package com.moviebooking.moviebooking.services.cachefiles;

import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.moviebooking.moviebooking.model.Payment;
import com.moviebooking.moviebooking.repository.PaymentRepository;

@Service
public class PaymentCacheService {

    private final PaymentRepository paymentRepository;

    public PaymentCacheService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Save payment and evict related caches
    @Caching(evict = {
            @CacheEvict(value = "confirmedBookingsByUser", allEntries = true),
    })
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

}
