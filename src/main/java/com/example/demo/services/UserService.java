package com.example.demo.services;


import com.example.demo.exceptions.AuthException;
import com.example.demo.exceptions.DuplicateException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.auth.AuthPayload;
import com.example.demo.models.user.RegisterUser;
import com.example.demo.models.user.User;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User getById(UUID id) {
        return this.userRepository.getReferenceById(id);
    }

    public User getByUsername(String username) throws NotFoundException {
        return this.userRepository.findByUsername(username).orElseThrow(
            () -> new NotFoundException("User does not exist with username: " + username)
        );
    }

    public User register(RegisterUser registerUser) throws DuplicateException {
        User user = new User(registerUser.getUsername(), registerUser.getPassword());
        try {
            return this.userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateException("User with the username already exists");
        }
    }

}
