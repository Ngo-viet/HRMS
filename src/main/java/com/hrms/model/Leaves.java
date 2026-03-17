package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

}
