package com.example.tendawaks.service;

import com.example.tendawaks.authconfig.AuthenticationResponse;
import com.example.tendawaks.dto.AppUserRequest;
import com.example.tendawaks.model.Users;
import org.springframework.http.ResponseEntity;

public interface UsersService {
    ResponseEntity<Object> register(AppUserRequest appUserRequest);

    AuthenticationResponse authenticate(AppUserRequest.AppUserRequestLogin appRequest) throws Exception;

    AuthenticationResponse handleOAuth2Login(String email, String name,  String googleUid);

//    Users findOrCreateUser(String email, String name);
}
