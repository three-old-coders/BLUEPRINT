package com.github.three_old_coders.blueprint.kubernetes;

import jakarta.jms.ConnectionFactory;
import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ContextConfiguration(classes = Sample_PiCalculatorJmsProducerApplication.class)
public class Sample_PiCalculatorJmsProducerApplication
{
    @SneakyThrows
    public static void main(final String[] args)
    {
        try (final ConfigurableApplicationContext context = SpringApplication.run(Sample_PiCalculatorJmsProducerApplication.class, args)) {
            final JmsProducer jmsProducer = context.getBean(JmsProducer.class);

            for (int i = 0; i <= 100; i++) {
                final JSONObject joPI = new JSONObject().put("pi", i);
                jmsProducer.sendMessage(joPI.toString());

                System.out.println(joPI);
            }
        }
    }

    // ----

    @Configuration
    public static class JmsConfig
    {
        @Value("${spring.activemq.broker-url}")
        private String _brokerUrl;

        @Bean
        public ConnectionFactory connectionFactory()
        {
            return new ActiveMQConnectionFactory(_brokerUrl);
        }

        @Bean
        public JmsTemplate jmsTemplate(final ConnectionFactory connectionFactory)
        {
            JmsTemplate jmsTemplate = new JmsTemplate();
            jmsTemplate.setConnectionFactory(connectionFactory);
            return jmsTemplate;
        }
    }

    @Component
    public static class JmsProducer
    {
        @Value("${jms.queue.name}")
        private String _queueName;

        @Autowired private JmsTemplate _jmsTemplate;

        public void sendMessage(final String message)
        {
            _jmsTemplate.convertAndSend(_queueName, message);
        }
    }
}
