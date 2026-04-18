package com.neritech.saas.fiscal.repository;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.fiscal.domain.ConfiguracaoNfe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoNfeRepository extends JpaRepository<ConfiguracaoNfe, Long> {
    Optional<ConfiguracaoNfe> findByAmbiente(Ambiente ambiente);
}
