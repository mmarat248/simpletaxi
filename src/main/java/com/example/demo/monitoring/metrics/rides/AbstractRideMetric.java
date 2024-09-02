package com.example.demo.monitoring.metrics.rides;


import io.micrometer.core.instrument.Tags;

public class AbstractRideMetric {

    protected Tags getTags() {
        return Tags.of("category", "ride");
    }

}
