package com.irons.library_management_system_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private final String type = "Bearer";
    private String userName;
    private boolean isAdmin;
}
