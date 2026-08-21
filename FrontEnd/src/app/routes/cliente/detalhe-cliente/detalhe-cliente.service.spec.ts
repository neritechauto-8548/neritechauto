import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { CustomerDetailReadService } from './detalhe-cliente.service';

describe('CustomerDetailReadService', () => {
  let service: CustomerDetailReadService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(CustomerDetailReadService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads customer identity only from the minimized summary endpoint', () => {
    service.getCustomer(42).subscribe();

    const request = http.expectOne('/api/v1/clientes/42/resumo');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.has('empresaId')).toBeFalse();
    expect(request.request.params.has('tenantId')).toBeFalse();
    request.flush({
      id: 42,
      displayName: 'Cliente Teste',
      type: 'PESSOA_FISICA',
      status: 'ATIVO',
      maskedTaxId: '***.***.***-00',
      maskedEmail: 'c***@e***.com',
      hasRelationshipNotes: false,
    });
  });

  it('loads contacts and addresses only from minimized summary endpoints', () => {
    service.getContacts(42).subscribe();
    service.getAddresses(42).subscribe();

    const contacts = http.expectOne('/api/v1/clientes/42/contatos/resumo');
    const addresses = http.expectOne('/api/v1/clientes/42/enderecos/resumo');

    expect(contacts.request.method).toBe('GET');
    expect(addresses.request.method).toBe('GET');

    contacts.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 20 });
    addresses.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 20 });
  });

  it('loads vehicles from the minimized endpoint without tenant authority', () => {
    service.getVehicles(42).subscribe();

    const request = http.expectOne(req => req.url === '/api/v1/veiculos/resumo');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('clienteId')).toBe('42');
    expect(request.request.params.has('empresaId')).toBeFalse();
    expect(request.request.params.has('tenantId')).toBeFalse();
    request.flush([]);
  });
});
