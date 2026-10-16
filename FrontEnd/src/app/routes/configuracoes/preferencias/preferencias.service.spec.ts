import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../../environments/environment';
import { PreferenciasService } from './preferencias.service';

describe('ADM-011 — políticas gerais', () => {
  let api: PreferenciasService;
  let http: HttpTestingController;
  const root = `${environment.baseUrl}/v1/politicas-gerais`;
  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    api = TestBed.inject(PreferenciasService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  it('busca políticas do tenant autenticado sem aceitar empresaId do navegador', () => {
    api.listar('locale', 'Empresa', true, 0, 20).subscribe(data =>
      expect(data.totalElements).toBe(1));
    const req = http.expectOne(x => x.url === root);
    expect(req.request.params.get('busca')).toBe('locale');
    expect(req.request.params.get('secao')).toBe('Empresa');
    expect(req.request.params.get('ativo')).toBe('true');
    expect(req.request.params.has('empresaId')).toBeFalse();
    req.flush({ content: [{}], totalElements: 1, number: 0, size: 20, totalPages: 1 });
  });

  it('persiste políticas no servidor sem sessão local ou mock', () => {
    api.criar('locale', 'pt-BR').subscribe(x => expect(x.valorAplicado).toBe('pt-BR'));
    const req = http.expectOne(root);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ chave: 'locale', valor: 'pt-BR' });
    req.flush({ chave: 'locale', valorAplicado: 'pt-BR' });
  });

  it('edita com If-Match e não confunde conflito 409 com sucesso', () => {
    let conflict = 0;
    api.editar('precisaoQuantidades', '3', 5).subscribe({
      error: e => conflict = e.status
    });
    const req = http.expectOne(`${root}/precisaoQuantidades`);
    expect(req.request.headers.get('If-Match')).toBe('5');
    req.flush({ message: 'conflito' }, { status: 409, statusText: 'Conflict' });
    expect(conflict).toBe(409);
  });

  it('inativa mantendo valor histórico no servidor', () => {
    api.status('avisosAtivos', false, 2).subscribe(x => expect(x.ativo).toBeFalse());
    const req = http.expectOne(`${root}/avisosAtivos/status`);
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({ ativo: false });
    expect(req.request.headers.get('If-Match')).toBe('2');
    req.flush({ chave: 'avisosAtivos', ativo: false });
  });

  it('preserva 403 em histórico de política de empresa não autorizada', () => {
    let forbidden = 0;
    api.historico('locale').subscribe({ error: e => forbidden = e.status });
    http.expectOne(`${root}/locale/historico?page=0&size=20`)
      .flush({}, { status: 403, statusText: 'Forbidden' });
    expect(forbidden).toBe(403);
  });
});
