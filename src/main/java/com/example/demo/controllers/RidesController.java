package com.example.demo.controllers;


import com.example.demo.exceptions.AuthException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.auth.AuthPayload;
import com.example.demo.models.ride.RequestRide;
import com.example.demo.models.ride.Ride;
import com.example.demo.monitoring.MetricsRegistry;
import com.example.demo.monitoring.metrics.rides.RideRequestCounter;
import com.example.demo.services.AuthService;
import com.example.demo.services.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;



@RequiredArgsConstructor
@RequestMapping("/api/v1/rides")
@RestController
public class RidesController {

    private final MetricsRegistry metricsRegistry;

    private final AuthService authService;

    private final RideService rideService;


    @PostMapping("/request")
    public ResponseEntity<Ride> request(
        @Valid @RequestBody RequestRide requestRide
    ) throws AuthException {
        AuthPayload auth = authService.getAuthPayload();

        metricsRegistry.getCounter(RideRequestCounter.METRIC_NAME).increment();
        Ride ride = rideService.requestRide(auth, requestRide);
        return ResponseEntity.status(HttpStatus.OK).body(ride);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Ride> accept(
        @PathVariable UUID id
    ) throws NotFoundException, BadRequestException, AuthException {
        AuthPayload auth = authService.getAuthPayload();

        metricsRegistry.getCounter(RideRequestCounter.METRIC_NAME).increment();
        Ride ride = rideService.accept(auth, id);
        return ResponseEntity.
            status(HttpStatus.OK).
            body(ride);
    }


}
