package com.neritech.saas.gestaoUsuarios.mapper;

import com.neritech.saas.TestDataBuilder;
import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = Mappers.getMapper(UsuarioMapper.class);

    @Test
    @DisplayName("Deve mapear Entity para Response")
    void deveMapearEntityParaResponse() {
        // Arrange
        Usuario usuario = TestDataBuilder.umUsuario().build();

        // Act
        UsuarioResponse response = mapper.toResponse(usuario);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(usuario.getId());
        assertThat(response.getEmail()).isEqualTo(usuario.getEmail());
        assertThat(response.getNomeCompleto()).isEqualTo(usuario.getNomeCompleto());
    }

    @Test
    @DisplayName("Deve mapear Request para Entity")
    void deveMapearRequestParaEntity() {
        // Arrange
        UsuarioRequest request = TestDataBuilder.umUsuarioRequest().build();

        // Act
        Usuario entity = mapper.toEntity(request);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getEmail()).isEqualTo(request.getEmail());
        assertThat(entity.getNomeCompleto()).isEqualTo(request.getNomeCompleto());
        assertThat(entity.getAtivo()).isEqualTo(request.isAtivo());
    }
}
