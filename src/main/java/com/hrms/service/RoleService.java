package com.hrms.service;

import com.hrms.model.Role;
import com.hrms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> fetchRoles() {
        return roleRepository.findAll();
    }

    public Role register(Role role) {
        return roleRepository.save(role);
    }

    public Optional<Role> getById(int id) {
        return roleRepository.findById(id);
    }

    public Role editRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(int id) {
        roleRepository.deleteById(id);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByRoleName(name);
    }
}
