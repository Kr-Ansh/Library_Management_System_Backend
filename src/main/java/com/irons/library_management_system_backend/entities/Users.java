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
@Table(name = "users_table")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private Boolean isUserAdmin;

    @OneToMany(mappedBy = "borrowedBy", cascade = CascadeType.ALL)
    private List<Books> booksBorrowed = new ArrayList<>();
}
