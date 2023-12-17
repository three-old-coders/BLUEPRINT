package com.github.three_old_coders.blueprint.kubernetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@SpringBootApplication
public class PiCalculatorRestApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(PiCalculatorRestApplication.class, args);
    }

    @RestController
    public static class PiCalculatorController
    {
        @GetMapping("/calculatePi/{precision}")
        public String calculatePi(@PathVariable int precision)
        {
            if (precision < 0) {
                return "Precision must be a non-negative integer.";
            }

            final BigDecimal pi = MathUtils.calculatePI(precision);
            return pi.toString();
        }

        private double calculatePiWithPrecision(int precision)
        {
            // Add your pi calculation logic here.
            // This is a simple example using the Math.PI constant.
            return Math.PI;
        }
    }
}
