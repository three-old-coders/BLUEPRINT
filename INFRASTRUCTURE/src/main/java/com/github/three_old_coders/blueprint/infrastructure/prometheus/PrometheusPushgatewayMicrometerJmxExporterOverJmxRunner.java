package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory;
import io.prometheus.client.exporter.HttpConnectionFactory;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PrometheusPushgatewayMicrometerJmxExporterOverJmxRunner extends PrometheusMeterRunnerBase
{
    // prometheus objects
    private final MeterRegistry _mr;

    @SneakyThrows
    public PrometheusPushgatewayMicrometerJmxExporterOverJmxRunner(final Map<String, String> envMap)
    {
        final PrometheusMeterRegistry pmr = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        final String processName = PrometheusPushgatewayMicrometerJmxExporterOverJmxRunner.class.getSimpleName();
        final URL url = new URL("http://localhost/pushgateway");
        final HttpConnectionFactory httpcf = new BasicAuthHttpConnectionFactory("admin", "admin");

        // ---- NON GENERIC PART

        // registry is a PrometheusMeterRegistry but this breaks basic OO principles, we use the base interface instead.
        final PrometheusPushgatewayJmxSupport ppgjmxs = new PrometheusPushgatewayJmxSupport(pmr, processName, envMap, url, httpcf);
        final CompositeMeterRegistry cmr = ppgjmxs.bindAndStart();
        _mr = cmr;

        // ---- THE "TO BECOME GENERIC WORKAROUND PART"
        //
        final JmxConfig jmxc = s -> null;
        final MeterRegistry mr = new JmxMeterRegistry(jmxc, Clock.SYSTEM);
        cmr.add(mr);

        // due to the fact we bind JvmMemoryMetric solely to JmxMeterRegistry they are not propagated to our pushgateway.
        // if really needed, we can add an JmxExporter as java agent
        new JvmMemoryMetrics().bindTo(mr);

        // in contrast all meters added to the CompositeMeterRegistry are collected as Jmx meter and send to pushgateway.
    }

    @Override protected MeterRegistry getMeterRegistry() { return _mr; }

    @SneakyThrows
    public static void main(final String[] args)
    {
        final Map<String, String> envMap = new HashMap<>();
        envMap.put("Key1", args[0]);
        envMap.put("Key2", "Value4");

        new PrometheusPushgatewayMicrometerJmxExporterOverJmxRunner(envMap).doBatchProcessSimulation();

        System.exit(0);
    }
}
