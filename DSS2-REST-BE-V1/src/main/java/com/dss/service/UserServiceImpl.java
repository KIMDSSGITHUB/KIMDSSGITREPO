package com.dss.service;

import com.dss.dto.UserRequestDTO;
import com.dss.dto.UserResponseDTO;
import com.dss.entity.User;
import com.dss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDTO registration(UserRequestDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        return null;
    }
}
