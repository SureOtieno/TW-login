package com.example.tendawaks.service;

import com.example.tendawaks.model.PrincipalUser;
import com.example.tendawaks.model.Role;
import com.example.tendawaks.model.Users;
import com.example.tendawaks.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    private PrincipalUser principalUser;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users appUser = userRepo.findByUsername(userName);

        if (appUser == null) {
            throw new UsernameNotFoundException("User " + userName + " not found");
        }

        if (appUser.getRole() == null) {
            appUser.setRole(Role.USER); // Fallback
            appUser = userRepo.save(appUser);
        }

        return new PrincipalUser(appUser);
    }
}
