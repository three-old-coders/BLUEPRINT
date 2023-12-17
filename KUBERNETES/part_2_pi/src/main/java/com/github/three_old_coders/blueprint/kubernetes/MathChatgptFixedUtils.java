package com.github.three_old_coders.blueprint.kubernetes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathChatgptFixedUtils
{
    private MathChatgptFixedUtils() {}

    public static void main(String[] args)
    {
        if (null == args || args.length != 1) {
            System.out.println("Usage: java MathUtils <number_of_decimals>");
            System.exit(1);
        }

        int decimals = Integer.parseInt(args[0]);

        // Set precision for BigDecimal
        // Calculate PI using the Bailey–Borwein–Plouffe (BBP) formula
        BigDecimal pi = calculatePI(decimals);

        // Display result
        System.out.println("PI with " + decimals + " decimals: " + pi);
    }

    /**
     * You can use various algorithms to calculate PI with more precision. One common algorithm is the
     * Bailey–Borwein–Plouffe (BBP) formula. Here's an updated version of the Java program that uses the BBP formula
     *
     * <a href="https://github.com/fredwilby/Math/blob/master/src/com/fredwilby/math/misc/BBP.java">src</a>
     *
     * @param mathContext
     * @return pi
     */
    public static BigDecimal calculatePI(int decimals)
    {
        final MathContext mathContext = MathContext.DECIMAL128;
        BigDecimal pi = BigDecimal.ZERO;

        for (int k = 0; k <= decimals; ++k) {
            final BigDecimal bd0 = BigDecimal.ONE.divide(new BigDecimal(16).pow(k), mathContext);
            final BigDecimal bd1 = new BigDecimal(4).divide(new BigDecimal(8*k+1), mathContext);
            final BigDecimal bd2 = new BigDecimal(2).divide(new BigDecimal(8*k+4), mathContext);
            final BigDecimal bd3 = BigDecimal.ONE.divide(new BigDecimal(8*k+5), mathContext);
            final BigDecimal bd4 = BigDecimal.ONE.divide(new BigDecimal(8*k+6), mathContext);

            pi = pi.add(bd0.multiply(bd1.subtract(bd2).subtract(bd3).subtract(bd4)));
        }

        // Adjust rounding for the final result
        return pi.setScale(decimals, RoundingMode.DOWN);
    }
}
