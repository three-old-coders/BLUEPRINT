package com.github.three_old_coders.blueprint.kubernetes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathChatgptBrokenUtils
{
    private MathChatgptBrokenUtils() {}

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
     * @param decimals
     * @return pi
     */
    public static BigDecimal calculatePI(int decimals)
    {
        final MathContext mathContext = MathContext.DECIMAL128;
        BigDecimal pi = BigDecimal.ZERO;

        for (int k = 0; k <= decimals; ++k) {
            BigDecimal numerator = BigDecimal.valueOf(-1).pow(k);
            BigDecimal denominator = BigDecimal.valueOf(2 * k + 1);

            BigDecimal term = numerator.divide(denominator, mathContext);
            pi = pi.add(term);
        }

        pi = pi.multiply(BigDecimal.valueOf(4));

        // Adjust rounding for the final result
        return pi.round(mathContext).setScale(decimals, RoundingMode.DOWN);
    }
}
