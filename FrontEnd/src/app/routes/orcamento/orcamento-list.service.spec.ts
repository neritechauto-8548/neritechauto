import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { OrcamentoListService } from './orcamento-list.service';

describe('OrcamentoListService', () => {
  let service: OrcamentoListService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(OrcamentoListService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('lists budgets without browser-controlled tenant authority', () => {
    service.list({ q: 'ORC-001', status: 'RASCUNHO', page: 1, size: 25, sort: 'updatedAt,desc' }).subscribe();

    const request = http.expectOne(req => req.url === '/api/v1/orcamentos');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('q')).toBe('ORC-001');
    expect(request.request.params.get('status')).toBe('RASCUNHO');
    expect(request.request.params.get('page')).toBe('1');
    expect(request.request.params.get('size')).toBe('25');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    request.flush({
      items: [],
      page: 1,
      size: 25,
      totalElements: 0,
      totalPages: 0,
      summaryAvailable: false,
      summaryUnavailableReason: 'INDICADORES_AGREGADOS_NAO_DISPONIVEIS',
    });
  });

  it('loads a minimized canonical budget by id without tenant parameters', () => {
    service.getById(91).subscribe();

    const request = http.expectOne('/api/v1/orcamentos/91');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    request.flush({
      id: 91,
      numero: 'ORC-001',
      versaoAtual: 0,
      cliente: null,
      veiculo: null,
      status: 'RASCUNHO',
      total: { currency: 'BRL', amount: 0 },
      validadeEm: null,
      responsavelId: null,
      comunicacaoStatus: null,
      proximaAcao: 'CONTINUAR_EDICAO',
      criadoEm: null,
      atualizadoEm: null,
      allowedActions: ['OPEN'],
    });
  });
});
