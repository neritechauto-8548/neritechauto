package com.neritech.saas.garantia.domain;

/**
 * Enum para status de garantia
 */
public enum StatusGarantia {
    ATIVA, // Garantia ativa e vÃ¡lida
    EXPIRADA, // Garantia expirada por prazo
    CANCELADA, // Garantia cancelada
    SUSPENSA, // Garantia temporariamente suspensa
    UTILIZADA // Garantia jÃ¡ utilizada completamente
}
