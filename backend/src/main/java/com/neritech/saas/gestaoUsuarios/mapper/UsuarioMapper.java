package com.neritech.saas.gestaoUsuarios.mapper;

import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "funcoes", ignore = true) // Handled manually or via helper
    UsuarioResponse toResponse(Usuario usuario);

    @Mapping(target = "senha", ignore = true)
    Usuario toEntity(UsuarioRequest request);
}
