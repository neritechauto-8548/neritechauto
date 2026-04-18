package com.neritech.saas.common.config;

import com.neritech.saas.common.dto.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
public class StandardResponseAdvice implements ResponseBodyAdvice<Object> {

    // Paths to exclude from wrapping
    private final List<String> exclusions = List.of(
            "/v3/api-docs",
            "/swagger-ui",
            "/auth",
            "/v1/ordens-servico/nfe",
            "/api/v1/ordens-servico/nfe",
            "/v1/ordens-servico/fotos",
            "/api/v1/ordens-servico/fotos"
    );

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Don't wrap if it's already ApiResponse or void
        String typeName = returnType.getParameterType().getName();
        return !typeName.equals("com.neritech.saas.common.dto.ApiResponse") 
               && !typeName.equals("void")
               && !returnType.getParameterType().getName().contains("springdoc")
               && !returnType.getDeclaringClass().getName().contains("springdoc");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        
        String path = request.getURI().getPath();
        for (String exclusion : exclusions) {
            if (path.contains(exclusion)) {
                return body;
            }
        }

        if (body instanceof ApiResponse) {
             return body;
        }

        // Do not wrap binary responses
        if (selectedContentType != null) {
            if (MediaType.APPLICATION_PDF.equals(selectedContentType)
                    || MediaType.IMAGE_JPEG.equals(selectedContentType)
                    || MediaType.IMAGE_PNG.equals(selectedContentType)
                    || MediaType.APPLICATION_OCTET_STREAM.equals(selectedContentType)) {
                return body;
            }
        }
        if (body instanceof org.springframework.core.io.Resource) {
            return body;
        }

        return ApiResponse.success(body);
    }
}
