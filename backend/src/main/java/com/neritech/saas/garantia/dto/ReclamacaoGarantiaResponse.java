package com.neritech.saas.garantia.dto;

import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.garantia.domain.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para ReclamacaoGarantia
 */
public record ReclamacaoGarantiaResponse(
        Long id,
        Long garantiaId,
        String numeroReclamacao,
        LocalDateTime dataAbertura,
        TipoReclamacao tipoReclamacao,
        String problemaRelatado,
        String sintomasObservados,
        String condicoesUso,
        Integer kilometragemAtual,
        Integer tempoUsoDesdeServico,
        String evidenciasFornecidas,
        String fotosProblema,
        String videosProblema,
        String documentosAnexos,
        PrioridadeReclamacao prioridade,
        CanalAberturaReclamacao canalAbertura,
        String clienteSolicitante,
        String contatoSolicitante,
        String enderecoAtendimento,
        LocalDateTime dataAgendamentoAnalise,
        Long tecnicoResponsavelId,
        String tecnicoResponsavelNome,
        LocalDateTime dataInicioAnalise,
        LocalDateTime dataFimAnalise,
        Integer tempoAnaliseHoras,
        String diagnosticoTecnico,
        String causaIdentificada,
        String procedimentoRealizado,
        Boolean aprovada,
        String motivoReprovacao,
        BigDecimal valorAprovado,
        String itensSubstituidos,
        String servicosRefeitos,
        BigDecimal custosAdicionais,
        String justificativaCustos,
        Integer satisfacaoCliente,
        String comentarioSatisfacao,
        StatusReclamacao status,
        LocalDateTime dataResolucao,
        String observacoesInternas,
        Long abertaPor) {
}
