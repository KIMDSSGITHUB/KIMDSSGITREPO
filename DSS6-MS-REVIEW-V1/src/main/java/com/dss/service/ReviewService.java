package com.dss.service;

import com.dss.dto.ReviewRequestDTO;
import com.dss.entity.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    List<Review> getReviews();
    Review getReviewById(UUID id);
    List<Review> getReviewsByMovie(UUID id);
    Review create(ReviewRequestDTO request);
    Review update(UUID id, ReviewRequestDTO request);
    String delete(UUID id);
}
