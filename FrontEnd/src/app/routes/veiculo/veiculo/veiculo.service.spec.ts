import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { VeiculoService } from './veiculo.service';

describe('VeiculoService', () => {
  let service: VeiculoService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(VeiculoService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTesting.verify());

  it('lists vehicles without browser-controlled tenant parameters', () => {
    service.list().subscribe();

    const request = httpTesting.expectOne('/api/v1/veiculos');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    request.flush([]);
  });

  it('uses only the customer relationship filter accepted by the backend', () => {
    service.list(42).subscribe();

    const request = httpTesting.expectOne(req => req.url === '/api/v1/veiculos');
    expect(request.request.params.get('clienteId')).toBe('42');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    request.flush([]);
  });

  it('deactivates through the explicit logical lifecycle endpoint', () => {
    service.deactivate(9).subscribe();

    const request = httpTesting.expectOne('/api/v1/veiculos/9/inativar');
    expect(request.request.method).toBe('PATCH');
    expect(request.request.body).toEqual({});
    request.flush({ id: 9, clienteId: 42, placa: 'ABC1D23', status: 'INATIVO' });
  });

  it('reactivates without browser-controlled tenant parameters', () => {
    service.reactivate(9).subscribe();

    const request = httpTesting.expectOne('/api/v1/veiculos/9/reativar');
    expect(request.request.method).toBe('PATCH');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    request.flush({ id: 9, clienteId: 42, placa: 'ABC1D23', status: 'ATIVO' });
  });
});
