package com.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> 
{
	    //some specific methods
		public User findByEmail(String tempEmail);
		public User findUserByEmailAndPassword(String tempEmail,String password);

}
