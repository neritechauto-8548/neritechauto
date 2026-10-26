package com.neritech.saas.empresa.service;

import com.neritech.saas.empresa.domain.PlanoAssinatura;
import com.neritech.saas.empresa.mapper.PlanoAssinaturaMapper;
import com.neritech.saas.empresa.repository.PlanoAssinaturaRepository;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

class PlanoAssinaturaPublicTest {
    @Test void publicCatalogExcludesAdministrativePlan() {
        PlanoAssinaturaRepository repository = mock(PlanoAssinaturaRepository.class);
        PlanoAssinaturaMapper mapper = mock(PlanoAssinaturaMapper.class);
        PlanoAssinaturaService service = new PlanoAssinaturaService(repository, mapper);
        PlanoAssinatura pro = new PlanoAssinatura(); pro.setNivel(1);
        PlanoAssinatura ultra = new PlanoAssinatura(); ultra.setNivel(2);
        PlanoAssinatura admin = new PlanoAssinatura(); admin.setNivel(3);
        when(repository.findByAtivoTrue()).thenReturn(List.of(admin, ultra, pro));

        assertThat(service.findPublicPlans()).hasSize(2);
        ArgumentCaptor<PlanoAssinatura> sent = ArgumentCaptor.forClass(PlanoAssinatura.class);
        verify(mapper, times(2)).toResponse(sent.capture());
        assertThat(sent.getAllValues()).extracting(PlanoAssinatura::getNivel).containsExactly(1, 2);
    }
}
