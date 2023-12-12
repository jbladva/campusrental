package com.rental.agency.service.impl;

import com.rental.agency.dto.LoginRequestDto;
import com.rental.agency.dto.UserDTO;
import com.rental.agency.entity.User;
import com.rental.agency.entity.UserRoles;
import com.rental.agency.exception.UserValidationException;
import com.rental.agency.repository.UserRepository;
import com.rental.agency.service.UserService;
import com.rental.agency.util.Common;
import com.rental.agency.util.JwtUtil;
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
