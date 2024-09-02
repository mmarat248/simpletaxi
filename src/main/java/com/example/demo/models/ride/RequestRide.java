package com.example.demo.models.ride;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class RequestRide {

    private Double startLocationLatitude;

    private Double startLocationLongitude;

    private Double targetLocationLatitude;

    private Double targetLocationLongitude;

    // META INFO

}
