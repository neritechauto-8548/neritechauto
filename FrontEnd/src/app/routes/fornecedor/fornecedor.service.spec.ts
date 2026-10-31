import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../environments/environment';
import { FornecedorService } from './fornecedor.service';
import { PedidoFornecedorService } from './pedido-fornecedor.service';

describe('ADM-008 — FornecedorService: isolamento e versão', () => {
  let service: FornecedorService;
  let pedidos: PedidoFornecedorService;
  let http: HttpTestingController;
  const stored: any = { value: 23 };

  beforeEach(() => {
    stored.value = 23;
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: LocalStorageService, useValue: {
          has: () => stored.value !== undefined,
          get: () => stored.value
        } }
      ]
    });
    service = TestBed.inject(FornecedorService);
    pedidos = TestBed.inject(PedidoFornecedorService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads contact records from the scoped authenticated supplier API', () => {
    service.contatos(77).subscribe(list => expect(list.length).toBe(1));
    const req = http.expectOne(`${environment.baseUrl}/v1/fornecedores/referencia/77/contatos`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('X-Tenant-Id')).toBe('23');
    req.flush([{ id: 1, nome: 'Atendimento', sexo: 'MASCULINO', contatoPrincipal: true, meios: [] }]);
  });

  it('never accepts an arbitrary empresaId on CSV supplier import', () => {
    service.importar([{
      tipoPessoa: 'JURIDICA', cpf: '', cnpj: '11222333000181',
      razaoSocial: 'Fornecedor', nomeFantasia: '', email: '', classificacao: 'OUTROS'
    }]).subscribe();
    const req = http.expectOne(`${environment.baseUrl}/v1/fornecedores/referencia/importar`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('X-Tenant-Id')).toBe('23');
    expect(req.request.body[0].empresaId).toBeUndefined();
    req.flush({ importados: 1 });
  });

  it('keeps forbidden accesses and validation failures as errors for contacts', () => {
    let status = 0;
    service.criarContato(5, {
      nome: 'Atendimento', sexo: 'MASCULINO', contatoPrincipal: false, meios: []
    }).subscribe({ error: e => status = e.status });
    const req = http.expectOne(`${environment.baseUrl}/v1/fornecedores/referencia/5/contatos`);
    req.flush({ message: 'Sem permissão' }, { status: 403, statusText: 'Forbidden' });
    expect(status).toBe(403);
  });

  it('never defaults missing tenant to company 1', () => {
    stored.value = undefined;
    let message = '';
    service.list().subscribe({ error: error => message = error.message });
    expect(message).toContain('Empresa autenticada');
    http.expectNone(`${environment.baseUrl}/v1/fornecedores`);
  });

  it('always scopes supplier list requests to the active session', () => {
    stored.value = { empresaId: 41 };
    service.list({ page: 0, size: 20, ativo: false, empresaId: 99 }).subscribe();
    const req = http.expectOne(r => r.url === `${environment.baseUrl}/v1/fornecedores`);
    expect(req.request.params.get('empresaId')).toBe('41');
    expect(req.request.params.get('ativo')).toBe('false');
    expect(req.request.headers.get('X-Tenant-Id')).toBe('41');
    req.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 20 });
  });

  it('overrides client supplied tenant and sends optimistic lock version', () => {
    service.update(11, {
      nomeFantasia: 'Oficina de peças', tipoPessoa: 'JURIDICA',
      cnpj: '11222333000181', versao: 8, empresaId: 99
    } as any).subscribe();
    const req = http.expectOne(`${environment.baseUrl}/v1/fornecedores/11`);
    expect(req.request.body.empresaId).toBe(23);
    expect(req.request.headers.get('If-Match')).toBe('8');
    req.flush({ id: 11, empresaId: 23, nomeFantasia: 'Oficina de peças', versao: 9 });
  });

  it('never mutates a supplier when no authenticated tenant is known', () => {
    stored.value = 'invalid';
    let error = '';
    service.create({ nomeFantasia: 'Supplier' }).subscribe({ error: e => error = e.message });
    expect(error).toContain('Empresa autenticada');
    http.expectNone(`${environment.baseUrl}/v1/fornecedores`);
  });
  it('never creates purchase orders for a missing tenant', () => {
    stored.value = null;
    let message = '';
    pedidos.create({ empresaId: 1, fornecedorId: 10, responsavel: 'Buyer' } as any)
      .subscribe({ error: e => message = e.message });
    expect(message).toContain('Empresa autenticada');
    http.expectNone(`${environment.baseUrl}/v1/pedidos-fornecedor`);
  });

  it('replaces purchase request tenant with the authenticated company', () => {
    stored.value = { tenantId: 41 };
    pedidos.create({ empresaId: 1, fornecedorId: 10, responsavel: 'Buyer' } as any)
      .subscribe();
    const req = http.expectOne(`${environment.baseUrl}/v1/pedidos-fornecedor`);
    expect(req.request.body.empresaId).toBe(41);
    expect(req.request.headers.get('X-Tenant-Id')).toBe('41');
    req.flush({ id: 55, empresaId: 41, fornecedorId: 10 });
  });

});
