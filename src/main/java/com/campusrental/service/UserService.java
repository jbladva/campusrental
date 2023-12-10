package com.campusrental.service;

import com.campusrental.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO addUser(UserDTO userDTO);
    // Add other user-related methods
}
