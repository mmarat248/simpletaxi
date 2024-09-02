package com.example.demo.repositories;

import com.example.demo.models.ride.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID>, JpaSpecificationExecutor<Ride> {
    long countByStatus(Ride.Status status);
}
