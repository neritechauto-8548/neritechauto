package com.neritech.saas.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaDTO {
    private String plano;
    private String status;
    private String precoFormatado;
    private String proximaCobranca;
    private String stripeCustomerId;
    private String stripeSubscriptionId;
    private String stripeProductId;
    private boolean trial;
    private String inicioTrial;
    private String fimTrial;
}
