package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "HRM_CONTRACT")
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private int contractId;

    @Column(name = "emp_id", nullable = false)
    private int employeeId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "contract_type")
    private String contractType; // e.g., FIXED, TEMP, FREELANCE

    @Column(name = "contract_salary")
    private String contractSalary;

    @Column(name = "status")
    private String status; // ACTIVE / EXPIRED / TERMINATED

    @Column(name = "notes", length = 2000)
    private String notes;
}
