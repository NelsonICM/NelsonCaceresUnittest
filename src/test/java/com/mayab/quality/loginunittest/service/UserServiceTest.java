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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

public class UserServiceTest {
	
	private UserService service;
	private IDAOUser dao;
	private HashMap<Integer , User> db;

	@BeforeEach
	public void setUp() throws Exception {
		dao = mock(IDAOUser.class);
		service = new UserService(dao);
		db = new HashMap<Integer, User>();
	}

	@Test
	public void whenPasswordShort_test() {
		String shortPass = "123";
		String name = "user1";
		String email = "user1@email.com";
		User user = null;

		when(dao.findUserByEmail(anyString())).thenReturn(user);
		when(dao.save(any(User.class))).thenReturn(1);
		
		user = service.createUser(name, email, shortPass);

	}

	@Test
	void whenAllDataCorrect_saveUserTest() {
		int sizeBefore = db.size();
		System.out.println("sizeBefore = " + sizeBefore);

		when(dao.findUserByEmail(anyString())).thenReturn(null);
		when(dao.save(any(User.class))).thenAnswer(new Answer<Integer>() {
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				User arg = (User) invocation.getArguments()[0];
				db.put(db.size() + 1, arg);
				return db.size();  
			}
		});

		User user = service.createUser("hola", "hola@email.com", "pass1234");

		assertThat(db.size(), is(sizeBefore + 1));
		assertThat(user.getName(), is("hola"));
	}

	@Test
	void whenUserUpdateData_test() {
		User oldUser = new User("oldUser", "oldEmail", "oldPassword");
		db.put(1, oldUser);
		oldUser.setId(1);

		User newUser = new User("newUser", "oldEmail", "newPassword");
		newUser.setId(1);

		when(dao.findById(1)).thenReturn(oldUser);
		when(dao.updateUser(any(User.class))).thenAnswer(new Answer<User>() {
			public User answer(InvocationOnMock invocation) throws Throwable {
				User arg = (User) invocation.getArguments()[0];
				db.replace(arg.getId(), arg);
				return db.get(arg.getId());
			}
		});

		User result = service.updateUser(newUser);

		assertThat(result.getName(), is("newUser"));
		assertThat(result.getPassword(), is("newPassword"));
	}
}
