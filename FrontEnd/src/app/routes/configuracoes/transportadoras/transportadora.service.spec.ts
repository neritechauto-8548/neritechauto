import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../../environments/environment';
import { TransportadoraService } from './transportadora.service';

describe('ADM-010 — integração Angular Transportadoras', () => {
  let api: TransportadoraService;
  let http: HttpTestingController;
  const url = `${environment.baseUrl}/v1/transportadoras`;
  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    api = TestBed.inject(TransportadoraService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  it('filters region/status and does not accept user-supplied tenant', () => {
    api.listar(1, 50, 'Expresso', 'PE', false).subscribe(x => expect(x.totalElements).toBe(0));
    const r = http.expectOne(x => x.url === url);
    expect(r.request.params.get('regiao')).toBe('PE');
    expect(r.request.params.get('ativo')).toBe('false');
    expect(r.request.params.get('size')).toBe('50');
    expect(r.request.params.has('empresaId')).toBeFalse();
    r.flush({ content: [], totalElements: 0, totalPages: 0, number: 1, size: 50 });
  });

  it('reuses a real supplier ID instead of creating a new fiscal identity', () => {
    api.criar({ fornecedorId: 42, tipoPessoa: 'JURIDICA', nomeFantasia: 'Transporte',
      cnpj: '11222333000181', modalidade: 'RODOVIARIO', ativo: true }).subscribe();
    const r = http.expectOne(url);
    expect(r.request.body.fornecedorId).toBe(42);
    expect(r.request.body.empresaId).toBeUndefined();
    r.flush({ id: 42, fornecedorId: 42 });
  });

  it('reports version conflicts with If-Match', () => {
    let code = 0;
    api.editar(21, { fornecedorId: 21, tipoPessoa: 'JURIDICA', nomeFantasia: 'Transporte',
      cnpj: '11222333000181', modalidade: 'OUTROS', ativo: true }, 8)
      .subscribe({ error: err => code = err.status });
    const r = http.expectOne(`${url}/21`);
    expect(r.request.headers.get('If-Match')).toBe('8');
    r.flush(null, { status: 409, statusText: 'Conflict' });
    expect(code).toBe(409);
  });

  it('keeps forbidden 403 when accessing purchase transport', () => {
    let code = 0;
    api.consultarPedido(98).subscribe({ error: err => code = err.status });
    http.expectOne(`${url}/pedidos/98`).flush(null, { status: 403, statusText: 'Forbidden' });
    expect(code).toBe(403);
  });

  it('links only to existing purchase with tracking document', () => {
    api.vincularPedido(12, 456, 'TRACK-REF').subscribe(x => expect(x.numeroNf).toBe('NF-123'));
    const r = http.expectOne(`${url}/12/pedidos/456`);
    expect(r.request.method).toBe('PUT');
    expect(r.request.body.documentoRastreio).toBe('TRACK-REF');
    r.flush({ pedidoId: 456, numeroNf: 'NF-123' });
  });

  it('inactivates carrier role without deleting purchase invoices', () => {
    api.inativar(12).subscribe();
    const r = http.expectOne(`${url}/12`);
    expect(r.request.method).toBe('DELETE');
    r.flush(null);
  });
});
