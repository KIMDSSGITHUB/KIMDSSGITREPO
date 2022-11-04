package com.dss.controller;

import com.dss.dto.LoginResponseDTO;
import com.dss.dto.UserLoginDTO;
import com.dss.dto.UserRequestDTO;
import com.dss.dto.UserResponseDTO;
import com.dss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDTO> registration(@Valid @RequestBody UserRequestDTO request) {
        return new ResponseEntity<>(userService.registration(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginDTO login) {
        return new ResponseEntity<>(userService.login(login), HttpStatus.OK);
    }

    @GetMapping("/instance")
    public String getInstancePort(){return environment.getProperty("local.server.port");}
}
