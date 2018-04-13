package pl.allegro.atl.infrastructure.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UndertowMetricsBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(UndertowMetricsBootstrap.class);

    @Bean
    UndertowUtils undertowUtils(ServletWebServerApplicationContext ctx) {
        return new UndertowUtils(ctx);
    }

    @Bean
    UndertowMetricsConfiguration createUndertowMetricsConfiguration(MeterRegistry meterRegistry, UndertowUtils undertowUtils) {
        logger.info("Registering Undertow XNIO metrics.");

        return new UndertowMetricsConfiguration(meterRegistry, undertowUtils);
    }
}