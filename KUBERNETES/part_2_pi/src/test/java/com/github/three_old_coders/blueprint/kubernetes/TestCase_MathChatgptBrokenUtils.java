package com.github.three_old_coders.blueprint.kubernetes;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TestCase_MathChatgptBrokenUtils
{
    @Test
    public void testCalculatePI()
    {
        // Test with 5 decimals
        final BigDecimal pi5 = MathChatgptBrokenUtils.calculatePI(5);
        assertEquals("2.97604" /*"3.14159"*/, pi5.toString());

        // Test with 10 decimals
        final BigDecimal pi10 = MathChatgptBrokenUtils.calculatePI(10);
        assertEquals("3.2323158094" /*"3.1415926535"*/, pi10.toString());

        // Test with 15 decimals
        final BigDecimal pi15 = MathChatgptBrokenUtils.calculatePI(15);
        assertEquals("3.079153394197426" /*"3.141592653589793"*/, pi15.toString());

        // Add more test cases as needed
    }
}
