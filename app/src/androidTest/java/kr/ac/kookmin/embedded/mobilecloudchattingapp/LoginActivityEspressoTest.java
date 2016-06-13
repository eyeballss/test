package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by kesl on 2016-05-30.
 */

public class LoginActivityEspressoTest {

    @Rule
    public ActivityTestRule<LoginActivity> testRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void checkHelloWorldText() {
        onView(withText("ID")).check(matches(isDisplayed()));
    }
}