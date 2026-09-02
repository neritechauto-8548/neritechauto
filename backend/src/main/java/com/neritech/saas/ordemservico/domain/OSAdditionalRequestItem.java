package com.neritech.saas.ordemservico.domain;

import com.neritech.saas.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "os_additional_request_items")
public class OSAdditionalRequestItem extends BaseEntity {

    public enum Operation { ADD, UPDATE, REMOVE }
    public enum ItemType { SERVICE, PRODUCT, OTHER }
    public enum Decision { PENDING, APPROVED, REJECTED }

    @Column(name = "additional_request_id", nullable = false)
    private Long additionalRequestId;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false, length = 12)
    private Operation operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false, length = 16)
    private ItemType itemType;

    @Column(name = "source_item_id")
    private Long sourceItemId;

    @Column(name = "catalog_item_id")
    private Long catalogItemId;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity = BigDecimal.ONE;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "amount_delta", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountDelta = BigDecimal.ZERO;

    @Column(name = "time_delta_minutes", nullable = false)
    private Integer timeDeltaMinutes = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 16)
    private Decision decision = Decision.PENDING;

    @Column(name = "decision_comment", length = 500)
    private String decisionComment;
}
