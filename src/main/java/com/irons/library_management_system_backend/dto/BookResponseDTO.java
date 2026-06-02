package com.irons.library_management_system_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {

    private Long bookId;
    private String bookName;
    private String bookAuthor;
    private String bookGenre;
    private Boolean isBookBorrowed;
}
