package com.coopfinance.auth.presentation.controller;

import com.coopfinance.auth.application.dto.*;
import com.coopfinance.auth.application.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Auth Controller - Testes de Integração")
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private AuthService authService;
    
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    
    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .name("João Silva")
                .email("joao@example.com")
                .password("senha123")
                .build();
        
        loginRequest = LoginRequest.builder()
                .email("joao@example.com")
                .password("senha123")
                .build();
        
        authResponse = AuthResponse.builder()
                .token("jwt-token")
                .userId(1L)
                .name("João Silva")
                .email("joao@example.com")
                .build();
    }
    
    @Test
    @DisplayName("POST /api/auth/register - Deve registrar com sucesso")
    void shouldRegisterSuccessfully() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }
    
    @Test
    @DisplayName("POST /api/auth/register - Deve retornar erro com dados inválidos")
    void shouldReturnBadRequestWithInvalidData() throws Exception {
        // Arrange
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .name("Jo")
                .email("email-invalido")
                .password("123")
                .build();
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("POST /api/auth/login - Deve fazer login com sucesso")
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.userId").value(1));
    }
    
    @Test
    @DisplayName("GET /api/auth/me - Deve retornar usuário logado")
    void shouldGetCurrentUser() throws Exception {
        // Arrange
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .name("João Silva")
                .email("joao@example.com")
                .active(true)
                .build();
        
        when(authService.getUserById(anyLong())).thenReturn(userResponse);
        
        // Act & Assert
        mockMvc.perform(get("/api/auth/me")
                .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João Silva"));
    }
}