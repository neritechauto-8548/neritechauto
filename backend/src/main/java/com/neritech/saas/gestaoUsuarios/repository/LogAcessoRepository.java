package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.gestaoUsuarios.domain.LogAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogAcessoRepository extends JpaRepository<LogAcesso, Long> {
    List<LogAcesso> findByUsuarioIdOrderByDataEventoDesc(Long usuarioId);
    
    List<LogAcesso> findByEmpresaIdOrderByDataEventoDesc(Long empresaId);
}
