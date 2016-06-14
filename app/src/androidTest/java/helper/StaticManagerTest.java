package helper;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import helper.StaticManager.StaticManager;

/**
 * Created by kesl on 2016-06-13.
 */
public class StaticManagerTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    @Test
    public void testSortByValue() throws Exception {
        HashMap<String, Double> map = new HashMap<String, Double>();
        List list;

        //Test1
        map.put("a", 0.0);
        map.put("b", -0.1);
        map.put("c", 0.1);
        map.put("d", 1.0);
        map.put("e", 0.9);
        map.put("f", 1.1);

        list = StaticManager.sortByValue(map);

        assertEquals("b", list.get(0));
        assertEquals("a", list.get(1));
        assertEquals("c", list.get(2));
        assertEquals("e", list.get(3));
        assertEquals("d", list.get(4));
        assertEquals("f", list.get(5));

        map.clear();
        list.clear();

        //Test2
        map.put("a",-9.9);
        map.put("b",-8.8);
        map.put("c",-7.7);
        map.put("d",-6.6);
        map.put("e",-5.5);

        list = StaticManager.sortByValue(map);

        assertEquals("e", list.get(0));
        assertEquals("d", list.get(1));
        assertEquals("c", list.get(2));
        assertEquals("b", list.get(3));
        assertEquals("a", list.get(4));


        map.clear();
        list.clear();

        //Test3
        map.put("a",0.00001);
        map.put("b",0.00002);
        map.put("c",0.00003);
        map.put("d",-0.0001);
        map.put("e",-0.0002);

        list = StaticManager.sortByValue(map);

        assertEquals("e", list.get(0));
        assertEquals("d", list.get(1));
        assertEquals("c", list.get(2));
        assertEquals("b", list.get(3));
        assertEquals("a", list.get(4));

        map.clear();
        list.clear();

        //Test4
        map.put("a",0.0);
        map.put("b",-10.0);
        map.put("c",100.0);
        map.put("d",1000.0);
        map.put("e",-100.0);

        list = StaticManager.sortByValue(map);

        assertEquals("e", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("a", list.get(2));
        assertEquals("c", list.get(3));
        assertEquals("d", list.get(4));

        map.clear();
        list.clear();

        //Test5
        map.put("a", 5.55555);
        map.put("b", 4.44444);
        map.put("c", 3.33333);
        map.put("d", 2.22222);
        map.put("e", 1.11111);

        list = StaticManager.sortByValue(map);

        assertEquals("e", list.get(0));
        assertEquals("d", list.get(1));
        assertEquals("c", list.get(2));
        assertEquals("b", list.get(3));
        assertEquals("a", list.get(4));
    }
}