package com.irons.library_management_system_backend.repository;

import com.irons.library_management_system_backend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUserName(String userName);

    List<Users> findAllByIsUserAdmin(Boolean isUserAdmin);
}
