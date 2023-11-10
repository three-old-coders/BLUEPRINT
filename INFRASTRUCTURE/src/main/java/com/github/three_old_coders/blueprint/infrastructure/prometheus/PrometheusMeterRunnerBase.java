package com.github.three_old_coders.blueprint.infrastructure.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

abstract class PrometheusMeterRunnerBase
{
    protected abstract MeterRegistry getMeterRegistry();

    protected void doBatchProcessSimulation()
    {
        final Counter batchProcessProgress = getMeterRegistry().counter("batch process progress", Tags.of("counter", "micrometer counter"));

        System.out.println("Started...");

        // ----

        for (int i = 1; i < 10; i++) {
            try {
                Thread.sleep(600);
            } catch (final InterruptedException e) {
                // someone woke us up
            }
            batchProcessProgress.increment();

            System.out.println("   running " + i);
        }

        System.out.println("...finished");
    }
}
