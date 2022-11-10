package com.dss.service;

import com.dss.dto.ActorRequestDTO;
import com.dss.dto.ActorsDTO;
import com.dss.dto.MovieDTO;
import com.dss.entity.Actor;
import com.dss.exception.ActorException;
import com.dss.exception.ActorNotFoundException;
import com.dss.repository.ActorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ActorService actorService = new ActorServiceImpl();

    private static final UUID ID = UUID.randomUUID();
    private static final UUID MOVIE_ID = UUID.randomUUID();
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
    @DisplayName("Find Movie By Actor Id When Actor Exists")
    void findMovieByActorId() {
        Optional<Actor> optionalActor = Optional.of(mockActor());
        when(actorRepository.findById(ID)).thenReturn(optionalActor);
        when(restTemplate.getForObject("http://MS-MOVIE-SERVICE/movies/" + MOVIE_ID, MovieDTO.class)).thenReturn(mockMovie());
        MovieDTO res = actorService.getMovieByActorId(ID);
        assertEquals(mockMovie(),res);
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

    @Test
    @DisplayName("Delete Actor When Actor Cannot Be Deleted")
    void deleteActorWhenActorCannotBeDeleted() {
        Optional<Actor> optionalActor = Optional.of(mockActor());
        when(actorRepository.findById(ID)).thenReturn(optionalActor);
        ActorException exception = assertThrows(ActorException.class,
                () -> actorService.delete(ID));

        assertEquals("Actor cannot be deleted", exception.getMessage());
    }

    private Actor mockActor(){
        Actor actor = new Actor();
        actor.setActorId(ID);
        actor.setFirstName("Kim");
        actor.setLastName("Santos");
        actor.setGender("Male");
        actor.setMovieId(MOVIE_ID);
        return actor;
    }
    private MovieDTO mockMovie() {
        MovieDTO movie = new MovieDTO();
        movie.setMovieId(MOVIE_ID);
        movie.setImage("No Time to Die.jpg");
        movie.setMovieTitle("No Time to Die");
        movie.setCost(3100000);
        movie.setYrOfRelease(2021);
        return movie;
    }
}
