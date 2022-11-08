package com.dss.service;

import com.dss.dto.ReviewRequestDTO;
import com.dss.entity.Review;
import com.dss.exception.ReviewNotFoundException;
import com.dss.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
 class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService = new ReviewServiceImpl();

    private static final UUID ID = UUID.randomUUID();
    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final Review REVIEW = new Review();
    private static final ReviewRequestDTO REQ = new ReviewRequestDTO();
    private static final List<Review> RES = new ArrayList<>();


    @Test
    @DisplayName("Find All Reviews")
    void findAllReviews() {
        when(reviewRepository.findAll()).thenReturn(RES);
        List<Review> res = reviewService.getReviews();
        assertEquals(RES, res);
    }
    @Test
    @DisplayName("Find Reviews by Movie Id When Reviews Exists")
    void findReviewsByMovieIdWhenReviewExists() {
        Optional<List<Review>> optionalReviews = Optional.of(Collections.singletonList(REVIEW));
        when(reviewRepository.findReviewsByMovieId(MOVIE_ID)).thenReturn(optionalReviews);
        List<Review> res = reviewService.getReviewsByMovie(MOVIE_ID);
        assertEquals(optionalReviews.get(), res);
    }

    @Test
    @DisplayName("Find Review By Id When Review Exists")
    void findReviewByIdWhenReviewExists() {
        Optional<Review> optionalReview = Optional.of(REVIEW);

        when(reviewRepository.findById(ID)).thenReturn(optionalReview);

        Review res = reviewService.getReviewById(ID);

        assertEquals(REVIEW, res);
    }

    @Test
    @DisplayName("Find Review By Id When Review Is Not Found")
    void findReviewByIdWhenReviewNotFound() {
        when(reviewRepository.findById(ID)).thenReturn(Optional.empty());

        ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class,
                () -> reviewService.getReviewById(ID));

        assertEquals("Review not found with Id: ".concat(ID.toString()), exception.getMessage());
    }

    @Test
    @DisplayName("Create Review")
    void createReview() {
        when(reviewRepository.save(REVIEW)).thenReturn(REVIEW);
        Review res = reviewService.create(REQ);
        assertEquals(REVIEW, res);
    }

    @Test
    @DisplayName("Update Review When Review Exists")
    void updateProductRecordWhenProductExists() {
        Optional<Review> optionalReview = Optional.of(REVIEW);

        when(reviewRepository.findById(ID)).thenReturn(optionalReview);
        when(reviewRepository.save(REVIEW)).thenReturn(REVIEW);

        Review res = reviewService.update(ID, REQ);

        assertEquals(REVIEW, res);
    }

    @Test
    @DisplayName("Delete Review")
    void deleteReview() {
        doNothing().when(reviewRepository).deleteById(ID);
        reviewService.delete(ID);
        verify(reviewRepository, times(1)).deleteById(ID);
    }
}
