package com.moviebooking.moviebooking.model.dto.bookingdto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookingResponseDto {

    private Long id;
    private String theaterName;
    private String movieName;
    private String showDate;
    private String showTime;
    private String screenName;
    private List<String> seats;
    private Double totalPrice;
}
