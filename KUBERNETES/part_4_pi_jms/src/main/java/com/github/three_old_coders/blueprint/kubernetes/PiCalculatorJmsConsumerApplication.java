package com.github.three_old_coders.blueprint.kubernetes;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@SpringBootApplication
public class PiCalculatorJmsConsumerApplication
{
    public static void main(String[] args)
    {
        try (final ConfigurableApplicationContext ac = SpringApplication.run(PiCalculatorJmsConsumerApplication.class, args)) {
            while (ac.isActive()) {
                try {
                    Thread.sleep(5000);
                } catch (final InterruptedException e) {
                    // do nothing.
                }
            }
        };
    }

    @Component
    public static class JmsConsumer
    {
        @Value("${jms.queue.name}")
        private String _queueName;

        @JmsListener(destination = "${jms.queue.name}")
        public void receiveMessage(final String message)
        {
            final JSONObject jo = new JSONObject(message);
            System.out.println("Received message: " + message);

            final int piDecimals = jo.getInt("pi");
            final BigDecimal pi = MathUtils.calculatePI(piDecimals);
            System.out.println("Calculated pi " + pi + " for " + message);
        }
    }
}
