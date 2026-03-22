package com.hrms.controller;

import com.hrms.service.KeycloakAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class KeycloakController {

    @Value("${keycloak.logout-url}")
    private String keycloakLogoutUrl;

    @Value("${keycloak.login-url}")
    private String keycloakLoginUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Autowired
    private KeycloakAdminService keycloakAdminService;

    // ----------------------------------------------------------------
    // 1. LOGIN — Redirect sang Keycloak login page
    // ----------------------------------------------------------------
    /**
     * GET /perform-login
     * Redirect người dùng sang trang đăng nhập Keycloak.
     */
    @GetMapping("/perform-login")
    public void login(HttpServletResponse response) throws IOException {
        String loginRedirect = keycloakLoginUrl
                + "?client_id="     + clientId
                + "&redirect_uri="  + appBaseUrl + "/login/oauth2/code/keycloak"
                + "&response_type=code"
                + "&scope=openid profile email";

        response.sendRedirect(loginRedirect);
    }

    // ----------------------------------------------------------------
    // 2. CHECK USER — Kiểm tra user có tồn tại trong Keycloak không
    // ----------------------------------------------------------------
    /**
     * GET /check-user?usernameOrEmail=...
     * Trả về thông tin cơ bản nếu user tồn tại trong Keycloak (sử dụng Keycloak Admin API).
     */
    @GetMapping("/check-user")
    public ResponseEntity<Map<String, Object>> checkUser(
            @RequestParam String usernameOrEmail) {

        Map<String, Object> result = new HashMap<>();

        boolean exists = keycloakAdminService.userExists(usernameOrEmail);

        if (!exists) {
            result.put("exists",  false);
            result.put("message", "User không tồn tại trong Keycloak");
            return ResponseEntity.status(404).body(result);
        }

        // Nếu tồn tại, trả thêm thông tin cơ bản
        Map<String, Object> detail = keycloakAdminService.getUserDetail(usernameOrEmail);

        result.put("exists",   true);
        result.put("id",       detail.get("id"));
        result.put("username", detail.get("username"));
        result.put("email",    detail.get("email"));
        result.put("enabled",  detail.get("enabled"));    // account có bị disable không
        result.put("message",  "User tồn tại trong Keycloak");

        return ResponseEntity.ok(result);
    }

    // ----------------------------------------------------------------
    // 3. ME — Lấy thông tin user hiện tại sau khi login
    // ----------------------------------------------------------------
    /**
     * GET /me
     * Trả thông tin user đã xác thực (lấy từ OidcUser). Phục vụ UI hiển thị profile.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @AuthenticationPrincipal OidcUser oidcUser) {

        if (oidcUser == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Chưa đăng nhập"));
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("subject",   oidcUser.getSubject());               // Keycloak user ID
        userInfo.put("username",  oidcUser.getPreferredUsername());
        userInfo.put("email",     oidcUser.getEmail());
        userInfo.put("fullName",  oidcUser.getFullName());
        userInfo.put("verified",  oidcUser.getEmailVerified());
        userInfo.put("roles",     oidcUser.getClaimAsStringList("roles"));
        userInfo.put("idToken",   oidcUser.getIdToken().getTokenValue());

        return ResponseEntity.ok(userInfo);
    }

    // ----------------------------------------------------------------
    // 4. LOGOUT — Xóa session + redirect Keycloak logout
    // ----------------------------------------------------------------
    /**
     * GET /perform-logout
     * Xóa local session và redirect sang Keycloak logout (kèm id_token_hint, post_logout_redirect_uri).
     */
    @GetMapping("/perform-logout")
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       @AuthenticationPrincipal OidcUser oidcUser) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);

        String idToken = "";
        if (oidcUser != null && oidcUser.getIdToken() != null) {
            idToken = oidcUser.getIdToken().getTokenValue();
        }

        response.sendRedirect(keycloakLogoutUrl
                + "?id_token_hint=" + idToken
                + "&post_logout_redirect_uri=" + appBaseUrl);
    }
}