package com.ahsen.contactmanagement.security;

import com.ahsen.contactmanagement.user.entity.User;
import com.ahsen.contactmanagement.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Long userId;
        try {
            userId = Long.valueOf(username);
        } catch (NumberFormatException ex) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user.getId(), user.getPasswordHash());
    }
}
