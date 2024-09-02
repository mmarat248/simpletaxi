package com.example.demo.monitoring;



import com.example.demo.monitoring.metrics.CustomMetric;
import com.example.demo.monitoring.metrics.rides.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import lombok.Getter;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Getter
@Configuration
public class MetricsRegistry {

    private final PrometheusMeterRegistry registry;

    private final Map<String, Meter> metrics = new ConcurrentHashMap<>();

    public MetricsRegistry() {
        this.registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.config().meterFilter(new MeterFilter() {
            @Override
            public MeterFilterReply accept(Meter.Id id) {
                if (id.getName().startsWith("app.custom")) {
                    return MeterFilterReply.ACCEPT;
                }
                return MeterFilterReply.DENY;
            }
        });
    }

    public Counter getCounter(String name) {
        return (Counter) metrics.get(name);
    }
//
//    @EventListener(ApplicationStartedEvent.class)
//    private void registerMetrics() {
//        metrics.forEach((_, metric) -> metric.bindTo(registry));
//    }

}

