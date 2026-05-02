package com.neritech.saas.common.exception;

import com.neritech.saas.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadPayload(HttpMessageNotReadableException ex) {
        String detail = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Payload inválido ou malformado: " + detail));
    }

    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiResponse<Object>> handleIntegrity(Exception ex) {
        String raw = extractMessage(ex);
        String userMessage = explainIntegrityMessage(raw);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(List.of(userMessage, "Detalhes: " + raw)));
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponse<Object>> handleTransaction(TransactionSystemException ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();

        if (t instanceof ConstraintViolationException) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiResponse.error("Erro de validação: " + t.getMessage()));
        }
        if (isIntegrityViolation(t)) {
            String raw = extractMessage(ex);
            String userMessage = explainIntegrityMessage(raw);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(List.of(userMessage, raw)));
        }
        if (t instanceof OptimisticLockException || t instanceof OptimisticLockingFailureException) {
             return ResponseEntity.status(HttpStatus.CONFLICT)
                     .body(ApiResponse.error("Conflito de concorrência: registro foi alterado por outro processo"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Falha na transação: " + (t.getMessage() != null ? t.getMessage() : ex.getMessage())));
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ApiResponse<Object>> handleRollback(RollbackException ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();

        if (t instanceof ConstraintViolationException) {
             return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                     .body(ApiResponse.error("Erro de validação: " + t.getMessage()));
        }
        if (isIntegrityViolation(t)) {
             String raw = extractMessage(ex);
             String userMessage = explainIntegrityMessage(raw);
             return ResponseEntity.status(HttpStatus.CONFLICT)
                     .body(ApiResponse.error(List.of(userMessage, raw)));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Transação reprovada (rollback): " + (t.getMessage() != null ? t.getMessage() : ex.getMessage())));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler({OptimisticLockException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ApiResponse<Object>> handleOptimisticLock(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Conflito de concorrência: registro alterado por outro processo"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        ex.printStackTrace(); // Log error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor: " + ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Credenciais inválidas"));
    }

    private boolean isIntegrityViolation(Throwable t) {
        return t instanceof ConstraintViolationException || 
               t instanceof SQLIntegrityConstraintViolationException || 
               t instanceof DataIntegrityViolationException;
    }

    private String extractMessage(Exception ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();
        return t.getMessage() != null ? t.getMessage() : ex.getMessage();
    }

    private String explainIntegrityMessage(String raw) {
        if (raw == null) return "Violação de integridade (PK/FK/unique)";
        String lower = raw.toLowerCase();
        if (lower.contains("duplicate key") && lower.contains("violates unique constraint")) {
            String field = extractKeyField(raw);
            return field != null ? "Conflito de unicidade: " + field + " já existe" : "Conflito de unicidade (valor já existente)";
        }
        if (lower.contains("violates foreign key constraint")) {
            return "Violação de chave estrangeira: registro referenciado ou referência inválida";
        }
        if (lower.contains("delete") && lower.contains("foreign key")) {
            return "Não é possível excluir: registro possui dependências (FK)";
        }
        return "Violação de integridade (PK/FK/unique)";
    }

    private String extractKeyField(String raw) {
        int idxKey = raw.indexOf("Key (");
        if (idxKey >= 0) {
            int endIdx = raw.indexOf(")=", idxKey);
            if (endIdx > idxKey + 5) {
                return raw.substring(idxKey + 5, endIdx).trim();
            }
        }
        return null;
    }
}
