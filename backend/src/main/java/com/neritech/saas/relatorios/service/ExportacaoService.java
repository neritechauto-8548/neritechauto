package com.neritech.saas.relatorios.service;

import com.neritech.saas.relatorios.domain.enums.StatusExportacao;
import com.neritech.saas.relatorios.domain.Exportacao;
import com.neritech.saas.relatorios.dto.ExportacaoRequest;
import com.neritech.saas.relatorios.dto.ExportacaoResponse;
import com.neritech.saas.relatorios.mapper.ExportacaoMapper;
import com.neritech.saas.relatorios.repository.ExportacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExportacaoService {

    private final ExportacaoRepository exportacaoRepository;
    private final ExportacaoMapper exportacaoMapper;

    @Transactional
    public ExportacaoResponse create(ExportacaoRequest request) {
        Exportacao entity = exportacaoMapper.toEntity(request);
        entity.setStatus(StatusExportacao.PROCESSANDO);
        entity.setDataInicio(LocalDateTime.now());
        entity.setDataCadastro(LocalDateTime.now());
        Exportacao saved = exportacaoRepository.save(entity);
        return exportacaoMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ExportacaoResponse> findByEmpresa(Long empresaId) {
        return exportacaoRepository.findByEmpresaId(empresaId).stream()
                .map(exportacaoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
