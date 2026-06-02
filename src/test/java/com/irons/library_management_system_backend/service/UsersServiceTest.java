package com.irons.library_management_system_backend.service;

import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.dto.UserRequestDTO;
import com.irons.library_management_system_backend.dto.UserResponseDTO;
import com.irons.library_management_system_backend.entities.Books;
import com.irons.library_management_system_backend.entities.Users;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.mapper.BookMapper;
import com.irons.library_management_system_backend.mapper.UserMapper;
import com.irons.library_management_system_backend.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private UsersService usersService;

    private Books sampleBook;
    private UserResponseDTO sampleUserResponseDTO;
    private Users sampleUser;

    @BeforeEach
    void setUp() {
        sampleBook = new Books();
        sampleBook.setBookId(1L);
        sampleBook.setBookName("Book 1");
        sampleBook.setBookAuthor("Book Author");
        sampleBook.setBookGenre("Book Genre");
        sampleBook.setIsBookBorrowed(false);

        sampleUserResponseDTO = new UserResponseDTO();
        sampleUserResponseDTO.setUserId(10L);
        sampleUserResponseDTO.setUserName("Someone");
        sampleUserResponseDTO.setIsUserAdmin(false);
        sampleUserResponseDTO.setBooksBorrowed(new ArrayList<>());

        sampleUser = new Users();
        sampleUser.setUserId(10L);
        sampleUser.setUserName("Someone");
        sampleUser.setIsUserAdmin(false);
        sampleUser.setBooksBorrowed(new ArrayList<>());
    }

    @Test
    void findAllUsersSuccess() {
        List<Users> usersList = List.of(sampleUser);
        Mockito.when(usersRepository.findAll()).thenReturn(usersList);
        Mockito.when(userMapper.toResponseDTO(sampleUser)).thenReturn(sampleUserResponseDTO);

        List<UserResponseDTO> result = usersService.findAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Someone", result.getFirst().getUserName());
        Mockito.verify(usersRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAllUsersFail() {
        Mockito.when(usersRepository.findAll()).thenReturn(Collections.emptyList());

        LibraryException exception = assertThrows(LibraryException.class, () -> usersService.findAllUsers());

        assertEquals("No user's data found in the system.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findUserByIdSuccess() {
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.of(sampleUser));
        Mockito.when(userMapper.toResponseDTO(sampleUser)).thenReturn(sampleUserResponseDTO);

        UserResponseDTO result = usersService.findUserById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getUserId());
        assertEquals("Someone", result.getUserName());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
    }

    @Test
    void findUserByIdFail() {
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> usersService.findUserById(10L));

        assertEquals("User with id 10 does not exist in the Library.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
    }

    @Test
    void findUserByNameSuccess() {
        Mockito.when(usersRepository.findByUserName("Someone")).thenReturn(sampleUser);
        Mockito.when(userMapper.toResponseDTO(sampleUser)).thenReturn(sampleUserResponseDTO);

        UserResponseDTO result = usersService.findUserByName("Someone");

        assertNotNull(result);
        assertEquals("Someone", result.getUserName());
        Mockito.verify(usersRepository, Mockito.times(1)).findByUserName("Someone");
    }

    @Test
    void findUserByNameFail() {
        Mockito.when(usersRepository.findByUserName("Someone")).thenReturn(null);

        LibraryException exception = assertThrows(LibraryException.class, () -> usersService.findUserByName("Someone"));

        assertEquals("User with name Someone does not exist in the Library system.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findByUserName("Someone");
    }

    @Test
    void addUserSuccess() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setUserName("Someone");
        requestDTO.setIsUserAdmin(false);

        usersService.addUser(requestDTO);

        Mockito.verify(usersRepository, Mockito.times(1)).save(any(Users.class));
    }

    @Test
    void removeUserSuccess() {
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.of(sampleUser));

        usersService.removeUser(10L);

        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
        Mockito.verify(usersRepository, Mockito.times(1)).delete(sampleUser);
    }

    @Test
    void removeUserFail() {
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> usersService.removeUser(10L));

        assertEquals("User with id 10 does not exist in the Library System.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
        Mockito.verify(usersRepository, Mockito.times(0)).delete(any(Users.class));
    }

    @Test
    void makeUserAdminSuccess() {
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.of(sampleUser));

        usersService.makeUserAdmin(10L);

        assertTrue(sampleUser.getIsUserAdmin());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
        Mockito.verify(usersRepository, Mockito.times(1)).save(sampleUser);
    }

    @Test
    void makeUserAdminFail() {
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> usersService.makeUserAdmin(10L));

        assertEquals("User with id 10 does not exist in the Library System.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
        Mockito.verify(usersRepository, Mockito.times(0)).save(any(Users.class));
    }

    @Test
    void findAllUsersByRoleSuccess() {
        List<Users> usersList = List.of(sampleUser);
        Mockito.when(usersRepository.findAllByIsUserAdmin(false)).thenReturn(usersList);
        Mockito.when(userMapper.toResponseDTO(sampleUser)).thenReturn(sampleUserResponseDTO);

        List<UserResponseDTO> result = usersService.findAllUsersByRole(false);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.getFirst().getIsUserAdmin());
        Mockito.verify(usersRepository, Mockito.times(1)).findAllByIsUserAdmin(false);
    }

    @Test
    void findAllUsersByRoleFail() {
        Mockito.when(usersRepository.findAllByIsUserAdmin(false)).thenReturn(Collections.emptyList());

        LibraryException exception = assertThrows(LibraryException.class, () -> usersService.findAllUsersByRole(false));

        assertEquals("No users found in the system.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findAllByIsUserAdmin(false);
    }

    @Test
    void findAllUsersByRoleNullCheckFail() {
        LibraryException exception = assertThrows(LibraryException.class, () -> usersService.findAllUsersByRole(null));

        assertEquals("Role cannot be null.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(0)).findAllByIsUserAdmin(any());
    }

    @Test
    void findAllBooksBorrowedByUserSuccess() {
        sampleUser.getBooksBorrowed().add(sampleBook);
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setBookId(1L);
        bookResponseDTO.setBookName("Book 1");

        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.of(sampleUser));
        Mockito.when(bookMapper.toResponseDTO(sampleBook)).thenReturn(bookResponseDTO);

        List<BookResponseDTO> result = usersService.findAllBooksBorrowedByUser(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Book 1", result.getFirst().getBookName());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
        Mockito.verify(bookMapper, Mockito.times(1)).toResponseDTO(sampleBook);
    }

    @Test
    void findAllBooksBorrowedByUserFail() {
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> usersService.findAllBooksBorrowedByUser(10L));

        assertEquals("User with id 10 does not exist in the Library.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
        Mockito.verify(bookMapper, Mockito.times(0)).toResponseDTO(any());
    }
}