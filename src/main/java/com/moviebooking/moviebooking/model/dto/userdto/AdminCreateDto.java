package com.moviebooking.moviebooking.model.dto.userdto;

import java.io.Serializable;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.moviebooking.moviebooking.model.Admin;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminCreateDto {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Admin.Role role;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Admin name is required")
    @Size(min = 2, max = 100, message = "Admin name must be between 2 and 100 characters")
    private String adminName;

    // @Pattern(regexp =
    // "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
    // message = "Password must be at least 8 characters long, contain 1 uppercase
    // letter, and 1 special symbol.")
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    @Size(min = 3, max = 100, message = "Confirm password must be between 3 and 100 characters")
    private String confirmPassword;
}
