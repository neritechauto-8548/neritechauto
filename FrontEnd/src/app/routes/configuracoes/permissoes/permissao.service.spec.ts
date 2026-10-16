import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../../environments/environment';
import { PermissaoService } from './permissao.service';

describe('PermissaoService ADM-006', () => {
  let service: PermissaoService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(PermissaoService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  it('uses real paged routes and sends optimistic version with capability ids', () => {
    service.listFuncoes({ page: 0, size: 20, q: 'gerente', ativo: true }).subscribe();
    const list = http.expectOne(r => r.url === `${environment.baseUrl}/v1/funcoes`);
    expect(list.request.params.get('q')).toBe('gerente');
    expect(list.request.params.get('ativo')).toBe('true');
    list.flush({ content: [], totalPages: 0 });

    service.updateFuncao(9, { nome: 'Gerente', permissaoIds: [2, 4], versao: 3 }).subscribe();
    const update = http.expectOne(`${environment.baseUrl}/v1/funcoes/9`);
    expect(update.request.method).toBe('PUT');
    expect(update.request.body).toEqual(jasmine.objectContaining({ permissaoIds: [2, 4], versao: 3 }));
    update.flush({ id: 9 });
  });

  it('uses logical inactivation endpoint', () => {
    service.deleteFuncao(9).subscribe();
    const request = http.expectOne(`${environment.baseUrl}/v1/funcoes/9`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });
});
