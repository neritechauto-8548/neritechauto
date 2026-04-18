package com.neritech.saas.ordemservico.service;

import com.neritech.saas.ordemservico.domain.FotoOS;
import com.neritech.saas.ordemservico.domain.OrdemServico;
import com.neritech.saas.ordemservico.dto.FotoOSResponse;
import com.neritech.saas.ordemservico.repository.FotoOSRepository;
import com.neritech.saas.ordemservico.repository.OrdemServicoRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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
        OrdemServico os = osRepository.findById(osId).orElseThrow();
        OSFotoStorageService.StorageInfo info = storage.store(osId, file);
        FotoOS entity = new FotoOS();
        entity.setEmpresaId(os.getEmpresaId());
        entity.setOrdemServicoId(osId);
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
        return repository.findByOrdemServicoIdOrderByIdAsc(osId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Resource download(Long id) {
        FotoOS foto = repository.findById(id).orElseThrow();
        return storage.load(foto.getArquivoPath());
    }

    @Transactional(readOnly = true)
    public String getContentType(Long id) {
        FotoOS foto = repository.findById(id).orElseThrow();
        return foto.getContentType() != null ? foto.getContentType() : "application/octet-stream";
    }

    public void delete(Long id) {
        repository.deleteById(id);
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
