package com.moviebooking.moviebooking.exceptionhandler.bookingexception;

public class BookingException {

    public static long showId;

    public static class BookingNotFoundException extends RuntimeException {
        public BookingNotFoundException(String message) {
            super(message);
        }
    }

    public static class SeatsAlreadyBookedException extends RuntimeException {
        public SeatsAlreadyBookedException(String message, long showId) {
            super(message);
            BookingException.showId = showId;
        }
    }

    public static long getShowId() {
        return showId;
    }
}
