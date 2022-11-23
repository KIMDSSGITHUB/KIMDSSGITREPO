package com.dss.service;

import com.dss.dto.MovieRequestDTO;
import com.dss.dto.MovieUpdateDTO;
import com.dss.entity.Actor;
import com.dss.entity.Movie;
import com.dss.repository.MovieRepository;
import com.dss.util.FeignServiceUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    FeignServiceUtil feignServiceUtil;

    @InjectMocks
    private MovieService movieService = new MovieServiceImpl();

    private static final UUID ID = UUID.randomUUID();
    private static final UUID ACTOR_ID = UUID.randomUUID();
    private static final Movie MOVIE = new Movie();
    private static final MovieUpdateDTO UPDATE_REQ = new MovieUpdateDTO();
    private static final List<Movie> RES = new ArrayList<>();


    @Test
    @DisplayName("Find All Movies")
    void findAllMovies() {
        List<Movie> movies = new ArrayList<>();
        when(movieRepository.findAll()).thenReturn(RES);
        List<Movie> res = movieService.getMovies();
        assertEquals(movies, res);
    }

    @Test
    @DisplayName("Find Movie by Id When Movie Exists")
    void findMovieByIdWhenMovieExists() {
        Optional<Movie> optionalMovie = Optional.of(mockMovie());
        when(movieRepository.findById(ID)).thenReturn(optionalMovie);
        Movie res = movieService.getMovieById(ID);
        assertEquals(mockMovie(), res);
    }

    @Test
    @DisplayName("Create Movie")
    void createMovie() {
        when(movieRepository.save(mockMovieReq())).thenReturn(mockMovie());
        when(feignServiceUtil.findActor(ACTOR_ID)).thenReturn(mockActors());
        Movie res = movieService.create(mockMovieRequest());
        Mockito.verify(movieRepository).save(mockMovieReq());
        assertEquals(mockMovie(), res);
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
        doNothing().when(movieRepository).deleteById(ID);
        movieService.delete(ID);
        verify(movieRepository, times(1)).deleteById(ID);
    }

    private MovieRequestDTO mockMovieRequest(){
        MovieRequestDTO movieRequest = new MovieRequestDTO();
        movieRequest.setImage(mockMovie().getImage());
        movieRequest.setMovieTitle(mockMovie().getMovieTitle());
        movieRequest.setActors(Collections.singleton(mockActors()));
        movieRequest.setCost(mockMovie().getCost());
        movieRequest.setYrOfRelease(mockMovie().getYrOfRelease());
        return movieRequest;
    }

    private Movie mockMovie(){
        Movie movie = new Movie();
        movie.setMovieId(ID);
        movie.setImage("Batman.jpg");
        movie.setMovieTitle("Batman");
        movie.setActors(Collections.singleton(mockActors()));
        movie.setCost(2300000);
        movie.setYrOfRelease(2019);
        return movie;
    }

    private Movie mockMovieReq() {
        Movie movie = new Movie();
        movie.setImage("Batman.jpg");
        movie.setMovieTitle("Batman");
        movie.setActors(Collections.singleton(mockActors()));
        movie.setCost(2300000);
        movie.setYrOfRelease(2019);
        return movie;
    }

    private Actor mockActors(){
        Actor actor = new Actor();
        actor.setActorId(ACTOR_ID);
        actor.setFirstName("Daniel");
        actor.setLastName("Craig");
        actor.setGender("Male");
        actor.setAge(45);
        return actor;
    }

}
