package com.att.acceptance.movie_theater.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.att.acceptance.movie_theater.entity.RoleEnum;
import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Initialize a sample user entity for testing
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.getRoles().add(RoleEnum.ROLE_CUSTOMER);
    }

    /**
     * Test for registering a new user.
     */
    @Test
    void testRegisterUser() {
        // Mock repository response
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the service method
        User registeredUser = userService.registerUser(user, RoleEnum.ROLE_CUSTOMER);

        // Assertions
        assertNotNull(registeredUser);
        assertEquals("Test User", registeredUser.getName());
        assertTrue(registeredUser.getRoles().contains(RoleEnum.ROLE_CUSTOMER));
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test for retrieving all users.
     */
    @Test
    void testGetAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // Call the service method
        Page<User> users = userService.getAllUsers(pageable);

        // Assertions
        assertNotNull(users);
        assertEquals(1, users.getContent().size());
        assertEquals("Test User", users.getContent().get(0).getName());
        verify(userRepository, times(1)).findAll(pageable);
    }

    /**
     * Test for retrieving a user by ID.
     */
    @Test
    void testGetUserById() {
        // Mock repository response
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the service method
        User retrievedUser = userService.getUserById(1L);

        // Assertions
        assertNotNull(retrievedUser);
        assertEquals(1L, retrievedUser.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    /**
     * Test for updating a user.
     */
    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Update user details
        user.setName("Updated User");

        // Call the service method
        User updatedUser = userService.updateUser(1L, user);

        // Assertions
        assertNotNull(updatedUser);
        assertEquals("Updated User", updatedUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test for assigning a role to a user.
     */
    @Test
    void testAssignRoleToUser() {
        // Mock repository response
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the service method
        userService.assignRoleToUser(1L, RoleEnum.ROLE_ADMIN);

        // Assertions
        assertTrue(user.getRoles().contains(RoleEnum.ROLE_ADMIN));
        verify(userRepository, times(1)).save(user);
    }


    /**
     * Test for deleting a user.
     */
    @Test
    void testDeleteUser() {
        // Mock repository response
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the service method
        userService.deleteUser(1L);

        // Verify repository interaction
        verify(userRepository, times(1)).deleteById(1L);
    }

    /**
     * Test for retrieving a user that does not exist.
     */
    @Test
    void testGetNonExistentUser() {
        // Mock repository response
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method and assert exception
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(2L));
        verify(userRepository, times(1)).findById(2L);
    }
}