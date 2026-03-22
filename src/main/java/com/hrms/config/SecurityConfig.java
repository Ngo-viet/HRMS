package com.hrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Use the current authorizeHttpRequests API and requestMatchers
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/public/**",
                                "/error",
                                "/login**",
                                "/perform-login",
                                "/perform-logout",
                                "/check-user"
                        ).permitAll()
                        .requestMatchers("/me").authenticated()
                        .anyRequest().authenticated()
                )

                // OIDC Login — Spring handles callback /login/oauth2/code/{registrationId}
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/me", false)
                        .failureUrl("/login?error=true")
                )

                // OAuth2 Client support
                .oauth2Client(Customizer.withDefaults())

                // JWT Resource Server for API validation
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))

                // Logout — clear local session (Keycloak logout handled by controller)
                .logout(logout -> logout
                        .logoutUrl("/local-logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}