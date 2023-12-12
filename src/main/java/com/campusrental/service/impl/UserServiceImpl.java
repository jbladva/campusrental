package com.campusrental.service.impl;

import com.campusrental.dto.LoginRequestDto;
import com.campusrental.dto.UserDTO;
import com.campusrental.entity.User;
import com.campusrental.entity.UserRoles;
import com.campusrental.exception.UserValidationException;
import com.campusrental.repository.UserRepository;
import com.campusrental.service.UserService;
import com.campusrental.util.Common;
import com.campusrental.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String addUser(UserDTO userDTO) {
        if (Boolean.FALSE.equals(Common.isValidEmail(userDTO.getEmail())))
            throw new UserValidationException("Please enter a valid email address");
        if (Boolean.FALSE.equals(Common.isValidPassword(userDTO.getPassword())))
            throw new UserValidationException("Please enter a valid password");
        userRepository.findByEmail(userDTO.getEmail()).ifPresent(user->{ throw new UserValidationException("User is already exist");}) ;

        User userEntity = modelMapper.map(userDTO, User.class);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserRoles userRoles = new UserRoles();
        userRoles.setRoleName(userDTO.getRole().getRoleName().toUpperCase());
        userEntity.setRole(userRoles);
        userRepository.save(userEntity);
        return "User Successfully added";
    }

    @Override
    public String authenticateUser(LoginRequestDto loginRequestDto) {
        Authentication authentication
                =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtil.generateJwtToken(authentication);
    }

}
