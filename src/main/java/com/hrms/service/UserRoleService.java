package com.hrms.service;

import com.hrms.model.Role;
import com.hrms.model.User;
import com.hrms.repository.RoleRepository;
import com.hrms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Set<Role> getRolesForUser(int userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        return user.getRoles();
    }

    public List<User> getUsersForRole(int roleId) throws Exception {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new Exception("Role not found"));
        // find all users that contain this role (could be optimized with a query)
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    @Transactional
    public void assignRoleToUser(int userId, int roleId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new Exception("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void removeRoleFromUser(int userId, int roleId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new Exception("Role not found"));
        user.getRoles().remove(role);
        userRepository.save(user);
    }
}
