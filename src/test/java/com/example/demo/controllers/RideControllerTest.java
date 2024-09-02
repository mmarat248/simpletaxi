package com.example.demo.controllers;

import com.example.demo.models.driver.*;
import com.example.demo.models.ride.RequestRide;
import com.example.demo.models.ride.Ride;
import com.example.demo.models.user.RegisterUser;
import com.example.demo.models.user.User;
import com.example.demo.utils.AuthTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RideControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthTestUtils authTestUtils;

    private User customer;

    private User driverUser;

    @BeforeEach
    void setUp() {
        RegisterUser registerUser = new RegisterUser("customer", "password".toCharArray());
        ResponseEntity<User> registerResponse = restTemplate.postForEntity(
            "/api/v1/users/register", registerUser, User.class
        );
        customer = registerResponse.getBody();

        registerUser = new RegisterUser("driver", "password".toCharArray());
        registerResponse = restTemplate.postForEntity(
            "/api/v1/users/register", registerUser, User.class
        );
        driverUser = registerResponse.getBody();
        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(driverUser);
        HttpEntity<RegisterDriver> entity = new HttpEntity<>(new RegisterDriver(), headers);
        restTemplate.postForEntity(
            "/api/v1/drivers/register", entity, Driver.class
        );
    }

    @Test
    public void testRequestRide() {
        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(customer);
        RequestRide requestRide = new RequestRide(
                12.34, 56.78,
                98.76, 54.32
        );
        HttpEntity<RequestRide> entity = new HttpEntity<>(requestRide, headers);

        ResponseEntity<Ride> response = restTemplate.postForEntity(
            "/api/v1/rides/request", entity, Ride.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAcceptRide() {
        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(customer);
        RequestRide requestRide = new RequestRide(
                12.34, 56.78,
                98.76, 54.32
        );
        HttpEntity<RequestRide> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<Ride> rideResponse = restTemplate.postForEntity(
            "/api/v1/rides/request", entity, Ride.class
        );
        Ride ride = rideResponse.getBody();

        HttpHeaders driverHeaders = authTestUtils.createHttpHeadersWithToken(driverUser);
        HttpEntity<Void> driverEntity = new HttpEntity<>(null, driverHeaders);
        assert ride != null;
        ResponseEntity<Ride> acceptResponse = restTemplate.postForEntity(
            "/api/v1/rides/{id}/accept", driverEntity, Ride.class, ride.getId()
        );

        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());
        assertNotNull(acceptResponse.getBody());
    }



}