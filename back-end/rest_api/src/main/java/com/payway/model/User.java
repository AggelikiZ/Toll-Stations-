package com.payway.model;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Matches your `userID` column
    @Column(name = "userID")
    private Integer id;

    @Column(name = "username", nullable = false, length = 50) // Matches the `username` column
    private String username;

    @Column(name = "passwordHash", nullable = false, length = 255) // Matches the `passwordHash` column
    private String password;

    @Enumerated(EnumType.STRING) // Maps the ENUM `user_role` column
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public enum UserRole {
        operator, ministry;
    }

}
