package com.neritech.saas.relatorios.service;

import com.neritech.saas.relatorios.domain.Backup;
import com.neritech.saas.relatorios.domain.enums.StatusBackup;
import com.neritech.saas.relatorios.dto.BackupRequest;
import com.neritech.saas.relatorios.dto.BackupResponse;
import com.neritech.saas.relatorios.mapper.BackupMapper;
import com.neritech.saas.relatorios.repository.BackupRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BackupService {

    private final BackupRepository backupRepository;
    private final BackupMapper backupMapper;

    @Transactional
    public BackupResponse create(BackupRequest request) {
        Backup entity = backupMapper.toEntity(request);
        entity.setStatus(StatusBackup.EM_ANDAMENTO);
        entity.setDataCadastro(LocalDateTime.now());
        Backup saved = backupRepository.save(entity);
        return backupMapper.toResponse(saved);
    }

    @Transactional
    public BackupResponse updateStatus(Long id, StatusBackup status, String erro) {
        Backup entity = backupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Backup nÃ£o encontrado: " + id));
        entity.setStatus(status);
        if (erro != null) {
            entity.setErroBackup(erro);
        }
        if (status == StatusBackup.CONCLUIDO || status == StatusBackup.FALHOU || status == StatusBackup.CANCELADO) {
            entity.setDataFim(LocalDateTime.now());
        }
        return backupMapper.toResponse(backupRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<BackupResponse> findByEmpresa(Long empresaId) {
        return backupRepository.findByEmpresaIdOrderByDataInicioDesc(empresaId).stream()
                .map(backupMapper::toResponse)
                .collect(Collectors.toList());
    }
}
