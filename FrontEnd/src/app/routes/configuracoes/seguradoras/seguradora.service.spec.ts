import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../../environments/environment';
import { SeguradoraService } from './seguradora.service';

describe('ADM-009 — SeguradoraService', () => {
  let service: SeguradoraService;
  let http: HttpTestingController;
  const base = `${environment.baseUrl}/v1/seguradoras`;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(SeguradoraService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  it('lists insurer records with tenant enforced by backend, page size and filters', () => {
    service.listar(1, 50, 'ABC', false).subscribe(res => expect(res.totalElements).toBe(0));
    const req = http.expectOne(r => r.url === base);
    expect(req.request.params.get('page')).toBe('1');
    expect(req.request.params.get('size')).toBe('50');
    expect(req.request.params.get('ativo')).toBe('false');
    expect(req.request.params.get('busca')).toBe('ABC');
    expect(req.request.params.has('empresaId')).toBeFalse();
    req.flush({ content: [], totalElements: 0, totalPages: 0, number: 1, size: 50 });
  });

  it('sends optimistic concurrency version for update', () => {
    const body = { nomeFantasia: 'Seguradora Teste', ativo: true } as any;
    service.editar(10, body, 8).subscribe(res => expect(res.versao).toBe(9));
    const req = http.expectOne(`${base}/10`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.headers.get('If-Match')).toBe('8');
    req.flush({ id: 10, versao: 9 });
  });

  it('does not report success for conflicts and forbidden operations', () => {
    let code = 0;
    service.criar({ nomeFantasia: 'Teste', ativo: true } as any).subscribe({
      error: err => code = err.status
    });
    const req = http.expectOne(base);
    req.flush({ message: 'conflito' }, { status: 409, statusText: 'Conflict' });
    expect(code).toBe(409);
  });

  it('links only existing OS endpoint without fictional insurer APIs', () => {
    service.vincular(15, 90, 'CONVENIO').subscribe(v => expect(v.ordemServicoId).toBe(90));
    const req = http.expectOne(`${base}/15/os/90?tipo=CONVENIO`);
    expect(req.request.method).toBe('POST');
    req.flush({ seguradoraId: 15, seguradora: 'Teste', ordemServicoId: 90, tipoVinculo: 'CONVENIO' });
  });

  it('inactivates without deleting OS links directly', () => {
    service.inativar(13).subscribe();
    const req = http.expectOne(`${base}/13`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
