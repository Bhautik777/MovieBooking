package com.moviebooking.moviebooking.exceptionhandler.showexception;

public class ShowException {

    public static class NoShowsFoundForMovieException extends RuntimeException {
        public NoShowsFoundForMovieException(String message) {
            super(message);
        }
    }
}
