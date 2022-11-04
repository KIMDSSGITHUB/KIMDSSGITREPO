package com.dss.proxy;

import com.dss.dto.LoginResponseDTO;
import com.dss.dto.UserLoginDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="ms-login-service")
public interface AuthenticationProxy {



    @PostMapping("/user/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginDTO login);

    @GetMapping("/user/instance")
    public String getServiceInstance();
}
