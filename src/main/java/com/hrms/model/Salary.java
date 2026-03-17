package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="HRM_SALARY")
public class Salary extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int salaryId;

	@Column(name="emp_id")
	private int eid;

	@Column(name="month")
	private String month;

	@Column(name="total_working_day")
	private int totalWorkingDay;

	@Column(name="basic_salary")
	private String basic;

	@Column(name="total_hra")
	private String hra;

	@Column(name="conveyance_allowance")
	private String ca;

	@Column(name="total_net_pay")
	private String pay;

	@Column(name="total_deductions")
	private String deduction;

}
