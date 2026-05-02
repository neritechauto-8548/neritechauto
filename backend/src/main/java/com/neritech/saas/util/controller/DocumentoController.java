package com.neritech.saas.util.controller;

import com.neritech.saas.util.DocumentoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/util/documentos")
@Tag(name = "Utilitários", description = "Endpoints de utilidade geral do sistema")
public class DocumentoController {

    /**
     * Valida matematicamente um CPF ou CNPJ.
     * Endpoint genérico que pode ser usado por qualquer módulo (Clientes, Fornecedores, etc).
     *
     * @param valor Valor do documento
     * @param tipo  Tipo de pessoa (Física ou Jurídica)
     * @return true se válido, false caso contrário
     */
    @GetMapping("/validar")
    @Operation(summary = "Validar CPF/CNPJ", description = "Valida matematicamente se um CPF ou CNPJ é válido seguindo as regras da Receita Federal (incluindo Alfanumérico).")
    public ResponseEntity<Boolean> validarDocumento(
            @Parameter(description = "Valor do documento") @RequestParam String valor,
            @Parameter(description = "Tipo de pessoa (Física ou Jurídica)") @RequestParam String tipo) {
        
        if ("Física".equalsIgnoreCase(tipo) || "PF".equalsIgnoreCase(tipo) || "CPF".equalsIgnoreCase(tipo)) {
            return ResponseEntity.ok(DocumentoValidator.isValidCpf(valor));
        } else {
            return ResponseEntity.ok(DocumentoValidator.isValidCnpj(valor));
        }
    }
}
