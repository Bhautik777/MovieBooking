package com.moviebooking.moviebooking.model.dto.paymentdto;

import lombok.Data;

@Data
public class PaymentRequestDto {

    private Long bookingId;
    private String paymentMode;
    private String userPhoneNumber;
}
