package com.rental.agency.service;

import com.rental.agency.dto.LoginRequestDto;
import com.rental.agency.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    String addUser(UserDTO userDTO);

    String authenticateUser(LoginRequestDto loginRequestDto);

}
