package com.neritech.saas.gestaoUsuarios.mapper;

import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "funcoes", ignore = true)
    @Mapping(target = "cargo", source = "perfil.cargo")
    @Mapping(target = "departamento", source = "perfil.departamento")
    @Mapping(target = "telefone", source = "perfil.telefone")
    @Mapping(target = "avatarUrl", source = "perfil.avatarUrl")
    @Mapping(target = "preferencias", source = "perfil.preferencias")
    UsuarioResponse toResponse(Usuario usuario);

    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "perfil", ignore = true) // Handled in Service
    Usuario toEntity(UsuarioRequest request);
}
