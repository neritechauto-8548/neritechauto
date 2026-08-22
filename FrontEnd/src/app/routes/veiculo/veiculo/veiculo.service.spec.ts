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
});
