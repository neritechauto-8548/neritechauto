package com.neritech.saas.relatorios.service;

import com.neritech.saas.relatorios.domain.ParametroRelatorio;
import com.neritech.saas.relatorios.domain.RelatorioSalvo;
import com.neritech.saas.relatorios.dto.ParametroRelatorioRequest;
import com.neritech.saas.relatorios.dto.RelatorioSalvoRequest;
import com.neritech.saas.relatorios.dto.RelatorioSalvoResponse;
import com.neritech.saas.relatorios.mapper.ParametroRelatorioMapper;
import com.neritech.saas.relatorios.mapper.RelatorioSalvoMapper;
import com.neritech.saas.relatorios.repository.ParametroRelatorioRepository;
import com.neritech.saas.relatorios.repository.RelatorioSalvoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioSalvoService {

    private final RelatorioSalvoRepository relatorioRepository;
    private final ParametroRelatorioRepository parametroRepository;
    private final RelatorioSalvoMapper relatorioMapper;
    private final ParametroRelatorioMapper parametroMapper;

    @Transactional(readOnly = true)
    public List<RelatorioSalvoResponse> findAllByEmpresa(Long empresaId) {
        return relatorioRepository.findByEmpresaId(empresaId).stream()
                .map(relatorioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RelatorioSalvoResponse findById(Long id) {
        return relatorioRepository.findById(id)
                .map(relatorioMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("RelatÃ³rio nÃ£o encontrado: " + id));
    }

    @Transactional
    public RelatorioSalvoResponse create(RelatorioSalvoRequest request) {
        RelatorioSalvo entity = relatorioMapper.toEntity(request);
        // Set default values or validations if needed
        RelatorioSalvo saved = relatorioRepository.save(entity);
        return relatorioMapper.toResponse(saved);
    }

    @Transactional
    public RelatorioSalvoResponse update(Long id, RelatorioSalvoRequest request) {
        RelatorioSalvo entity = relatorioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RelatÃ³rio nÃ£o encontrado: " + id));

        relatorioMapper.updateEntity(entity, request);
        RelatorioSalvo updated = relatorioRepository.save(entity);
        return relatorioMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!relatorioRepository.existsById(id)) {
            throw new EntityNotFoundException("RelatÃ³rio nÃ£o encontrado: " + id);
        }
        relatorioRepository.deleteById(id);
    }

    // Parameter management methods
    @Transactional
    public void addParametro(Long relatorioId, ParametroRelatorioRequest request) {
        RelatorioSalvo relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new EntityNotFoundException("RelatÃ³rio nÃ£o encontrado: " + relatorioId));

        ParametroRelatorio parametro = parametroMapper.toEntity(request);
        parametro.setRelatorio(relatorio);

        if (request.getDependenteDeId() != null) {
            ParametroRelatorio dependente = parametroRepository.findById(request.getDependenteDeId())
                    .orElseThrow(() -> new EntityNotFoundException("ParÃ¢metro dependente nÃ£o encontrado"));
            parametro.setDependenteDe(dependente);
        }

        parametroRepository.save(parametro);
    }
}
