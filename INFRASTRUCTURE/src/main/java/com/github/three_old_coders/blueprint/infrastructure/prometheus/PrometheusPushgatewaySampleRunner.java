package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Info;
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory;
import io.prometheus.client.exporter.PushGateway;
import lombok.SneakyThrows;

import java.net.URL;

public class PrometheusPushgatewaySampleRunner
{

    public static final String HELP_TEXT_OF = "help text of ";
    public static final String PROCESS = "Process";

    @SneakyThrows
    public static void main(final String[] args)
    {
        final PushGateway client = new PushGateway(new URL("http://localhost:9091"));
        client.setConnectionFactory(new BasicAuthHttpConnectionFactory("admin", "admin"));
        final CollectorRegistry registry = CollectorRegistry.defaultRegistry;

        final String processName = PrometheusPushgatewaySampleRunner.class.getName();

        final String nameInfo = "info";
        final Info info = Info.build()
                .name(nameInfo)
                .help(HELP_TEXT_OF + nameInfo)
                .register(registry);
        info.info(PROCESS, "Started");

        push(client, registry, processName, true);

        System.out.println("Started...");

        // ----

        final String nameCounter = "counter";
        final Counter counter = Counter.build()
                .name(nameCounter)
                .help(HELP_TEXT_OF + nameCounter)
                .labelNames(nameCounter)
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

            push(client, registry, processName, false);

            System.out.println("   running " + i);
        }

        info.info( PROCESS, "finished");

        push(client, registry, processName, false);

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
