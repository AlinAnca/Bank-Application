package com.bank.application.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 16)
    private String username;

    @Column(nullable = false, length = 32)
    private String password;

    @Column(name = "created_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdTime;

    @Column(name = "updated_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updatedTime;

    private User() {
    }

    public User(UserBuilder userBuilder) {
        this.id = userBuilder.id;
        this.username = userBuilder.username;
        this.password = userBuilder.password;
        this.createdTime = userBuilder.createdTime;
        this.updatedTime = userBuilder.updatedTime;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @PrePersist
    void preInsert() {
        if (this.createdTime == null)
            this.createdTime = LocalDateTime.now();
        if (this.updatedTime == null)
            this.updatedTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(createdTime, user.createdTime) &&
                Objects.equals(updatedTime, user.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, createdTime, updatedTime);
    }

    public static class UserBuilder {

        private Long id;
        private String username;
        private String password;
        private LocalDateTime createdTime;
        private LocalDateTime updatedTime;
        private List<Notification> notifications;
        private List<Account> accounts;

        public UserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public UserBuilder withUpdatedTime(LocalDateTime updatedTime) {
            this.updatedTime = updatedTime;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
