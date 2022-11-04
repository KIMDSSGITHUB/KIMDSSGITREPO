package com.dss.controller;

import com.dss.dto.ReviewRequestDTO;
import com.dss.entity.Review;
import com.dss.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @GetMapping()
    public ResponseEntity<List<Review>> getReviews(){
        return new ResponseEntity<>(reviewService.getReviews(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable UUID id) {
        return new ResponseEntity<>(reviewService.getReviewById(id), HttpStatus.OK);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<List<Review>> getReviewsByMovieId(@PathVariable UUID id) {
        return new ResponseEntity<>(reviewService.getReviewsByMovie(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Review> create(@RequestBody ReviewRequestDTO request) {
        return new ResponseEntity<>(reviewService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable UUID id, @RequestBody ReviewRequestDTO newRequest) {
        return new ResponseEntity<>(reviewService.update(id, newRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(reviewService.delete(id),HttpStatus.OK);
    }
}
