package com.dss.service;

import com.dss.dto.*;
import com.dss.entitty.Movie;
import com.dss.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    private MovieService movieService = new MovieServiceImpl();

    private static final UUID ID = UUID.randomUUID();
    private static final UUID ACTOR_ID = UUID.randomUUID();
    private static final Movie MOVIE = new Movie();
    private static final MovieUpdateDTO UPDATE_REQ = new MovieUpdateDTO();
    private static final List<Movie> RES = new ArrayList<>();


    @Test
    @DisplayName("Find All Reviews")
    void findAllMovies() {
        List<MovieDTO> movies = new ArrayList<>();
        when(movieRepository.findAll()).thenReturn(RES);
        List<MovieDTO> res = movieService.getMovies();
        assertEquals(movies, res);
    }

    @Test
    @DisplayName("Find Movie by Id When Movie Exists")
    void findMovieByIdWhenMovieExists() {
        Optional<Movie> optionalMovie = Optional.of(mockMovie());
        when(movieRepository.findById(ID)).thenReturn(optionalMovie);
        when(restTemplate.getForObject("http://MS-ACTOR-SERVICE/actors/movie/" + ID, ActorsResponseDTO.class)).thenReturn(mockActorResponse());
        MovieDTO res = movieService.getMovieById(ID);
        assertEquals(mockMovieDTO(), res);
    }

    @Test
    @DisplayName("Create Movie")
    void createMovie() {
        List<ActorRequestDTO> actorRequestList = Collections.singletonList(mockActorRequest());
        when(movieRepository.save(mockMovieReq())).thenReturn(mockMovie());
        when(restTemplate.postForObject("http://MS-ACTOR-SERVICE/actors", actorRequestList, ActorsResponseDTO.class)).thenReturn(mockActorResponse());
        MovieResponseDTO res = movieService.create(mockMovieRequest());
        Mockito.verify(movieRepository).save(mockMovieReq());
        Mockito.verify(restTemplate).postForObject("http://MS-ACTOR-SERVICE/actors", actorRequestList, ActorsResponseDTO.class);
        assertEquals(mockMovieResponse(), res);
    }

    @Test
    @DisplayName("Update Movie When Movie Exists")
    void updateMovieWhenMovieExists() {
        Optional<Movie> optionalReview = Optional.of(MOVIE);

        when(movieRepository.findById(ID)).thenReturn(optionalReview);
        when(movieRepository.save(MOVIE)).thenReturn(MOVIE);

        Movie res = movieService.update(ID, UPDATE_REQ);

        assertEquals(MOVIE, res);
    }

    @Test
    @DisplayName("Delete Movie")
    void deleteMovie() {
        Optional<Movie> optionalMovie = Optional.of(MOVIE);
        when(movieRepository.findById(ID)).thenReturn(optionalMovie);
        when(restTemplate.getForObject("http://MS-ACTOR-SERVICE/actors/movie/" + ID, ActorsResponseDTO.class)).thenReturn(mockActorResponse());
        doNothing().when(movieRepository).deleteById(ID);
        movieService.delete(ID);
        verify(movieRepository, times(1)).deleteById(ID);
    }

    private MovieRequestDTO mockMovieRequest(){
        MovieRequestDTO movieRequest = new MovieRequestDTO();
        movieRequest.setImage(mockMovie().getImage());
        movieRequest.setMovieTitle(mockMovie().getMovieTitle());
        movieRequest.setActor(Collections.singletonList(mockActors()));
        movieRequest.setCost(mockMovie().getCost());
        movieRequest.setYrOfRelease(mockMovie().getYrOfRelease());
        return movieRequest;
    }

    private MovieResponseDTO mockMovieResponse(){
        MovieResponseDTO movieResponse = new MovieResponseDTO();
        movieResponse.setMovie(mockMovieDTO());
        movieResponse.setMessage("Movie added.");
        return movieResponse;
    }

    private MovieDTO mockMovieDTO(){
        MovieDTO movie = new MovieDTO();;
        movie.setMovieId(mockMovie().getMovieId());
        movie.setImage("Batman.jpg");
        movie.setMovieTitle("Batman");
        movie.setActor(Collections.singletonList(mockActors()));
        movie.setCost(2300000);
        movie.setYrOfRelease(2019);
        return movie;
    }

    private Movie mockMovie(){
        Movie movie = new Movie();
        movie.setMovieId(ID);
        movie.setImage("Batman.jpg");
        movie.setMovieTitle("Batman");
        movie.setCost(2300000);
        movie.setYrOfRelease(2019);
        return movie;
    }

    private Movie mockMovieReq() {
        Movie movie = new Movie();
        movie.setImage("Batman.jpg");
        movie.setMovieTitle("Batman");
        movie.setCost(2300000);
        movie.setYrOfRelease(2019);
        return movie;
    }

    private ActorsResponseDTO mockActorResponse(){
        ActorsResponseDTO actorsResponse = new ActorsResponseDTO();
        actorsResponse.setActors(Collections.singletonList(mockActors()));
        return actorsResponse;
    }

    private ActorRequestDTO mockActorRequest(){
        ActorRequestDTO actorRequestDTO = new ActorRequestDTO();
        actorRequestDTO.setFirstName(mockActors().getFirstName());
        actorRequestDTO.setLastName(mockActors().getLastName());
        actorRequestDTO.setGender(mockActors().getGender());
        actorRequestDTO.setAge(mockActors().getAge());
        actorRequestDTO.setMovieId(null);
        return actorRequestDTO;
    }


    private ActorDTO mockActors(){
        ActorDTO actor = new ActorDTO();
        actor.setActorId(ACTOR_ID);
        actor.setFirstName("Daniel");
        actor.setLastName("Craig");
        actor.setGender("Male");
        actor.setAge(45);
        actor.setMovieId(ID);
        return actor;
    }

}
