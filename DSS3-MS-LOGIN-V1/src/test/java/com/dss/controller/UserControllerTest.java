package com.dss.controller;

import com.dss.dto.LoginResponseDTO;
import com.dss.dto.UserLoginDTO;
import com.dss.dto.UserRequestDTO;
import com.dss.dto.UserResponseDTO;
import com.dss.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String EMAIL = "john@email.com";
    private static final String PHONE = "12345789";
    private static final String PW = "Mouse@1234";

    private static final UserRequestDTO REQ = new UserRequestDTO(FIRSTNAME, LASTNAME, EMAIL, PHONE ,PW);
    private static final UserResponseDTO RES = new UserResponseDTO(UUID.randomUUID(), FIRSTNAME, LASTNAME, EMAIL, PHONE);
    private static final LoginResponseDTO LOGIN_RES = new LoginResponseDTO();
    private static final UserLoginDTO LOGIN_REQ = new UserLoginDTO();

    @Test
    @DisplayName("POST: Registration")
    void registration() throws Exception {
        when(userService.registration(REQ)).thenReturn(RES);

        MvcResult result = this.mockMvc.perform(post("/user/registration").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(REQ)))
                .andExpect(status().isCreated())
                .andReturn();

        assertValues(asObject(result.getResponse().getContentAsString(),UserResponseDTO.class));
    }

    @Test
    @DisplayName("POST: Login")
    void login() throws Exception {
        when(userService.login(LOGIN_REQ)).thenReturn(LOGIN_RES);

        MvcResult result = this.mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(LOGIN_REQ)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(LOGIN_RES, asObject(result.getResponse().getContentAsString(), LoginResponseDTO.class));
    }

    private void assertValues(UserResponseDTO dto) {
        assertEquals(FIRSTNAME, dto.getFirstName());
        assertEquals(LASTNAME, dto.getLastName());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(PHONE, dto.getPhone());
    }

    private <T> T asObject(String json, Class<T> clazz) {
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
