package com.example.demo.utils;

import com.example.demo.configurations.jwt.JWTTokenProvider;
import com.example.demo.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthTestUtils {

    @Autowired
    private JWTTokenProvider tokenProvider;

    public HttpHeaders createHttpHeadersWithToken(User user) {
        String token = tokenProvider.generateAccessToken(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
