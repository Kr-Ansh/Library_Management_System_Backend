package com.irons.library_management_system_backend.service;

import com.irons.library_management_system_backend.dto.BookRequestDTO;
import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.entities.Books;
import com.irons.library_management_system_backend.entities.Users;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.mapper.BookMapper;
import com.irons.library_management_system_backend.repository.BooksRepository;
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
class BooksServiceTest {

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BooksService booksService;

    private Books sampleBook;
    private BookResponseDTO sampleBookResponseDTO;
    private Users sampleUser;

    @BeforeEach
    void setUp() {
        sampleBook = new Books();
        sampleBook.setBookId(1L);
        sampleBook.setBookName("Book 1");
        sampleBook.setBookAuthor("Book Author");
        sampleBook.setBookGenre("Book Genre");
        sampleBook.setIsBookBorrowed(false);

        sampleBookResponseDTO = new BookResponseDTO();
        sampleBookResponseDTO.setBookId(1L);
        sampleBookResponseDTO.setBookName("Book 1");
        sampleBookResponseDTO.setBookAuthor("Book Author");
        sampleBookResponseDTO.setBookGenre("Book Genre");
        sampleBookResponseDTO.setIsBookBorrowed(false);

        sampleUser = new Users();
        sampleUser.setUserId(10L);
        sampleUser.setUserName("Someone");
        sampleUser.setBooksBorrowed(new ArrayList<>());
    }

    @Test
    void findAllBooksSuccess() {
        List<Books> booksList = List.of(sampleBook);
        Mockito.when(booksRepository.findAll()).thenReturn(booksList);
        Mockito.when(bookMapper.toResponseDTO(sampleBook)).thenReturn(sampleBookResponseDTO);

        List<BookResponseDTO> result = booksService.findAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Book 1", result.getFirst().getBookName());
        Mockito.verify(booksRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAllBooksFail() {
        Mockito.when(booksRepository.findAll()).thenReturn(Collections.emptyList());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.findAllBooks());

        assertEquals("No Books Found in the Library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findBookByIdSuccess() {
        Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.when(bookMapper.toResponseDTO(sampleBook)).thenReturn(sampleBookResponseDTO);

        BookResponseDTO result = booksService.findBookById(1L);

        assertNotNull(result);
        assertEquals("Book 1", result.getBookName());
        Mockito.verify(booksRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void findBookByIdFail() {
        Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.findBookById(1L));

        assertEquals("Book with id 1 does not exist in the Library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void findBookByNameSuccess() {
        Mockito.when(booksRepository.findByBookName("Book 1")).thenReturn(sampleBook);
        Mockito.when(bookMapper.toResponseDTO(sampleBook)).thenReturn(sampleBookResponseDTO);

        BookResponseDTO result = booksService.findBookByName("Book 1");

        assertNotNull(result);
        assertEquals("Book 1", result.getBookName());
        Mockito.verify(booksRepository, Mockito.times(1)).findByBookName("Book 1");
    }

    @Test
    void findBookByNameFail() {
        Mockito.when(booksRepository.findByBookName("Book 1")).thenReturn(null);

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.findBookByName("Book 1"));

        assertEquals("Book with name Book 1 does not exist in the Library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findByBookName("Book 1");
    }

    @Test
    void findBooksByAuthorSuccess() {
        List<Books> booksList = List.of(sampleBook);
        Mockito.when(booksRepository.findAllByBookAuthor("Book Author")).thenReturn(booksList);
        Mockito.when(bookMapper.toResponseDTO(sampleBook)).thenReturn(sampleBookResponseDTO);

        List<BookResponseDTO> result = booksService.findBooksByAuthor("Book Author");

        assertNotNull(result);
        assertEquals(1, result.size());
        Mockito.verify(booksRepository, Mockito.times(1)).findAllByBookAuthor("Book Author");
    }

    @Test
    void findBooksByAuthorFail() {
        Mockito.when(booksRepository.findAllByBookAuthor("Book Author")).thenReturn(Collections.emptyList());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.findBooksByAuthor("Book Author"));

        assertEquals("No Books of the author Book Author is found in the Library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findAllByBookAuthor("Book Author");
    }

    @Test
    void findBooksByGenreSuccess() {
        List<Books> booksList = List.of(sampleBook);
        Mockito.when(booksRepository.findAllByBookGenre("Book Genre")).thenReturn(booksList);
        Mockito.when(bookMapper.toResponseDTO(sampleBook)).thenReturn(sampleBookResponseDTO);

        List<BookResponseDTO> result = booksService.findBooksByGenre("Book Genre");

        assertNotNull(result);
        assertEquals(1, result.size());
        Mockito.verify(booksRepository, Mockito.times(1)).findAllByBookGenre("Book Genre");
    }

    @Test
    void findBooksByGenreFail() {
        Mockito.when(booksRepository.findAllByBookGenre("Book Genre")).thenReturn(Collections.emptyList());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.findBooksByGenre("Book Genre"));

        assertEquals("No Books of the genre Book Genre is found in the Library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findAllByBookGenre("Book Genre");
    }

    @Test
    void findAllAvailableBooksSuccess() {
        List<Books> booksList = List.of(sampleBook);
        Mockito.when(booksRepository.findAllByIsBookBorrowed(false)).thenReturn(booksList);
        Mockito.when(bookMapper.toResponseDTO(sampleBook)).thenReturn(sampleBookResponseDTO);

        List<BookResponseDTO> result = booksService.findAllAvailableBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        Mockito.verify(booksRepository, Mockito.times(1)).findAllByIsBookBorrowed(false);
    }

    @Test
    void findAllAvailableBooksFail() {
        Mockito.when(booksRepository.findAllByIsBookBorrowed(false)).thenReturn(Collections.emptyList());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.findAllAvailableBooks());

        assertEquals("No Books available to be borrowed in the Library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findAllByIsBookBorrowed(false);
    }

    @Test
    void findAllUnavailableBooksSuccess() {
        sampleBook.setIsBookBorrowed(true);
        sampleBookResponseDTO.setIsBookBorrowed(true);
        List<Books> booksList = List.of(sampleBook);
        Mockito.when(booksRepository.findAllByIsBookBorrowed(true)).thenReturn(booksList);
        Mockito.when(bookMapper.toResponseDTO(sampleBook)).thenReturn(sampleBookResponseDTO);

        List<BookResponseDTO> result = booksService.findAllUnavailableBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.getFirst().getIsBookBorrowed());
        Mockito.verify(booksRepository, Mockito.times(1)).findAllByIsBookBorrowed(true);
    }

    @Test
    void findAllUnavailableBooksFail() {
        Mockito.when(booksRepository.findAllByIsBookBorrowed(true)).thenReturn(Collections.emptyList());

        LibraryException exception = assertThrows(LibraryException.class, booksService::findAllUnavailableBooks);

        assertEquals("All books are available in the library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findAllByIsBookBorrowed(true);
    }

    @Test
    void addBookSuccess() {
        BookRequestDTO requestDTO = new BookRequestDTO();
        requestDTO.setBookName("Book 1");
        requestDTO.setBookAuthor("Book Author");
        requestDTO.setBookGenre("Book Genre");

        booksService.addBook(requestDTO);

        Mockito.verify(booksRepository, Mockito.times(1)).save(any(Books.class));
    }

    @Test
    void removeBookSuccess() {
        Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        booksService.removeBook(1L);

        Mockito.verify(booksRepository, Mockito.times(1)).delete(sampleBook);
    }

    @Test
    void removeBookFail() {
        Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.removeBook(1L));

        assertEquals("Book with id 1 does not exist in the Library.", exception.getMessage());
    }

    @Test
    void borrowBookSuccess() {
        Mockito.when(booksRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.of(sampleUser));

        booksService.borrowBook(1L, 10L);

        assertTrue(sampleBook.getIsBookBorrowed());
        assertEquals(sampleUser, sampleBook.getBorrowedBy());
        assertTrue(sampleUser.getBooksBorrowed().contains(sampleBook));
        Mockito.verify(booksRepository, Mockito.times(1)).save(sampleBook);
        Mockito.verify(usersRepository, Mockito.times(1)).save(sampleUser);
    }

    @Test
    void borrowBookFailWhenBookNotFound() {
        Mockito.when(booksRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.borrowBook(1L, 10L));

        assertEquals("Book with id 1 does not exist in the Library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findByIdWithLock(1L);
        Mockito.verify(booksRepository, Mockito.times(0)).save(any(Books.class));
    }

    @Test
    void borrowBookFailWhenUserNotFound() {
        Mockito.when(booksRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.borrowBook(1L, 10L));

        assertEquals("User with id 10 does not exist in the Library.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
        Mockito.verify(booksRepository, Mockito.times(0)).save(any(Books.class));
    }

    @Test
    void borrowBookFailWhenAlreadyBorrowed() {
        sampleBook.setIsBookBorrowed(true);
        Mockito.when(booksRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.of(sampleUser));

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.borrowBook(1L, 10L));

        assertEquals("Book is already borrowed by another patron.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(0)).save(any(Books.class));
    }

    @Test
    void returnBookSuccess() {
        sampleBook.setIsBookBorrowed(true);
        sampleBook.setBorrowedBy(sampleUser);
        sampleUser.getBooksBorrowed().add(sampleBook);

        Mockito.when(booksRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.of(sampleUser));

        booksService.returnBook(1L, 10L);

        assertFalse(sampleBook.getIsBookBorrowed());
        assertNull(sampleBook.getBorrowedBy());
        assertFalse(sampleUser.getBooksBorrowed().contains(sampleBook));
        Mockito.verify(booksRepository, Mockito.times(1)).save(sampleBook);
        Mockito.verify(usersRepository, Mockito.times(1)).save(sampleUser);
    }

    @Test
    void returnBookFailWhenBookNotFound() {
        Mockito.when(booksRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.returnBook(1L, 10L));

        assertEquals("Book with id 1 does not exist in the Library.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(1)).findByIdWithLock(1L);
    }

    @Test
    void returnBookFailWhenUserNotFound() {
        Mockito.when(booksRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.empty());

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.returnBook(1L, 10L));

        assertEquals("User with id 10 does not exist in the Library.", exception.getMessage());
        Mockito.verify(usersRepository, Mockito.times(1)).findById(10L);
    }

    @Test
    void returnBookFailWhenWrongUser() {
        Users differentUser = new Users();
        differentUser.setUserId(20L);
        differentUser.setUserName("SomeoneElse");

        sampleBook.setIsBookBorrowed(true);
        sampleBook.setBorrowedBy(differentUser);

        Mockito.when(booksRepository.findByIdWithLock(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.when(usersRepository.findById(10L)).thenReturn(Optional.of(sampleUser));

        LibraryException exception = assertThrows(LibraryException.class, () -> booksService.returnBook(1L, 10L));

        assertEquals("Transaction Denied: This user does not hold the lease for this book.", exception.getMessage());
        Mockito.verify(booksRepository, Mockito.times(0)).save(any(Books.class));
    }
}