package com.example.demo.controllers;

import com.example.demo.exceptions.AuthException;
import com.example.demo.exceptions.DuplicateException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.auth.AuthPayload;
import com.example.demo.models.driver.*;
import com.example.demo.services.AuthService;
import com.example.demo.services.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
@RestController
public class DriverController {

    private final AuthService authService;

    private final DriverService driverService;

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getById(@PathVariable UUID id) throws NotFoundException {
        Driver response = driverService.getById(id);
        return ResponseEntity.
            status(HttpStatus.OK).
            body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Driver>> list(@Valid DriverListFilter filter) {
        List<Driver> response = driverService.list(filter);
        return ResponseEntity.
            status(HttpStatus.OK).
            body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Driver> register(
        @Valid @RequestBody RegisterDriver registerDriver
    ) throws DuplicateException, AuthException {
        AuthPayload auth = authService.getAuthPayload();

        Driver driver = driverService.register(auth.getUserId(), registerDriver);
        return ResponseEntity.status(HttpStatus.OK).body(driver);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Driver> setStatus(
        @PathVariable UUID id, @Valid @RequestBody SetDriverStatus setDriverStatus
    ) throws AuthException, NotFoundException {
        AuthPayload auth = authService.getAuthPayload();

        driverService.setStatus(
            auth, id, setDriverStatus.getStatus()
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateDriverLocation updateDriverLocation
    ) throws NotFoundException {
        driverService.updateLocation(id, updateDriverLocation.getLatitude(), updateDriverLocation.getLongitude());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
