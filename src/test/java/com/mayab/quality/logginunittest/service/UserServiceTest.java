package com.mayab.quality.logginunittest.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat; // Updated from JUnit to Hamcrest
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;

class UserServiceTest {
    private UserService service;
    private IDAOUser dao;
    private HashMap<Integer, User> db;

    @BeforeEach
    public void setUp() throws Exception {
        dao = mock(IDAOUser.class);
        service = new UserService(dao);
        db = new HashMap<>();
    }

    // First test: Happy path for creating a user
    @Test
    public void whenAllDataCorrect_saveUserTest() {
        int sizeBefore = db.size();

        when(dao.findUserByEmail(anyString())).thenReturn(null); // Simulate no user with this email exists
        when(dao.save(any(User.class))).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) {
                User user = (User) invocation.getArguments()[0];
                int newId = db.size() + 1;
                user.setId(newId);
                db.put(newId, user);
                return newId;
            }
        });

        service.createUser("name", "email", "password");

        int sizeAfter = db.size();
        assertThat(sizeAfter, is(sizeBefore + ));
    }

    // Second test: Handle duplicate email
    @Test
    public void duplicatedUser_whenCreating() {
        User existingUser = new User("name", "emailduplicado", "password");
        when(dao.findUserByEmail("emailduplicado")).thenReturn(existingUser);

        User newUser = service.createUser("newName", "emailduplicado", "anotherpass");

        assertThat(newUser, is(existingUser));
    }

    // Third test: Update password
    @Test
    public void updatePassword_whenUserExists() {
        User existingUser = new User("oldName", "oldEmail", "oldPassword");
        existingUser.setId(1);
        db.put(1, existingUser);

        when(dao.findById(1)).thenReturn(existingUser);
        when(dao.updateUser(any(User.class))).thenAnswer(invocation -> {
            User updatedUser = (User) invocation.getArguments()[0];
            db.replace(updatedUser.getId(), updatedUser);
            return db.get(updatedUser.getId());
        });

        User updatedUser = new User("oldName", "oldEmail", "newPassword");
        updatedUser.setId(1);

        User result = service.updateUser(updatedUser);

        assertThat(result.getPassword(), is("newPassword"));
    }

    // Fourth test: Delete a user
    @Test
    public void deleteUser_whenUserExists() {
        int newId = db.size() + 1;
        User user = new User("name", "email", "password");
        user.setId(newId);
        db.put(user.getId(), user);

        int sizeBefore = db.size();

        when(dao.deleteById(user.getId())).thenAnswer(invocation -> {
            db.remove(invocation.getArgument(0));
            return true;
        });

        service.deleteUser(user.getId());

        int sizeAfter = db.size();
        assertThat(sizeAfter, is(sizeBefore - 1));
        assertThat(db.get(user.getId()), is(nullValue()));
    }

    // Fifth test: Find a user by email
    @Test
    public void whenUserExists_findUserByEmail() {
        User expectedUser = new User("name", "email", "password");
        when(dao.findUserByEmail("email")).thenReturn(expectedUser);

        User result = service.findUserByEmail("email");

        assertThat(result, is(expectedUser));
    }

    // Sixth test: Find user by email when not found
    @Test
    public void whenEmailNotFound_returnNull() {
        when(dao.findUserByEmail(anyString())).thenAnswer(invocation -> {
            String email = invocation.getArgument(0);
            return db.values().stream()
                     .filter(user -> user.getEmail().equals(email))
                     .findFirst()
                     .orElse(null);
        });

        User result = service.findUserByEmail("nonexistentemail");

        assertThat(result, is(nullValue()));
    }

    // Seventh test: Return all users
    @Test
    public void whenFindAllUsers_returnAllUsers() {
        User user1 = new User("user1", "email1", "password1");
        User user2 = new User("user2", "email2", "password2");
        user1.setId(1);
        user2.setId(2);

        db.put(user1.getId(), user1);
        db.put(user2.getId(), user2);

        when(dao.findAll()).thenAnswer(invocation -> new ArrayList<>(db.values()));

        List<User> users = service.findAllUsers();

        assertThat(users.size(), is(2));
        assertThat(users, containsInAnyOrder(user1, user2));
    }
}
