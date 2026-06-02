package com.irons.library_management_system_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books_table")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String bookName;
    private String bookAuthor;
    private String bookGenre;
    private Boolean isBookBorrowed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowe_by_user_id")
    private Users borrowedBy;
}
