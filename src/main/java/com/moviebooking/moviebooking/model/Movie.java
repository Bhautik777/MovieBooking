package com.moviebooking.moviebooking.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private int durationInMinutes;

    @Enumerated(EnumType.STRING)
    private MovieLanguage language;
    @Enumerated(EnumType.STRING)
    private MovieGenre genre;
    @Enumerated(EnumType.STRING)
    private MovieFormat format;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Show> shows = new ArrayList<>();

    public enum MovieLanguage {
        ENGLISH,
        HINDI,
        GUJARATI,
        TAMIL,
        MARATHI,
    }

    public enum MovieGenre {
        ACTION,
        COMEDY,
        DRAMA,
        HORROR,
        ROMANCE,
        SCIENCE_FICTION
    }

    public enum MovieFormat {
        TWO_D,
        THREE_D
    }
}
