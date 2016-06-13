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
        map.put("a", 0.0);
        map.put("b", -0.1);
        map.put("c", 0.1);
        map.put("d", 1.0);
        map.put("e", 0.9);
        map.put("f", 1.1);

        List list = StaticManager.sortByValue(map);

        assertEquals("b", list.get(0));
        assertEquals("a", list.get(1));
        assertEquals("c", list.get(2));
        assertEquals("e", list.get(3));
        assertEquals("d", list.get(4));
        assertEquals("f", list.get(5));

    }
}