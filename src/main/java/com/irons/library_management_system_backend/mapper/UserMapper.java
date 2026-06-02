package com.irons.library_management_system_backend.mapper;

import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.dto.UserResponseDTO;
import com.irons.library_management_system_backend.entities.Users;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    private final BookMapper bookMapper;

    public  UserMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public UserResponseDTO toResponseDTO(Users user) {

        if(user == null) return null;

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(user.getUserId());
        userResponseDTO.setUserName(user.getUserName());
        userResponseDTO.setIsUserAdmin(user.getIsUserAdmin());

        if (user.getBooksBorrowed() != null) {
            List<BookResponseDTO> bookDTOs = user.getBooksBorrowed().stream()
                    .map(bookMapper::toResponseDTO)
                    .toList();
            userResponseDTO.setBooksBorrowed(bookDTOs);
        }

        return userResponseDTO;
    }
}
