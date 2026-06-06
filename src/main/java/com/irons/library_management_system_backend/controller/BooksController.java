package com.irons.library_management_system_backend.controller;

import com.irons.library_management_system_backend.dto.BookRequestDTO;
import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.service.BooksService;
import com.irons.library_management_system_backend.service.KafkaProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;

    @Autowired
    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/all")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(booksService.findAllBooks());
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookResponseDTO>> getAllBooksAvailable() {
        return ResponseEntity.ok(booksService.findAllAvailableBooks());
    }

    @GetMapping("/unavailable")
    public ResponseEntity<List<BookResponseDTO>> getAllBooksUnavailable() {
        return ResponseEntity.ok(booksService.findAllUnavailableBooks());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(booksService.findBookById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BookResponseDTO> getBookByName(@PathVariable String name) {
        return ResponseEntity.ok(booksService.findBookByName(name));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookResponseDTO>> getBookByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(booksService.findBooksByAuthor(author));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookResponseDTO>> getBookByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(booksService.findBooksByGenre(genre));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        booksService.addBook(bookRequestDTO);

        String eventMessage = String.format("BOOK_EVENT: New Book '%s' by %s added to inventory.", bookRequestDTO.getBookName(), bookRequestDTO.getBookAuthor());
        kafkaProducerService.publishEvent(eventMessage);

        return new ResponseEntity<>("Book added to the library successfully.", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeBook(@PathVariable Long id) {
        booksService.removeBook(id);

        String eventMessage = String.format("BOOK_EVENT: Book with ID: " + id + " successfully removed from the library.");
        kafkaProducerService.publishEvent(eventMessage);

        return ResponseEntity.ok("Book with ID " + id + " was successfully removed from the library inventory.");
    }

    @PatchMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestParam Long bookId, @RequestParam Long userId) {
        booksService.borrowBook(bookId, userId);

        String eventMessage = String.format("TRANSACTION_EVENT: User ID %d successfully borrowed Book ID %d", userId, bookId);
        kafkaProducerService.publishEvent(eventMessage);

        return ResponseEntity.ok("Book with ID " + bookId + " was successfully issued to user ID " + userId + ".");
    }

    @PatchMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long bookId, @RequestParam Long userId) {
        booksService.returnBook(bookId, userId);

        String eventMessage = String.format("TRANSACTION_EVENT: Book with ID: " + bookId + " successfully returned by User ID " + userId);
        kafkaProducerService.publishEvent(eventMessage);

        return ResponseEntity.ok("Book with ID " + bookId + " was successfully returned by user ID " + userId + ".");
    }
}
