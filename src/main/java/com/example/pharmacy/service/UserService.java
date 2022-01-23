package com.example.pharmacy.service;

import com.example.pharmacy.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean save(UserDTO userDTO);

}
