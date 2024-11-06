package com.mayab.quality.unittest.service;

import com.mayab.quality.loginunittest.service.UserService;
import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private IDAOUser dao;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new User("Test User", "test@example.com", "password123");
    }

    @Test
    public void testCreateUser_HappyPath() {
        // Arrange
        when(dao.findUserByEmail(sampleUser.getEmail())).thenReturn(null);
        when(dao.save(any(User.class))).thenReturn(1);

        // Act
        User createdUser = userService.createUser(sampleUser.getName(), sampleUser.getEmail(), sampleUser.getPassword());

        // Assert
        assertNotNull(createdUser);
        assertEquals(sampleUser.getEmail(), createdUser.getEmail());
        verify(dao, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_DuplicatedEmail() {
        // Arrange
        when(dao.findUserByEmail(sampleUser.getEmail())).thenReturn(sampleUser);

        // Act
        User createdUser = userService.createUser(sampleUser.getName(), sampleUser.getEmail(), sampleUser.getPassword());

        // Assert
        assertNull(createdUser);
        verify(dao, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UpdatePassword() {
        // Arrange
        String newPassword = "newPassword123";
        sampleUser.setPassword(newPassword);
        when(dao.findById(sampleUser.getId())).thenReturn(sampleUser);
        when(dao.updateUser(any(User.class))).thenReturn(sampleUser);

        // Act
        User updatedUser = userService.updateUser(sampleUser);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(newPassword, updatedUser.getPassword());
        verify(dao, times(1)).updateUser(any(User.class));
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        when(dao.deleteById(sampleUser.getId())).thenReturn(true);

        // Act
        boolean isDeleted = userService.deleteUser(sampleUser.getId());

        // Assert
        assertTrue(isDeleted);
        verify(dao, times(1)).deleteById(sampleUser.getId());
    }

    @Test
    public void testFindUserByEmail_HappyPath() {
        // Arrange
        when(dao.findUserByEmail(sampleUser.getEmail())).thenReturn(sampleUser);

        // Act
        User foundUser = userService.findUserByEmail(sampleUser.getEmail());

        // Assert
        assertNotNull(foundUser);
        assertEquals(sampleUser.getEmail(), foundUser.getEmail());
        verify(dao, times(1)).findUserByEmail(sampleUser.getEmail());
    }

    @Test
    public void testFindUserByEmail_UserNotFound() {
        // Arrange
        when(dao.findUserByEmail(sampleUser.getEmail())).thenReturn(null);

        // Act
        User foundUser = userService.findUserByEmail(sampleUser.getEmail());

        // Assert
        assertNull(foundUser);
        verify(dao, times(1)).findUserByEmail(sampleUser.getEmail());
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        User user1 = new User("User 1", "user1@example.com", "password1");
        User user2 = new User("User 2", "user2@example.com", "password2");
        when(dao.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.findAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(dao, times(1)).findAll();
    }
}
