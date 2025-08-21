package com.sample.banking.controller;

import com.sample.banking.dto.UserDTO;
import com.sample.banking.entity.User;

public class UserMapper {
    public static User addToUser(UserDTO userDTO){
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setAccounts(userDTO.getAccounts());
        return user;
    }

    public static UserDTO addToUserDTO(User User){
        return new UserDTO(User.getUserId(), User.getUsername(), User.getAccounts());
    }
}
