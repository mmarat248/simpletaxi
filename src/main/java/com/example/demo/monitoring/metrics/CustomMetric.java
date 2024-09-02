package com.example.demo.monitoring.metrics;

import io.micrometer.core.instrument.MeterRegistry;

public interface CustomMetric {
    void bindTo(MeterRegistry registry);
}