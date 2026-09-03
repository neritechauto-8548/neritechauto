package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.ordemservico.domain.FotoOS;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.FotoOSResponse;
import com.neritech.saas.ordemservico.repository.FotoOSRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class FotoOSService {
    private final FotoOSRepository repository;
    private final OrdemServicoRepository osRepository;
    private final OSFotoStorageService storage;

    public FotoOSService(FotoOSRepository repository, OrdemServicoRepository osRepository, OSFotoStorageService storage) {
        this.repository = repository;
        this.osRepository = osRepository;
        this.storage = storage;
    }

    public FotoOSResponse upload(Long osId, MultipartFile file, String descricao, String baseUrl) {
        Long tenantId = requireTenant();
        OrdemServico os = requireOrdemServicoDoTenant(osId, tenantId);

        OSFotoStorageService.StorageInfo info = storage.store(osId, file);
        FotoOS entity = new FotoOS();
        entity.setEmpresaId(tenantId);
        entity.setOrdemServicoId(os.getId());
        entity.setArquivoPath(info.absolutePath);
        entity.setContentType(info.contentType);
        entity.setTamanho(info.size);
        entity.setDescricao(descricao);

        FotoOS saved = repository.save(entity);
        String url = baseUrl + "/v1/ordens-servico/fotos/" + saved.getId() + "/download";
        saved.setArquivoUrl(url);
        repository.save(saved);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<FotoOSResponse> list(Long osId) {
        Long tenantId = requireTenant();
        requireOrdemServicoDoTenant(osId, tenantId);
        return repository.findByOrdemServicoIdAndEmpresaIdOrderByIdAsc(osId, tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Resource download(Long id) {
        FotoOS foto = requireFotoDoTenant(id);
        return storage.load(foto.getArquivoPath());
    }

    @Transactional(readOnly = true)
    public String getContentType(Long id) {
        FotoOS foto = requireFotoDoTenant(id);
        return foto.getContentType() != null ? foto.getContentType() : "application/octet-stream";
    }

    public void delete(Long id) {
        FotoOS foto = requireFotoDoTenant(id);
        repository.delete(foto);
    }

    private FotoOS requireFotoDoTenant(Long id) {
        Long tenantId = requireTenant();
        return repository.findByIdAndEmpresaId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Foto da ordem de serviço não encontrada"));
    }

    private OrdemServico requireOrdemServicoDoTenant(Long osId, Long tenantId) {
        return osRepository.findByIdAndEmpresaId(osId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço não encontrada"));
    }

    private Long requireTenant() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("Contexto de empresa autenticada não disponível");
        }
        return tenantId;
    }

    private FotoOSResponse toResponse(FotoOS f) {
        return new FotoOSResponse(
                f.getId(),
                f.getEmpresaId(),
                f.getOrdemServicoId(),
                f.getArquivoUrl(),
                f.getContentType(),
                f.getTamanho(),
                f.getDescricao(),
                f.getDataCadastro(),
                f.getDataAtualizacao()
        );
    }
}
