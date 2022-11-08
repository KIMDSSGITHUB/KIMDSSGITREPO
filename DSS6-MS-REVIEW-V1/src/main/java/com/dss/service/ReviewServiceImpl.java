package com.dss.service;

import com.dss.dto.ReviewRequestDTO;
import com.dss.entity.Review;
import com.dss.exception.ReviewNotFoundException;
import com.dss.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviews() {
        List<Review> reviews;
        reviews = reviewRepository.findAll();
        return reviews;
    }

    @Override
    public Review getReviewById(UUID id) {
        log.info("Inside getReviewById with Id: " + id);
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @Override
    public List<Review> getReviewsByMovie(UUID id) {
        log.info("Inside getReviewsByMovieId with Id: " + id);
        return reviewRepository.findReviewsByMovieId(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @Override
    public Review create(ReviewRequestDTO request) {
        log.info("Inside create");
        Review review = new Review();
        dtoToEntity(request,review);
        return reviewRepository.save(review);
    }

    @Override
    public Review update(UUID id, ReviewRequestDTO request) {
        log.info("Inside update with id: " + id);
        return reviewRepository.findById(id).map(review -> {
            dtoToEntity(request, review);
            return reviewRepository.save(review);
        }).orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @Override
    public String delete(UUID id) {
        log.info("Inside delete with id: " + id);
        try{
            reviewRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ReviewNotFoundException(id);
        }
        return "Successfully deleted review with id: " + id;
    }

    private Review dtoToEntity(ReviewRequestDTO dto, Review entity) {
        entity.setDescription(dto.getDescription());
        entity.setRating(dto.getRating());
        entity.setMovieId(dto.getMovieId());
        return entity;
    }
}
