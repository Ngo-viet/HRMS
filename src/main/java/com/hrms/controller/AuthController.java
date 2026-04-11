package com.hrms.controller;

import com.hrms.model.User;
import com.hrms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    /**
     * POST /api/auth/login
     * Body: { "username": "...", "password": "..." }
     * Mô tả: Đăng nhập bằng username/email và password, tạo session trên server.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User u = userService.fetchUserByEmail(username);
        if (u == null) {
            u = userService.findByUsername(username);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Đăng nhập thành công");
        if (u != null) {
            resp.put("id", u.getUserId());
            resp.put("username", u.getUsername());
            resp.put("email", u.getEmail());
            resp.put("roles", u.getRoles());
        }

        return ResponseEntity.ok(resp);
    }

    /**
     * POST /api/auth/logout
     * Mô tả: Đăng xuất cục bộ (hủy session server-side)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().body(java.util.Collections.singletonMap("message", "Logged out"));
    }
}
