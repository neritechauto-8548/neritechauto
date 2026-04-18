package com.neritech.saas.integracao.repository;

import com.neritech.saas.integracao.domain.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebhookRepository extends JpaRepository<Webhook, Long> {
    List<Webhook> findByAtivoTrue();

    List<Webhook> findByEvento(String evento);
}
