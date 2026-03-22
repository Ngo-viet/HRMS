package com.hrms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="HRM_USER")
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

	// Many-to-Many user <-> role
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "HRM_USER_ROLE",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

    // Explicit getter/setter for tools that don't process Lombok
    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
