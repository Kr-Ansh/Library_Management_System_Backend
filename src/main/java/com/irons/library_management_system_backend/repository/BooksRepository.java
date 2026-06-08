package com.irons.library_management_system_backend.repository;

import com.irons.library_management_system_backend.entities.Books;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    Books findByBookName(String bookName);

    List<Books> findAllByBookAuthor(String bookAuthor);

    List<Books> findAllByBookGenre(String booksGenre);

    List<Books> findAllByIsBookBorrowed(Boolean isBookBorrowed);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b from Books b where b.bookId = :id")
    Optional<Books> findByIdWithLock(@Param("id") Long id);
}
