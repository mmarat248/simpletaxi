package com.example.demo.controllers;


import com.example.demo.services.RideManager;
import com.example.demo.exceptions.AuthException;
import com.example.demo.models.auth.AuthPayload;
import com.example.demo.models.event.RideEvent;
import com.example.demo.models.ride.PollForRideRequest;
import com.example.demo.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;


@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@RestController
public class EventsController {

    private final AuthService authService;

    private final RideManager rideManager;

    @PostMapping("/rides")
    public DeferredResult<ResponseEntity<RideEvent>> pollForRide(
        @Valid @RequestBody PollForRideRequest request
    ) throws AuthException {
        AuthPayload auth = authService.getAuthPayload();

        DeferredResult<ResponseEntity<RideEvent>> output = new DeferredResult<>(60_000L);
        output.onTimeout(() -> {
            rideManager.unregisterSession(auth.getUserId());
            output.setResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build());
        });

        rideManager.registerSession(auth.getUserId(), output);
        return output;
    }


}
