package com.dss.service;

import com.dss.dto.ActorRequestDTO;
import com.dss.entity.Actor;
import com.dss.entity.MovieActor;
import com.dss.exception.ActorCannotBeDeletedException;
import com.dss.exception.ActorNotFoundException;
import com.dss.repository.ActorRepository;
import com.dss.repository.MovieActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ActorServiceImpl  implements ActorService{

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieActorRepository movieActorRepository;

    @Override
    public List<Actor> getActors() {
        return actorRepository.findAll();
    }

    @Override
    public Actor getActorById(UUID id) {
        log.info("Inside getActorById with Id: " + id);
        return actorRepository.findById(id)
                .orElseThrow(() -> new ActorNotFoundException(id));
    }

    @Override
    public Actor create(ActorRequestDTO request) {
        log.info("Inside create");
        Actor actor = new Actor();
        dtoToEntity(request,actor);
        return actorRepository.save(actor);
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
            getActorById(id);
            List<MovieActor> savedMovieActor = movieActorRepository.findByActorId(id);
            if (savedMovieActor.isEmpty()){
            actorRepository.deleteById(id);
            } else throw new ActorCannotBeDeletedException();
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
        return entity;
    }
}
