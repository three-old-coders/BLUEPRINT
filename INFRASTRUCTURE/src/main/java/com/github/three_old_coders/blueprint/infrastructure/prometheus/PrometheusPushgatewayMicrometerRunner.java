package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory;
import io.prometheus.client.exporter.PushGateway;
import lombok.SneakyThrows;

import java.net.URL;

public class PrometheusPushgatewayMicrometerRunner {
    public static final String PROCESS = "Process";

    @SneakyThrows
    public static void main(final String[] args)
    {
        final PushGateway client = new PushGateway(new URL("http://localhost/pushgateway"));
        client.setConnectionFactory(new BasicAuthHttpConnectionFactory("admin", "admin"));

        // Default metrics
        final PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        CollectorRegistry collectorRegistry = prometheusMeterRegistry.getPrometheusRegistry();
        //final AtomicInteger batchProcessProgress=prometheusMeterRegistry.counter("batch process progress",);
        final Counter batchProcessProgress=prometheusMeterRegistry.counter("batch process progress", Tags.of("counter","micrometer counter"));

        // jvm metrics
        final JvmMemoryMetrics jmm=new JvmMemoryMetrics();
        jmm.bindTo(prometheusMeterRegistry);

        final String processName = PrometheusPushgatewayMicrometerRunner.class.getName();

        push(client, collectorRegistry, processName,true);

        System.out.println("Started...");

        // ----

        for (int i = 1; i < 100; i++) {
            try {
                Thread.sleep(600);
            } catch (final InterruptedException e) {
                // someone woke us up
            }
            batchProcessProgress.increment();

            push(client, collectorRegistry, processName, false);

            System.out.println("   running " + i);
        }

        push(client, collectorRegistry, processName,true);

        System.out.println("...finished");
    }

    private static void push(final PushGateway client, final CollectorRegistry registry,
                             final String processName, final boolean abortOnError)
    {
        try {
            client.push(registry, processName);
        } catch (final Exception e) {
            System.err.println("unable to push data");
            e.printStackTrace();
            if (abortOnError) {
                throw new IllegalStateException(e);
            }
        }
    }
}
