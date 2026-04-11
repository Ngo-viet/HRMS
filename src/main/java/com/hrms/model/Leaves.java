package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

// Quản lý đơn nghỉ phép

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "HRM_LEAVES")
public class Leaves extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "leave_id")
	private int leaveId;

	@Column(name = "emp_id")
	private int employeeId;

	@Column(name = "leave_Reason")
	private String leaveReason;

	@Column(name = "from_Date")
	private String fromDate;

	@Column(name = "to_Date")
	private String toDate;

	@Column(name = "description")
	private String description;

	@Column(name = "email")
	private String email;

	// Approval workflow fields
	@Column(name = "status")
	private String status; // PENDING / APPROVED / REJECTED

	@Column(name = "approved_by")
	private String approvedBy;

	@Column(name = "approved_date")
	private LocalDate approvedDate;

}
