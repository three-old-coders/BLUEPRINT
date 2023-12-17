package com.github.three_old_coders.blueprint.kubernetes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathUtils
{
    private MathUtils() {}

    public static void main(String[] args)
    {
        if (null == args || args.length != 1) {
            System.out.println("Usage: java MathUtils <number_of_decimals>");
            System.exit(1);
        }

        int decimals = Integer.parseInt(args[0]);

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
     * @param decimals
     * @return pi
     */
    public static BigDecimal calculatePI(int decimals)
    {
        /* empirical data suggests that iterations -> correct decimal digits is roughly linear (r-sq ~= 100%)
         * the coefficients given on my small data set (up to 3400 iterations) were
         * correct_digits = 1.20515 * it + 6.56522. Since the latter half of the data exceeded the regression
         * made from the first half, I'm going to overiterate and simply use correct_digits = it. This will
         * lead to suboptimal perforamnce, but the difference should be fairly negligible, and it'll hopefully
         * be sufficient for any imaginable number of digits.
         */
        final MathContext mathContext = new MathContext(1000);
        BigDecimal pi = BigDecimal.ZERO;

        for(int x = 0; x < mathContext.getPrecision(); x++)
        {
            BigDecimal tn = term(x, 1, 16, 8, new int[] {4, 0, 0, -2, -1, -1, 0, 0}, mathContext);
            pi = pi.add(tn.setScale(mathContext.getPrecision(), mathContext.getRoundingMode()));
        }

        return pi.setScale(decimals, RoundingMode.DOWN);
    }

    public static BigDecimal term(int k, int s, int b, int m, int[] A, MathContext mc)
    {
        BigDecimal result = new BigDecimal("0", mc);

        /* Sum Ai / (m*k + (i+1))^s */
        for(int i = 0; i < m; i++)
        {
            BigDecimal a = new BigDecimal(Integer.toString(A[i]), mc);
            BigDecimal c = new BigDecimal(Long.toString(m * k + i + 1), mc);

            result = result.add(a.divide(c.pow(s), mc));
        }

        /* sum = sum * 1 / b^k*/
        result = result.multiply(new BigDecimal(Integer.toString(b)).pow(-k, mc));

        return result;
    }
}
