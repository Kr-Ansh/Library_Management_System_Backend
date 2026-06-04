package com.irons.library_management_system_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long bookId;
    private String bookName;
    private String bookAuthor;
    private String bookGenre;
    private Boolean isBookBorrowed;
}
