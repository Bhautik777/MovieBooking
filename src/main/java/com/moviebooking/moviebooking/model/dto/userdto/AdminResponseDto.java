package com.moviebooking.moviebooking.model.dto.userdto;

import java.io.Serializable;

import com.moviebooking.moviebooking.model.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminResponseDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Role is required")
    private Admin.Role role;

    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Admin name is required")
    private String adminName;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

}
