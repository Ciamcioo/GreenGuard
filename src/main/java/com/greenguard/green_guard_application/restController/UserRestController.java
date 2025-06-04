package com.greenguard.green_guard_application.restController;

import com.greenguard.green_guard_application.aspect.annotation.EnableMethodLog;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/favorite-locations")
    @EnableMethodLog
    public ResponseEntity<Set<Location>> getFavoriteLocation() {
        return ResponseEntity.ok(
                userService.getFavoriteLocations(SecurityContextHolder.getContext()
                                                                      .getAuthentication()
                                                                      .getName())
        );
    }

    @PostMapping("/user/favorite-locations/{name}")
    @EnableMethodLog
    public ResponseEntity<Set<Location>> addFavoriteLocation(@PathVariable("name") String newFavoriteLocation) {
        return ResponseEntity.ok(
                userService.addFavoriteLocation(
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        newFavoriteLocation
                )
        );
    }

    @DeleteMapping("/user/favorite-locations/{name}")
    @EnableMethodLog
    public ResponseEntity<Set<Location>> deleteFavoriteLocation(@PathVariable("name") String locationToDelete) {
        return ResponseEntity.ok(
                userService.addFavoriteLocation(
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        locationToDelete
                )
        );
    }
}
