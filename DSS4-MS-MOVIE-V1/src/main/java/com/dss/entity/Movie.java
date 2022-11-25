package com.dss.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "movie_id")
    @Type(type = "pg-uuid")
    private UUID movieId;
    @Column(name = "image",nullable = false)
    private String image;
    @Column(name = "movie_title", nullable = false)
    private String movieTitle;
    @Column(name = "cost", nullable = false)
    private double cost;
    @Column(name = "year_of_release", nullable = false)
    private int yrOfRelease;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "movieActors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();
}
