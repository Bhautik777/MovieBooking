package com.moviebooking.moviebooking.model.dto.paymentdto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class PaymentResponseDto {

    private String paymentMode;
    private Double totalPrice;
    private String theaterName;
    private String movieName;
    private LocalDate showDate;
    private LocalTime showTime;
    private String screenName;
    private List<String> seats;
}
