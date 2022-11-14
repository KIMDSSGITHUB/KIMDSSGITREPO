package com.dss.service;

import com.dss.dto.UserRequestDTO;
import com.dss.dto.UserResponseDTO;
import com.dss.entity.User;
import com.dss.repository.UserRepository;
import com.dss.util.EncoderUtil;
import com.dss.util.TokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Import({TestConfig.class})
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Autowired
    private EncoderUtil encoderUtil;

    @Autowired
    private TokenUtil tokenUtil;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    private static final UUID ID = UUID.randomUUID();

    private static final User REQ = new User();




    @Test
    @DisplayName("Register User")
    void registerUser() {
//        String encodedPass = encoderUtil.encode("P@ssw0rd");
//        when(encoderUtil.encode("P@ssw0rd")).thenReturn(anyString());
//        when(userRepository.save(REQ)).thenReturn(mockUser());
        UserResponseDTO res = userService.registration(mockUserRequest());
        Mockito.verify(userRepository).save(mockUser());
        assertEquals(mockUserResponse(), res);
    }


    private User mockUser() {
        User user = new User();
        user.setId(ID);
        user.setFirstName("Kim");
        user.setLastName("Santos");
        user.setEmail("kim@gmail.com");
        user.setPhone("09123456789");
        user.setPassword("P@ssw0rd");
        return user;
    }


    private User mockUserReq() {
        User user = new User();
        user.setFirstName("Kim");
        user.setLastName("Santos");
        user.setEmail("kim@gmail.com");
        user.setPhone("09123456789");
        user.setPassword("P@ssw0rd");
        return user;
    }

    private UserRequestDTO mockUserRequest() {
        UserRequestDTO user = new UserRequestDTO();
        user.setFirstName("Kim");
        user.setLastName("Santos");
        user.setEmail("kim@gmail.com");
        user.setPhone("09123456789");
        user.setPassword("P@ssw0rd");
        return user;
    }

    private UserResponseDTO mockUserResponse() {
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setId(mockUser().getId());
        userResponse.setFirstName(mockUser().getFirstName());
        userResponse.setLastName(mockUser().getLastName());
        userResponse.setEmail(mockUser().getEmail());
        userResponse.setPhone(mockUser().getPhone());
        return userResponse;
    }
}
