package com.irons.library_management_system_backend.service;

import com.irons.library_management_system_backend.dto.AuthRequestDTO;
import com.irons.library_management_system_backend.dto.JwtResponseDTO;
import com.irons.library_management_system_backend.entities.Users;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.repository.UsersRepository;
import com.irons.library_management_system_backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public void registerUser(AuthRequestDTO registrationPayload) {

        if(usersRepository.findByUserName(registrationPayload.getUserName()) != null) {
            throw new LibraryException("Username '" + registrationPayload.getUserName() + "' is already taken.");
        }

        Users newProfile = new Users();
        newProfile.setUserName(registrationPayload.getUserName());
        newProfile.setPassword(passwordEncoder.encode(registrationPayload.getPassword()));
        newProfile.setIsUserAdmin(false);

        usersRepository.save(newProfile);
    }

    public JwtResponseDTO authenticateUser(AuthRequestDTO loginPayload) {

        Users profile = usersRepository.findByUserName(loginPayload.getUserName());
        if(profile == null) {
            throw new LibraryException("Invalid username or password credentials.");
        }

        if(!passwordEncoder.matches(loginPayload.getPassword(), profile.getPassword())) {
            throw new LibraryException("Invalid username or password credentials.");
        }

        String token = jwtUtils.generateToken(profile.getUserName(), profile.getIsUserAdmin());

        return new JwtResponseDTO(token, profile.getUserName(), profile.getIsUserAdmin());
    }
}
