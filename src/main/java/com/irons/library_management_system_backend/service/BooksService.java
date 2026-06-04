package com.irons.library_management_system_backend.service;

import com.irons.library_management_system_backend.dto.BookRequestDTO;
import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.entities.Books;
import com.irons.library_management_system_backend.entities.Users;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.mapper.BookMapper;
import com.irons.library_management_system_backend.repository.BooksRepository;
import com.irons.library_management_system_backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksService {

    @Autowired
    BooksRepository booksRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private BookMapper bookMapper;

    @Cacheable(value = "allBooksCache")
    public List<BookResponseDTO> findAllBooks(){

        List<Books> listOfAllBooks = booksRepository.findAll();

        if(listOfAllBooks.isEmpty()) throw new LibraryException("No Books Found in the Library.");

        return listOfAllBooks.stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "bookDetailsByIdCache", key = "#bookId")
    public BookResponseDTO findBookById(Long bookId) {

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book with id " + bookId + " does not exist in the Library."));

        return bookMapper.toResponseDTO(book);
    }

    @Cacheable(value = "bookDetailsByNameCache", key = "#bookName")
    public BookResponseDTO findBookByName(String bookName) {

        Books book = booksRepository.findByBookName(bookName);

        if(book == null) throw new LibraryException("Book with name " + bookName + " does not exist in the Library.");

        return bookMapper.toResponseDTO(book);
    }

    @Cacheable(value = "booksByAuthorCache", key = "#booksAuthor")
    public List<BookResponseDTO> findBooksByAuthor(String booksAuthor) {

        List<Books> booksOfAuthor = booksRepository.findAllByBookAuthor(booksAuthor);

        if(booksOfAuthor.isEmpty()) throw new LibraryException("No Books of the author " + booksAuthor + " is found in the Library.");

        return booksOfAuthor.stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "booksByGenreCache", key = "#booksGenre")
    public List<BookResponseDTO> findBooksByGenre(String booksGenre) {

        List<Books> booksOfGenre = booksRepository.findAllByBookGenre(booksGenre);

        if(booksOfGenre.isEmpty()) throw new LibraryException("No Books of the genre " + booksGenre + " is found in the Library.");

        return booksOfGenre.stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "availableBooksCache")
    public List<BookResponseDTO> findAllAvailableBooks() {

        List<Books> allBooks = booksRepository.findAllByIsBookBorrowed(false);

        if(allBooks.isEmpty()) throw new LibraryException("No Books available to be borrowed in the Library.");

        return allBooks.stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "unavailableBooksCache")
    public List<BookResponseDTO> findAllUnavailableBooks() {

        List<Books> allBooks = booksRepository.findAllByIsBookBorrowed(true);

        if(allBooks.isEmpty()) throw new LibraryException("All books are available in the library.");

        return allBooks.stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = "allBooksCache", allEntries = true),
            @CacheEvict(value = "availableBooksCache", allEntries = true)
    })
    public void addBook(BookRequestDTO bookRequestDTO) {

        Books book = new Books();
        book.setBookName(bookRequestDTO.getBookName());
        book.setBookAuthor(bookRequestDTO.getBookAuthor());
        book.setBookGenre(bookRequestDTO.getBookGenre());
        book.setIsBookBorrowed(false);

        booksRepository.save(book);
    }

    @Caching(evict = {
            @CacheEvict(value = "allBooksCache", allEntries = true),
            @CacheEvict(value = "availableBooksCache", allEntries = true),
            @CacheEvict(value = "unavailableBooksCache", allEntries = true),
            @CacheEvict(value = "booksByAuthorCache", allEntries = true),
            @CacheEvict(value = "booksByGenreCache", allEntries = true),
            @CacheEvict(value = "bookDetailsByNameCache", allEntries = true),
            @CacheEvict(value = "bookDetailsByIdCache", key = "#bookId")
    })
    public void removeBook(Long bookId) {

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book with id " + bookId + " does not exist in the Library."));

        booksRepository.delete(book);
    }

    @Caching(evict = {
            @CacheEvict(value = "allBooksCache", allEntries = true),
            @CacheEvict(value = "availableBooksCache", allEntries = true),
            @CacheEvict(value = "unavailableBooksCache", allEntries = true),
            @CacheEvict(value = "bookDetailsByIdCache", key = "#bookId"),
            @CacheEvict(value = "booksBorrowedByUserCache", key = "#userId")
    })
    @Transactional // ◄ Added to safely rollback if either save step encounters a system glitch
    public void borrowBook(Long bookId, Long userId) {

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book with id " + bookId + " does not exist in the Library."));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User with id " + userId + " does not exist in the Library."));

        if (book.getIsBookBorrowed()) {
            throw new LibraryException("Book is already borrowed by another patron.");
        }

        book.setIsBookBorrowed(true);
        book.setBorrowedBy(user); // ◄ Crucial step linking foreign key maps back to users_table
        user.getBooksBorrowed().add(book);

        booksRepository.save(book);
        usersRepository.save(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "allBooksCache", allEntries = true),
            @CacheEvict(value = "availableBooksCache", allEntries = true),
            @CacheEvict(value = "unavailableBooksCache", allEntries = true),
            @CacheEvict(value = "bookDetailsByIdCache", key = "#bookId"),
            @CacheEvict(value = "booksBorrowedByUserCache", key = "#userId")
    })
    @Transactional
    public void returnBook(Long bookId, Long userId) {

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book with id " + bookId + " does not exist in the Library."));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User with id " + userId + " does not exist in the Library."));

        // Guardrail safety check to confirm current transaction context ownership lines
        if (book.getBorrowedBy() == null || !book.getBorrowedBy().getUserId().equals(userId)) {
            throw new LibraryException("Transaction Denied: This user does not hold the lease for this book.");
        }

        book.setIsBookBorrowed(false);
        book.setBorrowedBy(null); // ◄ Break foreign key pointer mapping row references inside MySQL
        user.getBooksBorrowed().remove(book);

        booksRepository.save(book);
        usersRepository.save(user);
    }
}