package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by kesl on 2016-05-30.
 */
@RunWith(MockitoJUnitRunner.class)
public class AdderTest {

    @Mock
    private Adder adder;

    @Before //Test 시작 전에 수행해야 할 것을 구현한 Method
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(adder);
    }

    @After //Test 후에 수행해야 할 것을 구현한 Method
    public void tearDown() throws Exception {

    }

    @Test //@Test는 실제 Test를 구현한 Test Method
    public void testAdd() throws Exception {
        when(adder.add(anyInt(), anyInt())).thenCallRealMethod();
        assertThat(adder.add(1, 2), is(3));
    }

    @Test
    public void testMultiply() throws Exception {
        when(adder.multiply(2, 3)).thenReturn(6);
        assertThat(adder.multiply(2, 3), is(6));
    }
}