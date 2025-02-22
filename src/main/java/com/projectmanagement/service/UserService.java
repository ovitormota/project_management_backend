package com.projectmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectmanagement.config.JwtUtil;
import com.projectmanagement.domain.User;
import com.projectmanagement.domain.enumeration.Role;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.web.filter.UserAlreadyExistsException;

@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(username);
        }
        return null;
    }

    public void createUser(String username, String email, String password, String firstName, String lastName,
            Role role) {
        if (userRepository.findByUsername(username) != null) {
            throw new UserAlreadyExistsException("Username já existe.");
        }

        if (userRepository.findByEmail(email) != null) {
            throw new UserAlreadyExistsException("Email já existe.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
