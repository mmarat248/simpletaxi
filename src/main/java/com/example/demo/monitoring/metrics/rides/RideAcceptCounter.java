package com.example.demo.monitoring.metrics.rides;

import com.example.demo.monitoring.MetricsRegistry;
import com.example.demo.monitoring.metrics.CustomMetric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RideAcceptCounter extends AbstractRideMetric implements CustomMetric {

    public static final String METRIC_NAME = "app.custom.rideAccept.counter";

    public MetricsRegistry metricsRegistry;

    public RideAcceptCounter(MetricsRegistry metricsRegistry) {
        this.metricsRegistry = metricsRegistry;

        this.bindTo(this.metricsRegistry.getRegistry());
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        var counter = registry.counter(METRIC_NAME, this.getTags());
        this.metricsRegistry.getMetrics().put(METRIC_NAME, counter);
    }

}
