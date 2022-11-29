package com.dss.service;

import com.dss.dto.ActorRequestDTO;
import com.dss.entity.Actor;
import com.dss.entity.MovieActor;
import com.dss.exception.ActorCannotBeDeletedException;
import com.dss.exception.ActorNotFoundException;
import com.dss.repository.ActorRepository;
import com.dss.repository.MovieActorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private MovieActorRepository movieActorRepository;

    @InjectMocks
    private ActorService actorService = new ActorServiceImpl();

    private static final UUID ID = UUID.randomUUID();
    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final ActorRequestDTO UPDATE_REQ = new ActorRequestDTO();
    private static final List<Actor> ACTOR_LIST = new ArrayList<>();
    private static final Actor ACTOR = new Actor();
    private static final List<MovieActor> MOVIE_ACTOR = new ArrayList<>();

    @Test
    @DisplayName("Find All Actors")
    void findAllActors() {
        when(actorRepository.findAll()).thenReturn(ACTOR_LIST);
        List<Actor> res = actorService.getActors();
        assertEquals(ACTOR_LIST, res);
    }

    @Test
    @DisplayName("Find Actor By Id When Actor Exists")
    void findActorByIdWhenActorExists() {
        Optional<Actor> optionalActor = Optional.of(ACTOR);

        when(actorRepository.findById(ID)).thenReturn(optionalActor);

        Actor res = actorService.getActorById(ID);

        assertEquals(ACTOR, res);
    }

    @Test
    @DisplayName("Find Actor By Id When Actor Is Not Found")
    void findActorByIdWhenActorNotFound() {
        when(actorRepository.findById(ID)).thenReturn(Optional.empty());

        ActorNotFoundException exception = assertThrows(ActorNotFoundException.class,
                () -> actorService.getActorById(ID));

        assertEquals("Actor not found with Id: ".concat(ID.toString()), exception.getMessage());
    }


    @Test
    @DisplayName("Create Actors")
    void createActor() {
        when(actorRepository.save(mockCreateActor())).thenReturn(mockActor());
        Actor res = actorService.create(mockRequestActor());
        assertEquals(mockActor(), res);
    }

    @Test
    @DisplayName("Update Actor When Actor Exists")
    void updateActorWhenActorExists() {
        Optional<Actor> optionalActor = Optional.of(ACTOR);

        when(actorRepository.findById(ID)).thenReturn(optionalActor);
        when(actorRepository.save(ACTOR)).thenReturn(ACTOR);

        Actor res = actorService.update(ID, UPDATE_REQ);
        assertEquals(ACTOR, res);
    }

    @Test
    @DisplayName("Delete Actor")
    void deleteActor() {
        Optional<Actor> optionalActor = Optional.of(mockActor());
        when(actorRepository.findById(ID)).thenReturn(optionalActor);
        when(movieActorRepository.findByActorId(ID)).thenReturn(MOVIE_ACTOR);
        doNothing().when(actorRepository).deleteById(ID);
        actorService.delete(ID);
        verify(actorRepository, times(1)).deleteById(ID);
    }

    @Test
    @DisplayName("Delete Actor When Actor Cannot Be Deleted")
    void deleteActorWhenActorCannotBeDeleted() {
        Optional<Actor> optionalActor = Optional.of(mockActor());
        List<MovieActor> listMockMovieActor = Collections.singletonList(mockMovieActor());
        when(movieActorRepository.findByActorId(ID)).thenReturn(listMockMovieActor);
        when(actorRepository.findById(ID)).thenReturn(optionalActor);
        ActorCannotBeDeletedException exception = assertThrows(ActorCannotBeDeletedException.class,
                () -> actorService.delete(ID));

        assertEquals("Actor cannot be deleted with existing movies", exception.getMessage());
    }

    private MovieActor mockMovieActor() {
        MovieActor movieActor = new MovieActor();
        movieActor.setActorId(ID);
        movieActor.setMovieId(MOVIE_ID);
        return movieActor;
    }

    private Actor mockActor(){
        Actor actor = new Actor();
        actor.setActorId(ID);
        actor.setFirstName("Kim");
        actor.setLastName("Santos");
        actor.setGender("Male");
        actor.setAge(26);
        return actor;
    }
    private Actor mockCreateActor(){
        Actor actor = new Actor();
        actor.setFirstName("Kim");
        actor.setLastName("Santos");
        actor.setGender("Male");
        actor.setAge(26);
        return actor;
    }


    private ActorRequestDTO mockRequestActor() {
        ActorRequestDTO newActor = new ActorRequestDTO();
        newActor.setFirstName(mockActor().getFirstName());
        newActor.setLastName(mockActor().getLastName());
        newActor.setGender(mockActor().getGender());
        newActor.setAge(mockActor().getAge());
        return newActor;
    }
}
