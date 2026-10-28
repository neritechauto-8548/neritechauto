package com.neritech.saas.comunicacao.domain.enums;

/**
 * Uma pesquisa só pode receber respostas após publicação explícita.
 * O estado CLOSED é terminal; alterações estruturais exigirão versionamento futuro.
 */
public enum StatusPesquisa {
    DRAFT, PUBLISHED, PAUSED, CLOSED
}
