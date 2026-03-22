package com.hrms.service;

import com.hrms.model.Feature;
import com.hrms.model.Role;
import com.hrms.model.RoleFeaturePermission;
import com.hrms.repository.FeatureRepository;
import com.hrms.repository.RoleFeaturePermissionRepository;
import com.hrms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleFeaturePermissionService {

    @Autowired
    private RoleFeaturePermissionRepository rfpRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FeatureRepository featureRepository;

    public List<RoleFeaturePermission> fetchAll() {
        return rfpRepository.findAll();
    }

    public Optional<RoleFeaturePermission> getById(int id) {
        return rfpRepository.findById(id);
    }

    public RoleFeaturePermission create(RoleFeaturePermission rfp) {
        return rfpRepository.save(rfp);
    }

    public RoleFeaturePermission update(RoleFeaturePermission rfp) {
        return rfpRepository.save(rfp);
    }

    public void delete(int id) {
        rfpRepository.deleteById(id);
    }

    public List<RoleFeaturePermission> findByRoleId(int roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) return List.of();
        return rfpRepository.findByRole(role);
    }

    public List<RoleFeaturePermission> findByFeatureId(int featureId) {
        Feature feature = featureRepository.findById(featureId).orElse(null);
        if (feature == null) return List.of();
        return rfpRepository.findByFeature(feature);
    }

    public Optional<RoleFeaturePermission> findByRoleAndFeature(int roleId, int featureId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        Feature feature = featureRepository.findById(featureId).orElse(null);
        if (role == null || feature == null) return Optional.empty();
        return rfpRepository.findByRoleAndFeature(role, feature);
    }
}
