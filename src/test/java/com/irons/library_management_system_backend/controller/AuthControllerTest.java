package com.irons.library_management_system_backend.controller;

import com.irons.library_management_system_backend.dto.AuthRequestDTO;
import com.irons.library_management_system_backend.dto.JwtResponseDTO;
import com.irons.library_management_system_backend.exception.GlobalExceptionHandler;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void registerUserSuccess() throws Exception {
        Mockito.doNothing().when(authService).registerUser(any(AuthRequestDTO.class));

        String validRegisterJson = """
                {
                    "userName": "newUser",
                    "password": "securePassword"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRegisterJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully. You can now log in."));
    }

    @Test
    void registerUserFailure() throws Exception {
        Mockito.doThrow(new LibraryException("Username 'duplicateUser' is already taken."))
                .when(authService).registerUser(any(AuthRequestDTO.class));

        String duplicateUserJson = """
                {
                    "userName": "duplicateUser",
                    "password": "securePassword"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username 'duplicateUser' is already taken."));
    }

    @Test
    void loginUserSuccess() throws Exception {
        JwtResponseDTO mockJwtResponse = new JwtResponseDTO("mocked.jwt.token.string", "user", false);

        Mockito.when(authService.authenticateUser(any(AuthRequestDTO.class))).thenReturn(mockJwtResponse);

        String loginJson = """
                {
                    "userName": "user",
                    "password": "password"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked.jwt.token.string"))
                .andExpect(jsonPath("$.userName").value("user"));
    }

    @Test
    void loginUserFailure() throws Exception {
        Mockito.when(authService.authenticateUser(any(AuthRequestDTO.class)))
                .thenThrow(new LibraryException("Invalid username or password credentials."));

        String invalidLoginJson = """
                {
                    "userName": "wrongUser",
                    "password": "wrongPassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLoginJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid username or password credentials."));
    }
}
