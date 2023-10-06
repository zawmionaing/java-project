package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.Location;
import com.java.jobportal.repository.LocationRepository;
import com.java.jobportal.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Override
    public Location getLocationById(Long locationId) {
        return locationRepository.findById(locationId).get();
    }
}
