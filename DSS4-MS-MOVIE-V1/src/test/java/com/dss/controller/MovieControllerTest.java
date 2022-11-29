package com.dss.controller;

import com.dss.dto.MovieRequestDTO;
import com.dss.dto.MovieUpdateDTO;
import com.dss.entity.Actor;
import com.dss.entity.Movie;
import com.dss.service.MovieService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final UUID ACTOR_ID = UUID.randomUUID();
    private static final List<Movie> MOVIES = new ArrayList<>();

    @Test
    @DisplayName("GET: Get Movies")
    void getMovies() throws Exception {
        List<Movie> movies = Collections.singletonList(mockMovie());
        when(movieService.getMovies()).thenReturn(MOVIES);

        MvcResult result = this.mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(MOVIES, asObjectList(result.getResponse().getContentAsString(),new TypeReference<List<Movie>>() {}));
    }

    @Test
    @DisplayName("GET: Get Movies by Actor")
    void getMoviesByActor() throws Exception {
        List<Movie> movies = Collections.singletonList(mockMovie());
        when(movieService.getMoviesByActorId(ACTOR_ID)).thenReturn(MOVIES);

        MvcResult result = this.mockMvc.perform(get("/movies/actor/{id}", ACTOR_ID))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(MOVIES, asObjectList(result.getResponse().getContentAsString(),new TypeReference<List<Movie>>() {}));
    }


    @Test
    @DisplayName("GET: Movie By Id")
    void getMovie() throws Exception {
        Movie movie = mockMovie();
        when(movieService.getMovieById(MOVIE_ID)).thenReturn(movie);

        MvcResult result = this.mockMvc.perform(get("/movies/{id}", MOVIE_ID))
                .andExpect(status().isOk())
                .andReturn();

        Movie res = asObject(result.getResponse().getContentAsString(), Movie.class);
        Actor mockActor = mockMovie().getActors().iterator().next();
        Actor resActor = res.getActors().iterator().next();
        assertEquals(mockMovie().getMovieId(),res.getMovieId());
        assertEquals(mockMovie().getImage(),res.getImage());
        assertEquals(mockMovie().getMovieTitle(),res.getMovieTitle());
        assertEquals(mockActor.getActorId(),resActor.getActorId());
        assertEquals(mockActor.getFirstName(),resActor.getFirstName());
        assertEquals(mockActor.getLastName(),resActor.getLastName());
        assertEquals(mockActor.getGender(),resActor.getGender());
        assertEquals(mockActor.getAge(),resActor.getAge());
        assertEquals(mockMovie().getYrOfRelease(),res.getYrOfRelease());
        assertEquals(mockMovie().getCost(),res.getCost());
    }

    @Test
    @DisplayName("POST: Create Movie")
    void createMovie() throws Exception {
        MovieRequestDTO req = new MovieRequestDTO(mockMovie().getImage(),mockMovie().getMovieTitle(),mockMovie().getActors(),mockMovie().getCost(),mockMovie().getYrOfRelease());
        when(movieService.create(any())).thenReturn(mockMovie());

        MvcResult result = this.mockMvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        Movie res = asObject(result.getResponse().getContentAsString(), Movie.class);
        Actor mockActor = mockMovie().getActors().iterator().next();
        Actor resActor = res.getActors().iterator().next();
        assertEquals(mockMovie().getMovieId(),res.getMovieId());
        assertEquals(mockMovie().getImage(),res.getImage());
        assertEquals(mockMovie().getMovieTitle(),res.getMovieTitle());
        assertEquals(mockActor.getActorId(),resActor.getActorId());
        assertEquals(mockActor.getFirstName(),resActor.getFirstName());
        assertEquals(mockActor.getLastName(),resActor.getLastName());
        assertEquals(mockActor.getGender(),resActor.getGender());
        assertEquals(mockActor.getAge(),resActor.getAge());
        assertEquals(mockMovie().getYrOfRelease(),res.getYrOfRelease());
        assertEquals(mockMovie().getCost(),res.getCost());
    }

    @Test
    @DisplayName("PUT: Update Movie")
    void updateMovie() throws Exception {
        when(movieService.update(any(), any())).thenReturn(mockMovie());

        MvcResult result = this.mockMvc.perform(put("/movies/{id}", MOVIE_ID).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockMovieUpdate())))
                .andExpect(status().isOk())
                .andReturn();

        Movie res = asObject(result.getResponse().getContentAsString(), Movie.class);
        Actor mockActor = mockMovie().getActors().iterator().next();
        Actor resActor = res.getActors().iterator().next();
        assertEquals(mockMovie().getMovieId(),res.getMovieId());
        assertEquals(mockMovie().getImage(),res.getImage());
        assertEquals(mockMovie().getMovieTitle(),res.getMovieTitle());
        assertEquals(mockActor.getActorId(),resActor.getActorId());
        assertEquals(mockActor.getFirstName(),resActor.getFirstName());
        assertEquals(mockActor.getLastName(),resActor.getLastName());
        assertEquals(mockActor.getGender(),resActor.getGender());
        assertEquals(mockActor.getAge(),resActor.getAge());
        assertEquals(mockMovie().getYrOfRelease(),res.getYrOfRelease());
        assertEquals(mockMovie().getCost(),res.getCost());
    }

    @Test
    @DisplayName("DELETE: Delete Movie")
    void deleteMovie() throws Exception {
        when(movieService.delete(MOVIE_ID)).thenReturn("Success");

        MvcResult result = this.mockMvc.perform(delete("/movies/{id}", MOVIE_ID))
                .andExpect(status().isOk()).andReturn();

        assertEquals("Success", result.getResponse().getContentAsString());
    }

    private Movie mockMovie(){
       Movie movie = new Movie();
       movie.setMovieId(MOVIE_ID);
       movie.setImage("Batman.jpg");
       movie.setMovieTitle("Batman");
       movie.setActors(Collections.singleton(mockActors()));
       movie.setCost(2300000);
       movie.setYrOfRelease(2019);
       return movie;
    }
    private MovieUpdateDTO mockMovieUpdate(){
        MovieUpdateDTO movieUpdate = new MovieUpdateDTO();
        movieUpdate.setImage("Batman.jpg");
        movieUpdate.setCost(2300000);
        return movieUpdate;
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

    private <T> T asObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private <T> T asObjectList(String json, TypeReference<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
