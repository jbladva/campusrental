package com.campusrental.controller;

import com.campusrental.dto.JwtTokenDto;
import com.campusrental.dto.LoginRequestDto;
import com.campusrental.dto.UserDTO;
import com.campusrental.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;



    /*@GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }*/

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTO));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtTokenDto> login(@RequestBody LoginRequestDto loginRequestDto)
    {
       return ResponseEntity.ok( new JwtTokenDto(userService.authenticateUser(loginRequestDto)));
    }
}
