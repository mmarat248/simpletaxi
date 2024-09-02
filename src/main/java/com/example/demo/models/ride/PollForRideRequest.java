package com.example.demo.models.ride;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PollForRideRequest {
    private Double latitude;

    private Double longitude;
}
