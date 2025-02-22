package com.projectmanagement.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectmanagement.domain.dto.UserLoginRequest;
import com.projectmanagement.domain.dto.UserSignupRequest;
import com.projectmanagement.domain.enumeration.Role;
import com.projectmanagement.service.UserService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testLoginSuccessful() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("username", "password");
        String token = "mocked-jwt-token";

        when(userService.authenticate("username", "password")).thenReturn(token);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(token));
    }

    @Test
    public void testLoginFailed() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("username", "wrong-password");

        when(userService.authenticate("username", "wrong-password")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Credenciais inválidas"));
    }

    @Test
    public void testSignupSuccessful() throws Exception {
        UserSignupRequest signupRequest = new UserSignupRequest("username", "email@example.com", "password", "John",
                "Doe", Role.ADMIN);

        when(userService.usernameExists("username")).thenReturn(false);
        when(userService.emailExists("email@example.com")).thenReturn(false);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Usuário criado com sucesso!"));
    }

    @Test
    public void testSignupUsernameExists() throws Exception {
        UserSignupRequest signupRequest = new UserSignupRequest("username", "email@example.com", "password", "John",
                "Doe", Role.ADMIN);

        when(userService.usernameExists("username")).thenReturn(true);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Username already taken"));
    }

    @Test
    public void testSignupEmailExists() throws Exception {
        UserSignupRequest signupRequest = new UserSignupRequest("username", "email@example.com", "password", "John",
                "Doe", Role.ADMIN);

        when(userService.usernameExists("username")).thenReturn(false);
        when(userService.emailExists("email@example.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Email already in use"));
    }
}
