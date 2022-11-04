package com.dss.service;

import com.dss.dto.LoginResponseDTO;
import com.dss.dto.UserLoginDTO;
import com.dss.proxy.AuthenticationProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    AuthenticationProxy authenticationProxy;

    public ResponseEntity<LoginResponseDTO> loginUser(UserLoginDTO login){
        return authenticationProxy.login(login);
    }
    public String getServiceInstance() {
        return authenticationProxy.getServiceInstance();
    }
}
