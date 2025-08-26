package com.moviebooking.moviebooking.model.dto.seatdto;

import com.moviebooking.moviebooking.model.BookingSeat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeatAvailabilityDto {
    private Long seatId;
    private BookingSeat.SeatStatus status;
    private boolean available;

    // Constructor that matches the query projection
    public SeatAvailabilityDto(Long seatId, BookingSeat.SeatStatus status) {
        this.seatId = seatId;
        this.status = status;
        this.available = !(status == BookingSeat.SeatStatus.PENDING || status == BookingSeat.SeatStatus.CONFIRMED);
    }

    // Full constructor for manual creation
    public SeatAvailabilityDto(Long seatId, BookingSeat.SeatStatus status, boolean available) {
        this.seatId = seatId;
        this.status = status;
        this.available = available;
    }
}