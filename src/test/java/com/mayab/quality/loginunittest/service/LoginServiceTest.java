package com.mayab.quality.loginunittest.service;

//Import mockito

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//Import hamcrest
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginServiceTest {

	IDAOUser dao;
	User user;
	LoginService service;
	
	@BeforeEach
	void setUp() throws Exception {
		dao = mock(IDAOUser.class);
		user = mock(User.class);
		service = new LoginService(dao);
		
	}

	@Test
	void loginSuccess_whenPasswordCorrect() {
		when(user.getPassword()).thenReturn("123");
		when(dao.findByUserName("name")).thenReturn(user);
	
		
		//exercise
		boolean res = service.login("name","123");
		
		//assertion
		assertThat(res,is(true));
		
		
	}

}