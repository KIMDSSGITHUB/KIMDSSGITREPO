package com.dss.controller;

import com.dss.dto.MovieRequestDTO;
import com.dss.dto.MovieUpdateDTO;
import com.dss.entity.Movie;
import com.dss.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping()
    public ResponseEntity<List<Movie>> getMovies() {
        return new ResponseEntity<>(movieService.getMovies(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable UUID id) {
        return new ResponseEntity<>(movieService.getMovieById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Movie> create(@RequestBody MovieRequestDTO request) {
        return new ResponseEntity<>(movieService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable UUID id, @RequestBody MovieUpdateDTO newMovie) {
        return new ResponseEntity<>(movieService.update(id,newMovie), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(movieService.delete(id),HttpStatus.OK);
    }

}
