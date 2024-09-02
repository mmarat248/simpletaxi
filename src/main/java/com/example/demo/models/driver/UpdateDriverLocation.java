package com.example.demo.models.driver;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateDriverLocation {
    private Double latitude;

    private Double longitude;

}