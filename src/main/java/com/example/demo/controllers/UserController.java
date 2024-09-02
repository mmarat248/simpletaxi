package com.example.demo.controllers;

import com.example.demo.exceptions.DuplicateException;
import com.example.demo.models.user.RegisterUser;
import com.example.demo.models.user.User;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id) {
        User response = userService.getById(id);
        return ResponseEntity.
            status(HttpStatus.OK).
            body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterUser registerUser) throws DuplicateException {
        User user = userService.register(registerUser);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
