package com.dss.service;

import com.dss.dto.LoginResponseDTO;
import com.dss.dto.UserLoginDTO;
import com.dss.dto.UserRequestDTO;
import com.dss.dto.UserResponseDTO;
import com.dss.entity.User;
import com.dss.exception.AdminNotFoundException;
import com.dss.exception.ConflictException;
import com.dss.exception.InvalidCredentialsException;
import com.dss.repository.UserRepository;
import com.dss.util.EncoderUtil;
import com.dss.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    User user = userRepository.findOneByEmail(login.getEmail())
            .orElseThrow(() -> new AdminNotFoundException(login.getEmail()));

    if (encoderUtil.verify(login.getPassword(), user.getPassword())) {
        String token = tokenUtil.generateToken(user);
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(token);
        response.setStatus("Success");
        return response;
    } else {
        throw new InvalidCredentialsException();
    }
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
