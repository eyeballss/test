package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by kesl on 2016-06-13.
 */
public class Main_ActivityTest extends ActivityInstrumentationTestCase2<Main_Activity> {

    private Main_Activity main_Activity;

    //    private Resources mockResources;
    public Main_ActivityTest() {
        super(Main_Activity.class);
    }

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    @Test
    public void testDataParser() {
        ArrayList<String> name = main_Activity.getName();
        ArrayList<String> distance = main_Activity.getDistance();

        String[] data = {
                "idpw*a*sex*comment*1.0*" +
                        "idpw*b*sex*comment*1.1*" +
                        "idpw*c*sex*comment*0.9*" +
                        "idpw*d*sex*comment*0.0*" +
                        "idpw*e*sex*comment*0.1*"
        };

        main_Activity.callDataParser(data[0]);
        assertEquals(name.get(0), "d");
        assertEquals(name.get(1), "e");
        assertEquals(name.get(2), "c");
        assertEquals(name.get(3), "a");
        assertEquals(name.get(4), "b");
        assertEquals(distance.get(0), "0.0");
        assertEquals(distance.get(1), "0.1");
        assertEquals(distance.get(2), "0.9");
        assertEquals(distance.get(3), "1.0");
        assertEquals(distance.get(4), "1.1");

    }


}