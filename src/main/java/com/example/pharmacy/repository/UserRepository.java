package com.example.pharmacy.repository;

import com.example.pharmacy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String Username);
}
