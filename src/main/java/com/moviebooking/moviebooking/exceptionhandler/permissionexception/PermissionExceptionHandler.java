package com.moviebooking.moviebooking.exceptionhandler.permissionexception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PermissionExceptionHandler {

    @ExceptionHandler(PermissionException.DontHavePermissionException.class)
    public String handleDontHavePermissionException(PermissionException.DontHavePermissionException ex) {
        return "access-denied";
    }
}
