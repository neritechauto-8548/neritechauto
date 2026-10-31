import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { ComissaoFinanceiraService } from './comissao-financeira.service';

describe('ComissaoFinanceiraService - empresa e pagamento', () => {
  let service: ComissaoFinanceiraService;
  let http: HttpTestingController;
  beforeEach(() => {
    const storage = jasmine.createSpyObj<LocalStorageService>('LocalStorageService', ['get', 'has']);
    storage.has.and.callFake((key: string) => key === 'empresaId');
    storage.get.and.callFake((key: string) => key === 'empresaId' ? '12' : null);
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(), provideHttpClientTesting(),
      { provide: LocalStorageService, useValue: storage }
    ] });
    service = TestBed.inject(ComissaoFinanceiraService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());
  it('aplica tenant na consulta e em todas as transições, sem gerar contas a pagar', () => {
    service.listar({ nome: 'Maria', paga: false }, 0, 20).subscribe();
    const search = http.expectOne(r => r.url === `${environment.baseUrl}/v1/financeiro/comissoes`);
    expect(search.request.params.get('empresaId')).toBe('12');
    expect(search.request.params.get('paga')).toBe('false');
    expect(search.request.params.get('nome')).toBe('Maria');
    expect(search.request.headers.get('X-Tenant-Id')).toBe('12');
    search.flush({ content: [], totalElements: 0, totalPages: 0 });

    service.aprovar(8).subscribe();
    const approve = http.expectOne(r => r.url.endsWith('/comissoes/8/aprovar'));
    expect(approve.request.params.get('empresaId')).toBe('12');
    approve.flush({ id: 8, aprovado: true });

    service.quitar(8, '2026-10-31').subscribe();
    const pay = http.expectOne(r => r.url.endsWith('/comissoes/8/quitar'));
    expect(pay.request.body.gerarContaPagar).toBe(false);
    expect(pay.request.params.get('empresaId')).toBe('12');
    pay.flush({ id: 8, pago: true });

    service.estornar(8).subscribe();
    const reverse = http.expectOne(r => r.url.endsWith('/comissoes/8/estornar'));
    expect(reverse.request.params.get('empresaId')).toBe('12');
    reverse.flush({ id: 8, pago: false });
  });
});
