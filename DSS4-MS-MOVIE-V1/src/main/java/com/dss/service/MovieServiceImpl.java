package com.dss.service;

import com.dss.dto.MovieRequestDTO;
import com.dss.dto.MovieUpdateDTO;
import com.dss.entity.Actor;
import com.dss.entity.Movie;
import com.dss.exception.ActorException;
import com.dss.exception.MovieAlreadyExistException;
import com.dss.exception.MovieCannotBeDeletedException;
import com.dss.exception.MovieNotFoundException;
import com.dss.repository.MovieRepository;
import com.dss.util.FeignServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private FeignServiceUtil feignServiceUtil;

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieById(UUID id) {
        log.info("Inside getMovieByMovieId with Id: " + id);
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    @Override
    public Movie create(MovieRequestDTO request) {
        log.info("Inside create");
        validate(request);
        Movie movie = new Movie();
        dtoToEntity(request,movie);
        return movieRepository.save(movie);
    }

    @Override
    public Movie update(UUID id, MovieUpdateDTO request) {
        log.info("Inside update with id: " + id);
        return movieRepository.findById(id).map(movie -> {
            updateDtoToEntity(request, movie);
            return movieRepository.save(movie);
        }).orElseThrow(() -> new MovieNotFoundException(id));
    }

    @Override
    public String delete(UUID id) {
        log.info("Inside delete with id: " + id);
        try {
            Movie savedMovie = getMovieById(id);
            int yrOfTheMovie = savedMovie.getYrOfRelease();
            LocalDate date = LocalDate.of(yrOfTheMovie,1,1);
            LocalDate today = LocalDate.now();
            if(date.isBefore(today.minusYears(1))){
            movieRepository.deleteById(id);
            } else throw new MovieCannotBeDeletedException("Can't delete movie!");
        } catch (EmptyResultDataAccessException e) {
            throw new MovieNotFoundException(id);
        }
        return "Successfully deleted movie with id: " + id;
    }

    private Movie dtoToEntity(MovieRequestDTO dto, Movie entity) {
        entity.setImage(dto.getImage());
        entity.setMovieTitle(dto.getMovieTitle());
        entity.setActors(dto.getActors());
        entity.setCost(dto.getCost());
        entity.setYrOfRelease(dto.getYrOfRelease());
        return entity;
    }

    private Movie updateDtoToEntity(MovieUpdateDTO dto, Movie entity) {
        entity.setImage(dto.getImage());
        entity.setCost(dto.getCost());
        return entity;
    }

    public void validate(MovieRequestDTO request) {
//        if (request.getMovieTitle() == null || request.getCost() == 0 || request.getYrOfRelease() == 0 || request.getImage() == null || request.getActors().isEmpty()) {
//            //• Display proper messages in case of errors or exceptions
//            throw new InvalidInputException("Please fill all details");
//        }
        for (Actor actor : request.getActors()) {
            //find actor if existing using feign actor
            try {
                feignServiceUtil.findActor(actor.getActorId());
            } catch (Exception e) {
                throw new ActorException("Actor " + actor.getFirstName() + " " + actor.getLastName() + " does not exist");
            }
        }

        if (request.getMovieTitle() != null) {
            Movie movie = movieRepository.findByMovieTitle(request.getMovieTitle());
            if (movie != null){
            throw new MovieAlreadyExistException("Movie already exist");
            }
        }
    }

}
