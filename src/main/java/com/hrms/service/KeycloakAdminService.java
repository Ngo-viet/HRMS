package com.hrms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.admin.server-url}")   // http://localhost:8080
    private String serverUrl;

    @Value("${keycloak.admin.realm}")        // hrms
    private String realm;

    @Value("${keycloak.admin.client-id}")    // admin-cli
    private String adminClientId;

    @Value("${keycloak.admin.username}")     // admin
    private String adminUsername;

    @Value("${keycloak.admin.password}")     // admin password
    private String adminPassword;

    private final RestTemplate restTemplate = new RestTemplate();

    // ----------------------------------------------------------------
    // Lấy admin access token từ Keycloak
    // ----------------------------------------------------------------
    private String getAdminAccessToken() {
        String tokenUrl = serverUrl + "/realms/master/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id",  adminClientId);
        body.add("username",   adminUsername);
        body.add("password",   adminPassword);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, entity, Map.class);
        return (String) response.getBody().get("access_token");
    }

    // ----------------------------------------------------------------
    // Check user có tồn tại trong Keycloak theo username/email không
    // ----------------------------------------------------------------
    public boolean userExists(String usernameOrEmail) {
        String adminToken = getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Tìm theo username
        String url = serverUrl + "/admin/realms/" + realm
                + "/users?search=" + usernameOrEmail + "&max=1";

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        return response.getBody() != null && !response.getBody().isEmpty();
    }

    // ----------------------------------------------------------------
    // Lấy thông tin chi tiết user từ Keycloak theo username/email
    // ----------------------------------------------------------------
    public Map<String, Object> getUserDetail(String usernameOrEmail) {
        String adminToken = getAdminAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = serverUrl + "/admin/realms/" + realm
                + "/users?search=" + usernameOrEmail + "&max=1";

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        if (response.getBody() == null || response.getBody().isEmpty()) {
            return null;
        }

        return (Map<String, Object>) response.getBody().get(0);
    }
}