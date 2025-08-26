package com.moviebooking.moviebooking.exceptionhandler.permissionexception;

public class PermissionException {

    public static class DontHavePermissionException extends RuntimeException {
        public DontHavePermissionException(String message) {
            super(message);
        }
    }
}
