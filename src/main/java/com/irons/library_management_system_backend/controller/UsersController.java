package com.irons.library_management_system_backend.controller;

import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.dto.UserRequestDTO;
import com.irons.library_management_system_backend.dto.UserResponseDTO;
import com.irons.library_management_system_backend.service.KafkaProducerService;
import com.irons.library_management_system_backend.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @Autowired
    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(usersService.findAllUsers());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.findUserById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserResponseDTO> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(usersService.findUserByName(name));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<List<BookResponseDTO>> getBooksBorrowedByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.findAllBooksBorrowedByUser(id));
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UserResponseDTO>> getAllAdmins() {
        return ResponseEntity.ok(usersService.findAllUsersByRole(true));
    }

    @GetMapping("/members")
    public ResponseEntity<List<UserResponseDTO>> getAllMembers() {
        return ResponseEntity.ok(usersService.findAllUsersByRole(false));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        usersService.addUser(userRequestDTO);

        String eventMessage = String.format("USER_EVENT: New User '%s' registered in the library system.", userRequestDTO.getUserName());
        kafkaProducerService.publishEvent(eventMessage);

        return new ResponseEntity<>("User added to the library system.", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        usersService.removeUser(id);

        String eventMessage = String.format("USER_EVENT: User with User ID: " + id + " removed from the library system.");
        kafkaProducerService.publishEvent(eventMessage);

        return ResponseEntity.ok("User with id " + id + " has been deleted from the library system.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> makeUserAdmin(@PathVariable Long id) {
        usersService.makeUserAdmin(id);

        String eventMessage = String.format("USER_EVENT: User with User ID: " + id + " has been granted Admin permission.");
        kafkaProducerService.publishEvent(eventMessage);

        return ResponseEntity.ok("User with id " + id + " has been granted admin permissions in the library system.");
    }
}
