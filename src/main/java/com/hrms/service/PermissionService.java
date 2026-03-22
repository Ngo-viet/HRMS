package com.hrms.service;

import com.hrms.model.Feature;
import com.hrms.model.Permission;
import com.hrms.model.Role;
import com.hrms.repository.FeatureRepository;
import com.hrms.repository.PermissionRepository;
import com.hrms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FeatureRepository featureRepository;

    public List<Permission> fetchPermissions() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> getById(int id) {
        return permissionRepository.findById(id);
    }

    public Permission register(Permission p) {
        return permissionRepository.save(p);
    }

    public Permission editPermission(Permission p) {
        return permissionRepository.save(p);
    }

    public void deletePermission(int id) {
        permissionRepository.deleteById(id);
    }

    public Optional<Permission> getByRoleAndFeature(int roleId, int featureId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        Feature feature = featureRepository.findById(featureId).orElse(null);
        if (role == null || feature == null) return Optional.empty();
        return permissionRepository.findByRoleAndFeature(role, feature);
    }

    public List<Permission> getByRole(int roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) return List.of();
        return permissionRepository.findByRole(role);
    }

    public List<Permission> getByFeature(int featureId) {
        Feature feature = featureRepository.findById(featureId).orElse(null);
        if (feature == null) return List.of();
        return permissionRepository.findByFeature(feature);
    }
}
