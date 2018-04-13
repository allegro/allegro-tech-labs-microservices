package pl.allegro.atl.infrastructure.metrics;

import com.google.common.base.Joiner;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolMetrics {

    private static final String THREAD_POOLS_PREFIX = "thread-pools";
    private static final String ACTIVE_THREADS = "active-threads";
    private static final String CAPACITY = "capacity";
    private static final String UTILIZATION = "utilization";
    private static final String TASK_QUEUE_CAPACITY = "task-queue-capacity";
    private static final String TASK_QUEUED = "task-queue-size";
    private static final String TASKS_QUEUE_UTILIZATION = "task-queue-utilization";
    private static final String TASKS_QUEUE_WAIT = "task-queue-wait";

    public static void createGauges(MeterRegistry registry,
                                    String threadPoolName,
                                    ThreadPoolExecutor executor,
                                    BlockingQueue<Runnable> queue) {
        registry.gauge(name(threadPoolName, CAPACITY), executor, ThreadPoolExecutor::getPoolSize);
        registry.gauge(name(threadPoolName, ACTIVE_THREADS), executor, ThreadPoolExecutor::getActiveCount);
        registry.gauge(name(threadPoolName, UTILIZATION), executor,
                pool -> (double) pool.getActiveCount() / (double) pool.getPoolSize());
        registry.gauge(name(threadPoolName, TASK_QUEUE_CAPACITY), queue,
                q -> {
                    int qCapacity = q.size() + q.remainingCapacity();
                    return qCapacity < 0 ? Integer.MAX_VALUE : qCapacity;
                });
        registry.gauge(name(threadPoolName, TASK_QUEUED), queue, Collection::size);
        registry.gauge(name(threadPoolName, TASKS_QUEUE_UTILIZATION), queue,
                q -> {
                    int calculatedCapacity = q.size() + q.remainingCapacity();
                    int queueCapacity = calculatedCapacity < 0 ? Integer.MAX_VALUE : calculatedCapacity;
                    return (double) q.size() / (double) queueCapacity;
                });
    }

    public static String getTaskQueueWaitTimeMetricName(String threadPoolName) {
        return name(threadPoolName, TASKS_QUEUE_WAIT);
    }

    private static String name(String threadPoolName, String suffix) {
        return Joiner.on('.').join(THREAD_POOLS_PREFIX, threadPoolName, suffix);
    }
}