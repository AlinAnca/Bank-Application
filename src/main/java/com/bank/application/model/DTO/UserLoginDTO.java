package com.bank.application.model.DTO;


public class UserLoginDTO {
    private String username;
    private String password;

    public UserLoginDTO(UserLoginDTOBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
    }

    public UserLoginDTO() {
    }

    public static UserLoginDTOBuilder builder() {
        return new UserLoginDTOBuilder();
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

    public static class UserLoginDTOBuilder {

        private String username;
        private String password;

        public UserLoginDTOBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserLoginDTOBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserLoginDTO build() {
            return new UserLoginDTO(this);
        }
    }
}
