package com.neritech.saas.financeiro.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AnexoTituloDTO {
    private Long id;
    private String nomeArquivo;
    private String tipoArquivo;
    private Long tamanhoBytes;
    private String caminhoLocal;
    private String observacoes;
    private LocalDateTime dataCadastro;
}
