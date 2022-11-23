package com.dss.service;

import com.dss.dto.MovieRequestDTO;
import com.dss.dto.MovieUpdateDTO;
import com.dss.entity.Movie;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    List<Movie>  getMovies();
    Movie getMovieById(UUID id);
    Movie create(MovieRequestDTO request);
    Movie update(UUID id, MovieUpdateDTO request);
    String delete(UUID id);

}
