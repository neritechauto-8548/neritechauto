package com.neritech.saas.common.exception;

import com.neritech.saas.common.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Desligado por padrão, inclusive com profile "prod" rodando no computador.
    // NUNCA habilitar em servidor público: diagnóstico exige origem loopback.
    @Value("${neritech.diagnostics.expose-exceptions:false}")
    private boolean exposeExceptions;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex) {
        return buildErrorResponse("BUSINESS_RULE", ex.getMessage(), null, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(Exception ex) {
        return buildErrorResponse("NOT_FOUND", ex.getMessage(), null, HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String message = status == HttpStatus.FORBIDDEN ? "Acesso negado" : ex.getReason();
        return buildErrorResponse(status == HttpStatus.FORBIDDEN ? "FORBIDDEN" : "HTTP_ERROR", message, null, status, ex);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        return buildErrorResponse("NOT_FOUND", "Recurso não encontrado: " + ex.getRequestURL(), null, HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return buildErrorResponse("VALIDATION", "Erro de validação nos campos", errors, HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleBadPayload(HttpMessageNotReadableException ex) {
        return buildErrorResponse("VALIDATION", "Payload inválido ou malformado", null, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return buildErrorResponse("VALIDATION", "Erro de validação nos dados enviados", null, HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ApiErrorResponse> handleIntegrity(Exception ex) {
        String raw = extractMessage(ex);
        String userMessage = explainIntegrityMessage(raw);
        log.warn("Conflito de integridade na API", ex);
            return buildErrorResponse("BUSINESS_RULE", userMessage, null, HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiErrorResponse> handleTransaction(TransactionSystemException ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();

        if (t instanceof ConstraintViolationException) {
            return buildErrorResponse("VALIDATION", "Erro de validação nos dados enviados", null, HttpStatus.UNPROCESSABLE_ENTITY, ex);
        }
        if (isIntegrityViolation(t)) {
            String raw = extractMessage(ex);
            String userMessage = explainIntegrityMessage(raw);
            log.warn("Conflito de integridade na API", ex);
            return buildErrorResponse("BUSINESS_RULE", userMessage, null, HttpStatus.CONFLICT, ex);
        }
        if (t instanceof OptimisticLockException || t instanceof OptimisticLockingFailureException) {
             return buildErrorResponse("BUSINESS_RULE", "Conflito de concorrência: registro foi alterado por outro processo", null, HttpStatus.CONFLICT, ex);
        }
        log.error("Falha inesperada na transação", ex);
        return buildErrorResponse("INTERNAL_ERROR", "Não foi possível concluir a operação.", null, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ApiErrorResponse> handleRollback(RollbackException ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();

        if (t instanceof ConstraintViolationException) {
             return buildErrorResponse("VALIDATION", "Erro de validação nos dados enviados", null, HttpStatus.UNPROCESSABLE_ENTITY, ex);
        }
        if (isIntegrityViolation(t)) {
             String raw = extractMessage(ex);
             String userMessage = explainIntegrityMessage(raw);
             log.warn("Conflito de integridade na API", ex);
            return buildErrorResponse("BUSINESS_RULE", userMessage, null, HttpStatus.CONFLICT, ex);
        }
        log.error("Rollback inesperado na transação", ex);
        return buildErrorResponse("INTERNAL_ERROR", "Não foi possível concluir a operação.", null, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex) {
        return buildErrorResponse("BUSINESS_RULE", ex.getMessage(), null, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse("BUSINESS_RULE", ex.getMessage(), null, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler({OptimisticLockException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ApiErrorResponse> handleOptimisticLock(Exception ex) {
        return buildErrorResponse("BUSINESS_RULE", "Conflito de concorrência: registro alterado por outro processo", null, HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("code", "FORBIDDEN_PERMISSION");
        return buildErrorResponse("FORBIDDEN", "Acesso negado: você não tem permissão para realizar esta operação", errors, HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return buildErrorResponse("UNAUTHORIZED", "Credenciais inválidas", null, HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        log.error("Erro inesperado na API", ex);
        return buildErrorResponse("INTERNAL_ERROR", "Erro interno do servidor. Tente novamente mais tarde.", null, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            String type, String message, Map<String, String> errors, HttpStatus status, Throwable exception) {
        String classe = null;
        String causa = null;
        if (diagnosticoLocalPermitido() && exception != null) {
            classe = exception.getClass().getName();
            Throwable raiz = exception;
            int profundidade = 0;
            while (raiz.getCause() != null && raiz.getCause() != raiz && profundidade++ < 20) {
                raiz = raiz.getCause();
            }
            causa = raiz.getMessage() != null ? raiz.getMessage() : exception.getMessage();
        }
        ApiErrorResponse response = new ApiErrorResponse(
                type, message, errors, LocalDateTime.now(), classe, causa);
        return ResponseEntity.status(status).body(response);
    }

    private boolean diagnosticoLocalPermitido() {
        if (!exposeExceptions) return false;
        var attrs = RequestContextHolder.getRequestAttributes();
        if (!(attrs instanceof ServletRequestAttributes servlet)) return false;
        // A checagem usa a conexão real. Não confiar em X-Forwarded-For.
        String remoto = servlet.getRequest().getRemoteAddr();
        return "127.0.0.1".equals(remoto) || "0:0:0:0:0:0:0:1".equals(remoto)
                || "::1".equals(remoto);
    }

    private boolean isIntegrityViolation(Throwable t) {
        return t instanceof SQLIntegrityConstraintViolationException || 
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
