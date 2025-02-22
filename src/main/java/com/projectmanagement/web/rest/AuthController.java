package com.projectmanagement.web.rest;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectmanagement.domain.dto.UserLoginRequest;
import com.projectmanagement.domain.dto.UserSignupRequest;
import com.projectmanagement.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
        String token = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    /**
     * Create a new user.
     *
     * @param signupRequest the user signup details.
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         user,
     *         or with status 400 (Bad Request) if the user already has an ID.
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignupRequest signupRequest) throws URISyntaxException {
        log.debug("Request to sign up User : {}", signupRequest);
        if (userService.usernameExists(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        if (userService.emailExists(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        userService.createUser(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getRole());

        return ResponseEntity.ok("Usuário criado com sucesso!");
    }
}
