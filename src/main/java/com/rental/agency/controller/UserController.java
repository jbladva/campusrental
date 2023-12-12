package com.rental.agency.controller;

import com.rental.agency.dto.JwtTokenDto;
import com.rental.agency.dto.LoginRequestDto;
import com.rental.agency.dto.UserDTO;
import com.rental.agency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * Add user response entity.
     *
     * @param userDTO the user dto
     * @return the response entity
     */
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTO));
    }

    /**
     * Login response entity.
     *
     * @param loginRequestDto the login request dto
     * @return the response entity
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtTokenDto> login(@RequestBody LoginRequestDto loginRequestDto)
    {
       return ResponseEntity.ok( new JwtTokenDto(userService.authenticateUser(loginRequestDto)));
    }
}
