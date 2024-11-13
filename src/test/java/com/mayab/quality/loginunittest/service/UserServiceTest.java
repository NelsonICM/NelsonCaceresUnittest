package com.mayab.quality.loginunittest.service;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

public class UserServiceTest {

    private UserService service;
    private IDAOUser dao;
    private HashMap<Integer, User> db;

    @BeforeEach
    public void setUp() throws Exception {
        dao = mock(IDAOUser.class);
        service = new UserService(dao);
        db = new HashMap<>();
    }

    @Test
    public void whenPasswordShort_test() {
        String shortPass = "123";
        String name = "user1";
        String username = "username1";
        String email = "user1@email.com";
        User user = null;

        when(dao.findUserByEmail(anyString())).thenReturn(user);
        when(dao.save(any(User.class))).thenReturn(1);

        user = service.createUser(name, username, email, shortPass);

        Assertions.assertNull(user, "El usuario no debe crearse con una contraseña corta.");
    }

    @Test
    void whenAllDataCorrect_saveUserTest() {
        int sizeBefore = db.size();

        when(dao.findUserByEmail(anyString())).thenReturn(null);
        when(dao.save(any(User.class))).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                User userArg = (User) invocation.getArguments()[0];
                int newId = db.size() + 1;
                userArg.setId(newId);  
                db.put(newId, userArg);
                return newId;
            }
        });

        User user = service.createUser("hola", "username1", "hola@email.com", "pass1234");

        assertThat(db.size(), is(sizeBefore + 1));
        assertThat(user.getName(), is("hola"));
        assertThat(user.getUsername(), is("username1"));  
        Assertions.assertNotNull(user, "El usuario no debe ser null después de ser creado.");
    }

    @Test
    void whenUserUpdateData_test() {
        User oldUser = new User("oldUser", "oldUsername", "oldEmail", "oldPassword");
        oldUser.setId(1);
        db.put(1, oldUser);

        User newUser = new User("newUser", "newUsername", "oldEmail", "newPassword");
        newUser.setId(1);

        when(dao.findById(1)).thenReturn(oldUser);
        when(dao.updateUser(any(User.class))).thenAnswer(new Answer<User>() {
            public User answer(InvocationOnMock invocation) throws Throwable {
                User userArg = (User) invocation.getArguments()[0];
                db.replace(userArg.getId(), userArg);
                return db.get(userArg.getId());  
            }
        });

        User result = service.updateUser(newUser);

        assertThat(result.getName(), is("newUser"));
        assertThat(result.getPassword(), is("newPassword"));
        assertThat(result.getUsername(), is("newUsername"));  
        Assertions.assertNotNull(result, "El usuario actualizado no debe ser null.");
    }
}
