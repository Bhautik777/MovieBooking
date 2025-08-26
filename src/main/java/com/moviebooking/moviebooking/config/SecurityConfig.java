package com.moviebooking.moviebooking.config;

import static com.moviebooking.moviebooking.urlpaths.AdminMoviePaths.*;
import static com.moviebooking.moviebooking.urlpaths.AdminTheaterPaths.*;
import static com.moviebooking.moviebooking.urlpaths.AuthPaths.*;
import static com.moviebooking.moviebooking.urlpaths.CitySelectionPaths.*;
import static com.moviebooking.moviebooking.urlpaths.MoviePaths.*;
import static com.moviebooking.moviebooking.urlpaths.ShowsPaths.*;
import static com.moviebooking.moviebooking.urlpaths.UserPaths.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.moviebooking.moviebooking.model.Admin;
import com.moviebooking.moviebooking.repository.AdminRepository;

@Configuration
public class SecurityConfig {

        @Autowired
        private CustomLoginSuccessHandler customLoginSuccessHandler;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(AdminRepository adminRepository) {
                return email -> {
                        Admin admin = adminRepository.findByEmail(email)
                                        .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

                        // Convert enum to "ROLE_ADMIN" or "ROLE_USER"
                        String authority = "ROLE_" + admin.getRole().name();

                        return new org.springframework.security.core.userdetails.User(
                                        admin.getEmail(),
                                        admin.getPassword(),
                                        List.of(new SimpleGrantedAuthority(authority)));
                };
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf().disable()
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(AUTH_BASE + "/**",
                                                                CITY_BASE_PATH + "/**",
                                                                MOVIE_BASE_PATH + "/**",
                                                                SHOW_BASE_PATH + "/**",
                                                                USER_BASE_PATH + "/**",
                                                                "/booking/**",
                                                                "/payment/**",
                                                                "/css/**", "/js/**", "/uploads/**",
                                                                "/images/**",
                                                                "/favicon.ico/**")
                                                .permitAll() // allow login/register/static
                                                .requestMatchers(ADMIN_THEATER_BASE_PATH + "/**")
                                                .hasRole("ADMIN_THEATER")
                                                .requestMatchers(ADMIN_MOVIE_CREATOR_BASE_PATH + "/**")
                                                .hasRole("ADMIN_MOVIE_CREATOR")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage(AUTH_BASE + LOGIN_PAGE_URL)
                                                .loginProcessingUrl(AUTH_BASE + LOGIN_URL)
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .successHandler(customLoginSuccessHandler)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl(AUTH_BASE + LOGOUT_URL)
                                                .logoutSuccessUrl(CITY_BASE_PATH + MOVIE_LIST_PAGE_URL));
                http.exceptionHandling(exception -> exception.accessDeniedPage("/accessdenied"));

                return http.build();
        }

}
