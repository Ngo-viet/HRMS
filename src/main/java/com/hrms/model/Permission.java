package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "HRM_PERMISSION")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int permissionId;

    // The role this permission applies to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // The feature (API) being protected
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    // simple flags for CRUD allowed
    @Column(name = "can_create")
    private Boolean canCreate = false;

    @Column(name = "can_read")
    private Boolean canRead = false;

    @Column(name = "can_update")
    private Boolean canUpdate = false;

    @Column(name = "can_delete")
    private Boolean canDelete = false;
}
