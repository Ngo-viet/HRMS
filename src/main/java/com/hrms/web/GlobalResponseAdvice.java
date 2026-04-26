package com.hrms.web;

import com.hrms.dto.ApiResponse;
import com.hrms.util.ApiResponseFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public GlobalResponseAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // apply to all controllers producing JSON (and others) - filter if needed
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body == null) {
            return ApiResponseFactory.success(null);
        }

        if (body instanceof ApiResponse) {
            return body;
        }

        ApiResponse<Object> wrapper = ApiResponseFactory.success(body);

        // If controller declared return type String, Spring expects a String; serialize wrapper to JSON string.
        if (returnType != null && returnType.getParameterType() == String.class) {
            try {
                return objectMapper.writeValueAsString(wrapper);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize ApiResponse for String return type", e);
            }
        }

        return wrapper;
    }
}
