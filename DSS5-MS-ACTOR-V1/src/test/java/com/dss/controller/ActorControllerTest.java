package com.dss.controller;

import com.dss.dto.ActorRequestDTO;
import com.dss.dto.Actors;
import com.dss.dto.MovieDTO;
import com.dss.entity.Actor;
import com.dss.service.ActorService;
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

@WebMvcTest(controllers = ActorController.class)
public class ActorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActorService actorService;

    private static final UUID ACTOR_ID = UUID.randomUUID();
    private static final UUID MOVIE_ID = UUID.randomUUID();

    private static final ActorRequestDTO REQ = new ActorRequestDTO();
    private static final Actor UPDATE_RES = new Actor();

    @Test
    @DisplayName("GET: Get Actors")
    void getActors() throws Exception {
        List<Actor> actors = Collections.singletonList(mockActor());
        when(actorService.getActors()).thenReturn(actors);

        MvcResult result = this.mockMvc.perform(get("/actors"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(actors, asObjectList(result.getResponse().getContentAsString(),new TypeReference<List<Actor>>() {}));
    }

    @Test
    @DisplayName("GET: Actor By Id")
    void getActor() throws Exception {
        Actor actor = mockActor();
        when(actorService.getActorById(ACTOR_ID)).thenReturn(actor);

        MvcResult result = this.mockMvc.perform(get("/actors/{id}", ACTOR_ID))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(actor, asObject(result.getResponse().getContentAsString(), Actor.class));
    }

    @Test
    @DisplayName("GET: Actor By Movie Id")
    void getActorByMovie() throws Exception {
        Actors actors = mockActors();
        when(actorService.getActorByMovieId(MOVIE_ID)).thenReturn(actors);

        MvcResult result = this.mockMvc.perform(get("/actors/movie/{id}", MOVIE_ID))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(actors, asObject(result.getResponse().getContentAsString(), Actors.class));
    }

    @Test
    @DisplayName("GET: Movie By Actor Id")
    void getMovieByActor() throws Exception {
        MovieDTO movie = mockMovie();
        when(actorService.getMovieByActorId(ACTOR_ID)).thenReturn(movie);

        MvcResult result = this.mockMvc.perform(get("/actors/{id}/movie", ACTOR_ID))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(movie, asObject(result.getResponse().getContentAsString(), MovieDTO.class));
    }

    @Test
    @DisplayName("POST: Create Actor")
    void createActor() throws Exception {
        List<ActorRequestDTO> req = Collections.singletonList(mockCreateActor());
        when(actorService.create(req)).thenReturn(mockActors());

        MvcResult result = this.mockMvc.perform(post("/actors").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(mockActors(), asObject(result.getResponse().getContentAsString(), Actors.class));
    }

    @Test
    @DisplayName("PUT: Update Actor")
    void updateActor() throws Exception {
        when(actorService.update(ACTOR_ID, REQ)).thenReturn(UPDATE_RES);

        MvcResult result = this.mockMvc.perform(put("/actors/{id}", ACTOR_ID).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(REQ)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(UPDATE_RES, asObject(result.getResponse().getContentAsString(), Actor.class));
    }

    @Test
    @DisplayName("DELETE: Delete Actor")
    void deleteActor() throws Exception {
        when(actorService.delete(ACTOR_ID)).thenReturn("Success");

        MvcResult result = this.mockMvc.perform(delete("/actors/{id}", ACTOR_ID))
                .andExpect(status().isOk()).andReturn();

        assertEquals("Success", result.getResponse().getContentAsString());
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

    private Actor mockActor(){
        Actor actor = new Actor();
        UUID actorId = UUID.randomUUID();
        actor.setActorId(ACTOR_ID);
        actor.setFirstName("Tom");
        actor.setLastName("Cruise");
        actor.setGender("Male");
        actor.setAge(60);
        actor.setMovieId(MOVIE_ID);
        return actor;
    }

    private ActorRequestDTO mockCreateActor(){
        ActorRequestDTO actor = new ActorRequestDTO();
        actor.setFirstName("Tom");
        actor.setLastName("Cruise");
        actor.setGender("Male");
        actor.setAge(60);
        actor.setMovieId(MOVIE_ID);
        return actor;
    }

    private Actors mockActors(){
        Actors actors = new Actors();
        actors.setActors(Collections.singletonList(mockActor()));
        return actors;
    }

    private MovieDTO mockMovie(){
        MovieDTO movie = new MovieDTO();
        movie.setMovieId(UUID.randomUUID());
        movie.setImage("Top Gun.jpg");
        movie.setMovieTitle("Top Gun");
        movie.setCost(15000000);
        movie.setYrOfRelease(1986);
        return movie;
    }
}
