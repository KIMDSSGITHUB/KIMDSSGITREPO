package com.dss.dto;

import com.dss.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    private String description;
    private int rating;
    private Movie movie;
}
