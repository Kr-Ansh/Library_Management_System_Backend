package com.irons.library_management_system_backend.mapper;

import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.entities.Books;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookResponseDTO toResponseDTO(Books book) {

        if(book == null) return null;

        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setBookId(book.getBookId());
        bookResponseDTO.setBookName(book.getBookName());
        bookResponseDTO.setBookAuthor(book.getBookAuthor());
        bookResponseDTO.setBookGenre(book.getBookGenre());
        bookResponseDTO.setIsBookBorrowed(book.getIsBookBorrowed());

        return bookResponseDTO;
    }
}
