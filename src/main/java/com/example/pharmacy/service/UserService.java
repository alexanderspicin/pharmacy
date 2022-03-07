package com.example.pharmacy.service;


import com.example.pharmacy.DTO.UserDTO;
import com.example.pharmacy.entity.User;
import com.example.pharmacy.payload.request.SignupRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;


public interface UserService extends UserDetailsService {
    boolean save(SignupRequest signupRequest);
    void save(User user);

    UserDTO loadUserById(Long id);

    List<UserDTO> loadAll();

    UserDTO loadUserByEmail(String email);

    UserDTO userToUserDTO(User user);

    void updateProfile(UserDTO userDTO);

    User getCurrentUser(Principal principal);

    User findUserByUsername(String username);
    void deleteUser(Long id);
}
