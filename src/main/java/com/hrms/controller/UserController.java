package com.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.model.User;
import com.hrms.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService uservice;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * POST /api/users/addhr
     * Tạo tài khoản HR mới. Body: User JSON (username,email,password,...)
     */
    @PostMapping("/addhr")
    public User addHr(@RequestBody User user) throws Exception {
        String tempEmail = user.getEmail();
        System.out.println("Temp email is :" + tempEmail);
        User user1 = uservice.fetchUserByEmail(tempEmail);
        if (user1 != null) {
            throw new Exception("User with " + tempEmail + " allready exist ");
        } else {
            // encode password before saving
            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            user1 = uservice.register(user);
            return user1;
        }
    }
}