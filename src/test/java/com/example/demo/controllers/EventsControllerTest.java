package com.example.demo.controllers;

import com.example.demo.DemoApplication;
import com.example.demo.models.event.RideEvent;
import com.example.demo.models.ride.PollForRideRequest;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EventsControllerTest {

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
    }

    @Test
    public void testPollForRide() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<ResponseEntity<DeferredResult<ResponseEntity<RideEvent>>>> future = executorService.submit(() -> {
            HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(driverUser);
            PollForRideRequest request = new PollForRideRequest();
            HttpEntity<PollForRideRequest> entity = new HttpEntity<>(request, headers);
            return restTemplate.exchange(
                    "/api/v1/events/rides", HttpMethod.POST, entity, new ParameterizedTypeReference<>() {}
            );
        });

        HttpHeaders userHeaders = authTestUtils.createHttpHeadersWithToken(customer);
        RequestRide requestRide = new RequestRide(
            12.34, 56.78,
            98.76, 54.32
        );
        HttpEntity<RequestRide> rideEntity = new HttpEntity<>(requestRide, userHeaders);
        restTemplate.postForEntity("/api/v1/rides/request", rideEntity, Ride.class);

        // Check driver response
        ResponseEntity<DeferredResult<ResponseEntity<RideEvent>>> response = future.get(5, TimeUnit.SECONDS);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
