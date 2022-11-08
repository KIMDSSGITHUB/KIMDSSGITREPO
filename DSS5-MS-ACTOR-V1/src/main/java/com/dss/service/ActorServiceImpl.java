package com.dss.service;

import com.dss.dto.ActorRequestDTO;
import com.dss.dto.ActorsDTO;
import com.dss.dto.MovieDTO;
import com.dss.entity.Actor;
import com.dss.exception.ActorException;
import com.dss.exception.ActorNotFoundException;
import com.dss.repository.ActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class ActorServiceImpl  implements ActorService{

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<Actor> getActors() {
        List<Actor> actors;
        actors = actorRepository.findAll();
        return actors;
    }

    @Override
    public Actor getActorById(UUID id) {
        log.info("Inside getActorById with Id: " + id);
        return actorRepository.findById(id)
                .orElseThrow(() -> new ActorNotFoundException(id));
    }

    @Override
    public ActorsDTO getActorByMovieId(UUID id) {
        log.info("Inside getActorByMovieId with Id: " + id);
        ActorsDTO actorsDTO = new ActorsDTO();
        actorsDTO.setActors(actorRepository.findActorsByMovieId(id).orElseThrow(() -> new ActorNotFoundException(id)));
        return actorsDTO;
    }

    @Override
    public MovieDTO getMovieByActorId(UUID id) {
        Actor savedActor = getActorById(id);
        UUID movieId = savedActor.getMovieId();
        return getMoviesByMovieId(movieId);
    }


    @Override
    public ActorsDTO create(List<ActorRequestDTO> request) {
        log.info("Inside create");
        List<Actor> actorList = new ArrayList<>();
        for (ActorRequestDTO requests: request) {
        Actor actor = new Actor();
        dtoToEntity(requests,actor);
        actorList.add(actorRepository.save(actor));
        }
        ActorsDTO savedActorsDTO = new ActorsDTO();
        savedActorsDTO.setActors(actorList);
        return savedActorsDTO;
    }

    @Override
    public Actor update(UUID id, ActorRequestDTO request) {
        log.info("Inside update with id: " + id);
        return actorRepository.findById(id).map(actor -> {
            dtoToEntity(request, actor);
            return actorRepository.save(actor);
        }).orElseThrow(() -> new ActorNotFoundException(id));
    }

    @Override
    public String delete(UUID id) {
        log.info("Inside delete with id: " + id);
        try{
            Actor savedActor = getActorById(id);
            if (savedActor.getMovieId() == null){
            actorRepository.deleteById(id);
            } else throw new ActorException("Actor cannot be deleted");
        } catch (EmptyResultDataAccessException e) {
            throw new ActorNotFoundException(id);
        }
        return "Successfully deleted actor with id: " + id;
    }

    private Actor dtoToEntity(ActorRequestDTO dto, Actor entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setGender(dto.getGender());
        entity.setAge(dto.getAge());
        entity.setMovieId(dto.getMovieId());
        return entity;
    }
    private MovieDTO getMoviesByMovieId(UUID movieId) {
        MovieDTO movie;
        try {
            movie = restTemplate.getForObject("http://MS-MOVIE-SERVICE/movies/" + movieId, MovieDTO.class);
        } catch (HttpClientErrorException ex) {
            throw new ActorException(ex.getMessage());
        }

        if (Objects.isNull(movie)) {
            throw new ActorException("Actors not found.");
        }
        return movie;
    }
}
