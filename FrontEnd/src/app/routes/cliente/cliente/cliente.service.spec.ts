import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { TipoContato } from '../models/cliente.models';
import { ClientesService } from './cliente.service';

describe('ClientesService', () => {
  let service: ClientesService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(ClientesService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('lists customers without browser-controlled tenant parameters', () => {
    service.list({ page: 0, size: 10, status: 'ATIVO' }).subscribe();

    const request = httpTesting.expectOne(req => req.url === '/api/v1/clientes');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('page')).toBe('0');
    expect(request.request.params.get('status')).toBe('ATIVO');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();

    request.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 10 });
  });

  it('deactivates customer through logical lifecycle endpoint', () => {
    service.deactivate(15).subscribe();

    const request = httpTesting.expectOne('/api/v1/clientes/15/inativar');
    expect(request.request.method).toBe('PATCH');
    request.flush({ id: 15, status: 'INATIVO' });
  });

  it('reactivates customer through logical lifecycle endpoint', () => {
    service.reactivate(15).subscribe();

    const request = httpTesting.expectOne('/api/v1/clientes/15/reativar');
    expect(request.request.method).toBe('PATCH');
    request.flush({ id: 15, status: 'ATIVO' });
  });

  it('normalizes legacy contact value to the backend contato contract', () => {
    service.criarContato(15, {
      tipoContato: TipoContato.WHATSAPP,
      valor: '81999998888',
      principal: true,
    }).subscribe();

    const request = httpTesting.expectOne('/api/v1/clientes/15/contatos');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      tipoContato: TipoContato.WHATSAPP,
      contato: '81999998888',
      principal: true,
    });

    request.flush({
      id: 1,
      clienteId: 15,
      tipoContato: TipoContato.WHATSAPP,
      contato: '81999998888',
      principal: true,
    });
  });
});
