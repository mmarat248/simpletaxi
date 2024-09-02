package com.example.demo.services;


import com.example.demo.configurations.jwt.JWTTokenProvider;
import com.example.demo.exceptions.AuthException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.auth.AuthPayload;
import com.example.demo.models.auth.SignInRequest;
import com.example.demo.models.auth.SignInResponse;
import com.example.demo.models.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final JWTTokenProvider tokenService;

    private final UserService userService;

    public SignInResponse signIn(SignInRequest signInRequest) throws AuthException {
        User user;
        try {
            user = userService.getByUsername(signInRequest.username());
        } catch (NotFoundException ex) {
            throw new AuthException("Invalid username or password");
        }
        if (!user.checkPassword(signInRequest.password())) {
            throw new AuthException("Invalid username or password");
        }

        var accessToken = tokenService.generateAccessToken(user);
        return new SignInResponse(accessToken);
    }

    public User getUser() throws AuthException {
        var payload = getAuthPayload();
        return userService.getById(payload.getUserId());
    }

    public AuthPayload getAuthPayload() throws AuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthPayload payload)) {
            throw new AuthException("Unauthorized request");
        }
        return payload;
    }

}
