package com.dss.service;

import com.dss.dto.LoginResponseDTO;
import com.dss.dto.UserLoginDTO;
import com.dss.dto.UserRequestDTO;
import com.dss.dto.UserResponseDTO;
import com.dss.entity.User;
import com.dss.exception.BadRequestException;
import com.dss.exception.InvalidCredentialsException;
import com.dss.repository.UserRepository;
import com.dss.util.EncoderUtil;
import com.dss.util.TokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private PasswordEncoder passwordEncoder = Mockito.spy(new BCryptPasswordEncoder());
    @Spy
    @InjectMocks
    private EncoderUtil encoderUtil = Mockito.spy(EncoderUtil.class);

    @Mock
    private TokenUtil tokenUtil;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    private static final UUID ID = UUID.randomUUID();
    private static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJVc2VyIFNlcnZpY2UiLCJzdWIiOiI5MTcyZGUyNS0zMTBlLTRmNGUtYjkyMC05MGVhMzc2YzcwZDMiLCJpYXQiOjE2Njc4MDM1MjMsImV4cCI6MTY2NzgwNDQyMywidXNlciI6InJ1c3NlckBnbWFpbC5jb20ifQ.AquWFWo6d3wPnWjtSy7jpgovEbkemeN56Cbg1YX8zuhytbz8ADNBWZqn7Y6qRyuA2dniG_W4mjWhw56TIZB3fw";


    @Test
    @DisplayName("Register User")
    void registerUser() {
        User unsavedUser = mockUserReq();
        UserRequestDTO userRequest = mockUserRequest();
        User user = mockUser();

        //verify password
        String password = encoderUtil.encode(userRequest.getPassword());
        doReturn(password).when(encoderUtil).encode(userRequest.getPassword());
        Mockito.verify(encoderUtil).encode(userRequest.getPassword());
        user.setPassword(password);
        unsavedUser.setPassword(password);

        //can't call mockito.verify on Mocks.
        //mockito.verify is only called on Spy
        //reference https://stackoverflow.com/a/63938231
        doReturn(user).when(userRepository).save(unsavedUser);

        UserResponseDTO res = userService.registration(userRequest);

        assertEquals(mockUserResponse(), res);
    }

    @Test
    @DisplayName("Email Already Registered")
    void throwValidationException() {
        User user = mockUser();
        UserRequestDTO userRequest = mockUserRequest();
    when(userRepository.findOneByEmail(user.getEmail())).thenReturn(Optional.of(user));
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> userService.registration(userRequest));
        assertEquals("Email already in use.", exception.getMessage());
    }

    @Test
    @DisplayName("Login User With Valid Credentials")
    void loginUserValid() {
        when(encoderUtil.verify(mockLogin().getPassword(), mockUser().getPassword())).thenReturn(true);
        when(tokenUtil.generateToken(mockUser())).thenReturn(TOKEN);
        when(userRepository.findOneByEmail(mockUser().getEmail())).thenReturn(Optional.of(mockUser()));
        LoginResponseDTO res = userService.login(mockLogin());
        assertEquals(mockLoginResponse(), res);
    }

    @Test
    @DisplayName("Login User With Invalid Credentials")
    void loginUserInvalid() {
        UserLoginDTO login = mockLogin();
        when(encoderUtil.verify(mockLogin().getPassword(), mockUser().getPassword())).thenReturn(false);
        when(userRepository.findOneByEmail(mockUser().getEmail())).thenReturn(Optional.of(mockUser()));
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> userService.login(login));
        assertEquals("Email or password is incorrect", exception.getMessage());
    }

    private UserLoginDTO mockLogin(){
        UserLoginDTO login = new UserLoginDTO();
        login.setEmail(mockUser().getEmail());
        login.setPassword(mockUser().getPassword());
        return login;
    }

    private LoginResponseDTO mockLoginResponse() {
        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setAccessToken(TOKEN);
        loginResponse.setStatus("Success");
        return loginResponse;
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
