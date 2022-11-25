package com.dss.repository;

import com.dss.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
Movie findByMovieId(UUID movieId);
}
