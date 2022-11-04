package com.dss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    private UUID movieId;
    private String image;
    private String movieTitle;
    private List<ActorDTO> actor;
    private double cost;
    private int yrOfRelease;
}
