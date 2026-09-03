package com.neritech.saas.orcamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrcamentoIdempotencyConflictException extends RuntimeException {

    public OrcamentoIdempotencyConflictException(String message) {
        super(message);
    }
}
