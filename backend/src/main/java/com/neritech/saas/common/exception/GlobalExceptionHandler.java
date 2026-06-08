package com.neritech.saas.common.exception;

import com.neritech.saas.common.dto.ApiErrorResponse;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.servlet.NoHandlerFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex) {
        return buildErrorResponse("BUSINESS_RULE", ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse("NOT_FOUND", ex.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        return buildErrorResponse("NOT_FOUND", "Recurso não encontrado: " + ex.getRequestURL(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return buildErrorResponse("VALIDATION", "Erro de validação nos campos", errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleBadPayload(HttpMessageNotReadableException ex) {
        String detail = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        return buildErrorResponse("VALIDATION", "Payload inválido ou malformado: " + detail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiErrorResponse> handleIntegrity(Exception ex) {
        String raw = extractMessage(ex);
        String userMessage = explainIntegrityMessage(raw);
        Map<String, String> errors = new HashMap<>();
        errors.put("detalhes", raw);
        return buildErrorResponse("BUSINESS_RULE", userMessage, errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiErrorResponse> handleTransaction(TransactionSystemException ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();

        if (t instanceof ConstraintViolationException) {
            return buildErrorResponse("VALIDATION", "Erro de validação: " + t.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (isIntegrityViolation(t)) {
            String raw = extractMessage(ex);
            String userMessage = explainIntegrityMessage(raw);
            Map<String, String> errors = new HashMap<>();
            errors.put("detalhes", raw);
            return buildErrorResponse("BUSINESS_RULE", userMessage, errors, HttpStatus.CONFLICT);
        }
        if (t instanceof OptimisticLockException || t instanceof OptimisticLockingFailureException) {
             return buildErrorResponse("BUSINESS_RULE", "Conflito de concorrência: registro foi alterado por outro processo", null, HttpStatus.CONFLICT);
        }
        return buildErrorResponse("INTERNAL_ERROR", "Falha na transação: " + (t.getMessage() != null ? t.getMessage() : ex.getMessage()), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ApiErrorResponse> handleRollback(RollbackException ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();

        if (t instanceof ConstraintViolationException) {
             return buildErrorResponse("VALIDATION", "Erro de validação: " + t.getMessage(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (isIntegrityViolation(t)) {
             String raw = extractMessage(ex);
             String userMessage = explainIntegrityMessage(raw);
             Map<String, String> errors = new HashMap<>();
             errors.put("detalhes", raw);
             return buildErrorResponse("BUSINESS_RULE", userMessage, errors, HttpStatus.CONFLICT);
        }
        return buildErrorResponse("INTERNAL_ERROR", "Transação reprovada (rollback): " + (t.getMessage() != null ? t.getMessage() : ex.getMessage()), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex) {
        return buildErrorResponse("BUSINESS_RULE", ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse("BUSINESS_RULE", ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OptimisticLockException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ApiErrorResponse> handleOptimisticLock(Exception ex) {
        return buildErrorResponse("BUSINESS_RULE", "Conflito de concorrência: registro alterado por outro processo", null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("code", "FORBIDDEN_PERMISSION");
        return buildErrorResponse("FORBIDDEN", "Acesso negado: você não tem permissão para realizar esta operação", errors, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return buildErrorResponse("UNAUTHORIZED", "Credenciais inválidas", null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace(); // Log error
        return buildErrorResponse("INTERNAL_ERROR", "Erro interno do servidor: " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(String type, String message, Map<String, String> errors, HttpStatus status) {
        ApiErrorResponse response = new ApiErrorResponse(type, message, errors, LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
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
