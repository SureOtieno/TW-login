package com.example.tendawaks.model.repository;

import com.example.tendawaks.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
}
