package com.example.demo.controllers;

import com.example.demo.models.auth.SignInRequest;
import com.example.demo.models.auth.SignInResponse;
import com.example.demo.models.user.RegisterUser;
import com.example.demo.models.user.User;
import com.example.demo.utils.AuthTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthTestUtils authTestUtils;

    @Test
    public void testGetInfoWithValidToken() {
        RegisterUser registerUser = new RegisterUser("testuser", "password".toCharArray());
        ResponseEntity<User> registerResponse = restTemplate.postForEntity(
                "/api/v1/users/register", registerUser, User.class
        );

        HttpHeaders headers = authTestUtils.createHttpHeadersWithToken(registerResponse.getBody());
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<User> response = restTemplate.exchange(
                "/api/v1/auth/info", HttpMethod.GET, entity, User.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    public void testSignIn() {
        RegisterUser registerUser = new RegisterUser("testuser", "password".toCharArray());
        restTemplate.postForEntity("/api/v1/users/register", registerUser, User.class);

        SignInRequest signInRequest = new SignInRequest("testuser", "password".toCharArray());
        ResponseEntity<SignInResponse> response = restTemplate.postForEntity(
            "/api/v1/auth/sign-in", signInRequest, SignInResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().accessToken());
    }

}