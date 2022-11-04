package com.dss.service;

import com.dss.dto.UserRequestDTO;
import com.dss.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO registration(UserRequestDTO request);
}
