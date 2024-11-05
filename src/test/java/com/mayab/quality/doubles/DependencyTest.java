package com.mayab.quality.doubles;

//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DependencyTest {
	private SubDependency subdependency;
	private Dependency dependency;
	private static final int testNum = 3;
	
	@BeforeEach
	public void setup() {
		subdependency = mock(SubDependency.class);
		dependency = new Dependency(subdependency);
	}
	@Test
	void getClassNameTest() {
		String name = dependency.getSubdependencyClassName();
		//System.out.println("Name " + name);
		assertThat(name, is ("SubDependency.class"));
	}
	
	@Test
	void addTwoTest(int i) {
		int num=10;
		int expected =3;
		when(this.dependency.addTwo(num)).thenReturn(testNum);
		int result =dependency.addTwo(anyInt());
		assertThat(expected, is(result));
	}
	@Test
	public void testAnswer() {
		when(dependency.addTwo(anyInt())).thenAnswer(new Answer<Integer>() {
			public Integer answer(InvocationOnMock invocation) throws Throwable{
				int arg =(Integer) invocation.getArguments()[0];
				return arg + 20;
			}
		});
	}
	
	

}
