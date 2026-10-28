package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.ItQuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository("comunicacaoItQuestionarioRepository")
public interface ItQuestionarioRepository extends JpaRepository<ItQuestionario, Long>, JpaSpecificationExecutor<ItQuestionario> {
    List<ItQuestionario> findByQuestionarioId(Long questionarioId);
    Page<ItQuestionario> findByQuestionarioEmpresaId(Long empresaId, Pageable pageable);
    void deleteByQuestionarioId(Long questionarioId);
    boolean existsByQuestionarioIdAndDsItQuestionarioIgnoreCase(Long questionarioId, String dsItQuestionario);
    boolean existsByQuestionarioIdAndDsItQuestionarioIgnoreCaseAndIdNot(Long questionarioId, String dsItQuestionario, Long id);
}
