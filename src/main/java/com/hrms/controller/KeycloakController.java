package com.hrms.controller;

import com.hrms.model.User;
import com.hrms.service.KeycloakAdminService;
import com.hrms.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/keycloak")
public class KeycloakController {

    @Autowired
    private UserService userService;

    // Optional Keycloak admin helper. This is injected only when Keycloak
    // admin configuration exists in application.properties. The application
    // uses local DB authentication by default; KeycloakAdminService is kept
    // as a legacy convenience to query Keycloak if administrators want to
    // check remote Keycloak users.
    @Autowired(required = false)
    private KeycloakAdminService keycloakAdminService; // retained but optional

    /**
     * GET /api/keycloak/check-user?usernameOrEmail=...
     * Mô tả: Kiểm tra user có tồn tại trong local DB (trước đây dùng Keycloak).
     * - Trả về: { exists: boolean, message, id?, username?, email? }
     *
     * Behavior:
     * - First check local DB using `UserService.fetchUserByEmail`.
     * - If not found and `KeycloakAdminService` is present, fallback to Keycloak
     *   to check if the user exists remotely.
     *
     * To fully disable Keycloak checks, remove Keycloak properties from
     * application.properties and ensure this autowired field is null.
     */
    @GetMapping("/check-user")
    public ResponseEntity<Map<String, Object>> checkUser(@RequestParam String usernameOrEmail) {
        Map<String, Object> result = new HashMap<>();

        User u = userService.fetchUserByEmail(usernameOrEmail);
        if (u == null && keycloakAdminService != null) {
            // fallback to Keycloak admin if configured
            boolean exists = keycloakAdminService.userExists(usernameOrEmail);
            if (!exists) {
                result.put("exists", false);
                result.put("message", "User không tồn tại");
                return ResponseEntity.status(404).body(result);
            }
            result.put("exists", true);
            result.put("message", "User tồn tại trong Keycloak");
            return ResponseEntity.ok(result);
        }

        if (u == null) {
            result.put("exists", false);
            result.put("message", "User không tồn tại");
            return ResponseEntity.status(404).body(result);
        }

        result.put("exists", true);
        result.put("id", u.getUserId());
        result.put("username", u.getUsername());
        result.put("email", u.getEmail());
        result.put("message", "User tồn tại trong DB");

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/keycloak/me
     * Mô tả: Trả thông tin user đã xác thực (dựa trên Authentication principal). Nếu chưa đăng nhập trả 401.
     *
     * Note: When using local DB auth, the principal is a `UserDetails` built from
     * our `UserService`. If you later restore Keycloak OIDC login, the principal
     * might be an OIDC user object (e.g., OidcUser) — update this method accordingly.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Chưa đăng nhập"));
        }

        Object principal = authentication.getPrincipal();
        Map<String, Object> userInfo = new HashMap<>();

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            User u = userService.fetchUserByEmail(username);
            if (u == null) {
                // maybe username is not email, try by username field
                u = userService.findByUsername(username);
            }
            if (u != null) {
                userInfo.put("id", u.getUserId());
                userInfo.put("username", u.getUsername());
                userInfo.put("email", u.getEmail());
                userInfo.put("roles", u.getRoles());
                return ResponseEntity.ok(userInfo);
            }
        }

        // fallback: return auth name
        userInfo.put("name", authentication.getName());
        return ResponseEntity.ok(userInfo);
    }

    /**
     * GET /api/keycloak/perform-logout
     * Mô tả: Thực hiện logout cục bộ (xóa session). Trước đây redirect sang Keycloak logout; giờ chỉ local logout.
     *
     * If you re-enable Keycloak OIDC login in the future, you may want to
     * redirect to Keycloak's end session endpoint with id_token_hint/post_logout_redirect_uri.
     */
    @GetMapping("/perform-logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler().logout(request, response, auth);
        }
        response.sendRedirect("/");
    }
}