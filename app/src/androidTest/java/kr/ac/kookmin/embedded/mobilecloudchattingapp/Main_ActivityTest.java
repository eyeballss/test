package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by kesl on 2016-06-13.
 */
public class Main_ActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity main_Activity;

    //    private Resources mockResources;
    public Main_ActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    //거리가 가까운 순으로 파싱 잘 되는지 확인
    @Test
    public void testDataParser() {
        ArrayList<String> name = main_Activity.getName();
        ArrayList<String> distance = main_Activity.getDistance();

        String[] data = {
                "idpw*a*sex*comment*1.0*" +
                        "idpw*b*sex*comment*1.1*" +
                        "idpw*c*sex*comment*0.9*" +
                        "idpw*d*sex*comment*0.0*" +
                        "idpw*e*sex*comment*0.1*",
                "idpw*a*sex*comment*1*" +
                        "idpw*b*sex*comment*2*" +
                        "idpw*c*sex*comment*3*" +
                        "idpw*d*sex*comment*4*" +
                        "idpw*e*sex*comment*5*",
                "idpw*c*sex*comment*20*" +
                        "idpw*b*sex*comment*10*" +
                        "idpw*d*sex*comment*30*" +
                        "idpw*a*sex*comment*0*" +
                        "idpw*e*sex*comment*50*"
        };

        //test1
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

        //test2
        main_Activity.callDataParser(data[1]);
        assertEquals(name.get(0), "a");
        assertEquals(name.get(1), "b");
        assertEquals(name.get(2), "c");
        assertEquals(name.get(3), "d");
        assertEquals(name.get(4), "e");
        assertEquals(distance.get(0), "1.0");
        assertEquals(distance.get(1), "2.0");
        assertEquals(distance.get(2), "3.0");
        assertEquals(distance.get(3), "4.0");
        assertEquals(distance.get(4), "5.0");

        //test3
        main_Activity.callDataParser(data[1]);
        assertEquals(name.get(0), "a");
        assertEquals(name.get(1), "b");
        assertEquals(name.get(2), "c");
        assertEquals(name.get(3), "d");
        assertEquals(name.get(4), "e");
        assertEquals(distance.get(0), "0.0");
        assertEquals(distance.get(1), "10.0");
        assertEquals(distance.get(2), "20.0");
        assertEquals(distance.get(3), "30.0");
        assertEquals(distance.get(4), "50.0");
    }


}