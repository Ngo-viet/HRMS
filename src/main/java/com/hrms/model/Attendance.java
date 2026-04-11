package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "HRM_ATTENDANCE")
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private int attendanceId;

    @Column(name = "emp_id", nullable = false)
    private int employeeId;

    @Column(name = "att_date", nullable = false)
    private LocalDate date;

    @Column(name = "hours_worked")
    private Double hoursWorked;

    @Column(name = "status")
    private String status; // PRESENT / ABSENT / LEAVE

    @Column(name = "leave_id")
    private Integer leaveId; // optional FK to HRM_LEAVES.leave_id
}
