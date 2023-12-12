package com.campusrental.service.impl;

import com.campusrental.entity.User;
import com.campusrental.exception.UserNotFoundException;
import com.campusrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found with email: " + email));
        return UserDetailsServiceImpl.build(user);
    }

    public User loadUser(String email)
    {
       return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found with email: " + email));
    }
}
