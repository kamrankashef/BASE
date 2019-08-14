package common;


import common.StringUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class TestStringUtil {

    @Test
    public void testParseClock() {
        assertNull(StringUtil.parseClock(null));
        assertEquals(0d, StringUtil.parseClock("0:00"), 0);
        assertEquals(0d, StringUtil.parseClock("00:00"), 0);
        assertEquals(0d, StringUtil.parseClock("0:00:00"), 0);
        assertEquals(10d, StringUtil.parseClock("0:00:10"), 0);
        assertEquals(12.3d, StringUtil.parseClock("0:00:12.3"), 0);
        assertEquals(-12.3d, StringUtil.parseClock("-00:12.3"), 0);
        assertEquals(5 * 3600 + 7 * 60 + 12.3, StringUtil.parseClock("5:07:12.3"), 0);
        assertEquals(5 * 3600 + 1 * 60 + 12, StringUtil.parseClock("5:01:12.0"), 0);
    }
}