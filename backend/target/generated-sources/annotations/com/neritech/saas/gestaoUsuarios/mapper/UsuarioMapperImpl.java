package com.neritech.saas.gestaoUsuarios.mapper;

import com.neritech.saas.gestaoUsuarios.domain.Usuario;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioRequest;
import com.neritech.saas.gestaoUsuarios.dto.UsuarioResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:22-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioResponse toResponse(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioResponse.UsuarioResponseBuilder usuarioResponse = UsuarioResponse.builder();

        if ( usuario.getAtivo() != null ) {
            usuarioResponse.ativo( usuario.getAtivo() );
        }
        if ( usuario.getBloqueado() != null ) {
            usuarioResponse.bloqueado( usuario.getBloqueado() );
        }
        usuarioResponse.email( usuario.getEmail() );
        usuarioResponse.empresaId( usuario.getEmpresaId() );
        usuarioResponse.id( usuario.getId() );
        usuarioResponse.nomeCompleto( usuario.getNomeCompleto() );
        usuarioResponse.ultimoAcesso( usuario.getUltimoAcesso() );

        return usuarioResponse.build();
    }

    @Override
    public Usuario toEntity(UsuarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Usuario.UsuarioBuilder<?, ?> usuario = Usuario.builder();

        usuario.ativo( request.isAtivo() );
        usuario.bloqueado( request.isBloqueado() );
        usuario.email( request.getEmail() );
        usuario.nomeCompleto( request.getNomeCompleto() );

        return usuario.build();
    }
}
