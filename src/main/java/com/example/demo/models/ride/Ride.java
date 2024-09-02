package com.example.demo.models.ride;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@Entity(name = "rides")
public class Ride {

    public enum Status {
        New,
        InProgress,
        Completed
        // TODO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private Ride.Status status = Ride.Status.New;

    @Column(nullable = false)
    private UUID userID;

    @Column
    private UUID driverID;

    @Column(name = "start_latitude")
    private Double startLocationLatitude;

    @Column(name = "start_longitude")
    private Double startLocationLongitude;


    @Column(name = "target_latitude")
    private Double targetLocationLatitude;

    @Column(name = "target_longitude")
    private Double targetLocationLongitude;

    public Ride(
        UUID userID,
        Double startLocationLatitude,
        Double startLocationLongitude,
        Double targetLocationLatitude,
        Double targetLocationLongitude
    ) {
        this.userID = userID;
        this.startLocationLatitude = startLocationLatitude;
        this.startLocationLongitude = startLocationLongitude;
        this.targetLocationLatitude = targetLocationLatitude;
        this.targetLocationLongitude = targetLocationLongitude;
    }

}
