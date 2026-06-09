package com.irons.library_management_system_backend.service;

import com.irons.library_management_system_backend.dto.AuthRequestDTO;
import com.irons.library_management_system_backend.dto.JwtResponseDTO;
import com.irons.library_management_system_backend.entities.Users;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.repository.UsersRepository;
import com.irons.library_management_system_backend.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUserSuccess() {
        AuthRequestDTO requestDTO = new AuthRequestDTO();

        authService.registerUser(requestDTO);

        Mockito.verify(usersRepository, Mockito.times(1)).save(Mockito.any(Users.class));
    }

    @Test
    void registerUserFailure() {
        AuthRequestDTO requestDTO = new AuthRequestDTO();
        Mockito.when(usersRepository.findByUserName(requestDTO.getUserName())).thenReturn(new Users());

        LibraryException exception = assertThrows(LibraryException.class, () -> authService.registerUser(requestDTO));

        assertEquals("Username 'null' is already taken.", exception.getMessage());
    }

    @Test
    void authenticateUserSuccess() {
        AuthRequestDTO requestDTO = new AuthRequestDTO();
        requestDTO.setUserName("user");
        requestDTO.setPassword("password");

        Users mockUser = new Users();
        mockUser.setUserId(10L);
        mockUser.setUserName("user");
        mockUser.setPassword("password");

        String mockJwtToken = "mocked.jwt.token.string";

        Mockito.when(usersRepository.findByUserName("user")).thenReturn(mockUser);
        Mockito.when(passwordEncoder.matches("password", "password")).thenReturn(true);
        Mockito.when(jwtUtils.generateToken("user", false)).thenReturn(mockJwtToken);

        JwtResponseDTO jwtResponseDTO = authService.authenticateUser(requestDTO);

        Assertions.assertNotNull(jwtResponseDTO);
        assertEquals(mockJwtToken, jwtResponseDTO.getToken());
        assertEquals("user", jwtResponseDTO.getUserName());

        Mockito.verify(usersRepository, Mockito.times(1)).findByUserName("user");
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches("password", "password");
        Mockito.verify(jwtUtils, Mockito.times(1)).generateToken("user", false);
    }

    @Test
    void authenticateUserFailure() {
        AuthRequestDTO requestDTO = new AuthRequestDTO();

        LibraryException exception = assertThrows(LibraryException.class, () -> authService.authenticateUser(requestDTO));

        assertEquals("Invalid username or password credentials.", exception.getMessage());
    }
}
