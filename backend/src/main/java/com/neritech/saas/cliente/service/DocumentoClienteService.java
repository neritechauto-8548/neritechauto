package com.neritech.saas.cliente.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.domain.DocumentoCliente;
import com.neritech.saas.cliente.dto.DocumentoClienteResponse;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.cliente.repository.DocumentoClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.exception.ResourceNotFoundException;
import com.neritech.saas.common.tenancy.TenantAccess;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentoClienteService {
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final Set<String> SUPPORTED_TYPES = Set.of("application/pdf", "image/jpeg", "image/png");

    private final DocumentoClienteRepository repository;
    private final ClienteRepository clientes;

    public DocumentoClienteService(DocumentoClienteRepository repository, ClienteRepository clientes) {
        this.repository = repository;
        this.clientes = clientes;
    }

    private Cliente requireCliente(Long clienteId) {
        TenantAccess.requireCurrent();
        return clientes.findByIdScoped(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", clienteId));
    }

    @Transactional(readOnly = true)
    public Page<DocumentoClienteResponse> list(Long clienteId, Pageable pageable) {
        requireCliente(clienteId);
        return repository.findScoped(clienteId, TenantAccess.requireCurrent(), pageable)
                .map(DocumentoClienteResponse::from);
    }

    @Transactional(readOnly = true)
    public DocumentoCliente requireDocumento(Long clienteId, Long id) {
        requireCliente(clienteId);
        return repository.findOneScoped(id, clienteId, TenantAccess.requireCurrent())
                .orElseThrow(() -> new ResourceNotFoundException("Documento de cliente", "ID", id));
    }

    @Transactional(readOnly = true)
    public DocumentoClienteResponse get(Long clienteId, Long id) {
        return DocumentoClienteResponse.from(requireDocumento(clienteId, id));
    }

    private static boolean matchesContentType(String type, byte[] bytes) {
        if ("application/pdf".equals(type))
            return bytes.length >= 5 && bytes[0] == '%' && bytes[1] == 'P'
                    && bytes[2] == 'D' && bytes[3] == 'F' && bytes[4] == '-';
        if ("image/jpeg".equals(type))
            return bytes.length >= 3 && (bytes[0] & 0xff) == 0xff
                    && (bytes[1] & 0xff) == 0xd8 && (bytes[2] & 0xff) == 0xff;
        if ("image/png".equals(type))
            return bytes.length >= 8 && Arrays.equals(Arrays.copyOf(bytes, 8),
                    new byte[]{(byte)0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a});
        return false;
    }

    @Transactional
    public DocumentoClienteResponse upload(Long clienteId, MultipartFile file, String tipoDocumento, String descricao)
            throws IOException {
        Cliente cliente = requireCliente(clienteId);
        if (file == null || file.isEmpty() || file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("Arquivo obrigatório com tamanho máximo de 5 MB.");
        }
        String tipo = file.getContentType();
        if (tipo == null || !SUPPORTED_TYPES.contains(tipo)) {
            throw new BusinessException("Envie apenas PDF, JPG ou PNG.");
        }
        byte[] bytes = file.getBytes();
        if (!matchesContentType(tipo, bytes)) {
            throw new BusinessException("O arquivo não corresponde ao formato informado.");
        }
        if (tipoDocumento == null || tipoDocumento.isBlank() || tipoDocumento.length() > 50
                || (descricao != null && descricao.length() > 255)) {
            throw new BusinessException("Tipo ou descrição do documento inválidos.");
        }
        String original = file.getOriginalFilename();
        String fileName = original == null ? "documento" : original.replace('\\', '/');
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1)
                .replaceAll("[^\\p{L}\\p{N}._-]", "_");
        if (fileName.isBlank()) fileName = "documento";
        if (fileName.length() > 180) fileName = fileName.substring(fileName.length() - 180);

        DocumentoCliente doc = new DocumentoCliente();
        doc.setEmpresaId(TenantAccess.requireCurrent());
        doc.setCliente(cliente);
        doc.setTipoDocumento(tipoDocumento);
        doc.setDescricao(descricao);
        doc.setArquivoNome(fileName);
        doc.setArquivoTipo(tipo);
        doc.setArquivoTamanho((long)bytes.length);
        doc.setArquivoConteudo(bytes);
        return DocumentoClienteResponse.from(repository.save(doc));
    }

    @Transactional
    public void delete(Long clienteId, Long id) {
        DocumentoCliente entity = requireDocumento(clienteId, id);
        repository.delete(entity);
    }
}
