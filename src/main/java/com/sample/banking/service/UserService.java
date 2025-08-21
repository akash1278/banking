package com.sample.banking.service;

import com.sample.banking.dto.UserDTO;
import com.sample.banking.entity.User;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long userId);

    List<UserDTO> getAllusers();

    void deleteUser(Long userId);
}
