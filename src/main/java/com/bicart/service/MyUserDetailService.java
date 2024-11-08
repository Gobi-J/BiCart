package com.bicart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bicart.model.User;
import com.bicart.repository.UserRepository;

/**
 * <p>
 * Service class that handles business logic related to user details.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmailAndIsDeletedFalse(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }
}

