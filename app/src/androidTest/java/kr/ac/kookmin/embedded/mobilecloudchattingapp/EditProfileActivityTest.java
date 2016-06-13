package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;

import helper.StaticManager.StaticManager;

/**
 * Created by kesl on 2016-06-10.
 */
public class EditProfileActivityTest extends ActivityInstrumentationTestCase2<EditProfileActivity> {

    private EditProfileActivity editProfileActivity;

    public EditProfileActivityTest(){
        super(EditProfileActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        editProfileActivity = getActivity();
    }


    @Test
    //뷰들을 StaticManager에서 따와서 세팅함. 잘 세팅되는지 확인
    public void testSetViews() {

        String[] nickname = {
                "ID",
                ",./,./,.//.,!@#$%^&()_+",
                "/852fuefhefjwewejiwfdmsdmfkwkfw",
                "password",
                "789456123/*-!?!?",
                ".",
        };
        String[] comment ={
                "eq8                uhwurwhr\t\tehwur24e2rifeenfnefkefnjn wemwekr//853+++",
                "987654321 f 987 654 321    987 654 321    987 654 321",
                "///+!@#$%^&() f ///+!@#$%^&() ///+!@#$%^&()",
                "",
                "00.00.00.00 .00 .000.00",
                "\tdwdw\twdwd\twdwdw"
        };

        //test1
        StaticManager.nickname = nickname[0];
        StaticManager.comment = comment[0];
        StaticManager.sex=false;

        editProfileActivity.callSetViews();

        assertEquals(editProfileActivity.getNicknameEditTxt().getText(), "ID");
        assertEquals(editProfileActivity.getCommentEditTxt().getText(),
                "eq8                uhwurwhr\t\tehwur24e2rifeenfnefkefnjn wemwekr//853+++");
        assertTrue(editProfileActivity.getRadioWomanBtn().isChecked());
        assertFalse(editProfileActivity.getRadioManBtn().isChecked());

        //test2
        StaticManager.nickname = nickname[1];
        StaticManager.comment = comment[1];
        StaticManager.sex=false;

        editProfileActivity.callSetViews();

        assertEquals(editProfileActivity.getNicknameEditTxt().getText(), ",./,./,.//.,!@#$%^&()_+");
        assertEquals(editProfileActivity.getCommentEditTxt().getText(),
                "987654321 f 987 654 321    987 654 321    987 654 321");
        assertTrue(editProfileActivity.getRadioWomanBtn().isChecked());
        assertFalse(editProfileActivity.getRadioManBtn().isChecked());
        //test3
        StaticManager.nickname = nickname[2];
        StaticManager.comment = comment[2];
        StaticManager.sex=false;

        editProfileActivity.callSetViews();

        assertEquals(editProfileActivity.getNicknameEditTxt().getText(), "/852fuefhefjwewejiwfdmsdmfkwkfw");
        assertEquals(editProfileActivity.getCommentEditTxt().getText(), "///+!@#$%^&() f ///+!@#$%^&() ///+!@#$%^&()");
        assertTrue(editProfileActivity.getRadioWomanBtn().isChecked());
        assertFalse(editProfileActivity.getRadioManBtn().isChecked());
        //test4
        StaticManager.nickname = nickname[3];
        StaticManager.comment = comment[3];
        StaticManager.sex=true;

        editProfileActivity.callSetViews();

        assertEquals(editProfileActivity.getNicknameEditTxt().getText(), "password");
        assertEquals(editProfileActivity.getCommentEditTxt().getText(), "");
        assertFalse(editProfileActivity.getRadioWomanBtn().isChecked());
        assertTrue(editProfileActivity.getRadioManBtn().isChecked());
        //test5
        StaticManager.nickname = nickname[4];
        StaticManager.comment = comment[4];
        StaticManager.sex=true;

        editProfileActivity.callSetViews();

        assertEquals(editProfileActivity.getNicknameEditTxt().getText(), "789456123/*-!?!?");
        assertEquals(editProfileActivity.getCommentEditTxt().getText(), "00.00.00.00 .00 .000.00");
        assertFalse(editProfileActivity.getRadioWomanBtn().isChecked());
        assertTrue(editProfileActivity.getRadioManBtn().isChecked());
        //test6
        StaticManager.nickname = nickname[5];
        StaticManager.comment = comment[5];
        StaticManager.sex=true;

        editProfileActivity.callSetViews();

        assertEquals(editProfileActivity.getNicknameEditTxt().getText(), ".");
        assertEquals(editProfileActivity.getCommentEditTxt().getText(), "\tdwdw\twdwd\twdwdw");
        assertFalse(editProfileActivity.getRadioWomanBtn().isChecked());
        assertTrue(editProfileActivity.getRadioManBtn().isChecked());
    }


    //호출을 잘 하는지 확인
    public void testOnBackPressed(){
        editProfileActivity.onBackPressed();
        assertTrue(editProfileActivity.checkBackPressed);
    }


}