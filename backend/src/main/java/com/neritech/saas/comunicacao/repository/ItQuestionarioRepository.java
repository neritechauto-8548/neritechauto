package com.neritech.saas.comunicacao.repository;

import com.neritech.saas.comunicacao.domain.ItQuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("comunicacaoItQuestionarioRepository")
public interface ItQuestionarioRepository extends JpaRepository<ItQuestionario, Long>, JpaSpecificationExecutor<ItQuestionario> {
    List<ItQuestionario> findByQuestionarioId(Long questionarioId);
    void deleteByQuestionarioId(Long questionarioId);
}
