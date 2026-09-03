package com.neritech.saas.veiculo.service;

import com.neritech.saas.cliente.domain.Cliente;
import com.neritech.saas.cliente.repository.ClienteRepository;
import com.neritech.saas.common.exception.BusinessException;
import com.neritech.saas.common.tenancy.TenantContext;
import com.neritech.saas.veiculo.domain.Veiculo;
import com.neritech.saas.veiculo.domain.enums.StatusVeiculo;
import com.neritech.saas.veiculo.dto.ExternalVehicleDTO;
import com.neritech.saas.veiculo.dto.VeiculoRequest;
import com.neritech.saas.veiculo.dto.VeiculoResponse;
import com.neritech.saas.veiculo.mapper.VeiculoMapper;
import com.neritech.saas.veiculo.repository.AnoModeloRepository;
import com.neritech.saas.veiculo.repository.MarcaVeiculoRepository;
import com.neritech.saas.veiculo.repository.ModeloVeiculoRepository;
import com.neritech.saas.veiculo.repository.TipoCombustivelRepository;
import com.neritech.saas.veiculo.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock private VeiculoRepository repository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private MarcaVeiculoRepository marcaRepository;
    @Mock private ModeloVeiculoRepository modeloRepository;
    @Mock private AnoModeloRepository anoModeloRepository;
    @Mock private TipoCombustivelRepository tipoCombustivelRepository;
    @Mock private VehicleExternalLookupService externalLookupService;
    @Mock private VeiculoMapper mapper;

    private VeiculoService service;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(1L);
        service = new VeiculoService(
                repository,
                clienteRepository,
                marcaRepository,
                modeloRepository,
                anoModeloRepository,
                tipoCombustivelRepository,
                externalLookupService,
                mapper);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Placa deve ser normalizada de forma idêntica para persistência e pesquisa")
    void deveNormalizarPlaca() {
        assertThat(VeiculoService.normalizePlate(" abc-1d23 ")).isEqualTo("ABC1D23");
        assertThat(VeiculoService.normalizePlate("ABC1D23")).isEqualTo("ABC1D23");
    }

    @Test
    @DisplayName("Chassi deve ser normalizado antes da verificação de unicidade")
    void deveNormalizarChassi() {
        assertThat(VeiculoService.normalizeUpperIdentifier(" 9bw zzz377 vt004251 "))
                .isEqualTo("9BWZZZ377VT004251");
        assertThat(VeiculoService.normalizeUpperIdentifier("   ")).isNull();
    }

    @Test
    @DisplayName("Criação deve rejeitar cliente que não exista no tenant autenticado")
    void deveRejeitarClienteDeOutroTenant() {
        VeiculoRequest request = request("ABC1D23", 99L, 1000);
        when(clienteRepository.findByIdScoped(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");

        verify(repository, never()).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Criação deve persistir placa normalizada e tenant autenticado")
    void devePersistirPlacaNormalizadaETenant() {
        Cliente cliente = new Cliente();
        Veiculo entity = new Veiculo();
        VeiculoResponse expected = response(10L, "ABC1D23", StatusVeiculo.ATIVO, 1000);
        VeiculoRequest request = request("abc-1d23", 7L, 1000);

        when(clienteRepository.findByIdScoped(7L)).thenReturn(Optional.of(cliente));
        when(repository.findByEmpresaIdAndPlaca(1L, "ABC1D23")).thenReturn(Optional.empty());
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(expected);

        VeiculoResponse actual = service.create(request);

        assertThat(actual).isSameAs(expected);
        assertThat(entity.getEmpresaId()).isEqualTo(1L);
        assertThat(entity.getPlaca()).isEqualTo("ABC1D23");
        verify(repository).findByEmpresaIdAndPlaca(1L, "ABC1D23");
    }

    @Test
    @DisplayName("Criação deve rejeitar chassi duplicado somente dentro do tenant autenticado")
    void deveRejeitarChassiDuplicadoNoMesmoTenant() {
        Cliente cliente = new Cliente();
        Veiculo existing = new Veiculo();
        existing.setId(77L);
        existing.setEmpresaId(1L);
        existing.setChassi("9BWZZZ377VT004251");
        VeiculoRequest request = requestWithChassi(
                "ABC1D23",
                7L,
                1000,
                "9bw zzz377 vt004251");

        when(clienteRepository.findByIdScoped(7L)).thenReturn(Optional.of(cliente));
        when(repository.findByEmpresaIdAndPlaca(1L, "ABC1D23")).thenReturn(Optional.empty());
        when(repository.findByEmpresaIdAndChassi(1L, "9BWZZZ377VT004251"))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("chassi");

        verify(repository).findByEmpresaIdAndChassi(1L, "9BWZZZ377VT004251");
        verify(repository, never()).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Busca por ID deve usar id e tenant, sem fallback global")
    void findByIdDeveSerTenantScoped() {
        when(repository.findByIdAndEmpresaId(55L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(55L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository).findByIdAndEmpresaId(55L, 1L);
        verify(repository, never()).findById(55L);
    }

    @Test
    @DisplayName("Listagem sem contexto de tenant deve falhar fechada")
    void listagemSemTenantDeveFalharFechada() {
        TenantContext.clear();

        assertThatThrownBy(service::findAll)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("empresa autenticada");

        verify(repository, never()).findAll();
    }

    @Test
    @DisplayName("Regressão de odômetro não deve sobrescrever leitura confiável")
    void deveBloquearRegressaoDeOdometro() {
        Veiculo current = new Veiculo();
        current.setId(10L);
        current.setEmpresaId(1L);
        current.setPlaca("ABC1D23");
        current.setQuilometragemAtual(120_000);

        Cliente cliente = new Cliente();
        VeiculoRequest request = request("ABC1D23", 7L, 110_000);

        when(repository.findByIdAndEmpresaId(10L, 1L)).thenReturn(Optional.of(current));
        when(clienteRepository.findByIdScoped(7L)).thenReturn(Optional.of(cliente));
        when(repository.findByEmpresaIdAndPlaca(1L, "ABC1D23")).thenReturn(Optional.of(current));

        assertThatThrownBy(() -> service.update(10L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Regressão de odômetro");

        verify(mapper, never()).updateEntityFromRequest(any(), any());
        verify(repository, never()).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Inativação deve preservar registro e marcar status INATIVO")
    void deveInativarSemExcluirFisicamente() {
        Veiculo current = new Veiculo();
        current.setId(10L);
        current.setEmpresaId(1L);
        current.setStatus(StatusVeiculo.ATIVO);
        VeiculoResponse expected = response(10L, "ABC1D23", StatusVeiculo.INATIVO, 1000);

        when(repository.findByIdAndEmpresaId(10L, 1L)).thenReturn(Optional.of(current));
        when(repository.save(current)).thenReturn(current);
        when(mapper.toResponse(current)).thenReturn(expected);

        VeiculoResponse actual = service.deactivate(10L);

        assertThat(current.getStatus()).isEqualTo(StatusVeiculo.INATIVO);
        assertThat(actual.status()).isEqualTo(StatusVeiculo.INATIVO);
        verify(repository).save(current);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Tenant A não deve inativar veículo existente apenas no tenant B")
    void tenantANaoDeveInativarVeiculoDoTenantB() {
        when(repository.findByIdAndEmpresaId(88L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deactivate(88L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository, never()).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Consulta externa não deve devolver chassi, RENAVAM ou motor ao formulário")
    void enriquecimentoExternoDeveRedigirIdentificadoresSensiveis() {
        ExternalVehicleDTO external = new ExternalVehicleDTO(
                "abc-1d23",
                "FORD",
                "KA",
                "2018",
                "2019",
                "BRANCA",
                "CHASSI-SENSIVEL",
                "12345678901",
                "MOTOR-SENSIVEL",
                "FLEX",
                "RECIFE",
                "PE");

        when(externalLookupService.lookup("ABC1D23")).thenReturn(Optional.of(external));
        when(marcaRepository.findByNomeIgnoreCase("FORD")).thenReturn(Optional.empty());

        VeiculoResponse suggestion = service.lookupExternalByPlaca("abc-1d23").orElseThrow();

        assertThat(suggestion.placa()).isEqualTo("ABC1D23");
        assertThat(suggestion.chassi()).isNull();
        assertThat(suggestion.renavam()).isNull();
        assertThat(suggestion.numeroMotor()).isNull();
    }

    private VeiculoRequest request(String placa, Long clienteId, Integer kmAtual) {
        return requestWithChassi(placa, clienteId, kmAtual, null);
    }

    private VeiculoRequest requestWithChassi(String placa, Long clienteId, Integer kmAtual, String chassi) {
        return new VeiculoRequest(
                clienteId,
                null,
                null,
                null,
                null,
                placa,
                null,
                chassi,
                null,
                "BRANCA",
                kmAtual,
                kmAtual,
                null,
                null,
                null,
                StatusVeiculo.ATIVO,
                null);
    }

    private VeiculoResponse response(Long id, String placa, StatusVeiculo status, Integer kmAtual) {
        return new VeiculoResponse(
                id,
                7L,
                "Cliente",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                placa,
                null,
                null,
                null,
                "BRANCA",
                kmAtual,
                kmAtual,
                null,
                null,
                null,
                status,
                null);
    }
}
