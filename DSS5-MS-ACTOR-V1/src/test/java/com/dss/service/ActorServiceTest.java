package com.dss.service;

import com.dss.dto.ActorRequestDTO;
import com.dss.dto.ActorsDTO;
import com.dss.dto.MovieDTO;
import com.dss.entity.Actor;
import com.dss.exception.ActorNotFoundException;
import com.dss.repository.ActorRepository;
import org.junit.jupiter.api.Disabled;
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

    @InjectMocks
    private ActorService actorService = new ActorServiceImpl();

    private static final UUID ID = UUID.randomUUID();
    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final MovieDTO MOVIE_RES = new MovieDTO();
    private static final ActorRequestDTO UPDATE_REQ = new ActorRequestDTO();
    private static final ActorsDTO RES = new ActorsDTO(new ArrayList<>(new ArrayList<>(Collections.singleton(new Actor()))));
    private static final List<Actor> ACTOR_LIST = new ArrayList<>();
    private static final Actor ACTOR = new Actor();

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
    @DisplayName("Find Actor By Movie Id When Actor Exists")
    void findActorByMovieIdWhenActorExists() {
        Optional<List<Actor>> optionalActors = Optional.of(Collections.singletonList(ACTOR));

        when(actorRepository.findActorsByMovieId(MOVIE_ID)).thenReturn(optionalActors);

        ActorsDTO res = actorService.getActorByMovieId(MOVIE_ID);

        assertEquals(RES, res);
    }

    @Test
    @DisplayName("Find Actor By Movie Id When Actor Is Not Found")
    void findActorByMovieIdWhenActorNotFound() {
        when(actorRepository.findActorsByMovieId(MOVIE_ID)).thenReturn(Optional.empty());
        ActorNotFoundException exception = assertThrows(ActorNotFoundException.class,
                () -> actorService.getActorById(ID));

        assertEquals("Actor not found with Id: ".concat(ID.toString()), exception.getMessage());
    }

    @Test
    @Disabled("Work in Progress")
    @DisplayName("Find Movie By Actor Id When Actor Exists")
    void findMovieByActorId() {
        Optional<Actor> optionalActor = Optional.of(ACTOR);
        when(actorRepository.findById(ID)).thenReturn(optionalActor);
        MovieDTO res = actorService.getMovieByActorId(ID);
        assertEquals(MOVIE_RES,res);
    }


    @Test
    @DisplayName("Create Actors")
    void createActor() {
        List<ActorRequestDTO> actors = Collections.singletonList(UPDATE_REQ);
        when(actorRepository.save(ACTOR)).thenReturn(ACTOR);
        ActorsDTO res = actorService.create(actors);
        assertEquals(RES, res);
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
        Optional<Actor> optionalActor = Optional.of(ACTOR);
        when(actorRepository.findById(ID)).thenReturn(optionalActor);
        doNothing().when(actorRepository).deleteById(ID);
        actorService.delete(ID);
        verify(actorRepository, times(1)).deleteById(ID);
    }

}
