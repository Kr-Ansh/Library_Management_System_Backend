package com.irons.library_management_system_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotBlank(message = "Book's name can't be empty.")
    private String bookName;

    @NotBlank(message = "Author's name can't be empty.")
    private String bookAuthor;

    @NotBlank(message = "Genre can't be empty.")
    private String bookGenre;

    private Boolean isBookBorrowed = false;
}
