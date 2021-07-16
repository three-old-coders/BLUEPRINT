package com.github.three_old_coders.blueprint.common;

import java.util.concurrent.*;

public class BlockingExecutorService
{
    private final ExecutorService _executorService;

    public BlockingExecutorService(final int noOfThreads, final int workQueueSize, final int abortIfStalled_sec)
    {
        final ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(workQueueSize);
        final RejectedExecutionHandler reh = (r, executor) -> {
            final long start_sec = System.currentTimeMillis() / 1000;
            while (0 == workQueue.remainingCapacity()) {
                try {
                    final long end_sec = System.currentTimeMillis() / 1000;
                    if (end_sec - start_sec > abortIfStalled_sec) {
                        throw new IllegalStateException("executor service seems stalled. no changes after [" + abortIfStalled_sec + "_sec]");
                    }
                    Thread.sleep(100);
                    // System.out.println("waited [1_sec]");
                } catch (final InterruptedException e) {
                }
            }

            execute(r);
            // System.out.println("re-submitted [" + r + "]");
        };

         _executorService = new ThreadPoolExecutor(noOfThreads, noOfThreads, 0L, TimeUnit.MILLISECONDS, workQueue, reh);
    }

    public void execute(final Runnable runnable)
    {
        // System.out.printf("submitted [" + runnable + "]");
        _executorService.execute(runnable);
    }

    public void shutdownAndAwaitTermination()
    {
        _executorService.shutdown();
        try {
            _executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (final InterruptedException e) {
            System.err.println("shutdown interrupted" + e);
        }
    }
}
