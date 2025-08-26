package com.moviebooking.moviebooking.services;

import org.springframework.stereotype.Service;

import com.moviebooking.moviebooking.model.User;
import com.moviebooking.moviebooking.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(String phoneNumber) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user); // This will create a new user if not exists, or update if exists
        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }
}
