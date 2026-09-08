package com.neritech.saas.orcamento.service;

import com.neritech.saas.orcamento.domain.OrcamentoLineItem;
import com.neritech.saas.orcamento.domain.OrcamentoServiceGroup;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrcamentoCommercialCalculationServiceTest {

    private final OrcamentoCommercialCalculationService service =
            new OrcamentoCommercialCalculationService();

    @Test
    void weightedDistributionClosesEveryCentWithoutResidual() {
        OrcamentoServiceGroup group = group("100.00", OrcamentoServiceGroup.PackageDistributionMethod.WEIGHTED);
        List<OrcamentoLineItem> lines = List.of(
                line(1L, OrcamentoLineItem.LineType.PART, "1.00"),
                line(2L, OrcamentoLineItem.LineType.PART, "1.00"),
                line(3L, OrcamentoLineItem.LineType.LABOR, "1.00"));

        service.distributePackage(group, lines);

        assertThat(lines).extracting(OrcamentoLineItem::getAllocatedPackageAmount)
                .containsExactly(
                        new BigDecimal("33.33"),
                        new BigDecimal("33.34"),
                        new BigDecimal("33.33"));
        assertThat(lines.stream()
                .map(OrcamentoLineItem::getAllocatedPackageAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add))
                .isEqualByComparingTo("100.00");
        assertThat(group.getPackageAdjustmentAmount()).isEqualByComparingTo("97.00");
    }

    @Test
    void laborFirstConsumesLaborBucketBeforeAllocatingParts() {
        OrcamentoServiceGroup group = group("80.00", OrcamentoServiceGroup.PackageDistributionMethod.LABOR_FIRST);
        OrcamentoLineItem labor = line(1L, OrcamentoLineItem.LineType.LABOR, "100.00");
        OrcamentoLineItem part = line(2L, OrcamentoLineItem.LineType.PART, "200.00");

        service.distributePackage(group, List.of(labor, part));

        assertThat(labor.getAllocatedPackageAmount()).isEqualByComparingTo("80.00");
        assertThat(part.getAllocatedPackageAmount()).isEqualByComparingTo("0.00");
        assertThat(labor.getAllocatedPackageAmount().add(part.getAllocatedPackageAmount()))
                .isEqualByComparingTo("80.00");
    }

    @Test
    void fixedDiscountCannotExceedGrossLineValue() {
        OrcamentoLineItem line = line(1L, OrcamentoLineItem.LineType.PART, "50.00");
        line.setDiscountType(OrcamentoLineItem.DiscountType.FIXED);
        line.setDiscountValue(new BigDecimal("50.01"));

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.recalculateLine(line))
                .hasMessageContaining("nao pode superar");
    }

    private OrcamentoServiceGroup group(
            String packagePrice,
            OrcamentoServiceGroup.PackageDistributionMethod method) {
        OrcamentoServiceGroup group = new OrcamentoServiceGroup();
        group.setPackagePrice(new BigDecimal(packagePrice));
        group.setPackageDistributionMethod(method);
        return group;
    }

    private OrcamentoLineItem line(Long id, OrcamentoLineItem.LineType type, String total) {
        OrcamentoLineItem line = new OrcamentoLineItem();
        line.setId(id);
        line.setLineType(type);
        line.setQuantity(BigDecimal.ONE);
        line.setUnitPrice(new BigDecimal(total));
        line.setGrossAmount(new BigDecimal(total));
        line.setDiscountType(OrcamentoLineItem.DiscountType.NONE);
        line.setDiscountValue(BigDecimal.ZERO);
        line.setDiscountAmount(BigDecimal.ZERO);
        line.setTotalAmount(new BigDecimal(total));
        return line;
    }
}

