package com.example.demo.controllers;

import com.example.demo.models.driver.*;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DriverControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthTestUtils authTestUtils;

    private User user;

    private Driver driver;

    @BeforeEach
    void setUp() {
        RegisterUser registerUser = new RegisterUser("driver", "password".toCharArray());
        ResponseEntity<User> registerResponse = restTemplate.postForEntity(
            "/api/v1/users/register", registerUser, User.class
        );
        user = registerResponse.getBody();

        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(user);
        HttpEntity<RegisterDriver> entity = new HttpEntity<>(new RegisterDriver(), headers);
        ResponseEntity<Driver> response = restTemplate.postForEntity(
            "/api/v1/drivers/register", entity, Driver.class
        );
        driver = response.getBody();
    }

    @Test
    public void testGetDriverById() {
        // Create headers with authentication token
        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(user);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Send GET request to retrieve driver by ID
        ResponseEntity<Driver> response = restTemplate.exchange(
                "/api/v1/drivers/{id}", HttpMethod.GET, entity, Driver.class, driver.getId()
        );

        // Verify that the response has OK status and contains correct data
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(driver.getId(), response.getBody().getId());
        assertEquals(driver.getUserId(), response.getBody().getUserId());
        assertEquals(Driver.Status.Unavailable, response.getBody().getStatus());
    }

    @Test
    public void testListDrivers() {
        // Create a filter for searching drivers
        DriverListFilter filter = new DriverListFilter();
        filter.setStatus(Driver.Status.Unavailable);

        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(user);
        HttpEntity<DriverListFilter> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Driver>> response = restTemplate.exchange(
            "/api/v1/drivers/list?status={status}",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Driver>>() {}, filter.getStatus()
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().stream().anyMatch(d -> d.getId().equals(driver.getId())));
    }

    @Test
    public void testSetDriverStatus() {
        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(user);
        HttpEntity<SetDriverStatus> entity = new HttpEntity<>(new SetDriverStatus(Driver.Status.Available), headers);

        ResponseEntity<Void> response = restTemplate.exchange(
            "/api/v1/drivers/{id}/status", HttpMethod.POST, entity, Void.class, driver.getId()
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void testUpdateDriverLocation() {
        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(user);
        UpdateDriverLocation updateLocation = new UpdateDriverLocation(12.34, 56.78);
        HttpEntity<UpdateDriverLocation> entity = new HttpEntity<>(updateLocation, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/drivers/{id}/location", HttpMethod.PUT, entity, Void.class, driver.getId()
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }



}