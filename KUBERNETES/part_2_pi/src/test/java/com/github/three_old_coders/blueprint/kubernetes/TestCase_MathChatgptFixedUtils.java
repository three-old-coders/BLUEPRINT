package com.github.three_old_coders.blueprint.kubernetes;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TestCase_MathChatgptFixedUtils
{
    @Test
    public void testCalculatePI()
    {
        // Test with 5 decimals
        final BigDecimal pi5 = MathChatgptFixedUtils.calculatePI(5);
        assertEquals("3.14159", pi5.toString());

        // Test with 10 decimals
        final BigDecimal pi10 = MathChatgptFixedUtils.calculatePI(10);
        assertEquals("3.1415926535", pi10.toString());

        // Test with 15 decimals
        final BigDecimal pi15 = MathChatgptFixedUtils.calculatePI(15);
        assertEquals("3.141592653589793", pi15.toString());

        // Add more test cases as needed
    }
}
