import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { FuncionarioService } from './funcionario.service';

describe('FuncionarioService ADM-004', () => {
  let service: FuncionarioService;
  let http: HttpTestingController;

  beforeEach(() => {
    const storage = jasmine.createSpyObj<LocalStorageService>('LocalStorageService', ['get', 'has']);
    storage.has.and.callFake((key: string) => key === 'empresaId');
    storage.get.and.callFake((key: string) => key === 'empresaId' ? '12' : null);
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(), provideHttpClientTesting(),
      { provide: LocalStorageService, useValue: storage }
    ] });
    service = TestBed.inject(FuncionarioService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('sends tenant-scoped filters and uses soft-delete endpoint', () => {
    service.list({ page: 0, size: 20, nomeCompleto: 'Ana', status: 'ATIVO' }).subscribe();
    const list = http.expectOne(r => r.url === `${environment.baseUrl}/v1/rh/funcionarios`);
    expect(list.request.params.get('empresaId')).toBe('12');
    expect(list.request.params.get('nomeCompleto')).toBe('Ana');
    expect(list.request.params.get('status')).toBe('ATIVO');
    list.flush({ content: [], totalElements: 0 });

    service.delete(7).subscribe();
    const deactivate = http.expectOne(`${environment.baseUrl}/v1/rh/funcionarios/7`);
    expect(deactivate.request.method).toBe('DELETE');
    deactivate.flush(null);
  });
});
