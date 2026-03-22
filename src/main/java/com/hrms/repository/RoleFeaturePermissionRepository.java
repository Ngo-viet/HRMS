package com.hrms.repository;

import com.hrms.model.RoleFeaturePermission;
import com.hrms.model.Role;
import com.hrms.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleFeaturePermissionRepository extends JpaRepository<RoleFeaturePermission, Integer> {
    List<RoleFeaturePermission> findByRole(Role role);
    List<RoleFeaturePermission> findByFeature(Feature feature);
    Optional<RoleFeaturePermission> findByRoleAndFeature(Role role, Feature feature);
}
