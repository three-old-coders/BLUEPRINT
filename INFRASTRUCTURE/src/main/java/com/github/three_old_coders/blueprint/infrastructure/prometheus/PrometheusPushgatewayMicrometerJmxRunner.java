package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory;
import io.prometheus.client.exporter.HttpConnectionFactory;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PrometheusPushgatewayMicrometerJmxRunner extends PrometheusMeterRunnerBase
{
    private final MeterRegistry _mr;

    // prometheus objects
    @SneakyThrows
    public PrometheusPushgatewayMicrometerJmxRunner(final Map<String, String> envMap)
    {
        final PrometheusMeterRegistry pmr = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        final String processName = PrometheusPushgatewayMicrometerJmxRunner.class.getSimpleName();
        final URL url = new URL("http://localhost/pushgateway");
        final HttpConnectionFactory httpcf = new BasicAuthHttpConnectionFactory("admin", "admin");

        // ---- NON GENERIC PART

        final PrometheusPushgatewayJmxSupport ppgjmxs = new PrometheusPushgatewayJmxSupport(pmr, processName, envMap, url, httpcf);
        ppgjmxs.bindAndStart();

        // ---- THE "TO BECOME GENERIC WORKAROUND PART"
        //
        // registry is a PrometheusMeterRegistry but this breaks basic OO principles, we use the base interface instead.
        // multiple ways to deal with MeterRegistry
        // 1) pmr
        // 2) ppgjmxs.getMeterRegistry()
        // 3) downcast to mr
        _mr = pmr;

        new JvmMemoryMetrics().bindTo(getMeterRegistry());
    }

    @Override protected MeterRegistry getMeterRegistry() { return _mr; }

    @SneakyThrows
    public static void main(final String[] args)
    {
        final Map<String, String> envMap = new HashMap<>();
        envMap.put("Key1", args[0]);
        envMap.put("Key2", "Value3");

        new PrometheusPushgatewayMicrometerJmxRunner(envMap).doBatchProcessSimulation();

        System.exit(0);
    }
}
