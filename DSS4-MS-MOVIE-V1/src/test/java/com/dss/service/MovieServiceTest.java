package com.dss.service;

import com.dss.dto.MovieRequestDTO;
import com.dss.dto.MovieUpdateDTO;
import com.dss.entity.Actor;
import com.dss.entity.Movie;
import com.dss.exception.*;
import com.dss.repository.MovieRepository;
import com.dss.util.FeignServiceUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Find All Movies Of Actor")
    void findAllMoviesOfActor() {
        List<Movie> movies = Collections.singletonList(mockMovie());
        when(movieRepository.findAllByActorsActorId(ACTOR_ID)).thenReturn(movies);
        List<Movie> res = movieService.getMoviesByActorId(ACTOR_ID);
        assertEquals(movies, res);
    }

    @Test
    @DisplayName("Find All Movies Of Actor When Movies Are Not Found")
    void findAllMoviesOfActorWhenMoviesAreNotFound() {
        List<Movie> movies = new ArrayList<>();
        when(movieRepository.findAllByActorsActorId(ACTOR_ID)).thenReturn(movies);
       ActorException exception =  assertThrows(ActorException.class, () -> movieService.getMoviesByActorId(ACTOR_ID));
        assertEquals("No movies found with actor ID:" + ACTOR_ID + ".", exception.getMessage());
    }

    @Test
    @DisplayName("Find Movie by Id When Movie Exists")
    void findMovieByIdWhenMovieExists() {
        Optional<Movie> optionalMovie = Optional.of(mockMovie());
        when(movieRepository.findById(ID)).thenReturn(optionalMovie);
        Movie res = movieService.getMovieById(ID);
        assertNotNull(res);
        verify(movieRepository).findById(ID);
    }

    @Test
    @DisplayName("Create Movie")
    void createMovie() {
        when(feignServiceUtil.findActor(ACTOR_ID)).thenReturn(mockActors());
        when(movieRepository.save(mockMovieReq())).thenReturn(mockMovie());
        Movie res = movieService.create(mockMovieRequest());
        assertNull(res);
    }

//    @Test
//    @DisplayName("Create Movie But Movie Title is Blank")
//    void createMovieButMovieTitleIsBlank() {
//        MovieRequestDTO movieReq = mockMovieRequest();
//        movieReq.setMovieTitle("");
//        when(feignServiceUtil.findActor(ACTOR_ID)).thenReturn(mockActors());
//        when(movieRepository.save(mockMovieReq())).thenReturn(mockMovie());
//        Movie res = movieService.create(movieReq);
//        assertNull(res);
//    }

    @Test
    @DisplayName("Create Movie But Actor Not Found")
    void createMovieButActorNotFound() {
        MovieRequestDTO movieReq = mockMovieRequest();
        Actor actor = new Actor();
        when(feignServiceUtil.findActor(ACTOR_ID)).thenReturn(actor);
        when(movieRepository.findByMovieTitle(mockMovieRequest().getMovieTitle()));
        ActorException exception =  assertThrows(ActorException.class, () -> movieService.create(movieReq));
        assertEquals("Actor " + mockActors().getFirstName() + " " + mockActors().getLastName() + " does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Create Movie But Movie Already Exists")
    void createMovieButMovieAlreadyExists() {
        MovieRequestDTO movieReq = mockMovieRequest();
        Actor actor = mockActors();
        when(feignServiceUtil.findActor(ACTOR_ID)).thenReturn(actor);
        when(movieRepository.findByMovieTitle(mockMovieRequest().getMovieTitle())).thenReturn(mockMovie());
        MovieAlreadyExistException exception =  assertThrows(MovieAlreadyExistException.class, () -> movieService.create(movieReq));
        assertEquals("Movie already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Create Movie But Movie Title Is Blank")
    void createMovieButMovieTitleIsBlank() {
        MovieRequestDTO movieReq = mockMovieRequest();
        movieReq.setMovieTitle(null);
        Actor actor = mockActors();
        when(feignServiceUtil.findActor(ACTOR_ID)).thenReturn(actor);
        NullDetailsException exception =  assertThrows(NullDetailsException.class, () -> movieService.create(movieReq));
        assertEquals("Movie title is required", exception.getMessage());
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

    @Test
    @DisplayName("Delete Movie Unsuccessful")
    void deleteMovieUnsuccessful() {
        MOVIE.setYrOfRelease(2022);
        Optional<Movie> optionalMovie = Optional.of(MOVIE);
        when(movieRepository.findById(ID)).thenReturn(optionalMovie);
        MovieCannotBeDeletedException exception =  assertThrows(MovieCannotBeDeletedException.class, () -> movieService.delete(ID));
        assertEquals("Can't delete movie!", exception.getMessage());
    }

    @Test
    @DisplayName("Delete Movie But Movie Not Found")
    void deleteMovieButMovieNotFound() {
        Optional<Movie> optionalMovie = Optional.empty();
        when(movieRepository.findById(ID)).thenReturn(optionalMovie);
        MovieNotFoundException exception =  assertThrows(MovieNotFoundException.class, () -> movieService.delete(ID));
        assertEquals("Movie not found with Id: ".concat(ID.toString()), exception.getMessage());
    }

    private MovieRequestDTO mockMovieRequest(){
        MovieRequestDTO movieRequest = new MovieRequestDTO();
        movieRequest.setImage("Batman.jpg");
        movieRequest.setMovieTitle("Batman");
        movieRequest.setActors(Collections.singleton(mockActors()));
        movieRequest.setCost(2300000);
        movieRequest.setYrOfRelease(2019);
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
