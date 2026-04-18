package com.neritech.saas.garantia.domain;

/**
 * Enum para tipo de resoluÃ§Ã£o de garantia
 */
public enum TipoResolucao {
    REPARO, // Reparo do item
    TROCA, // Troca do item
    REEMBOLSO, // Reembolso do valor
    DESCONTO, // Desconto em prÃ³ximo serviÃ§o
    RETRABALHO, // Retrabalho do serviÃ§o
    SEM_ACAO // Sem aÃ§Ã£o necessÃ¡ria
}
