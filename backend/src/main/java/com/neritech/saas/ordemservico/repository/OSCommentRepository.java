package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.OSComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OSCommentRepository extends JpaRepository<OSComment, Long> {
    List<OSComment> findByEmpresaIdAndOrdemServicoIdOrderByDataCadastroDescIdDesc(Long empresaId, Long ordemServicoId);
}
