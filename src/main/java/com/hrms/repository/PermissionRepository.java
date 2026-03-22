package com.hrms.repository;

import com.hrms.model.Permission;
import com.hrms.model.Role;
import com.hrms.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findByRole(Role role);
    List<Permission> findByFeature(Feature feature);
    Optional<Permission> findByRoleAndFeature(Role role, Feature feature);
}
