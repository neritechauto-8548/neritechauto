package com.neritech.saas.ordemservico.repository;

import com.neritech.saas.ordemservico.domain.OSAdditionalRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OSAdditionalRequestItemRepository extends JpaRepository<OSAdditionalRequestItem, Long> {
    List<OSAdditionalRequestItem> findByAdditionalRequestIdOrderById(Long additionalRequestId);
    void deleteByAdditionalRequestId(Long additionalRequestId);
}
