import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { DashboardService } from './dashboard.service';

describe('DashboardService', () => {
  let service: DashboardService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(DashboardService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('requests dashboard without tenant or company query parameters', () => {
    service.getDashboardData().subscribe();

    const request = httpTesting.expectOne('/api/dashboard');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();

    request.flush({
      totalClientes: 0,
      osAbertas: 0,
      osEmAndamento: 0,
      osConcluidas: 0,
      osCanceladas: 0,
      faturamentoMes: 0,
      despesasMes: 0,
      lucroMes: 0,
      ticketMedio: 0,
      contasReceber: 0,
      contasPagar: 0,
      valoresVencidos: 0,
      veiculosEmAtraso: 0,
      historicoFaturamento: [],
      historicoServicos: [],
      historicoMeses: [],
      abertosMes: 0,
      abertosTotal: 0,
      autorizadosMes: 0,
      autorizadosTotal: 0,
      canceladosMes: 0,
      canceladosTotal: 0,
      fechadosMes: 0,
      fechadosTotal: 0,
      entradasVeiculosMes: 0,
      saidasVeiculosMes: 0,
    });
  });
});
