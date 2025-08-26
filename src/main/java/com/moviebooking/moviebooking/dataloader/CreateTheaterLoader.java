package com.moviebooking.moviebooking.dataloader;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.moviebooking.moviebooking.model.Movie;
import com.moviebooking.moviebooking.model.Show;
import com.moviebooking.moviebooking.model.Seat;
import com.moviebooking.moviebooking.model.Screen;
import com.moviebooking.moviebooking.model.Theater;
import com.moviebooking.moviebooking.model.Movie.MovieLanguage;
import com.moviebooking.moviebooking.model.Admin;
import com.moviebooking.moviebooking.repository.TheaterRepository;
import com.moviebooking.moviebooking.repository.MovieRepository;
import com.moviebooking.moviebooking.repository.ScreenRepository;
import com.moviebooking.moviebooking.repository.ShowRepository;
import com.moviebooking.moviebooking.repository.SeatRepository;
import com.moviebooking.moviebooking.repository.BookingRepository;
import com.moviebooking.moviebooking.repository.PaymentRepository;
import com.moviebooking.moviebooking.repository.BookingSeatsRepository;
import com.moviebooking.moviebooking.repository.AdminRepository;
import com.moviebooking.moviebooking.repository.UserRepository;

@Component
public class CreateTheaterLoader implements CommandLineRunner {

    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final BookingSeatsRepository bookingSeatsRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    public CreateTheaterLoader(
            TheaterRepository theaterRepository,
            MovieRepository movieRepository,
            ScreenRepository screenRepository,
            ShowRepository showRepository,
            SeatRepository seatRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            BookingSeatsRepository bookingSeatsRepository,
            AdminRepository adminRepository,
            UserRepository userRepository,
            PasswordEncoder encoder) {
        this.theaterRepository = theaterRepository;
        this.movieRepository = movieRepository;
        this.screenRepository = screenRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.bookingSeatsRepository = bookingSeatsRepository;
        this.adminRepository = adminRepository;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        /*
         * Theater theater = theaterRepository.findTheatersByTheaterName("PVR");
         * if (theater != null) {
         * theaterRepository.delete(theater);
         * System.out.println("Theater deleted");
         * } else {
         * System.out.println("Theater not found");
         * }
         */

       
        deleteBookedTickets();
       /*  deleteAll();
        
        createAdminForMovieCreator();
        createAdminForTheater();
        createMovie(); */
    }

    private void createAdminForMovieCreator() {
        Admin admin = new Admin();
        admin.setFirstName("Bhautik");
        admin.setLastName("Ranpara");
        admin.setEmail("bhautikranpara@gmail.com");
        admin.setPassword(encoder.encode("123"));
        admin.setAdminName("Bhautik");
        admin.setRole(Admin.Role.ADMIN_MOVIE_CREATOR);
        adminRepository.save(admin);
    }

    private void createAdminForTheater() {
        Admin admin = new Admin();
        admin.setFirstName("PVR");
        admin.setLastName("Theater");
        admin.setEmail("pvr@gmail.com");
        admin.setPassword(encoder.encode("123"));
        admin.setAdminName("PVR");
        admin.setRole(Admin.Role.ADMIN_THEATER);
        adminRepository.save(admin);
    }

    private void createMovie() {
        Movie movie = new Movie();
        movie.setTitle("Spiderman");
        movie.setDescription("A superhero movie");
        movie.setLanguage(MovieLanguage.ENGLISH);
        movie.setGenre(Movie.MovieGenre.ACTION);
        movie.setFormat(Movie.MovieFormat.TWO_D);
        movie.setDurationInMinutes(120);

        movieRepository.save(movie);
    }

    private void deleteAll() {
        showRepository.deleteAll();
        seatRepository.deleteAll();
        screenRepository.deleteAll();
        theaterRepository.deleteAll();
        movieRepository.deleteAll();
        adminRepository.deleteAll();
    }

    void deleteBookedTickets() {
        paymentRepository.deleteAll();
        bookingRepository.deleteAll();
        bookingSeatsRepository.deleteAll();
        userRepository.deleteAll();
    }
}
