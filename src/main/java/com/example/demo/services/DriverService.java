package com.example.demo.services;


import com.example.demo.exceptions.AuthException;
import com.example.demo.exceptions.DuplicateException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.auth.AuthPayload;
import com.example.demo.models.driver.Driver;
import com.example.demo.models.driver.DriverListFilter;
import com.example.demo.models.driver.RegisterDriver;
import com.example.demo.models.driver.specifications.DriverSpecification;
import com.example.demo.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.where;

@RequiredArgsConstructor
@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public List<Driver> list(DriverListFilter filter) {
        DriverSpecification specification = new DriverSpecification(filter);
        return driverRepository.findAll(where(specification));
    }

    public Driver getById(UUID id) throws NotFoundException {
        return this.driverRepository.findById(id).orElseThrow(() -> new NotFoundException("Driver not found"));
    }

    public Driver register(UUID userId, RegisterDriver registerDriver) throws DuplicateException {
        Driver driver = new Driver(userId);
        return this.driverRepository.save(driver);
    }

    public void setStatus(AuthPayload auth, UUID id, Driver.Status status) throws AuthException, NotFoundException {
        Driver driver = this.driverRepository.findById(id).orElseThrow(() -> new NotFoundException("Driver not found"));
        if (!driver.getUserId().equals(auth.getUserId())) {
            throw new AuthException("wrong user");
        }

        driver.setStatus(status);
        this.driverRepository.save(driver);
    }

    public void updateLocation(UUID id, Double latitude, Double longitude) throws NotFoundException {
        Driver driver = this.driverRepository.findById(id).orElseThrow(() -> new NotFoundException("Driver not found"));
        driver.setLongitude(latitude);
        driver.setLongitude(longitude);
        this.driverRepository.save(driver);
    }

}
