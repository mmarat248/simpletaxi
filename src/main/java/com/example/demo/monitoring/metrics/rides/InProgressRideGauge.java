package com.example.demo.monitoring.metrics.rides;

import com.example.demo.models.ride.Ride;
import com.example.demo.monitoring.MetricsRegistry;
import com.example.demo.monitoring.metrics.CustomMetric;
import com.example.demo.repositories.RideRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class InProgressRideGauge extends AbstractRideMetric implements CustomMetric {

    public static final String METRIC_NAME = "app.custom.rides.inprogress";

    public RideRepository rideRepository;

    public MetricsRegistry metricsRegistry;

    private AtomicLong gauge;

    public InProgressRideGauge(RideRepository rideRepository, MetricsRegistry metricsRegistry) {
        this.rideRepository = rideRepository;
        this.metricsRegistry = metricsRegistry;

        this.bindTo(this.metricsRegistry.getRegistry());
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        gauge = new AtomicLong(this.getValue());
        metricsRegistry.getRegistry().gauge(METRIC_NAME, this.getTags(), gauge);
    }

    @Scheduled(fixedRate = 30000)
    public void updateGaugeValue() {
        gauge.set(this.getValue());
    }

    private long getValue() {
        return this.rideRepository.countByStatus(Ride.Status.InProgress);
    }

}
