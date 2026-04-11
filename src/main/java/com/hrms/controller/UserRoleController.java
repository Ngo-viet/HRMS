package com.hrms.controller;

import com.hrms.model.Role;
import com.hrms.model.User;
import com.hrms.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * GET /users/{userId}/roles
     * Lấy danh sách role mà user đang có.
     */
    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<Set<Role>> getRolesForUser(@PathVariable int userId) throws Exception {
        return ResponseEntity.ok(userRoleService.getRolesForUser(userId));
    }

    /**
     * GET /roles/{roleId}/users
     * Lấy danh sách user thuộc role này.
     */
    @GetMapping("/roles/{roleId}/users")
    public ResponseEntity<List<User>> getUsersForRole(@PathVariable int roleId) throws Exception {
        return ResponseEntity.ok(userRoleService.getUsersForRole(roleId));
    }

    /**
     * POST /users/{userId}/roles/{roleId}
     * Gán role cho user.
     */
    @PostMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable int userId, @PathVariable int roleId) throws Exception {
        userRoleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /users/{userId}/roles
     * (Optional) API để gán nhiều role cho user bằng danh sách roleId trong body.
     * Body: [1,2,3]
     */
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<Void> assignMultipleRoles(@PathVariable int userId, @RequestBody java.util.List<Integer> roleIds) throws Exception {
        for (Integer r : roleIds) userRoleService.assignRoleToUser(userId, r);
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /users/{userId}/roles/{roleId}
     * Bỏ role khỏi user.
     */
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable int userId, @PathVariable int roleId) throws Exception {
        userRoleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }
}
