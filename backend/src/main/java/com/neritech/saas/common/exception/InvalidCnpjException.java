package com.neritech.saas.common.exception;

import org.springframework.http.HttpStatus;

/**
 * ExceÃ§Ã£o lanÃ§ada quando um CNPJ Ã© invÃ¡lido
 */
public class InvalidCnpjException extends ApiException {

    private static final String DEFAULT_MESSAGE = "CNPJ invÃ¡lido";
    private static final String DEFAULT_ERROR_CODE = "invalid-cnpj";

    /**
     * Construtor com mensagem padrÃ£o
     */
    public InvalidCnpjException() {
        super(DEFAULT_MESSAGE, HttpStatus.BAD_REQUEST, DEFAULT_ERROR_CODE);
    }

    /**
     * Construtor com mensagem personalizada
     *
     * @param message Mensagem personalizada
     */
    public InvalidCnpjException(String message) {
        super(message, HttpStatus.BAD_REQUEST, DEFAULT_ERROR_CODE);
    }

    /**
     * Construtor com mensagem personalizada e CNPJ
     *
     * @param message Mensagem personalizada
     * @param cnpj CNPJ invÃ¡lido
     */
    public InvalidCnpjException(String message, String cnpj) {
        super(message + ": " + cnpj, HttpStatus.BAD_REQUEST, DEFAULT_ERROR_CODE);
    }
}
