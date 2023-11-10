package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Info;
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory;
import io.prometheus.client.exporter.PushGateway;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PrometheusPushgatewaySampleRunner
{
    public static final String HELP_TEXT_OF = "help text of ";
    public static final String PROCESS = "Process";

    private static final Map<String, String> MAP = new HashMap<>();

    @SneakyThrows
    public static void main(final String[] args)
    {
        MAP.put("Key1", args[0]);
        MAP.put("Key2", "Value2");

        final PushGateway client = new PushGateway(new URL("http://localhost/pushgateway"));
        client.setConnectionFactory(new BasicAuthHttpConnectionFactory("admin", "admin"));
        final CollectorRegistry registry = CollectorRegistry.defaultRegistry;

        final String processName = PrometheusPushgatewaySampleRunner.class.getSimpleName();

        final String nameInfo = "info";
        final Info info = Info.build()
                .namespace("namespace")
                .name(nameInfo)
                .help(HELP_TEXT_OF + nameInfo)
                .register(registry);
        info.info(PROCESS, "Started");

        push(client, registry, processName, true);

        System.out.println("Started...");

        // ----

        final String nameCounter = "counter";
        final Counter counter = Counter.build()
                .labelNames(nameCounter)
                .namespace("namespace")
                .name(nameCounter)
                .help(HELP_TEXT_OF + nameCounter)
                .withExemplars()
                .register(registry);

        // ----

        final String nameCounterGauge = "counterGauge";
        final Gauge counterGauge = Gauge.build()
                .labelNames(nameCounterGauge)
                .namespace("namespace")
                .name(nameCounterGauge)
                .help(HELP_TEXT_OF + nameCounterGauge)
                .register(registry);

        for (int i = 1; i < 100; i++) {
            if (i == 50) {
                info.info(PROCESS, "running (50% done)");
            }

            try {
                Thread.sleep(600);
            } catch (final InterruptedException e) {
                // someone woke us up
            }

            counter.labels(nameCounter).inc();
            counterGauge.labels(nameCounterGauge).inc();

            push(client, registry, processName, false);

            System.out.println("   running " + i);
        }

        info.info( PROCESS, "finished");

        counterGauge.labels(nameCounterGauge).set(0);
        push(client, registry, processName, false);

        System.out.println("...finished");
    }

    private static void push(final PushGateway client, final CollectorRegistry registry,
                             final String processName, final boolean abortOnError)
    {
        try {
            client.push(registry, processName, MAP);
        } catch (final Exception e) {
            System.err.println("unable to push data");
            e.printStackTrace();
            if (abortOnError) {
                throw new IllegalStateException(e);
            }
        }
    }
}
