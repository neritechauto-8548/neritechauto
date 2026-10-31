import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { EpiService } from './epi.service';

describe('EpiService - catálogo por empresa', () => {
  let service: EpiService;
  let http: HttpTestingController;
  beforeEach(() => {
    const storage = jasmine.createSpyObj<LocalStorageService>('LocalStorageService', ['get', 'has']);
    storage.has.and.callFake((key: string) => key === 'empresaId');
    storage.get.and.callFake((key: string) => key === 'empresaId' ? '12' : null);
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(), provideHttpClientTesting(),
      { provide: LocalStorageService, useValue: storage }
    ] });
    service = TestBed.inject(EpiService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());
  it('restringe consulta, criação e entrega à sessão da empresa', () => {
    service.listar('Luva', 0, 20).subscribe();
    const list = http.expectOne(r => r.url === `${environment.baseUrl}/v1/rh/epis`);
    expect(list.request.headers.get('X-Tenant-Id')).toBe('12');
    expect(list.request.params.get('q')).toBe('Luva');
    list.flush({ content: [], totalElements: 0, totalPages: 0 });

    service.criar({ nome: 'Luva de segurança' }).subscribe();
    const create = http.expectOne(`${environment.baseUrl}/v1/rh/epis`);
    expect(create.request.method).toBe('POST');
    expect(create.request.headers.get('X-Tenant-Id')).toBe('12');
    create.flush({ id: 4, nome: 'Luva de segurança', ativo: true, versao: 0 });

    service.entregar(7, { epiId: 4, dataEntrega: '2026-10-28' }).subscribe();
    const assign = http.expectOne(`${environment.baseUrl}/v1/rh/funcionarios/7/epis`);
    expect(assign.request.headers.get('X-Tenant-Id')).toBe('12');
    expect(assign.request.body.epiId).toBe(4);
    assign.flush({ id: 9, funcionarioId: 7, epiId: 4 });
  });
});
