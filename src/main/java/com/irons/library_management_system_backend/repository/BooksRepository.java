package com.irons.library_management_system_backend.repository;

import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    Books findByBookName(String bookName);

    List<Books> findAllByBookAuthor(String bookAuthor);

    List<Books> findAllByBookGenre(String booksGenre);

    List<Books> findAllByIsBookBorrowed(Boolean isBookBorrowed);
}
