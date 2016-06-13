package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;

import helper.StaticManager;

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
    //뷰들을 StaticManager에서 따와서 세팅함.
    private void testSetViews() {

        //test1
        StaticManager.nickname = "nickname";
        StaticManager.comment = "comment";
        StaticManager.sex=false;

        editProfileActivity.callSetViews();

        assertEquals(editProfileActivity.getNicknameEditTxt().getText(), "nickname");
        assertEquals(editProfileActivity.getCommentEditTxt().getText(), "comment");
        assertFalse(editProfileActivity.getRadioWomanBtn().isChecked());

        //test2



    }



}