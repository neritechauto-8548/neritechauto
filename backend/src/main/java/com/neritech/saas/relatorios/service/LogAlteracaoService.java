package com.neritech.saas.relatorios.service;

import com.neritech.saas.relatorios.domain.LogAlteracao;
import com.neritech.saas.relatorios.dto.LogAlteracaoRequest;
import com.neritech.saas.relatorios.dto.LogAlteracaoResponse;
import com.neritech.saas.relatorios.mapper.LogAlteracaoMapper;
import com.neritech.saas.relatorios.repository.LogAlteracaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogAlteracaoService {

    private final LogAlteracaoRepository logRepository;
    private final LogAlteracaoMapper logMapper;

    @Transactional
    public LogAlteracaoResponse create(LogAlteracaoRequest request) {
        LogAlteracao entity = logMapper.toEntity(request);
        entity.setDataAlteracao(LocalDateTime.now());
        LogAlteracao saved = logRepository.save(entity);
        return logMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LogAlteracaoResponse> findByEmpresa(Long empresaId) {
        return logRepository.findByEmpresaId(empresaId).stream()
                .map(logMapper::toResponse)
                .collect(Collectors.toList());
    }
}
