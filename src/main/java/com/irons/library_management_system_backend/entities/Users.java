package com.irons.library_management_system_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users_table",
        indexes = {
                @Index(name = "idx_user_name", columnList = "user_name")
        }
)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_user_admin")
    private Boolean isUserAdmin = false;

    @OneToMany(mappedBy = "borrowedBy", cascade = CascadeType.ALL)
    private List<Books> booksBorrowed = new ArrayList<>();
}
