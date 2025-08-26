package com.moviebooking.moviebooking.exceptionhandler.movieexception;

public class MovieException {

    public static class AdminMovieNotFoundException extends RuntimeException {
        public AdminMovieNotFoundException() {
            super();
        }
    }
}
