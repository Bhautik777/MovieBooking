package com.moviebooking.moviebooking.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;
    private Double paymentAmount;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public enum PaymentMode {
        CREDIT_CARD,
        DEBIT_CARD,
        PAYPAL,
        PAYTM,
        GOOGLE_PAY
    }
}
