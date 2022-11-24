package com.dss.controller;

import com.dss.dto.ReviewRequestDTO;
import com.dss.entity.Review;
import com.dss.service.ReviewService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
 class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    private static final UUID REVIEW_ID = UUID.randomUUID();
    private static final ReviewRequestDTO REQ = new ReviewRequestDTO();
    private static final Review RES = new Review();

    @Test
    @DisplayName("GET: Get Reviews")
    void getReviews() throws Exception {
        List<Review> reviews = Collections.singletonList(mockReview());
        when(reviewService.getReviews()).thenReturn(reviews);

        MvcResult result = this.mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(reviews, asObjectList(result.getResponse().getContentAsString(),new TypeReference<List<Review>>() {}));
    }

    @Test
    @DisplayName("GET: Review By Id")
    void getReview() throws Exception {
        Review review = mockReview();
        when(reviewService.getReviewById(REVIEW_ID)).thenReturn(review);

        MvcResult result = this.mockMvc.perform(get("/reviews/{id}", REVIEW_ID))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(review, asObject(result.getResponse().getContentAsString(), Review.class));
    }


    @Test
    @DisplayName("POST: Create Review")
    void createReview() throws Exception {
        when(reviewService.create(REQ)).thenReturn(RES);

        MvcResult result = this.mockMvc.perform(post("/reviews").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(RES)))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(RES, asObject(result.getResponse().getContentAsString(), Review.class));
    }

    @Test
    @DisplayName("PUT: Update Review")
    void updateReview() throws Exception {
        when(reviewService.update(REVIEW_ID, REQ)).thenReturn(RES);

        MvcResult result = this.mockMvc.perform(put("/reviews/{id}", REVIEW_ID).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(REQ)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(RES, asObject(result.getResponse().getContentAsString(), Review.class));
    }

    @Test
    @DisplayName("DELETE: Delete Review")
    void deleteReview() throws Exception {
        when(reviewService.delete(REVIEW_ID)).thenReturn("Success");

        MvcResult result = this.mockMvc.perform(delete("/reviews/{id}", REVIEW_ID))
                .andExpect(status().isOk()).andReturn();

        assertEquals("Success", result.getResponse().getContentAsString());
    }

    private Review mockReview() {
        Review review = new Review();
        review.setReviewId(REVIEW_ID);
        review.setDescription("About Gotham");
        review.setRating(5);
        return review;
    }

    private <T> T asObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private <T> T asObjectList(String json, TypeReference<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
