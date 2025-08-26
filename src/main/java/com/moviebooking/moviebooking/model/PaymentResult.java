package com.moviebooking.moviebooking.model;

import com.moviebooking.moviebooking.model.dto.paymentdto.PaymentResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResult {
    private PaymentResponseDto paymentResponse;
    private long showId;

}
