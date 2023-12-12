package com.campusrental.service;

import com.campusrental.dto.LoginRequestDto;
import com.campusrental.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    String addUser(UserDTO userDTO);

    String authenticateUser(LoginRequestDto loginRequestDto);

}
