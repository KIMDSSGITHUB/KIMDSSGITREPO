package com.dss.service;

import com.dss.dto.ReviewRequestDTO;
import com.dss.entity.Review;
import com.dss.exception.MovieNotFoundException;
import com.dss.exception.ReviewNotFoundException;
import com.dss.repository.ReviewRepository;
import com.dss.util.FeignServiceUtil;
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

    @Autowired
    private FeignServiceUtil feignServiceUtil;

    @Override
    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(UUID id) {
        log.info("Inside getReviewById with Id: " + id);
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @Override
    public Review create(ReviewRequestDTO request) {
        log.info("Inside create");
        validate(request);
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
        entity.setMovie(dto.getMovie());
        return entity;
    }

    public void validate(ReviewRequestDTO review) {
        try {
            feignServiceUtil.findMovie(review.getMovie().getMovieId());
        } catch (Exception e) {
            throw new MovieNotFoundException("Movie does not exist");
        }
    }
}
