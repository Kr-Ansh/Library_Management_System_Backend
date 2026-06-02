package com.irons.library_management_system_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String userName;
    private Boolean isUserAdmin;
    private List<BookResponseDTO> booksBorrowed;
}
