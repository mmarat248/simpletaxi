package com.example.demo.services;


import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.auth.AuthPayload;
import com.example.demo.models.driver.Driver;
import com.example.demo.models.ride.RequestRide;
import com.example.demo.models.ride.Ride;
import com.example.demo.repositories.DriverRepository;
import com.example.demo.repositories.RideRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class RideService {

    private final RideRepository rideRepository;

    private final DriverRepository driverRepository;

    public Ride requestRide(AuthPayload auth, RequestRide requestRide) {
        Ride ride = new Ride(
            auth.getUserId(),
            requestRide.getStartLocationLatitude(),
            requestRide.getStartLocationLongitude(),
            requestRide.getTargetLocationLatitude(),
            requestRide.getTargetLocationLongitude()
        );
        return this.rideRepository.save(ride);
    }

    public Ride accept(AuthPayload auth, UUID id) throws NotFoundException, BadRequestException {
        Ride ride = rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride not found"));
        if (ride.getStatus() != Ride.Status.New) {
            throw new BadRequestException("wrong status");
        }
        ride.setStatus(Ride.Status.InProgress);

        if (ride.getDriverID() != null) {
            throw new BadRequestException("ride is already accepted");
        }
        // TODO: move to context
        Driver driver = this.driverRepository.findOneByUserId(
            auth.getUserId()
        ).orElseThrow(() -> new NotFoundException("Driver not found"));

        ride.setDriverID(driver.getId());

        // TODO: use optimistic lock to avoid race
        return this.rideRepository.save(ride);
    }

}
