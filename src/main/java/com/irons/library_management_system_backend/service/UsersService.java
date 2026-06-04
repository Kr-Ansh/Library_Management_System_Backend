package com.irons.library_management_system_backend.service;

import com.irons.library_management_system_backend.dto.BookResponseDTO;
import com.irons.library_management_system_backend.dto.UserRequestDTO;
import com.irons.library_management_system_backend.dto.UserResponseDTO;
import com.irons.library_management_system_backend.entities.Users;
import com.irons.library_management_system_backend.exception.LibraryException;
import com.irons.library_management_system_backend.mapper.BookMapper;
import com.irons.library_management_system_backend.mapper.UserMapper;
import com.irons.library_management_system_backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private BookMapper bookMapper;

    @Cacheable(value = "allUsersCache")
    public List<UserResponseDTO> findAllUsers() {

        List<Users> users = usersRepository.findAll();

        if(users.isEmpty()) throw new LibraryException("No users data found in the system.");

        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "userDetailsByIdCache", key = "#userId")
    public UserResponseDTO findUserById(Long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User with id " + userId + " does not exist in the Library."));

        return userMapper.toResponseDTO(user);
    }

    @Cacheable(value = "userDetailsByNameCache", key = "#userName")
    public UserResponseDTO findUserByName(String userName) {

        Users user = usersRepository.findByUserName(userName);

        if(user == null) throw new LibraryException("User with name " + userName + " does not exist in the Library system.");

        return userMapper.toResponseDTO(user);
    }



    @Caching(evict = {
            @CacheEvict(value = "allUsersCache", allEntries = true),
            @CacheEvict(value = "usersByRoleCache", allEntries = true) // 👈 FIXED: Maintain role filter accuracy
    })
    public void addUser(UserRequestDTO userRequestDTO) {

        Users user = new Users();
        user.setUserName(userRequestDTO.getUserName());
        user.setIsUserAdmin(false);

        usersRepository.save(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "allUsersCache", allEntries = true),
            @CacheEvict(value = "usersByRoleCache", allEntries = true),
            @CacheEvict(value = "userDetailsByNameCache", allEntries = true),
            @CacheEvict(value = "booksBorrowedByUserCache", key = "#userId"), // 👈 FIXED: Clear dangling relationships
            @CacheEvict(value = "userDetailsByIdCache", key = "#userId")
    })
    public void removeUser(Long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User with id " + userId + " does not exist in the Library System."));

        usersRepository.delete(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "allUsersCache",  allEntries = true),
            @CacheEvict(value = "usersByRoleCache", allEntries = true),
            @CacheEvict(value = "userDetailsByNameCache", allEntries = true),
            @CacheEvict(value = "userDetailsByIdCache", key = "#userId")
    })
    public void makeUserAdmin(Long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User with id " + userId + " does not exist in the Library System."));

        user.setIsUserAdmin(true);
        usersRepository.save(user);
    }

    @Cacheable(value = "usersByRoleCache", key = "#isAdmin")
    public List<UserResponseDTO> findAllUsersByRole(Boolean isAdmin) {

        if(isAdmin == null) throw new LibraryException("Role cannot be null.");

        List<Users>  users = usersRepository.findAllByIsUserAdmin(isAdmin);

        if(users.isEmpty()) throw new LibraryException("No users found in the system.");

        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "booksBorrowedByUserCache", key = "#userId")
    public List<BookResponseDTO> findAllBooksBorrowedByUser(Long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User with id " + userId + " does not exist in the Library."));
        
        return user.getBooksBorrowed().stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
