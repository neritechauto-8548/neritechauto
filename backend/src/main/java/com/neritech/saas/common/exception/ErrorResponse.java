package com.neritech.saas.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Estrutura padrÃ£o de resposta de erro da API")
public class ErrorResponse {

    @Schema(description = "CÃ³digo HTTP do erro", example = "400")
    private int status;

    @Schema(description = "Mensagem descritiva do erro", example = "Dados invÃ¡lidos")
    private String message;

    @Schema(description = "Data e hora do erro")
    private LocalDateTime timestamp;

    @Schema(description = "Lista de erros de validaÃ§Ã£o (opcional)")
    private List<ValidationError> errors;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(int status, String message, List<ValidationError> errors) {
        this(status, message);
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    @Schema(description = "Detalhe de erro de validaÃ§Ã£o de campo")
    public static class ValidationError {
        @Schema(description = "Nome do campo", example = "cpf")
        private String field;

        @Schema(description = "Mensagem de erro", example = "CPF invÃ¡lido")
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
