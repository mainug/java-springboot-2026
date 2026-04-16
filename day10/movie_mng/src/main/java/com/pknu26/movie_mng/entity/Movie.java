package com.pknu26.movie_mng.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "MOVIE")
@Getter @Setter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    @SequenceGenerator(name = "movie_seq", sequenceName = "SEQ_MOVIE", allocationSize = 1)
    @Column(name = "MOVIE_ID")
    private Long movieId;

    @Column(nullable = false)
    private String title;

    private String originalTitle;
    private String director;
    private String actors;
    private String genre;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    
    private Integer runningTime;
    
    private Double rating;

    @Column(columnDefinition = "CLOB")
    private String description;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}