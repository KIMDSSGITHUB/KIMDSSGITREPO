package com.dss.service;

import com.dss.dto.ReviewRequestDTO;
import com.dss.entity.Movie;
import com.dss.entity.Review;
import com.dss.exception.MovieNotFoundException;
import com.dss.exception.ReviewNotFoundException;
import com.dss.repository.ReviewRepository;
import com.dss.util.FeignServiceUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
 class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private FeignServiceUtil feignServiceUtil;

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
        when(reviewRepository.save(mockReqReview())).thenReturn(mockReview());
        when(feignServiceUtil.findMovie(MOVIE_ID)).thenReturn(mockMovie());
        Review res = reviewService.create(mockRequestReview());
        assertEquals(mockReview(), res);
    }
    @Test
    @DisplayName("Create Review but Movie does not exist")
    void createReviewButMovieDoesNotExist() {
        ReviewRequestDTO review = mockRequestReview();
        when(reviewRepository.save(mockReqReview())).thenReturn(mockReview());
        when(feignServiceUtil.findMovie(MOVIE_ID)).thenReturn(mockMovie());
//        Review res = reviewService.create(mockRequestReview());
        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
                () -> reviewService.create(REQ));

        assertEquals("Movie does not exist", exception.getMessage());
//        assertEquals(mockReview(), res);
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


    private ReviewRequestDTO mockRequestReview(){
        ReviewRequestDTO requestReview = new ReviewRequestDTO();
        requestReview.setDescription(mockReview().getDescription());
        requestReview.setRating(mockReview().getRating());
        requestReview.setMovie(mockMovie());
        return requestReview;
    }
    private Review mockReqReview(){
        Review reqReview = new Review();
        reqReview.setDescription(mockReview().getDescription());
        reqReview.setRating(mockReview().getRating());
        reqReview.setMovie(mockMovie());
        return reqReview;
    }
    private Review mockReview(){
        Review review = new Review();
        review.setReviewId(ID);
        review.setDescription("Funny Movie");
        review.setRating(10);
        review.setMovie(mockMovie());
        return review;
    }
    private Movie mockMovie() {
        Movie movie = new Movie();
        movie.setMovieId(MOVIE_ID);
        movie.setMovieTitle("Batman");
        movie.setImage("Batman.jpg");
        movie.setCost(30000);
        movie.setYrOfRelease(2021);
        movie.setReviews(null);
        return movie;
    }
}
