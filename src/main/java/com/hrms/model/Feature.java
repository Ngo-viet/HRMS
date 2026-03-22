package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "HRM_FEATURE")
public class Feature extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private int featureId;

    @Column(name = "feature_name", nullable = false, unique = true)
    private String featureName;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "description")
    private String description;
}
