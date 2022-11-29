package com.dss.service;

import com.dss.dto.LoginResponseDTO;
import com.dss.dto.UserLoginDTO;
import com.dss.dto.UserRequestDTO;
import com.dss.dto.UserResponseDTO;
import com.dss.entity.User;
import com.dss.exception.ConflictException;
import com.dss.exception.InvalidCredentialsException;
import com.dss.exception.NullCredentialsException;
import com.dss.repository.UserRepository;
import com.dss.util.EncoderUtil;
import com.dss.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private EncoderUtil encoderUtil;

    @Override
    public UserResponseDTO registration(UserRequestDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setPassword(encoderUtil.encode(request.getPassword()));

        userRepository.findOneByEmail(request.getEmail()).ifPresent(c -> {
            throw new ConflictException("Email already in use.");
        });
        userRepository.findOneByPhone(request.getPhone()).ifPresent(c -> {
            throw new ConflictException("Phone already in use.");
        });
         User userEntity= userRepository.save(user);
        return entityToDTO(new UserResponseDTO(), userEntity);
    }

    @Override
    public LoginResponseDTO login(UserLoginDTO login) {
    Optional<User> user = userRepository.findOneByEmail(login.getEmail());
        if (login.getEmail().isBlank() || login.getPassword().isBlank() ) {
                throw new NullCredentialsException("Please input your email/password");
    }
    if (user.isPresent()) {
        User savedUser = user.get();
    if (encoderUtil.verify(login.getPassword(), savedUser.getPassword())) {
        String token = tokenUtil.generateToken(savedUser);
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(token);
        response.setStatus("Success");
        return response;
    } else {
        throw new InvalidCredentialsException();
    }
    } else throw new  InvalidCredentialsException();
}


    private UserResponseDTO entityToDTO(UserResponseDTO dto, User entity) {
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        return dto;
    }
}
