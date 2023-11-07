package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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
    // prometheus objects
    private final PushGateway _pushGateway;
    private final CollectorRegistry _collectorRegistry;
    private final MeterRegistry _meterRegistry;

    @SneakyThrows
    public PrometheusPushgatewayMicrometerRunner() {
        _pushGateway = new PushGateway(new URL("http://localhost/pushgateway"));
        _pushGateway.setConnectionFactory(new BasicAuthHttpConnectionFactory("admin", "admin"));

        // Default metrics
        final PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        _meterRegistry=prometheusMeterRegistry;
        _collectorRegistry=prometheusMeterRegistry.getPrometheusRegistry();

        // add monitoring jvm metrics
        new JvmMemoryMetrics().bindTo(_meterRegistry);
    }

    @SneakyThrows
    public static void main(final String[] args)
    {
        new PrometheusPushgatewayMicrometerRunner().doBatchProcess();
    }

    public void doBatchProcess()
    {
        final Counter batchProcessProgress=_meterRegistry.counter("batch process progress", Tags.of("counter","micrometer counter"));
        final String processName = PrometheusPushgatewayMicrometerRunner.class.getName();

        push(processName,true);

        System.out.println("Started...");

        // ----

        for (int i = 1; i < 100; i++) {
            try {
                Thread.sleep(600);
            } catch (final InterruptedException e) {
                // someone woke us up
            }
            batchProcessProgress.increment();

            push(processName, false);

            System.out.println("   running " + i);
        }

        push(processName,true);

        System.out.println("...finished");
    }

    private void push(final String processName, final boolean abortOnError)
    {
        try {
            _pushGateway.push(_collectorRegistry, processName);
        } catch (final Exception e) {
            System.err.println("unable to push data");
            e.printStackTrace();
            if (abortOnError) {
                throw new IllegalStateException(e);
            }
        }
    }
}
