package com.dss.repository;

import com.dss.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Optional<List<Review>> findReviewsByMovieId(UUID movieId);
}
