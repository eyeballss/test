package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by kesl on 2016-06-08.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    @Mock
    private LoginActivity loginActivity;
    private Resources mockResources;

    public LoginActivityTest(){
        super(LoginActivity.class);
    }


    public void setUp() throws Exception {
        super.setUp();
        loginActivity = getActivity();
        MockitoAnnotations.initMocks(mockResources);

    }

    public void tearDown() throws Exception {

    }

    public void testLoginBtnOnClick() throws Exception {

    }
    @Test
    public void testTest1() throws Exception {
        assertEquals(loginActivity.test1(1,2), 3);
    }

    public void testOnActivityResult() throws Exception {

    }

    public void testGetLoginBtn() throws Exception {

    }
}