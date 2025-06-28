package com.example.tendawaks.service;


import com.example.tendawaks.model.Users;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JWTService {

    String generateToken(Users appUser);

    String generateToken(
            Map<String, Object> claims,
            Users appUser);

    String extractUserName(String jwtToken);


    boolean isValidToken(String jwtToken, UserDetails userDetails);

}
