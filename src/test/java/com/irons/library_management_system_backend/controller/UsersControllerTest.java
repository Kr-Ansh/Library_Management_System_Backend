package com.irons.library_management_system_backend.controller;

import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.dto.UserRequestDTO;
import com.irons.library_management_system_backend.dto.UserResponseDTO;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsersService usersService;

    private UserResponseDTO sampleUserResponse;
    private BookResponseDTO sampleBookResponse;

    @BeforeEach
    void setUp() {
        sampleUserResponse = new UserResponseDTO();
        sampleUserResponse.setUserId(10L);
        sampleUserResponse.setUserName("TestUser");
        sampleUserResponse.setIsUserAdmin(false);
        sampleUserResponse.setBooksBorrowed(new ArrayList<>());

        sampleBookResponse = new BookResponseDTO();
        sampleBookResponse.setBookId(1L);
        sampleBookResponse.setBookName("Borrowed Book");
        sampleBookResponse.setBookAuthor("Author Name");
        sampleBookResponse.setBookGenre("Genre");
        sampleBookResponse.setIsBookBorrowed(true);
    }

    @Test
    void getAllUsersSuccess() throws Exception {
        Mockito.when(usersService.findAllUsers()).thenReturn(List.of(sampleUserResponse));

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].userName").value("TestUser"));
    }

    @Test
    void getUserByIdSuccess() throws Exception {
        Mockito.when(usersService.findUserById(10L)).thenReturn(sampleUserResponse);

        mockMvc.perform(get("/api/users/id/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.userName").value("TestUser"));
    }

    @Test
    void getUserByIdNotFound() throws Exception {
        Mockito.when(usersService.findUserById(10L)).thenThrow(new LibraryException("User with id 10 does not exist in the Library."));

        mockMvc.perform(get("/api/users/id/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User with id 10 does not exist in the Library."));
    }

    @Test
    void getUserByNameSuccess() throws Exception {
        Mockito.when(usersService.findUserByName("TestUser")).thenReturn(sampleUserResponse);

        mockMvc.perform(get("/api/users/name/TestUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("TestUser"));
    }

    @Test
    void getBooksBorrowedByUserIdSuccess() throws Exception {
        Mockito.when(usersService.findAllBooksBorrowedByUser(10L)).thenReturn(List.of(sampleBookResponse));

        mockMvc.perform(get("/api/users/books/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].bookName").value("Borrowed Book"));
    }

    @Test
    void getAllAdminsSuccess() throws Exception {
        sampleUserResponse.setIsUserAdmin(true);
        Mockito.when(usersService.findAllUsersByRole(true)).thenReturn(List.of(sampleUserResponse));

        mockMvc.perform(get("/api/users/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isUserAdmin").value(true));
    }

    @Test
    void getAllMembersSuccess() throws Exception {
        Mockito.when(usersService.findAllUsersByRole(false)).thenReturn(List.of(sampleUserResponse));

        mockMvc.perform(get("/api/users/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isUserAdmin").value(false));
    }

    @Test
    void addUserSuccess() throws Exception {
        Mockito.doNothing().when(usersService).addUser(any(UserRequestDTO.class));

        String validUserJson = """
                {
                    "userName": "NewUser",
                    "isUserAdmin": false
                }
                """;

        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("User added to the library system."));
    }

    @Test
    void addUserValidationFailure() throws Exception {
        String invalidUserJson = """
                {
                    "userName": "",
                    "isUserAdmin": false
                }
                """;

        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.userName").exists());
    }

    @Test
    void deleteUserSuccess() throws Exception {
        Mockito.doNothing().when(usersService).removeUser(10L);

        mockMvc.perform(delete("/api/users/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id 10 has been deleted from the library system."));
    }

    @Test
    void makeUserAdminSuccess() throws Exception {
        Mockito.doNothing().when(usersService).makeUserAdmin(10L);

        mockMvc.perform(patch("/api/users/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id 10 has been granted admin permissions in the library system."));
    }
}