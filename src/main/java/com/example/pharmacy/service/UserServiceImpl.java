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
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", Pattern.CASE_INSENSITIVE);
    private final Pattern VALID_USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$");

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
    }

    @Override
    public boolean save(SignupRequest signupRequest) throws DataIntegrityViolationException {
        if (signupRequest.getUsername() == null || !VALID_USERNAME_REGEX.matcher(signupRequest.getUsername()).find()) {
            throw new RuntimeException("Incorrect username");
        }

        if (signupRequest.getPassword() == null || !VALID_PASSWORD_REGEX.matcher(signupRequest.getPassword()).find()) {
            throw new RuntimeException("Incorrect password");
        }

        if (signupRequest.getPassword() == null || !Objects.equals(signupRequest.getPassword(), signupRequest.getMatchingPassword())) {
            throw new RuntimeException("Password not matching!");
        }
        if (userRepository.findUserByEmail(signupRequest.getEmail()) != null) {
            throw new RuntimeException("User with this email already exist");
        }
        if (signupRequest.getFirstname() == null || signupRequest.getFirstname().isEmpty()) {
            throw new RuntimeException("First name is empty");
        }
        if (signupRequest.getLastname() == null || signupRequest.getLastname().isEmpty()) {
            throw new RuntimeException("Last name is empty");
        }
        if (signupRequest.getEmail() == null || !VALID_EMAIL_ADDRESS_REGEX.matcher(signupRequest.getEmail()).find()) {
            throw new RuntimeException("Incorrect email");
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
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDTO loadUserById(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        UserDTO userDTO = userToUserDTO(user);
        return userDTO;
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }
        return user;
    }

    @Override
    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public List<UserDTO> loadAll() {
        return userRepository.findAll().stream().map(this::userToUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO loadUserByEmail(String email) {
        return userToUserDTO(userRepository.findUserByEmail(email));
    }

    @Override
    public UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional
    public void updateProfile(UserDTO userDTO) {
        User savedUser = userRepository.findUserByUsername(userDTO.getUsername());
        if (savedUser == null) {
            throw new RuntimeException("User not found");
        }

        if (userDTO.getEmail() == null || !VALID_EMAIL_ADDRESS_REGEX.matcher(userDTO.getEmail()).find()) {
            throw new RuntimeException("Incorrect email");
        }

        if (userDTO.getFirstname() == null || userDTO.getFirstname().isEmpty()) {
            throw new RuntimeException("First name is empty");
        }
        if (userDTO.getLastname() == null || userDTO.getLastname().isEmpty()) {
            throw new RuntimeException("Last name is empty");
        }

        savedUser.setEmail(userDTO.getEmail());
        savedUser.setFirstname(userDTO.getFirstname());
        savedUser.setLastname(userDTO.getLastname());
        if (!(userDTO.getPassword() == null)) {
            if (userDTO.getLastname().isEmpty()) {
                savedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }else if (!VALID_PASSWORD_REGEX.matcher(userDTO.getPassword()).find()) {
                throw new RuntimeException("Incorrect password");
            }
        } else {
            savedUser.setPassword(savedUser.getPassword());
        }
        userRepository.save(savedUser);
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
                roles);
    }

}
