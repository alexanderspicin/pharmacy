package com.example.pharmacy.service;


import com.example.pharmacy.DTO.UserDTO;
import com.example.pharmacy.entity.User;
import com.example.pharmacy.payload.request.SignupRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {
    boolean save(SignupRequest signupRequest);

    UserDTO loadUserById(Long id);

    List<UserDTO> loadAll();

    UserDTO userToUserDTO(User user);
}
