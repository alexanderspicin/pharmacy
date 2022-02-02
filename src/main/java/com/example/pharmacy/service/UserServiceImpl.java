package com.example.pharmacy.service;

import com.example.pharmacy.DTO.UserDTO;
import com.example.pharmacy.entity.Role;
import com.example.pharmacy.entity.User;
import com.example.pharmacy.payload.request.SignupRequest;
import com.example.pharmacy.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean save(SignupRequest signupRequest) throws DataIntegrityViolationException {
        if (!Objects.equals(signupRequest.getPassword(), signupRequest.getMatchingPassword())) {
            throw new RuntimeException("Password not matching!!!");
        }
        User user = User.builder()
                .username(signupRequest.getUsername())
                .firstname(signupRequest.getFirstname())
                .lastname(signupRequest.getLastname())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(Role.ClIENT)
                .build();
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDTO loadUserById(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + id);
        }
        UserDTO userDTO = userToUserDTO(user);
        return userDTO;
    }

    @Override
    public List<UserDTO> loadAll() {
        return userRepository.findAll().stream().map(this::userToUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole().name())
                .createTime(user.getCreateTime().toString()).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name:" + username);
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                roles
        );
    }

}
