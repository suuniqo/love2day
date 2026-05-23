package es.upm.fi.love2day.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

import jakarta.persistence.Column;

@Entity
@Table(name = "Accounts")  
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant lastLogin;
    
    // necesario para JPA
    public Account() {}
    
    private Account(
        String username,
        String email,
        String passwordHash,
        Boolean verified,
        Instant createdAt,
        Instant lastLogin
    ) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.verified = verified;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    public static Account create(
        String username,
        String email,
        String passwordHash
    ) {
        return new Account(
            username,
            email,
            passwordHash,
            false,
            Instant.now(),
            Instant.now()
        );
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }
}
