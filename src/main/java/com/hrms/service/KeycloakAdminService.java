package com.hrms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * KeycloakAdminService
 * ---------------------
 * This service contains helper methods that call the Keycloak Admin REST API
 * to (optionally) check if a user exists in the Keycloak realm and to
 * retrieve basic user details.
 *
 * Notes for maintainers:
 * - This project has been migrated to use local DB authentication by default.
 *   The Keycloak integration is kept here as an optional/legacy helper.
 * - To fully disable Keycloak integration:
 *     1. Remove or clear Keycloak properties from application.properties (e.g., keycloak.admin.server-url).
 *     2. Remove any @Autowired references to this service (e.g. in controllers), or set them as required=false.
 *     3. Alternatively, you can delete this class if Keycloak will never be used again.
 * - To re-enable Keycloak Admin calls, restore the Keycloak server URL, admin credentials
 *   in the configuration and ensure network access to the Keycloak server.
 *
 * Security note:
 * - The admin credentials are read from application properties. Keep those secrets safe
 *   and avoid committing real credentials into source control.
 */
@Deprecated
@Service
// Only create this bean when the keycloak.admin.server-url property is set.
@ConditionalOnProperty(prefix = "keycloak.admin", name = "server-url")
public class KeycloakAdminService {

    @Value("${keycloak.admin.server-url:}")   // default empty to avoid missing placeholder
    private String serverUrl;

    @Value("${keycloak.admin.realm:}")        // default empty
    private String realm;

    @Value("${keycloak.admin.client-id:}")    // default empty
    private String adminClientId;

    @Value("${keycloak.admin.username:}")     // default empty
    private String adminUsername;

    @Value("${keycloak.admin.password:}")     // default empty
    private String adminPassword;

    private final RestTemplate restTemplate = new RestTemplate();

    // ----------------------------------------------------------------
    // Lấy admin access token từ Keycloak
    // ----------------------------------------------------------------
    /**
     * If Keycloak is configured (serverUrl not empty), this method requests
     * an admin access token from the Keycloak master realm using password grant.
     *
     * If Keycloak is not configured (serverUrl is blank) this helper will
     * throw an IllegalStateException to indicate Keycloak is not enabled.
     */
    private String getAdminAccessToken() {
        if (serverUrl == null || serverUrl.trim().isEmpty()) {
            throw new IllegalStateException("Keycloak admin server URL is not configured.");
        }

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
    /**
     * Search Keycloak for a user by username or email. Returns true if found.
     *
     * WARNING: This calls the remote Keycloak server and will throw when
     * Keycloak configuration is missing (see getAdminAccessToken()).
     */
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
    /**
     * Get raw Keycloak user representation (first result) for the given username/email.
     * Returns null if not found. The returned Map is the JSON payload from Keycloak.
     */
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