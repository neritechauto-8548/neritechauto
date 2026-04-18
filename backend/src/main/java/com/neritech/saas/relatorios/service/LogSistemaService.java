package com.neritech.saas.relatorios.service;

import com.neritech.saas.relatorios.domain.LogSistema;
import com.neritech.saas.relatorios.dto.LogSistemaRequest;
import com.neritech.saas.relatorios.dto.LogSistemaResponse;
import com.neritech.saas.relatorios.mapper.LogSistemaMapper;
import com.neritech.saas.relatorios.repository.LogSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogSistemaService {

    private final LogSistemaRepository logRepository;
    private final LogSistemaMapper logMapper;

    @Transactional
    public LogSistemaResponse create(LogSistemaRequest request) {
        LogSistema entity = logMapper.toEntity(request);
        entity.setDataOcorrencia(LocalDateTime.now());
        LogSistema saved = logRepository.save(entity);
        return logMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LogSistemaResponse> findByEmpresa(Long empresaId) {
        return logRepository.findByEmpresaId(empresaId).stream()
                .map(logMapper::toResponse)
                .collect(Collectors.toList());
    }
}
