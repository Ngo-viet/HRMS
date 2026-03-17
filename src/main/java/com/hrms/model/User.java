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
@Table(name="HR_USER")
public class User extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column(name="username")
	private String username;

	@Column(name="email")
	private String email;

	@Column(name="mobile")
	private String mobile;

	@Column(name="orgGroup")
	private String orgGroup;

	@Column(name="address")
	private String address;

	@Column(name="password")
	private String password;

}
