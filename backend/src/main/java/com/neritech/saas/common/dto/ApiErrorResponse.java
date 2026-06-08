package com.neritech.saas.common.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
    String type,
    String message,
    Map<String, String> errors,
    LocalDateTime timestamp
) {}
