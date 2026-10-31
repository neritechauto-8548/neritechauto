package com.neritech.saas.common.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Campos exception/cause são opcionais e só devem ser populados no
 * diagnóstico local explicitamente habilitado.
 */
public record ApiErrorResponse(
    String type,
    String message,
    Map<String, String> errors,
    LocalDateTime timestamp,
    String exception,
    String cause
) {
    public ApiErrorResponse(String type, String message, Map<String, String> errors, LocalDateTime timestamp) {
        this(type, message, errors, timestamp, null, null);
    }
}
