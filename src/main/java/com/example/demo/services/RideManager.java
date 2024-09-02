package com.example.demo.services;

import com.example.demo.models.ride.Ride;
import com.example.demo.models.event.RideEvent;
import com.example.demo.models.ride.RideListFilter;
import com.example.demo.models.ride.specifications.RideSpecification;
import com.example.demo.repositories.RideRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class RideManager {

    private final RideRepository rideRepository;

    private final Map<UUID, DeferredResult<ResponseEntity<RideEvent>>> sessions = new ConcurrentHashMap<>();

    public RideManager(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public void registerSession(UUID id, DeferredResult<ResponseEntity<RideEvent>> session) {
        // TODO: allow to have multiple session for a single user
        sessions.put(id, session);
    }

    public void unregisterSession(UUID id) {
        sessions.remove(id);
    }


    @Scheduled(fixedDelay = 5000)
    public void HandleRideRequests() {
        RideListFilter filter = new RideListFilter();
        filter.status = Ride.Status.New;
        RideSpecification specification = new RideSpecification(filter);

        // TODO: add limits & filters & logic
        List<Ride> rideRequests = this.rideRepository.findAll(where(specification));
        for (Ride ride : rideRequests) {
            for (DeferredResult<ResponseEntity<RideEvent>> session : sessions.values()) {
                session.setResult(ResponseEntity.ok(new RideEvent(ride.getId())));
            }
        }
    }

}
