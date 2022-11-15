package com.dss.service;

import com.dss.dto.MovieDTO;
import com.dss.dto.MovieRequestDTO;
import com.dss.dto.MovieResponseDTO;
import com.dss.dto.MovieUpdateDTO;
import com.dss.entity.Movie;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    List<MovieDTO>  getMovies();
    MovieDTO getMovieById(UUID id);
    MovieResponseDTO create(MovieRequestDTO request);
    Movie update(UUID id, MovieUpdateDTO request);
    String delete(UUID id);

}
