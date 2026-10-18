package com.neritech.saas.empresa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.empresa.domain.DepartamentoContabio;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.DepartamentoContabioResponse;
import com.neritech.saas.empresa.mapper.DepartamentoContabioMapper;
import com.neritech.saas.empresa.repository.DepartamentoContabioRepository;
import com.neritech.saas.empresa.repository.EmpresaRepository;
import com.neritech.saas.empresa.service.DepartamentoContabioService;
import com.neritech.saas.gestaoUsuarios.repository.UsuarioRepository;
import com.neritech.saas.rh.repository.CargoRepository;
import com.neritech.saas.rh.repository.FuncionarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartamentoContabioServiceTest {

    @Mock private DepartamentoContabioRepository repository;
    @Mock private EmpresaRepository empresaRepository;
    @Mock private DepartamentoContabioMapper mapper;
    @Mock private FuncionarioRepository funcionarioRepository;
    @Mock private CargoRepository cargoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private JdbcTemplate jdbc;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks
    private DepartamentoContabioService service;

    private static final Long EMPRESA_ID = 12L;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(EMPRESA_ID);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("findAll com size=1000 e sem filtros executa Specification corretamente")
    void findAllComSize1000SemFiltros() {
        Pageable pageable = PageRequest.of(0, 1000);
        Empresa empresa = new Empresa();
        empresa.setId(EMPRESA_ID);
        empresa.setNomeFantasia("Oficina Modelo");

        DepartamentoContabio depto = new DepartamentoContabio();
        depto.setId(1L);
        depto.setEmpresa(empresa);
        depto.setDescricao("Mecânica Geral");
        depto.setCodigo("MEC-01");

        Page<DepartamentoContabio> page = new PageImpl<>(List.of(depto), pageable, 1);
        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(funcionarioRepository.countByEmpresaIdAndDepartamentoId(EMPRESA_ID, 1L)).thenReturn(3L);
        when(cargoRepository.countByEmpresaIdAndAreaId(EMPRESA_ID, 1L)).thenReturn(2L);

        DepartamentoContabioResponse responseMock = new DepartamentoContabioResponse(
                1L, EMPRESA_ID, "Oficina Modelo", "Mecânica Geral", "MEC-01",
                null, null, null, true, 0, null, 3L, 2L, 0L, null, null, 1
        );
        when(mapper.toResponse(depto)).thenReturn(responseMock);

        Page<DepartamentoContabioResponse> result = service.findAll(null, null, null, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).descricao()).isEqualTo("Mecânica Geral");
        assertThat(result.getContent().get(0).empresaId()).isEqualTo(EMPRESA_ID);

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("findAll com paginação não permitida deve lançar ResponseStatusException 422")
    void findAllPaginacaoInvalida() {
        Pageable pageableInvalido = PageRequest.of(0, 15);

        assertThatThrownBy(() -> service.findAll(null, null, null, pageableInvalido))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Paginação inválida");

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("findByEmpresaId retorna lista mapeada")
    void findByEmpresaIdRetornaLista() {
        Empresa empresa = new Empresa();
        empresa.setId(EMPRESA_ID);

        DepartamentoContabio depto = new DepartamentoContabio();
        depto.setId(2L);
        depto.setEmpresa(empresa);
        depto.setDescricao("Funilaria e Pintura");
        depto.setCodigo("FUN-01");

        when(repository.findByEmpresaId(EMPRESA_ID)).thenReturn(List.of(depto));
        when(funcionarioRepository.countByEmpresaIdAndDepartamentoId(EMPRESA_ID, 2L)).thenReturn(1L);
        when(cargoRepository.countByEmpresaIdAndAreaId(EMPRESA_ID, 2L)).thenReturn(1L);

        DepartamentoContabioResponse responseMock = new DepartamentoContabioResponse(
                2L, EMPRESA_ID, null, "Funilaria e Pintura", "FUN-01",
                null, null, null, true, 0, null, 1L, 1L, 0L, null, null, 1
        );
        when(mapper.toResponse(depto)).thenReturn(responseMock);

        List<DepartamentoContabioResponse> result = service.findByEmpresaId(EMPRESA_ID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).descricao()).isEqualTo("Funilaria e Pintura");
    }
}
