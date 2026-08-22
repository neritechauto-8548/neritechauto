import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { OrcamentoCompositionService } from './orcamento-composition.service';

describe('OrcamentoCompositionService', () => {
  let service: OrcamentoCompositionService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(OrcamentoCompositionService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads composition without browser-controlled tenant authority', () => {
    service.get(91).subscribe();

    const request = http.expectOne('/api/v1/orcamentos/91/composition');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    request.flush({ groups: [] });
  });

  it('sends revision and catalog reference but never price or tenant on item mutation', () => {
    service
      .addCatalogItem(91, 15, {
        expectedRevision: 4,
        lineType: 'PART',
        catalogItemId: 22,
        quantity: 2,
      })
      .subscribe();

    const request = http.expectOne('/api/v1/orcamentos/91/composition/groups/15/items');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      expectedRevision: 4,
      lineType: 'PART',
      catalogItemId: 22,
      quantity: 2,
    });
    expect(request.request.body.price).toBeUndefined();
    expect(request.request.body.tenantId).toBeUndefined();
    expect(request.request.body.empresaId).toBeUndefined();
    request.flush({ groups: [] });
  });

  it('searches the canonical catalog with a minimized query only', () => {
    service.searchCatalog('filtro').subscribe();

    const request = http.expectOne(req => req.url === '/api/v1/orcamentos/catalog');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('q')).toBe('filtro');
    expect(request.request.params.has('tenantId')).toBeFalse();
    request.flush({ query: 'filtro', items: [], truncated: false });
  });
});
