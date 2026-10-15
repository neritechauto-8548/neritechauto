package com.neritech.saas.common.exception;

import com.neritech.saas.common.dto.ApiErrorResponse;
import jakarta.persistence.RollbackException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private static final String SENSITIVE = "secret@oficina.example";

    @Test
    void conflictKeepsUsefulMessageWithoutDatabaseValues() {
        String raw = "duplicate key value violates unique constraint \"cliente_email\" " +
            "Detail: Key (email)=(" + SENSITIVE + ") already exists";
        ResponseEntity<ApiErrorResponse> response =
            handler.handleIntegrity(new DataIntegrityViolationException(raw));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("BUSINESS_RULE", body.type());
        assertTrue(body.message().contains("email"));
        assertFalse(body.toString().contains(SENSITIVE));
        assertNull(body.errors());
    }

    @Test
    void invalidPayloadNeverLeaksParserDetails() {
        ResponseEntity<ApiErrorResponse> response =
            handler.handleBadPayload(new HttpMessageNotReadableException("invalid JSON: " + SENSITIVE));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VALIDATION", response.getBody().type());
        assertFalse(response.getBody().toString().contains(SENSITIVE));
    }

    @Test
    void internalServerErrorsNeverLeakExceptionDetails() {
        ResponseEntity<ApiErrorResponse> response =
            handler.handleGeneric(new Exception("JDBC password: " + SENSITIVE));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INTERNAL_ERROR", response.getBody().type());
        assertFalse(response.getBody().toString().contains(SENSITIVE));
    }

    @Test
    void transactionAndRollbackFailuresDoNotExposeInternalMessages() {
        ResponseEntity<ApiErrorResponse> transaction =
            handler.handleTransaction(new TransactionSystemException("transaction " + SENSITIVE));
        ResponseEntity<ApiErrorResponse> rollback =
            handler.handleRollback(new RollbackException("rollback " + SENSITIVE));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transaction.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rollback.getStatusCode());
        assertNotNull(transaction.getBody());
        assertFalse(transaction.getBody().toString().contains(SENSITIVE));
        assertNotNull(rollback.getBody());
        assertFalse(rollback.getBody().toString().contains(SENSITIVE));
    }
}
