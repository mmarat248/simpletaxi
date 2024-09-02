package com.example.demo.monitoring.metrics.rides;

import com.example.demo.monitoring.MetricsRegistry;
import com.example.demo.monitoring.metrics.CustomMetric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RideRequestCounter extends AbstractRideMetric implements CustomMetric {

    public static final String METRIC_NAME = "app.custom.rideRequest.counter";

    public MetricsRegistry metricsRegistry;

    public RideRequestCounter(MetricsRegistry metricsRegistry) {
        this.metricsRegistry = metricsRegistry;

        this.bindTo(this.metricsRegistry.getRegistry());
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        var counter = registry.counter(METRIC_NAME, this.getTags());
        this.metricsRegistry.getMetrics().put(METRIC_NAME, counter);
    }

}
