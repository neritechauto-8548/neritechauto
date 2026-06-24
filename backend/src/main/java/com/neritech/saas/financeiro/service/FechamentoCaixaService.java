package com.neritech.saas.financeiro.service;

import com.neritech.saas.financeiro.domain.FechamentoCaixa;
import com.neritech.saas.financeiro.dto.FechamentoCaixaRequest;
import com.neritech.saas.financeiro.dto.FechamentoCaixaResponse;
import com.neritech.saas.financeiro.mapper.FechamentoCaixaMapper;
import com.neritech.saas.financeiro.repository.FechamentoCaixaRepository;
import com.neritech.saas.gestaoUsuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FechamentoCaixaService {

    private final FechamentoCaixaRepository repository;
    private final FechamentoCaixaMapper mapper;
    private final UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public Page<FechamentoCaixaResponse> findAll(Long empresaId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        Page<FechamentoCaixa> page;
        if (dataInicio != null && dataFim != null) {
             page = repository.findByEmpresaIdAndDataAberturaBetween(
                empresaId, 
                dataInicio.atStartOfDay(), 
                dataFim.atTime(23, 59, 59), 
                pageable
            );
        } else {
             page = repository.findByEmpresaId(empresaId, pageable);
        }

        java.util.Set<Long> userIds = page.getContent().stream()
                .map(FechamentoCaixa::getUsuarioResponsavel)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());

        java.util.Map<Long, String> userNames = new java.util.HashMap<>();
        if (!userIds.isEmpty()) {
            java.util.List<Object[]> results = usuarioService.findNomesCompletosByIdsAndEmpresaId(userIds, empresaId);
            for (Object[] row : results) {
                userNames.put((Long) row[0], (String) row[1]);
            }
        }

        return page.map(entity -> {
            FechamentoCaixaResponse response = mapper.toResponse(entity);
            if (entity.getUsuarioResponsavel() != null) {
                response.setResponsavelNome(userNames.get(entity.getUsuarioResponsavel()));
            }
            return response;
        });
    }

    @Transactional(readOnly = true)
    public FechamentoCaixaResponse findById(Long id, Long empresaId) {
        FechamentoCaixa entity = repository.findById(id)
                .filter(e -> e.getEmpresaId().equals(empresaId))
                .orElseThrow(() -> new RuntimeException("Fechamento de caixa não encontrado"));
        return mapper.toResponse(entity);
    }

    @Transactional
    public FechamentoCaixaResponse create(Long empresaId, FechamentoCaixaRequest request) {
        FechamentoCaixa entity = mapper.toEntity(request);
        entity.setEmpresaId(empresaId);
        
        // Se a situação não for fornecida, padrão para FECHADO
        if (entity.getSituacao() == null) {
            entity.setSituacao("FECHADO");
        }
        
        // Se usuarioResponsavel não foi fornecido, usar o usuário atual
        if (entity.getUsuarioResponsavel() == null) {
            try {
                Long currentUserId = usuarioService.getCurrentUser().getId();
                entity.setUsuarioResponsavel(currentUserId);
            } catch (Exception e) {
                // Fallback: usar criadoPor se disponível após o save
                // Por enquanto, vamos salvar e depois atualizar
                entity = repository.save(entity);
                if (entity.getCriadoPor() != null) {
                    entity.setUsuarioResponsavel(entity.getCriadoPor());
                    entity = repository.save(entity);
                } else {
                    // Se ainda não tiver, lançar exceção
                    throw new RuntimeException("Não foi possível identificar o usuário responsável pelo fechamento");
                }
                return mapper.toResponse(entity);
            }
        }
        
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public FechamentoCaixaResponse update(Long id, Long empresaId, FechamentoCaixaRequest request) {
        FechamentoCaixa entity = repository.findById(id)
                .filter(e -> e.getEmpresaId().equals(empresaId))
                .orElseThrow(() -> new RuntimeException("Fechamento de caixa não encontrado"));
        
        mapper.updateEntityFromDTO(request, entity);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public void delete(Long id, Long empresaId) {
        FechamentoCaixa entity = repository.findById(id)
                .filter(e -> e.getEmpresaId().equals(empresaId))
                .orElseThrow(() -> new RuntimeException("Fechamento de caixa não encontrado"));
        repository.delete(entity);
    }

    @Transactional(readOnly = true)
    public void validarCaixaAberto(Long empresaId, LocalDate dataMovimentacao) {
        if (dataMovimentacao == null) return;
        repository.findFirstByEmpresaIdAndSituacaoOrderByDataFechamentoDesc(empresaId, "FECHADO")
                .ifPresent(ultimoFechamento -> {
                    LocalDate limite = ultimoFechamento.getDataFechamento().toLocalDate();
                    if (!dataMovimentacao.isAfter(limite)) {
                        throw new RuntimeException("Não é possível realizar lançamentos ou alterações em um período de caixa fechado (" + 
                            limite.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
                    }
                });
    }

    @Transactional(readOnly = true)
    public LocalDate getUltimaDataFechamento(Long empresaId) {
        return repository.findFirstByEmpresaIdAndSituacaoOrderByDataFechamentoDesc(empresaId, "FECHADO")
                .map(f -> f.getDataFechamento().toLocalDate())
                .orElse(null);
    }
}
