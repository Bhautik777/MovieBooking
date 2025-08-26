package com.moviebooking.moviebooking.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.moviebooking.moviebooking.model.Admin;
import com.moviebooking.moviebooking.model.dto.userdto.AdminCreateDto;
import com.moviebooking.moviebooking.repository.AdminRepository;

@Service
public class AdminService {

    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    // Constructor injection
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Here we are saving the admin
    public boolean saveAdmin(AdminCreateDto admin, BindingResult bindingResult) {

        // Here we are checking if user already exists
        Optional<List<Admin>> existingUser = adminRepository.findByEmailOrAdminName(admin.getEmail(),
                admin.getAdminName());

        // Here we are validating the user
        if (existingUser.isPresent()) {
            for (Admin admin2 : existingUser.get()) {
                if (admin2.getEmail().equals(admin.getEmail())) {
                    bindingResult.rejectValue("email", "error.admin", "Email already exists");
                    return false;
                }
                if (admin2.getAdminName().equals(admin.getAdminName())) {
                    bindingResult.rejectValue("adminName", "error.admin", "Admin name already exists");
                    return false;
                }
            }
        }

        // Here we are validating the password
        if (!admin.getPassword().equals(admin.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.admin", "Password and Confirm Password do not match");
            return false;
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        System.out.println(admin.getPassword());
        Admin adminEntity = new Admin();
        adminEntity.setFirstName(admin.getFirstName());
        adminEntity.setLastName(admin.getLastName());
        adminEntity.setEmail(admin.getEmail());
        adminEntity.setAdminName(admin.getAdminName());
        adminEntity.setPassword(admin.getPassword());
        adminEntity.setRole(admin.getRole());

        adminRepository.save(adminEntity);
        return true;
    }

    // Here we are logging in the admin
    public boolean loginAdmin(String email, String password) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (!admin.isPresent()) {
            return false;
        }

        if (passwordEncoder.matches(password, admin.get().getPassword())) {
            return true;
        }
        return false;
    }

    // Here we are getting the logged in admin
    public Admin getLoggedInAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // username = email
        } else {
            email = principal.toString();
        }

        return adminRepository.findByEmail(email).orElse(null);
    }
}
