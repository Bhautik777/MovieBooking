package com.moviebooking.moviebooking.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moviebooking.moviebooking.model.Show;
import com.moviebooking.moviebooking.model.dto.bookingdto.BookingRequestDto;
import com.moviebooking.moviebooking.model.dto.seatdto.SeatAvailabilityDto;
import com.moviebooking.moviebooking.repository.BookingSeatsRepository;
import com.moviebooking.moviebooking.repository.ShowRepository;
import com.moviebooking.moviebooking.exceptionhandler.showexception.ShowException;
import com.moviebooking.moviebooking.services.cachefiles.ShowCacheService;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final BookingSeatsRepository bookingSeatsRepository;
    private final ShowCacheService showCacheService;

    public ShowService(ShowRepository showRepository, BookingSeatsRepository bookingSeatsRepository,
            ShowCacheService showCacheService) {
        this.showRepository = showRepository;
        this.bookingSeatsRepository = bookingSeatsRepository;
        this.showCacheService = showCacheService;
    }

    public Show getShowById(long showId) {
        // Get show by id
        return showCacheService.getShowById(showId);
    }

    public List<SeatAvailabilityDto> getSeatsStatus(Long showId, Long screenId) {
        return bookingSeatsRepository.findUnavailableSeats(showId, screenId);
    }

    public Show getShowByRequiredField(BookingRequestDto bookingRequestDto) {
        Show show = showRepository.findShowByRequiredField(bookingRequestDto.getCity(), bookingRequestDto.getMovieId(),
                bookingRequestDto.getShowDate(), bookingRequestDto.getShowId(), bookingRequestDto.getScreenId());
        if (show == null) {
            throw new ShowException.NoShowsFoundForMovieException("No shows found for movie");
        }
        return show;
    }
}
