package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "HRM_PERFORMANCE")
public class Performance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private int performanceId;

    @Column(name = "emp_id", nullable = false)
    private int employeeId;

    @Column(name = "period")
    private String period; // e.g., 2026-03

    @Column(name = "kpi_score")
    private Double kpiScore;

    @Column(name = "rating")
    private String rating; // e.g., EXCELLENT / GOOD / AVERAGE

    @Column(name = "comments", length = 2000)
    private String comments;

    @Column(name = "review_date")
    private LocalDate reviewDate;
}
