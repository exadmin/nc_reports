package org.qubership.reporter.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    private void assertFirstIsEarlier(String firstDate, String secondDate) {
        boolean actResult = DateUtils.firstDateEarlierThenSecond(firstDate, secondDate);
        assertTrue("First date was [" + firstDate + "], second date was [" + secondDate + "]", actResult);
    }

    @Test
    public void test1() {
        String date1 = "09-03-2025-00-00-00";
        String date2 = "10-03-2025-00-00-00";

        assertFirstIsEarlier(date1, date2);
    }

    @Test
    public void test2() {
        String date1 = "10-03-2025-00-00-00";
        String date2 = "10-03-2025-00-00-01";

        assertFirstIsEarlier(date1, date2);
    }

    @Test
    public void test3() {
        String date1 = "25-03-2025-00-00-00";
        String date2 = "10-04-2025-00-00-00";

        assertFirstIsEarlier(date1, date2);
    }

    @Test(expected = AssertionError.class)
    public void test4() {
        String date1 = "11-03-2025-00-00-00";
        String date2 = "10-03-2025-00-00-00";

        assertFirstIsEarlier(date1, date2);
    }
}
