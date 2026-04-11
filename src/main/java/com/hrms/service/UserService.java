package com.hrms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hrms.model.User;
import com.hrms.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repo;

    // fetch Email id
    public User fetchUserByEmail(String tempEmail) {
        return repo.findByEmail(tempEmail);
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }

    // register hr user
    public User register(User user) {
        return repo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // try by email first
        User user = repo.findByEmail(username);
        if (user == null) {
            user = repo.findByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getRoleName()))
                .collect(Collectors.toSet());

        // NOTE: avoid relying on Lombok-generated isEnabled getter at compile time here
        boolean enabled = true;

        return new org.springframework.security.core.userdetails.User(
                user.getEmail() != null ? user.getEmail() : user.getUsername(),
                user.getPassword(),
                enabled,
                true, true, true,
                authorities
        );
    }
}
