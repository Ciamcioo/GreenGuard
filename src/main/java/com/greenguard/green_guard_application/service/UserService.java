package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.entity.Location;

import java.util.List;
import java.util.Set;

public interface UserService {
    Set<Location> getFavoriteLocations(String username);

    Set<Location> addFavoriteLocation(String username, String locationName);

    Set<Location> removeFavoriteLocation(String username, String locationName);
}
