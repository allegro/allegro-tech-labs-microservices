package pl.allegro.atl.infrastructure.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

class UndertowMetricsConfiguration implements ApplicationListener<ServletWebServerInitializedEvent> {

    private static final String UNDERTOW_THREAD_POOL_NAME = "undertow";

    private final MeterRegistry registry;

    private final UndertowUtils undertowUtils;

    UndertowMetricsConfiguration(MeterRegistry registry, UndertowUtils undertowUtils) {
        this.registry = registry;
        this.undertowUtils = undertowUtils;
    }

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        ThreadPoolExecutor executor = undertowUtils.getUndertowTaskPool();
        BlockingQueue<Runnable> queue = executor.getQueue();

        ThreadPoolMetrics.createGauges(registry, UNDERTOW_THREAD_POOL_NAME, executor, queue);
    }
}