package com.hrms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrms.model.User;
import com.hrms.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repo;

	// fetch Email id
	public User fetchUserByEmail(String tempEmail) {
		return repo.findByEmail(tempEmail);
	}

	// Fetch EmailId and password
	public User fetchUserByEmailAndPassword(String tempEmail, String password) {
		return repo.findUserByEmailAndPassword(tempEmail, password);
	}

	// register hr user
	public User register(User user) {
		return repo.save(user);
	}

}
