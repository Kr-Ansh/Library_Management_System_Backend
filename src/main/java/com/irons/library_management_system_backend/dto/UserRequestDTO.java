package com.irons.library_management_system_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "User's name can't be empty.")
    @Size(min = 3, message = "Username must be at least 3 characters.")
    private String userName;

    private Boolean isUserAdmin = false;
}
