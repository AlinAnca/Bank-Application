package com.bank.application.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "authentication")
public class Authentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "token")
    private String token;

    @Column(name = "reference")
    private Long reference;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    public Authentication() {
    }

    public Authentication(AuthenticationBuilder builder) {
        this.token = builder.token;
        this.reference = builder.reference;
        this.createdTime = builder.createdTime;
    }

    public static AuthenticationBuilder builder() {
        return new AuthenticationBuilder();
    }

    @PrePersist
    void preInsert() {
        if (this.createdTime == null)
            this.createdTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getReference() {
        return reference;
    }

    public void setReference(Long reference) {
        this.reference = reference;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public static class AuthenticationBuilder {

        private String token;
        private Long reference;
        private LocalDateTime createdTime;

        public AuthenticationBuilder withToken(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationBuilder withReference(Long reference) {
            this.reference = reference;
            return this;
        }

        public AuthenticationBuilder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public Authentication build() {
            return new Authentication(this);
        }
    }
}
