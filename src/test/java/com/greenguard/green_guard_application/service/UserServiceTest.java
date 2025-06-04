package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.LocationRepository;
import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.service.exception.LocationNotFoundException;
import com.greenguard.green_guard_application.service.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    // DEFAULT TEST VALUES
    private static final String username = "example user";
    private static final String password = "password";
    private static final String locationName_Rzeszow = "Rzeszow";
    private static final String locationName_Wroclaw = "Wroclaw";
    private static final Location locationRzeszow = new Location(locationName_Rzeszow);
    private static final Location locationWroclaw = new Location(locationName_Wroclaw);

    UserService userService;
    UserRepository userRepository;
    LocationRepository locationRepository;

    List<Location> favLocations;
    User user;

    @BeforeEach
    void setup() {
        favLocations = List.of(locationRzeszow, locationWroclaw);
        user = new User(username, password, favLocations);

        userRepository = mock(UserRepository.class);
        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        locationRepository = mock(LocationRepository.class);
        when(locationRepository.findById(locationName_Rzeszow)).thenReturn(Optional.of(locationRzeszow));
        when(locationRepository.findById(locationName_Wroclaw)).thenReturn(Optional.of(locationWroclaw));

        userService = new UserServiceImpl(userRepository, locationRepository);
    }

    @Test
    @DisplayName("Method getFavoriteLocations() should not return null value")
    void getFavoriteLocationsShouldNotReturnNullValue() {
        assertNotNull(userService.getFavoriteLocations(username));
    }

    @Test
    @DisplayName("Method getFavoriteLocations() should return favorite locations for user with specified username")
    void getFavoriteLocationsShouldReturnLocationsForSpecifiedUser() {
        List<Location> twoLocationList = List.of(new Location("Rzeszow"), new Location("Wroclaw"));
        User twoFavLocationUser = new User(username, password, twoLocationList);

        List<Location> oneLocationList = List.of(new Location("Wroclaw"));
        User oneFavLocationUser = new User("one location test user", password, oneLocationList);

        when(userRepository.findById(twoFavLocationUser.getUsername()))
                .thenReturn(Optional.of(twoFavLocationUser));
        when(userRepository.findById(oneFavLocationUser.getUsername()))
                .thenReturn(Optional.of(oneFavLocationUser));

        final Set<Location> resultWithTwoFavLocations = userService.getFavoriteLocations(twoFavLocationUser.getUsername());
        twoLocationList.forEach(location -> assertTrue(resultWithTwoFavLocations.contains(location)));

        final Set<Location> resultWithOneFavLocation = userService.getFavoriteLocations(oneFavLocationUser.getUsername());
        oneLocationList.forEach(location -> assertTrue(resultWithOneFavLocation.contains(location)));
    }

    @Test
    @DisplayName("Method getFavoriteLocations() should throw an UserNotFoundException if there is no user with provided username")
    void getFavoriteLocationsShouldThrowUserNotFoundException() {
        String notValidUsername = username;
        when(userRepository.findById(notValidUsername)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getFavoriteLocations(notValidUsername));
    }

    @Test
    @DisplayName("Method addFavoriteLocation() not return null value")
    void addFavoriteLocationShouldNotReturnNullValue() {
        assertNotNull(userService.addFavoriteLocation(username, locationName_Rzeszow));
    }

    @Test
    @DisplayName("Method addFavoriteLocation() should return a list of locations not update")
    void addFavoriteLocationShouldReturnList() {
        final Set<Location> resultFavLocations = userService.addFavoriteLocation(username, "Rzeszow");
        favLocations.forEach(location -> assertTrue(resultFavLocations.contains(location)) );
    }

    @Test
    @DisplayName("Method addFavoriteLocation() should throw an UserNotFoundException if there is no user with provided username")
    void addFavoriteLocationsShouldThrowUserNotFoundException() {
        String notValidUsername = username;
        when(userRepository.findById(notValidUsername)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.addFavoriteLocation(notValidUsername, "Katowice"));
    }

    @Test
    @DisplayName("Method addFavoriteLocation() should add new location to the favorite locations and return update list")
    void addFavoriteLocationShouldAddLocationAndReturnUpdateList() {
        String newLocation = "Katowice";

        when(locationRepository.findById(newLocation)).thenReturn(Optional.of(new Location(newLocation)));

        final Set<Location> resultFavLocations = userService.addFavoriteLocation(username, newLocation);

        assertEquals(favLocations.size() + 1, resultFavLocations.size());
        favLocations.forEach(location -> assertTrue(resultFavLocations.contains(location)) );
        assertTrue(resultFavLocations.stream().anyMatch(location -> location.getName().equals(newLocation)));
    }

    @Test
    @DisplayName("Method addFavoriteLocation() should perform the save to UserRepository")
    void addFavoriteLocationShouldCallTheUserRepository() {
        String newLocation = "Katowice";

        when(locationRepository.findById(newLocation)).thenReturn(Optional.of(new Location(newLocation)));

        userService.addFavoriteLocation(username, newLocation);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Method addFavoriteLocation() should throw LocationNotFoundException if there is no location with such a name")
    void addFavoriteLocationShouldThrowLocationNotFoundException() {
        String notKnownLocation = "not known location";
        when(locationRepository.findById(notKnownLocation)).thenReturn(Optional.empty());

        assertThrows(LocationNotFoundException.class, () -> userService.addFavoriteLocation(username, notKnownLocation));
    }

    @Test
    @DisplayName("Method removeFavoriteLocation() should not return null value")
    void removeFavoriteLocationShouldNotReturnNull() {
        assertNotNull(userService.removeFavoriteLocation(username, locationName_Wroclaw));
    }

    @Test
    @DisplayName("Method removeFavoriteLocation() should throw UserNotFound if the user with specified username was not found")
    void removeFavoriteLocationShouldThrowUserNotFoundException() {
        String notKnownUser = "not known user";

        assertThrows(UserNotFoundException.class, () -> userService.removeFavoriteLocation(notKnownUser, locationName_Rzeszow));
    }

    @Test
    @DisplayName("If provided locationName is not valid the favoriteLocation set should remain the same")
    void removeFavoriteLocationShouldNotChangeSetFavoriteLocationIfTheLocationNameIsNotValid() {
        String notFavLocationName = "not favorite location name";

        assertEquals(favLocations.size(), userService.removeFavoriteLocation(username, notFavLocationName).size());
    }

    @Test
    @DisplayName("If location with specified name will be removed the result location set should not contains it")
    void removeFavoriteLocationShouldNotContainedRemovedLocation() {
        String locationNameToRemove = locationName_Wroclaw;
        Set<Location> resultLocationSet = userService.removeFavoriteLocation(username, locationNameToRemove);

        assertFalse(resultLocationSet.stream().anyMatch
                (resultLocation -> resultLocation.getName().equals(locationNameToRemove))
        );
    }

    @Test
    @DisplayName("After remove user object should contains the actual collection of locations")
    void removeFavoriteLocationShouldUpdateUserCollectionsOfFavLocations() {
        Set<Location> resultLocationSet = userService.removeFavoriteLocation(username, locationName_Wroclaw);

       assertEquals(user.getFavoriteLocations().size(), resultLocationSet.size());
       resultLocationSet.forEach(
               resultLocation -> assertTrue(user.getFavoriteLocations().contains(resultLocation))
       );
    }

}
