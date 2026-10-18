import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { ServicoService } from './servico.service';

describe('ServicoService (OFC-008)', () => {
  let service: ServicoService;
  let http: HttpTestingController;
  const storage = jasmine.createSpyObj<LocalStorageService>('LocalStorageService', ['has', 'get']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServicoService, { provide: LocalStorageService, useValue: storage }],
    });
    service = TestBed.inject(ServicoService);
    http = TestBed.inject(HttpTestingController);
    storage.has.and.returnValue(true);
    storage.get.and.returnValue('42');
  });

  afterEach(() => http.verify());

  it('envia tenant autenticado e filtros de busca, área e status', () => {
    service.list({ search: 'ALIN', areaId: 7, ativo: false, page: 0, size: 20 }).subscribe();
    const req = http.expectOne(r => r.url === `${environment.baseUrl}/v1/servicos`);
    expect(req.request.headers.get('X-Tenant-Id')).toBe('42');
    expect(req.request.params.get('empresaId')).toBe('42');
    expect(req.request.params.get('search')).toBe('ALIN');
    expect(req.request.params.get('areaId')).toBe('7');
    expect(req.request.params.get('ativo')).toBe('false');
    req.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 20 });
  });

  it('não assume empresa 1 quando o tenant está ausente', () => {
    storage.has.and.returnValue(false);
    expect(() => service.list()).toThrowError('Contexto da empresa não encontrado.');
  });
});
