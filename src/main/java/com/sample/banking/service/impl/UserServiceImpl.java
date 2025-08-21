package com.sample.banking.service.impl;

import com.sample.banking.controller.UserMapper;
import com.sample.banking.dto.UserDTO;
import com.sample.banking.entity.User;
import com.sample.banking.repository.UserRepository;
import com.sample.banking.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user= UserMapper.addToUser(userDTO);
        User saveduser=userRepository.save(user);
        return UserMapper.addToUserDTO(saveduser);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user Doesn't exists"));
        return UserMapper.addToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllusers() {
        List<User>users=userRepository.findAll();
        return users.stream().map(UserMapper::addToUserDTO).toList();
    }

    @Override
    public void deleteUser(Long userId) {

        User user=userRepository.findById(userId).orElseThrow(()-> new RuntimeException("Account Doesn't exists"));
        userRepository.deleteById(userId);
    }
}
