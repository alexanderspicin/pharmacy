package com.example.pharmacy.repository;


import com.example.pharmacy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    User findUserById(Long id);

    User findUserByEmail(String email);

    void deleteUserById(Long id);

    List<User> findAll();}
