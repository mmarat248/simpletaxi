package com.example.demo.controllers;


import com.example.demo.exceptions.AuthException;
import com.example.demo.models.auth.SignInRequest;
import com.example.demo.models.auth.SignInResponse;
import com.example.demo.models.user.User;
import com.example.demo.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/info")
    public ResponseEntity<User> getInfo() throws AuthException {
        User info = authService.getUser();
        return ResponseEntity.
                status(HttpStatus.OK).
                body(info);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(
        @Valid @RequestBody SignInRequest signIn
    ) throws AuthException {
        SignInResponse response = authService.signIn(signIn);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(response);
    }

}
