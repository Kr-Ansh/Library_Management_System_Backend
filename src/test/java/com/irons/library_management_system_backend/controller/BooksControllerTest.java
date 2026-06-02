package com.irons.library_management_system_backend.controller;

import com.irons.library_management_system_backend.dto.BookRequestDTO;
import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.service.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BooksController.class)
class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BooksService booksService;

    private BookResponseDTO sampleBookResponse;

    @BeforeEach
    void setUp() {
        sampleBookResponse = new BookResponseDTO();
        sampleBookResponse.setBookId(1L);
        sampleBookResponse.setBookName("Test Book");
        sampleBookResponse.setBookAuthor("Test Author");
        sampleBookResponse.setBookGenre("Test Genre");
        sampleBookResponse.setIsBookBorrowed(false);
    }

    @Test
    void getAllBooksSuccess() throws Exception {
        Mockito.when(booksService.findAllBooks()).thenReturn(List.of(sampleBookResponse));

        mockMvc.perform(get("/api/books/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].bookName").value("Test Book"));
    }

    @Test
    void getAllBooksAvailableSuccess() throws Exception {
        Mockito.when(booksService.findAllAvailableBooks()).thenReturn(List.of(sampleBookResponse));

        mockMvc.perform(get("/api/books/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isBookBorrowed").value(false));
    }

    @Test
    void getAllBooksUnavailableSuccess() throws Exception {
        sampleBookResponse.setIsBookBorrowed(true);
        Mockito.when(booksService.findAllUnavailableBooks()).thenReturn(List.of(sampleBookResponse));

        mockMvc.perform(get("/api/books/unavailable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isBookBorrowed").value(true));
    }

    @Test
    void getBookByIdSuccess() throws Exception {
        Mockito.when(booksService.findBookById(1L)).thenReturn(sampleBookResponse);

        mockMvc.perform(get("/api/books/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.bookName").value("Test Book"));
    }

    @Test
    void getBookByIdNotFound() throws Exception {
        Mockito.when(booksService.findBookById(1L)).thenThrow(new LibraryException("Book with id 1 does not exist in the Library."));

        mockMvc.perform(get("/api/books/id/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Book with id 1 does not exist in the Library."));
    }

    @Test
    void getBookByNameSuccess() throws Exception {
        Mockito.when(booksService.findBookByName("Test Book")).thenReturn(sampleBookResponse);

        mockMvc.perform(get("/api/books/name/Test Book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("Test Book"));
    }

    @Test
    void getBookByAuthorSuccess() throws Exception {
        Mockito.when(booksService.findBooksByAuthor("Test Author")).thenReturn(List.of(sampleBookResponse));

        mockMvc.perform(get("/api/books/author/Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookAuthor").value("Test Author"));
    }

    @Test
    void getBookByGenreSuccess() throws Exception {
        Mockito.when(booksService.findBooksByGenre("Test Genre")).thenReturn(List.of(sampleBookResponse));

        mockMvc.perform(get("/api/books/genre/Test Genre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookGenre").value("Test Genre"));
    }

    @Test
    void addBookSuccess() throws Exception {
        Mockito.doNothing().when(booksService).addBook(any(BookRequestDTO.class));

        String validJsonPayload = """
                {
                    "bookName": "Test Book",
                    "bookAuthor": "Test Author",
                    "bookGenre": "Test Genre"
                }
                """;

        mockMvc.perform(post("/api/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonPayload))
                .andExpect(status().isCreated())
                .andExpect(content().string("Book added to the library successfully."));
    }

    @Test
    void addBookValidationFailure() throws Exception {
        String invalidJsonPayload = """
                {
                    "bookName": "",
                    "bookAuthor": "Test Author",
                    "bookGenre": "Test Genre"
                }
                """;

        mockMvc.perform(post("/api/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.bookName").exists());
    }

    @Test
    void removeBookSuccess() throws Exception {
        Mockito.doNothing().when(booksService).removeBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book with ID 1 was successfully removed from the library inventory."));
    }

    @Test
    void borrowBookSuccess() throws Exception {
        Mockito.doNothing().when(booksService).borrowBook(1L, 10L);

        mockMvc.perform(patch("/api/books/borrow")
                        .param("bookId", "1")
                        .param("userId", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book with ID 1 was successfully issued to user ID 10."));
    }

    @Test
    void returnBookSuccess() throws Exception {
        Mockito.doNothing().when(booksService).returnBook(1L, 10L);

        mockMvc.perform(patch("/api/books/return")
                        .param("bookId", "1")
                        .param("userId", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book with ID 1 was successfully returned by user ID 10."));
    }
}