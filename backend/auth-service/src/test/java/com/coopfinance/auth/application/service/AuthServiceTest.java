package com.coopfinance.auth.application.service;

import com.coopfinance.auth.application.dto.*;
import com.coopfinance.auth.domain.entity.User;
import com.coopfinance.auth.domain.repository.UserRepository;
import com.coopfinance.auth.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Service - Testes Unitários")
class AuthServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private AuthService authService;
    
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
    
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
        
        user = User.builder()
                .id(1L)
                .name("João Silva")
                .email("joao@example.com")
                .password("$2a$10$encodedPassword")
                .active(true)
                .build();
    }
    
    @Test
    @DisplayName("Deve registrar novo usuário com sucesso")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anyLong())).thenReturn("jwt-token");
        
        // Act
        AuthResponse response = authService.register(registerRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("João Silva", response.getName());
        assertEquals("joao@example.com", response.getEmail());
        assertEquals(1L, response.getUserId());
        
        verify(userRepository, times(1)).existsByEmail(registerRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(registerRequest.getPassword());
    }
    
    @Test
    @DisplayName("Não deve registrar usuário com email duplicado")
    void shouldNotRegisterUserWithDuplicateEmail() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.register(registerRequest));
        
        assertEquals("Email já cadastrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Deve fazer login com sucesso")
    void shouldLoginSuccessfully() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyLong())).thenReturn("jwt-token");
        
        // Act
        AuthResponse response = authService.login(loginRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(user.getName(), response.getName());
        
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), user.getPassword());
    }
    
    @Test
    @DisplayName("Não deve fazer login com credenciais inválidas")
    void shouldNotLoginWithInvalidCredentials() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.login(loginRequest));
        
        assertEquals("Credenciais inválidas", exception.getMessage());
        verify(jwtUtil, never()).generateToken(anyString(), anyLong());
    }
    
    @Test
    @DisplayName("Não deve fazer login com usuário não encontrado")
    void shouldNotLoginWithUserNotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.login(loginRequest));
        
        assertEquals("Credenciais inválidas", exception.getMessage());
    }
    
    @Test
    @DisplayName("Não deve fazer login com usuário inativo")
    void shouldNotLoginWithInactiveUser() {
        // Arrange
        user.setActive(false);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.login(loginRequest));
        
        assertEquals("Usuário inativo", exception.getMessage());
    }
    
    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void shouldGetUserByIdSuccessfully() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        
        // Act
        UserResponse response = authService.getUserById(1L);
        
        // Assert
        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
        
        verify(userRepository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário inexistente")
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.getUserById(999L));
        
        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}