package com.dss.dto;

import com.dss.entity.Actor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequestDTO {

    @NotBlank(message = "image is required")
    private String image;
    @NotBlank(message = "title is required")
    private String movieTitle;
    private Set<Actor> actors;
    @NotBlank(message = "cost is required")
    private double cost;
    @NotBlank(message = "year is required")
    private int yrOfRelease;
}
