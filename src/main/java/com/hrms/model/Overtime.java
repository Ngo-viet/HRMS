package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "HRM_OT")
public class Overtime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ot_id")
    private int otId;

    @Column(name = "emp_id")
    private int employeeId;

    @Column(name = "ot_date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "hours")
    private Double hours;

    @Column(name = "reason")
    private String reason;

    // Approval workflow
    @Column(name = "status")
    private String status; // PENDING / APPROVED / REJECTED

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    private LocalDate approvedDate;
}
