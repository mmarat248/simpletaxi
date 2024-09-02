package com.example.demo.models.driver;

import com.example.demo.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "drivers")
public class Driver {

    public enum Status {
        Available,
        Unavailable,
        Booked
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(nullable = false)
    private Status status = Status.Unavailable;

    // TODO: META_INFO

    public Driver(UUID userId) {
        this.userId = userId;
    }

}
