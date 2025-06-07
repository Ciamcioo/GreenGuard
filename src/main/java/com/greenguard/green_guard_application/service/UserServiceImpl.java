package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.aspect.annotation.EnableMethodLog;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.LocationRepository;
import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.service.exception.LocationNotFoundException;
import com.greenguard.green_guard_application.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements  UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public UserServiceImpl(UserRepository userRepository, LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    @EnableMethodLog
    public Set<Location> getFavoriteLocations(String username) {
        User  tragetUser = userRepository.findById(username)
                                 .orElseThrow(() -> new UserNotFoundException("User with provided username not found."));

        return Set.copyOf(tragetUser.getFavoriteLocations());
    }

    @Override
    @Transactional
    @EnableMethodLog
    public Set<Location> addFavoriteLocation(String username, String locationName) {
        User  tragetUser = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User with provided username not found."));

        Location newLocation = locationRepository.findById(locationName)
                .orElseThrow(() -> new LocationNotFoundException("Location with provided name not found."));

        Set<Location> favLocations = new HashSet<>(tragetUser.getFavoriteLocations());
        favLocations.add(newLocation);
        tragetUser.setFavoriteLocations(favLocations);
        userRepository.save(tragetUser);

        return favLocations;
    }

    @Override
    @EnableMethodLog
    public Set<Location> removeFavoriteLocation(String username, String locationName) {
        User targetUser = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User with specified username does not exists."));

        log.trace("Fetching favorite locations");
        Set<Location> favLocations = targetUser.getFavoriteLocations();

        log.trace("Finding matching location");
        Optional<Location> locationToRemove = favLocations.stream()
                                                          .filter
                                                            (favLocation -> favLocation.getName().equals(locationName))
                                                          .findFirst();
        if (locationToRemove.isPresent()) {
            log.trace("Location to remove is present");
            favLocations.remove(locationToRemove.get());
            targetUser.setFavoriteLocations(favLocations);
            userRepository.save(targetUser);
        }

        return favLocations;
    }


}
