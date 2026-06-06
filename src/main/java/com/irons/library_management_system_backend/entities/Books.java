package com.irons.library_management_system_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "books_table",
        indexes = {
                @Index(name = "idx_book_name", columnList = "book_name"),
                @Index(name = "idx_book_author", columnList = "book_author")
        }
)
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "book_author", nullable = false)
    private String bookAuthor;

    @Column(name = "book_genre")
    private String bookGenre;

    @Column(name = "is_book_borrowed")
    private Boolean isBookBorrowed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowe_by_user_id")
    private Users borrowedBy;
}
