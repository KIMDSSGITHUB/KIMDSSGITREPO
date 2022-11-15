package com.dss.service;

import com.dss.dto.*;
import com.dss.entity.Movie;
import com.dss.exception.ActorException;
import com.dss.exception.ExternalServiceException;
import com.dss.exception.MovieCannotBeDeletedException;
import com.dss.exception.MovieNotFoundException;
import com.dss.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<MovieDTO> getMovies() {
        List<Movie> movies;
        movies = movieRepository.findAll();
        List<MovieDTO> movieList = new ArrayList<>();
        for (Movie movie: movies){
            movieList.add(getMovieById(movie.getMovieId()));
        }
        return movieList;
    }

    @Override
    public MovieDTO getMovieById(UUID id) {
        log.info("Inside getMovieByMovieId with Id: " + id);
        ActorsResponseDTO actorsResponseDTO = getActorsByMovieId(id);
        Movie savedMovie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
        return entityToMovieDTO(savedMovie, actorsResponseDTO,new MovieDTO());
    }

    @Override
    public MovieResponseDTO create(MovieRequestDTO request) {
        log.info("Inside create");
        Movie movie = new Movie();
        dtoToEntity(request,movie);
        Movie savedMovie = movieRepository.save(movie);
        List<ActorRequestDTO> actorRequestList = new ArrayList<>();
        for (ActorDTO actor: request.getActor()){
            ActorRequestDTO actorRequest = new ActorRequestDTO();
            actorRequest.setFirstName(actor.getFirstName());
            actorRequest.setLastName(actor.getLastName());
            actorRequest.setGender(actor.getGender());
            actorRequest.setAge(actor.getAge());
            actorRequest.setMovieId(movie.getMovieId());
            actorRequestList.add(actorRequest);
        }
        ActorsResponseDTO restActor = addActor(actorRequestList);
        String message = "Movie added.";
        return entityToDTO(savedMovie,restActor.getActors(),new MovieResponseDTO(), message);
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
            MovieDTO savedMovie = getMovieById(id);
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
        entity.setCost(dto.getCost());
        entity.setYrOfRelease(dto.getYrOfRelease());
        return entity;
    }

    private Movie updateDtoToEntity(MovieUpdateDTO dto, Movie entity) {
        entity.setImage(dto.getImage());
        entity.setCost(dto.getCost());
        return entity;
    }

    private MovieDTO entityToMovieDTO(Movie entity, ActorsResponseDTO actor, MovieDTO dto) {
        dto.setMovieId(entity.getMovieId());
        dto.setImage(entity.getImage());
        dto.setMovieTitle(entity.getMovieTitle());
        dto.setActor(actor.getActors());
        dto.setCost(entity.getCost());
        dto.setYrOfRelease(entity.getYrOfRelease());
        return dto;
    }

    private MovieResponseDTO entityToDTO(Movie entity,List<ActorDTO> actors,MovieResponseDTO dto, String message) {
        for(ActorDTO actor: actors){
        actor.setMovieId(entity.getMovieId());
        }
        MovieDTO toMovie = new MovieDTO();
        toMovie.setMovieId(entity.getMovieId());
        toMovie.setImage(entity.getImage());
        toMovie.setMovieTitle(entity.getMovieTitle());
        toMovie.setActor(actors);
        toMovie.setCost(entity.getCost());
        toMovie.setYrOfRelease(entity.getYrOfRelease());
        dto.setMovie(toMovie);
        dto.setMessage(message);
        return dto;
    }

    private ActorsResponseDTO addActor(List<ActorRequestDTO> newActor) {
        ActorsResponseDTO actorList;
        try {
             actorList = restTemplate.postForObject("http://MS-ACTOR-SERVICE/actors", newActor, ActorsResponseDTO.class);
        } catch (HttpClientErrorException ex) {
            throw new ExternalServiceException(ex.getMessage());
        }
        if(Objects.isNull(actorList)) {
            throw new ExternalServiceException("Actor not created.");
        }
        return actorList;
    }

    private ActorsResponseDTO getActorsByMovieId(UUID movieId) {
        ActorsResponseDTO actorsResponseDTO;
        try {
            actorsResponseDTO = restTemplate.getForObject("http://MS-ACTOR-SERVICE/actors/movie/" + movieId, ActorsResponseDTO.class);
        } catch (HttpClientErrorException ex) {
            throw new ActorException(ex.getMessage());
        }

        if (Objects.isNull(actorsResponseDTO)) {
            throw new ActorException("Actors not found.");
        }
        return actorsResponseDTO;
    }
}
