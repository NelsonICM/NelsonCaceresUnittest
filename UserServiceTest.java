package com.mayab.quality.unittest.service;

import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

// Assuming UserService and User are part of your project
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Assuming a UserRepository interface that UserService depends on

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new User("test@example.com", "password123");
    }

    @Test
    public void testCreateUser_HappyPath() {
        // Arrange
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        // Act
        User createdUser = userService.createUser(sampleUser);

        // Assert
        assertNotNull(createdUser);
        assertEquals(sampleUser.getEmail(), createdUser.getEmail());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    public void testCreateUser_DuplicatedEmail() {
        // Arrange
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));

        // Act
        User createdUser = userService.createUser(sampleUser);

        // Assert
        assertNull(createdUser);
        verify(userRepository, never()).save(sampleUser);
    }

    @Test
    public void testUpdateUser_UpdatePassword() {
        // Arrange
        String newPassword = "newPassword123";
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));
        sampleUser.setPassword(newPassword);
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        // Act
        User updatedUser = userService.updateUserPassword(sampleUser.getEmail(), newPassword);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(newPassword, updatedUser.getPassword());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));
        doNothing().when(userRepository).delete(sampleUser);

        // Act
        boolean isDeleted = userService.deleteUser(sampleUser.getEmail());

        // Assert
        assertTrue(isDeleted);
        verify(userRepository, times(1)).delete(sampleUser);
    }

    @Test
    public void testFindUserByEmail_HappyPath() {
        // Arrange
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));

        // Act
        User foundUser = userService.findUserByEmail(sampleUser.getEmail());

        // Assert
        assertNotNull(foundUser);
        assertEquals(sampleUser.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(sampleUser.getEmail());
    }

    @Test
    public void testFindUserByEmail_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.empty());

        // Act
        User foundUser = userService.findUserByEmail(sampleUser.getEmail());

        // Assert
        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail(sampleUser.getEmail());
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        User user1 = new User("user1@example.com", "password1");
        User user2 = new User("user2@example.com", "password2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.findAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }
}
