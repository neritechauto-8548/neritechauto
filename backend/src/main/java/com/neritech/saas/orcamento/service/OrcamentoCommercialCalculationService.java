package com.neritech.saas.orcamento.service;

import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.orcamento.domain.OrcamentoLineItem;
import com.neritech.saas.orcamento.domain.OrcamentoServiceGroup;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class OrcamentoCommercialCalculationService {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    public void recalculateLine(OrcamentoLineItem line) {
        BigDecimal gross = money(line.getQuantity().multiply(line.getUnitPrice()));
        BigDecimal discount = switch (line.getDiscountType()) {
            case NONE -> ZERO;
            case FIXED -> money(line.getDiscountValue());
            case PERCENT -> money(gross.multiply(line.getDiscountValue())
                    .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
        };
        if (discount.compareTo(gross) > 0) {
            throw new BusinessException("O desconto nao pode superar o valor bruto da linha.");
        }
        line.setGrossAmount(gross);
        line.setDiscountAmount(discount);
        line.setTotalAmount(money(gross.subtract(discount)));
    }

    public void distributePackage(OrcamentoServiceGroup group, List<OrcamentoLineItem> lines) {
        if (group.getPackagePrice() == null) {
            group.setPackageOriginalSubtotal(null);
            group.setPackageAdjustmentAmount(null);
            for (OrcamentoLineItem line : lines) {
                line.setAllocatedPackageAmount(null);
                line.setPackageAdjustmentAmount(ZERO);
            }
            return;
        }

        List<OrcamentoLineItem> billable = lines.stream()
                .filter(line -> line.getLineType() != OrcamentoLineItem.LineType.NOTE)
                .toList();
        BigDecimal original = billable.stream()
                .map(OrcamentoLineItem::getTotalAmount)
                .reduce(ZERO, BigDecimal::add);
        BigDecimal target = money(group.getPackagePrice());
        if (billable.isEmpty() && target.signum() > 0) {
            throw new BusinessException("Preco fechado exige ao menos uma linha faturavel no grupo.");
        }
        if (original.signum() == 0 && target.signum() > 0) {
            throw new BusinessException("Preco fechado nao pode ser distribuido sobre linhas sem valor.");
        }

        billable.forEach(line -> line.setAllocatedPackageAmount(ZERO));
        switch (group.getPackageDistributionMethod()) {
            case WEIGHTED, POLICY -> allocate(target, billable, OrcamentoLineItem::getTotalAmount);
            case LABOR_FIRST -> allocateLaborFirst(target, billable);
        }
        for (OrcamentoLineItem line : lines) {
            BigDecimal allocated = line.getAllocatedPackageAmount();
            if (allocated == null) {
                line.setPackageAdjustmentAmount(ZERO);
            } else {
                line.setPackageAdjustmentAmount(money(allocated.subtract(line.getTotalAmount())));
            }
        }
        group.setPackageOriginalSubtotal(money(original));
        group.setPackageAdjustmentAmount(money(target.subtract(original)));
    }

    public BigDecimal effectiveAmount(OrcamentoServiceGroup group, OrcamentoLineItem line) {
        if (group.getPackagePrice() != null && line.getAllocatedPackageAmount() != null) {
            return money(line.getAllocatedPackageAmount());
        }
        return money(line.getTotalAmount());
    }

    public BigDecimal equivalentDiscountPercent(OrcamentoLineItem line) {
        if (line.getGrossAmount() == null || line.getGrossAmount().signum() == 0) return BigDecimal.ZERO.setScale(4);
        return line.getDiscountAmount()
                .multiply(new BigDecimal("100"))
                .divide(line.getGrossAmount(), 4, RoundingMode.HALF_UP);
    }

    private void allocateLaborFirst(BigDecimal target, List<OrcamentoLineItem> lines) {
        List<OrcamentoLineItem> labor = lines.stream()
                .filter(line -> line.getLineType() == OrcamentoLineItem.LineType.LABOR)
                .toList();
        List<OrcamentoLineItem> others = lines.stream()
                .filter(line -> line.getLineType() != OrcamentoLineItem.LineType.LABOR)
                .toList();
        if (labor.isEmpty() || others.isEmpty()) {
            allocate(target, lines, OrcamentoLineItem::getTotalAmount);
            return;
        }

        BigDecimal laborBase = labor.stream()
                .map(OrcamentoLineItem::getTotalAmount)
                .reduce(ZERO, BigDecimal::add);
        BigDecimal laborTarget = target.min(laborBase);
        allocate(laborTarget, labor, OrcamentoLineItem::getTotalAmount);
        allocate(target.subtract(laborTarget), others, OrcamentoLineItem::getTotalAmount);
    }

    private void allocate(
            BigDecimal target,
            List<OrcamentoLineItem> source,
            Function<OrcamentoLineItem, BigDecimal> weight) {
        if (source.isEmpty()) return;
        List<OrcamentoLineItem> positive = source.stream()
                .filter(line -> weight.apply(line).signum() > 0)
                .toList();
        List<OrcamentoLineItem> recipients = positive.isEmpty() ? new ArrayList<>(source) : positive;
        BigDecimal remainingTarget = money(target);
        BigDecimal remainingWeight = recipients.stream()
                .map(weight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (int index = 0; index < recipients.size(); index++) {
            OrcamentoLineItem line = recipients.get(index);
            BigDecimal amount;
            if (index == recipients.size() - 1) {
                amount = remainingTarget;
            } else if (remainingWeight.signum() == 0) {
                amount = remainingTarget.divide(
                        BigDecimal.valueOf(recipients.size() - index), 2, RoundingMode.DOWN);
            } else {
                amount = remainingTarget.multiply(weight.apply(line))
                        .divide(remainingWeight, 2, RoundingMode.HALF_UP)
                        .max(ZERO)
                        .min(remainingTarget);
            }
            line.setAllocatedPackageAmount(money(line.getAllocatedPackageAmount().add(amount)));
            remainingTarget = money(remainingTarget.subtract(amount));
            remainingWeight = remainingWeight.subtract(weight.apply(line)).max(BigDecimal.ZERO);
        }
    }

    private BigDecimal money(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }
}

