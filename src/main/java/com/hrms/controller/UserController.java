package com.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.model.User;
import com.hrms.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService uservice;

    /**
     * POST /loginuser
     * Đăng nhập cho HR user (local auth - DB). Body: User JSON (email,password).
     * Trả về User khi credentials đúng, hoặc ném Exception nếu sai.
     */
    @PostMapping("/loginuser")
    public User loginUser(@RequestBody User user) throws Exception {
        String tempEmail = user.getEmail();
        String tempPassword = user.getPassword();
        User userObj = null;
        if (tempEmail != null && tempPassword != null) {
            userObj = uservice.fetchUserByEmailAndPassword(tempEmail, tempPassword);
        }
        if (userObj == null) {
            throw new Exception("Bad Crediential");
        }
        return userObj;
    }

    /**
     * POST /addhr
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
            user1 = uservice.register(user);
            return user1;
        }
    }
}