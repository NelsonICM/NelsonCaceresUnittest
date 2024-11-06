package com.mayab.quality.doubles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
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
        when(subdependency.getClassName()).thenReturn("SubDependency.class");
        
        // Use a spy on Dependency to mock addTwo behavior
        dependency = spy(new Dependency(subdependency));
    }

    @Test
    void getClassNameTest() {
        String name = dependency.getSubdependencyClassName();
        assertThat(name, is("SubDependency.class"));
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void addTwoTest(int i) {
        int num = 10;
        int expected = testNum;

        // Using spy with stubbing on a concrete method
        when(dependency.addTwo(anyInt())).thenReturn(testNum);

        int result = dependency.addTwo(num);  
        assertThat(result, is(expected));
    }
    
    @Test
    public void testAnswer() {
        // Stub the addTwo method to add 20 to any given integer input
        when(dependency.addTwo(anyInt())).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) {
                int arg = (Integer) invocation.getArguments()[0];
                return arg + 20;
            }
        });

        int result = dependency.addTwo(5);  // Test with specific input
        assertThat(result, is(25));  // 5 + 20 = 25 based on the Answer
    }
}
