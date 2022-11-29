package com.dss.service;

import com.dss.dto.MovieRequestDTO;
import com.dss.dto.MovieUpdateDTO;
import com.dss.entity.Actor;
import com.dss.entity.Movie;
import com.dss.exception.*;
import com.dss.repository.MovieRepository;
import com.dss.util.FeignServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Movie> getMoviesByActorId(UUID actorId) {
        List<Movie> movieFound = null;
        movieFound = movieRepository.findAllByActorsActorId(actorId);

        if(!movieFound.isEmpty()){
            return movieFound;
        }else{
            throw new ActorException("No movies found with actor ID:" + actorId + ".");
        }

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
        Movie savedMovie = getMovieById(id);
            int yrOfTheMovie = savedMovie.getYrOfRelease();
            LocalDate date = LocalDate.of(yrOfTheMovie,1,1);
            LocalDate today = LocalDate.now();
            if(date.isBefore(today.minusYears(1))){
            movieRepository.deleteById(id);
            return "Successfully deleted movie with id: " + id;
            } else throw new MovieCannotBeDeletedException("Can't delete movie!");
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
            throw new MovieAlreadyExistException("Movie already exists");
            }
        } else throw new NullDetailsException("Movie title is required");
    }
}
