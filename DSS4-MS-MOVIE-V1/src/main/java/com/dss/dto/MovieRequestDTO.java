package com.dss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequestDTO {

    private String image;
    private String movieTitle;
    private List<ActorDTO> actor;
    private double cost;
    private int yrOfRelease;
}
