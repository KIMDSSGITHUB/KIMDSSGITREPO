package com.dss.controller;

import com.dss.dto.*;
import com.dss.entitty.Movie;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private static final MovieDTO RES = new MovieDTO();
    private static final MovieRequestDTO REQ = new MovieRequestDTO();
    private static final MovieUpdateDTO UPDATE_REQ = new MovieUpdateDTO();
    private static final MovieResponseDTO CREATE_RES = new MovieResponseDTO();
    private static final Movie UPDATE_RES = new Movie();

    @Test
    @DisplayName("GET: Get Movies")
    void getMovies() throws Exception {
        List<MovieDTO> movies = Collections.singletonList(mockMovie());
        when(movieService.getMovies()).thenReturn(movies);

        MvcResult result = this.mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(movies, asObjectList(result.getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>() {}));
    }


    @Test
    @DisplayName("GET: Movie By Id")
    void getMovie() throws Exception {
        MovieDTO movie = mockMovie();
        when(movieService.getMovieById(MOVIE_ID)).thenReturn(movie);

        MvcResult result = this.mockMvc.perform(get("/movies/{id}", MOVIE_ID))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(movie, asObject(result.getResponse().getContentAsString(), MovieDTO.class));
    }

    @Test
    @DisplayName("POST: Create Movie")
    void createMovie() throws Exception {
        when(movieService.create(REQ)).thenReturn(CREATE_RES);

        MvcResult result = this.mockMvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(CREATE_RES)))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(CREATE_RES, asObject(result.getResponse().getContentAsString(), MovieResponseDTO.class));
    }

    @Test
    @DisplayName("PUT: Update Movie")
    void updateMovie() throws Exception {
        when(movieService.update(MOVIE_ID, UPDATE_REQ)).thenReturn(UPDATE_RES);

        MvcResult result = this.mockMvc.perform(put("/movies/{id}", MOVIE_ID).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(UPDATE_REQ)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(UPDATE_RES, asObject(result.getResponse().getContentAsString(), Movie.class));
    }

    @Test
    @DisplayName("DELETE: Delete Movie")
    void deleteMovie() throws Exception {
        when(movieService.delete(MOVIE_ID)).thenReturn("Success");

        MvcResult result = this.mockMvc.perform(delete("/movies/{id}", MOVIE_ID))
                .andExpect(status().isOk()).andReturn();

        assertEquals("Success", result.getResponse().getContentAsString());
    }

    private MovieDTO mockMovie(){
       MovieDTO movie = new MovieDTO();
       UUID movieId = UUID.randomUUID();
       movie.setMovieId(movieId);
       movie.setImage("Batman.jpg");
       movie.setMovieTitle("Batman");
       movie.setActor(Collections.singletonList(mockActors(movieId)));
       movie.setCost(2300000);
       movie.setYrOfRelease(2019);
       return movie;
    }

    private ActorDTO mockActors(UUID movieId){
        ActorDTO actor = new ActorDTO();
        actor.setActorId(UUID.randomUUID());
        actor.setFirstName("Daniel");
        actor.setLastName("Craig");
        actor.setGender("Male");
        actor.setAge(45);
        actor.setMovieId(movieId);
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
