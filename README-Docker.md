# HRMS - Docker & Keycloak Setup

This repository includes Docker assets to run the full stack locally: MySQL, Keycloak, and the HRMS Spring Boot app.

Files added:
- `docker-compose.yml` - runs MySQL, Keycloak (start-dev), and the `hrms-app` (built from the included `Dockerfile`).
- `Dockerfile` - multi-stage build (Maven build + OpenJDK runtime).
- `keycloak-realm.json` - a sample realm `hrms` with a `hrms-client` (client-secret `hrms-secret`) and a `testuser/password` user.

Quick start
1. Ensure Docker is installed and running.
2. From the project root, run:

```powershell
docker-compose up --build -d
```

3. Wait for services to become healthy. Keycloak will be available on http://localhost:8180 (admin/admin).
4. The HRMS app will be available on http://localhost:8081.

Notes
- `docker-compose.yml` maps Keycloak container port 8080 to host 8180 to avoid conflict with the app.
- If you prefer Keycloak on 8080, change the mapping in `docker-compose.yml` and adjust `application.properties` environment variables accordingly.
- The `keycloak-realm.json` is automatically mounted into Keycloak's import folder; depending on the Keycloak image version, automatic import may require additional flags. If import doesn't happen, log in to the admin console and import the realm manually.
- DB files and Keycloak data are persisted in `./mysql-data` and `./keycloak-data` respectively.

Environment variables
- The `hrms-app` service sets environment variables used by `application.properties`. Adjust secrets and ports before production use.

Troubleshooting
- If the app can't reach Keycloak, ensure service names are correct and ports match. Inside Docker network `keycloak` is reachable at `http://keycloak:8080`.

