package com.dss.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie")
public class Movie {
    @Id
    @Column(name = "movie_id")
    private UUID movieId;
    @Column(name = "image",nullable = false)
    private String image;
    @Column(name = "movie_title", nullable = false)
    private String movieTitle;
    @Column(name = "cost", nullable = false)
    private double cost;
    @Column(name = "year_of_release", nullable = false)
    private int yrOfRelease;

    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private Set<Review> reviews = new HashSet<>();
}
