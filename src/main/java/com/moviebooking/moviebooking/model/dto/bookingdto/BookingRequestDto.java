package com.moviebooking.moviebooking.model.dto.bookingdto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {

    private Long showId;
    private Long screenId;
    private Long movieId;
    private String seatIdsString;
    private List<Long> seatIds;
    private String seatIdentifiersString;
    private List<String> seatIdentifiers;
    private LocalDate showDate;
    private String city;
    private String paymentMode;
}
