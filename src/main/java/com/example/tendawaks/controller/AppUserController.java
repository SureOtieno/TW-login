package com.example.tendawaks.controller;



import com.example.tendawaks.authconfig.AuthenticationResponse;
import com.example.tendawaks.dto.AppUserRequest;
import com.example.tendawaks.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AppUserController {

    @Autowired
    private UsersService appUserService;

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody AppUserRequest appUserRequest) {
        return ResponseEntity.ok(appUserService.register(appUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AppUserRequest.AppUserRequestLogin request) throws Exception {
        return ResponseEntity.ok(appUserService.authenticate(request));
    }

}
