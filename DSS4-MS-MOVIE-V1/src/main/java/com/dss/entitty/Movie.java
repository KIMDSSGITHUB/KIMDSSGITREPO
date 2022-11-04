package com.dss.entitty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
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
}
