package com.example.demo.controllers;

import com.example.demo.models.ErrorDetails;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthTestUtils authTestUtils;

    @Test
    public void testRegisterUser() {
        RegisterUser registerUser = new RegisterUser("testuser", "password".toCharArray());

        ResponseEntity<User> response = restTemplate.postForEntity(
        "/api/v1/users/register", registerUser, User.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    public void testRegisterUserDuplicateException() {
        RegisterUser registerUser = new RegisterUser("testuser", "password".toCharArray());
        restTemplate.postForEntity(
                "/api/v1/users/register", registerUser, User.class
        );

        ResponseEntity<ErrorDetails> response = restTemplate.postForEntity(
            "/api/v1/users/register", registerUser, ErrorDetails.class
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().message().contains("User with the username already exists"));
    }

    @Test
    public void testGetUserById() {
        RegisterUser registerUser = new RegisterUser("testuser", "password".toCharArray());
        ResponseEntity<User> registerResponse = restTemplate.postForEntity(
                "/api/v1/users/register", registerUser, User.class
        );
        UUID userId = registerResponse.getBody().getId();

        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(registerResponse.getBody());
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<User> response = restTemplate.exchange(
            "/api/v1/users/{id}", HttpMethod.GET, entity, User.class, registerResponse.getBody().getId()
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals(userId, response.getBody().getId());
    }

}