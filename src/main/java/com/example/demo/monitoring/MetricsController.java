package com.example.demo.monitoring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MetricsController {

    private final MetricsRegistry metricsRegistry;

    public MetricsController(MetricsRegistry metricsRegistry) {
        this.metricsRegistry = metricsRegistry;
    }

    @GetMapping(value = "/metrics", produces = "text/plain")
    public ResponseEntity<String> getAppMetrics() {
        return ResponseEntity.ok(metricsRegistry.getRegistry().scrape());
    }

}
