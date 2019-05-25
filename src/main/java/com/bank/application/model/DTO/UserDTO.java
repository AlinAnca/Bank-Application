package com.bank.application.model.DTO;

import java.time.LocalDateTime;

public class UserDTO {
    private String username;
    private LocalDateTime createdTime;

    public UserDTO(UserDTOBuilder builder) {
        this.username = builder.username;
        this.createdTime = builder.createdTime;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public static class UserDTOBuilder {

        private String username;
        private LocalDateTime createdTime;

        public UserDTOBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserDTOBuilder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
