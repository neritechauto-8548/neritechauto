import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { CargoService } from './cargo.service';

describe('CargoService ADM-005', () => {
  let service: CargoService;
  let http: HttpTestingController;

  beforeEach(() => {
    const storage = jasmine.createSpyObj<LocalStorageService>('LocalStorageService', ['get', 'has']);
    storage.has.and.callFake((key: string) => key === 'empresaId');
    storage.get.and.returnValue('12');
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(), provideHttpClientTesting(),
      { provide: LocalStorageService, useValue: storage }
    ] });
    service = TestBed.inject(CargoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('applies tenant filters and keeps version on update', () => {
    service.list({ page: 0, size: 20, q: 'Mecânico', ativo: true }).subscribe();
    const list = http.expectOne(r => r.url === `${environment.baseUrl}/v1/rh/cargos`);
    expect(list.request.params.get('empresaId')).toBe('12');
    expect(list.request.params.get('q')).toBe('Mecânico');
    expect(list.request.headers.get('X-Tenant-Id')).toBe('12');
    list.flush({ content: [], totalElements: 0 });

    service.atualizar(7, { nome: 'Mecânico', temComissao: false, versao: 3 }).subscribe();
    const update = http.expectOne(`${environment.baseUrl}/v1/rh/cargos/7`);
    expect(update.request.method).toBe('PUT');
    expect(update.request.body).toEqual(jasmine.objectContaining({ empresaId: 12, versao: 3 }));
    update.flush({ id: 7 });
  });

  it('uses the inactivation endpoint without deleting tenant context', () => {
    service.excluir(7).subscribe();
    const request = http.expectOne(`${environment.baseUrl}/v1/rh/cargos/7`);
    expect(request.request.method).toBe('DELETE');
    expect(request.request.headers.get('X-Tenant-Id')).toBe('12');
    request.flush(null);
  });
});
