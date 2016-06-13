package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import helper.StaticManager.StaticManager;

/**
 * Created by kesl on 2016-05-30.
 */
//@RunWith(MockitoJUnitRunner.class)
//@SmallTest
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity loginActivity;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        loginActivity = getActivity();
    }

    //    @Test
    public void testSaveProfileToStaticManager() throws Exception {
        String[] testMsg = {
                "nickname f comment1 comment2 comment3",
                "myNickName m I'm eating a book.",
                "1 m 1",
                "987654321 f 987 654 321    987 654 321    987 654 321",
                "///+!@#$%^&() f ///+!@#$%^&() ///+!@#$%^&()"
        };
        loginActivity.callSaveProfileToStaticManager(testMsg[0]);
        assertEquals(StaticManager.nickname, "nickname");
        assertFalse((StaticManager.sex));
        assertEquals(StaticManager.comment, "comment1 comment2 comment3 ");
        assertTrue(StaticManager.checkIfSMHasProfile);

        loginActivity.callSaveProfileToStaticManager(testMsg[1]);
        assertEquals(StaticManager.nickname, "myNickName");
        assertTrue((StaticManager.sex));
        assertEquals(StaticManager.comment, "I'm eating a book. ");
        assertTrue(StaticManager.checkIfSMHasProfile);

        loginActivity.callSaveProfileToStaticManager(testMsg[2]);
        assertEquals(StaticManager.nickname, "1");
        assertTrue((StaticManager.sex));
        assertEquals(StaticManager.comment, "1 ");
        assertTrue(StaticManager.checkIfSMHasProfile);

        loginActivity.callSaveProfileToStaticManager(testMsg[3]);
        assertEquals(StaticManager.nickname, "987654321");
        assertFalse((StaticManager.sex));
        assertEquals(StaticManager.comment, "987 654 321 987 654 321 987 654 321 ");
        assertTrue(StaticManager.checkIfSMHasProfile);

        loginActivity.callSaveProfileToStaticManager(testMsg[4]);
        assertEquals(StaticManager.nickname, "///+!@#$%^&()");
        assertFalse((StaticManager.sex));
        assertEquals(StaticManager.comment, "///+!@#$%^&() ///+!@#$%^&() ");
        assertTrue(StaticManager.checkIfSMHasProfile);

    }

    public void testMakeHashCode() throws Exception {
        String[] id = {
                "ID",
                " I D ",
                "0987654321",
                "",
                "/852fuefhefj weweji w  fdmsdmf dkwk   fw "
        };
        String[] pw = {
                "password",
                " p a s s w o r d ",
                "=-0987654321",
                "",
                "eq8                uhwurwhr        ehwur24e2rifeenfnefkefnjn wemwekr//853+++"
        };

        loginActivity.callMakeHashCode(id[0], pw[0]);
        assertEquals((id[0].trim() + "" + pw[0].trim()).hashCode(), StaticManager.idpw);

        loginActivity.callMakeHashCode(id[1], pw[1]);
        assertEquals((id[1].trim() + "" + pw[1].trim()).hashCode(), StaticManager.idpw);

        loginActivity.callMakeHashCode(id[2], pw[2]);
        assertEquals((id[2].trim() + "" + pw[2].trim()).hashCode(), StaticManager.idpw);

        loginActivity.callMakeHashCode(id[3], pw[3]);
        assertEquals((id[3].trim() + "" + pw[3].trim()).hashCode(), StaticManager.idpw);

        loginActivity.callMakeHashCode(id[4], pw[4]);
        assertEquals((id[4].trim() + "" + pw[4].trim()).hashCode(), StaticManager.idpw);

    }


}



/*

LoginActivity
-로그인 버튼 클릭하면 로그인 할 텐데, edittext들이 비어있는지 아닌지에 대해서
-해쉬코드 잘 만들어서 보내는지
-



 */