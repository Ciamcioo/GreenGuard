package com.greenguard.green_guard_application.model.entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "gg_users")
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(name = "users_locations",
               joinColumns = @JoinColumn(name = "username"),
               inverseJoinColumns = @JoinColumn(name = "location_name")
    )
    private Set<Location> favoriteLocations;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Collection<Location> favoriteLocations) {
        this.username = username;
        this.password = password;
        this.favoriteLocations = Set.of(favoriteLocations.toArray(new Location[0]));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Location> getFavoriteLocations() {
        return new HashSet<>(favoriteLocations);
    }

    public void setFavoriteLocations(Set<Location> favoriteLocations) {
        this.favoriteLocations = Set.copyOf(favoriteLocations);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
