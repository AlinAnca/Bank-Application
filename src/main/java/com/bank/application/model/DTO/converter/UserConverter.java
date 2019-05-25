package com.bank.application.model.DTO.converter;

import com.bank.application.model.DTO.UserDTO;
import com.bank.application.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .withUsername(user.getUsername())
                .withCreatedTime(user.getCreatedTime())
                .build();
    }
}
