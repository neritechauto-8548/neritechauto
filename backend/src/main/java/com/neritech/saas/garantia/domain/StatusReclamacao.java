package com.neritech.saas.garantia.domain;

/**
 * Enum para status de reclamaÃ§Ã£o de garantia
 */
public enum StatusReclamacao {
    ABERTA, // ReclamaÃ§Ã£o aberta
    EM_ANALISE, // Em anÃ¡lise tÃ©cnica
    APROVADA, // ReclamaÃ§Ã£o aprovada
    REPROVADA, // ReclamaÃ§Ã£o reprovada
    EM_EXECUCAO, // Em execuÃ§Ã£o da resoluÃ§Ã£o
    CONCLUIDA, // ReclamaÃ§Ã£o concluÃ­da
    CANCELADA // ReclamaÃ§Ã£o cancelada
}
