package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "HRM_ROLE_FP")
public class RoleFeaturePermission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    @Column(name = "can_create")
    private Boolean canCreate = false;

    @Column(name = "can_read")
    private Boolean canRead = false;

    @Column(name = "can_update")
    private Boolean canUpdate = false;

    @Column(name = "can_delete")
    private Boolean canDelete = false;
}
