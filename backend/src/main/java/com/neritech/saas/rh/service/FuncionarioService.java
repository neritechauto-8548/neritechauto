package com.neritech.saas.rh.service;

import com.neritech.saas.rh.domain.Funcionario;
import com.neritech.saas.rh.dto.FuncionarioRequest;
import com.neritech.saas.rh.dto.FuncionarioResponse;
import com.neritech.saas.rh.mapper.FuncionarioMapper;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neritech.saas.util.DocumentoValidator;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final FuncionarioMapper mapper;
    private final com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository usuarioRepository;
    private final UsuarioPerfilHelper usuarioPerfilHelper;
    private final jakarta.persistence.EntityManager entityManager;

    public Page<FuncionarioResponse> findAll(Long empresaId, Pageable pageable) {
        return repository.findByEmpresaId(empresaId, pageable)
                .map(mapper::toResponse);
    }

    public FuncionarioResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado"));
    }

    @Transactional
    public FuncionarioResponse create(FuncionarioRequest request) {
        if (repository.existsByEmpresaIdAndMatricula(request.empresaId(), request.matricula())) {
            throw new IllegalArgumentException("Matrícula já existe para esta empresa");
        }
        validarFuncionario(request, null);

        Funcionario entity = mapper.toEntity(request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public FuncionarioResponse update(Long id, FuncionarioRequest request) {
        Funcionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado"));

        validarFuncionario(request, entity);

        mapper.updateEntity(request, entity);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    private void validarFuncionario(FuncionarioRequest request, Funcionario entity) {
        // Validação do CPF
        if (request.cpf() != null && !request.cpf().isBlank()) {
            if (!DocumentoValidator.isValidCpf(request.cpf())) {
                throw new IllegalArgumentException("CPF inválido");
            }
            boolean isNewOrChangedCpf = entity == null || !request.cpf().equals(entity.getCpf());
            if (isNewOrChangedCpf && repository.existsByEmpresaIdAndCpf(request.empresaId(), request.cpf())) {
                throw new IllegalArgumentException("CPF já cadastrado para esta empresa");
            }
        }

        // Validação de Idade Mínima (16 anos)
        if (request.dataNascimento() != null) {
            LocalDate today = LocalDate.now();
            int age = java.time.Period.between(request.dataNascimento(), today).getYears();
            if (age < 16) {
                throw new IllegalArgumentException("O colaborador deve ter pelo menos 16 anos");
            }
        }

        // Validação de Datas de Vínculo
        if (request.dataDemissao() != null && request.dataAdmissao() != null) {
            if (request.dataDemissao().isBefore(request.dataAdmissao())) {
                throw new IllegalArgumentException("A data de demissão não pode ser anterior à data de admissão");
            }
        }

        // Validação de Motivo de Inativação
        if ((request.status() == com.neritech.saas.rh.domain.enums.StatusFuncionario.INATIVO ||
             request.status() == com.neritech.saas.rh.domain.enums.StatusFuncionario.DEMITIDO ||
             request.status() == com.neritech.saas.rh.domain.enums.StatusFuncionario.AFASTADO) &&
            (request.motivoInativacao() == null || request.motivoInativacao().isBlank())) {
                throw new IllegalArgumentException("O motivo de inativação é obrigatório para o status selecionado");
        }

        // Validação Financeira
        if (request.salarioBase() != null && request.salarioBase().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O salário base não pode ser negativo");
        }
        if (request.valeTransporte() != null && request.valeTransporte().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O vale transporte não pode ser negativo");
        }
        if (request.valeAlimentacao() != null && request.valeAlimentacao().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O vale alimentação não pode ser negativo");
        }

        // Validação da CNH
        if (request.cnhNumero() != null && !request.cnhNumero().isBlank()) {
            if (request.cnhCategoria() == null || request.cnhCategoria().isBlank()) {
                throw new IllegalArgumentException("A categoria da CNH é obrigatória quando o número é informado");
            }
            if (request.cnhValidade() != null && request.cnhValidade().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("A CNH informada está vencida");
            }
        }

        // Validação de Endereço
        if (request.cep() == null || request.cep().isBlank()) {
            throw new IllegalArgumentException("CEP é obrigatório");
        }
        String cleanCep = request.cep().replaceAll("\\D", "");
        if (cleanCep.length() != 8) {
            throw new IllegalArgumentException("CEP inválido");
        }
        if (request.logradouro() == null || request.logradouro().isBlank()) {
            throw new IllegalArgumentException("Logradouro é obrigatório");
        }
        if (request.bairro() == null || request.bairro().isBlank()) {
            throw new IllegalArgumentException("Bairro é obrigatório");
        }
        if (request.cidade() == null || request.cidade().isBlank()) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }
        if (request.estado() == null || request.estado().isBlank()) {
            throw new IllegalArgumentException("UF é obrigatória");
        }

        // Validação de E-mail Pessoal
        if (request.emailPessoal() != null && !request.emailPessoal().isBlank()) {
            if (!request.emailPessoal().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new IllegalArgumentException("E-mail pessoal inválido");
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Funcionário não encontrado");
        }
        if (hasReferences(id)) {
            throw new IllegalArgumentException("Não é possível excluir o colaborador pois ele possui registros vinculados no sistema.");
        }
        repository.deleteById(id);
    }

    private boolean hasReferences(Long id) {
        // Check comissoes_funcionarios
        Number countComissoes = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM comissoes_funcionarios WHERE funcionario_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countComissoes.longValue() > 0) return true;

        // Check reclamacoes_garantia
        Number countReclamacoes = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM reclamacoes_garantia WHERE tecnico_responsavel_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countReclamacoes.longValue() > 0) return true;

        // Check resolucoes_garantia
        Number countResolucoes = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM resolucoes_garantia WHERE funcionario_executor_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countResolucoes.longValue() > 0) return true;

        // Check ferias_funcionarios (substituido_por)
        Number countFeriasSubst = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM ferias_funcionarios WHERE substituido_por = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countFeriasSubst.longValue() > 0) return true;

        // Check avaliacoes_funcionarios (avaliador_id)
        Number countAvaliacoesAvaliador = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM avaliacoes_funcionarios WHERE avaliador_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countAvaliacoesAvaliador.longValue() > 0) return true;

        // Check treinamentos (instrutor_interno_id)
        Number countTreinamentos = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM treinamentos WHERE instrutor_interno_id = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countTreinamentos.longValue() > 0) return true;

        // Check logs_sistema (responsavel_resolucao)
        Number countLogs = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM logs_sistema WHERE responsavel_resolucao = :id")
                .setParameter("id", id)
                .getSingleResult();
        if (countLogs.longValue() > 0) return true;

        return false;
    }

    public FuncionarioResponse findByUsuarioId(Long empresaId, Long usuarioId) {
        return repository.findByEmpresaIdAndUsuarioId(empresaId, usuarioId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não vinculado a este usuário"));
    }

    @Transactional
    public FuncionarioResponse updateFotoPath(Long id, String fotoPath) {
        Funcionario entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado"));
        entity.setFotoFuncionarioUrl(fotoPath);
        entity = repository.save(entity);

        // Update the user profile avatar if a user is linked
        if (entity.getUsuarioId() != null) {
            String baseUrl = (fotoPath == null || fotoPath.isBlank()) ? null : "/api/v1/rh/funcionarios/" + id + "/foto?t=" + System.currentTimeMillis();
            usuarioPerfilHelper.updateAvatar(entity.getUsuarioId(), entity.getEmpresaId(), baseUrl);
        }

        return mapper.toResponse(entity);
    }
}
