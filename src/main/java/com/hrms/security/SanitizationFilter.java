package com.hrms.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SanitizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest sanitized = new HttpServletRequestWrapper(request) {

            private String sanitize(String input) {
                if (input == null) return null;
                // remove simple HTML tags
                return input.replaceAll("<[^>]*>", "").trim();
            }

            @Override
            public String getParameter(String name) {
                return sanitize(super.getParameter(name));
            }

            @Override
            public String[] getParameterValues(String name) {
                String[] vals = super.getParameterValues(name);
                if (vals == null) return null;
                String[] out = new String[vals.length];
                for (int i = 0; i < vals.length; i++) out[i] = sanitize(vals[i]);
                return out;
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                Map<String, String[]> original = super.getParameterMap();
                Map<String, String[]> copy = new HashMap<>();
                for (Map.Entry<String, String[]> e : original.entrySet()) {
                    String[] vals = e.getValue();
                    if (vals == null) { copy.put(e.getKey(), null); continue; }
                    String[] out = new String[vals.length];
                    for (int i = 0; i < vals.length; i++) out[i] = sanitize(vals[i]);
                    copy.put(e.getKey(), out);
                }
                return Collections.unmodifiableMap(copy);
            }

            @Override
            public String getHeader(String name) {
                return sanitize(super.getHeader(name));
            }
        };

        filterChain.doFilter(sanitized, response);
    }
}
