package com.github.three_old_coders.blueprint.kubernetes;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

public class TestCase_MathUtils
{
    @Test
    public void testCalculatePI()
    {
        // Test with 5 decimals
        final BigDecimal pi5 = MathUtils.calculatePI(5);
        assertEquals("3.14159", pi5.toString());

        // Test with 10 decimals
        final BigDecimal pi10 = MathUtils.calculatePI(10);
        assertEquals("3.1415926535", pi10.toString());

        // Test with 15 decimals
        final BigDecimal pi15 = MathUtils.calculatePI(15);
        assertEquals("3.141592653589793", pi15.toString());

        // Add more test cases as needed
        final BigDecimal pi100 = MathUtils.calculatePI(100);
        System.out.println(pi100);
    }
}
