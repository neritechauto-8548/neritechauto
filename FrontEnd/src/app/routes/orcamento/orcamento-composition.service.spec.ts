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

  it('filters versioned kits without exposing tenant authority', () => {
    service.searchCatalog('revisao', 'KIT').subscribe();

    const request = http.expectOne(req => req.url === '/api/v1/orcamentos/catalog');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('q')).toBe('revisao');
    expect(request.request.params.get('type')).toBe('KIT');
    expect(request.request.params.has('tenantId')).toBeFalse();
    request.flush({ query: 'revisao', items: [], truncated: false });
  });

  it('instantiates a kit with explicit idempotency and no browser-owned price', () => {
    service
      .instantiateKit(91, 44, 'kit-request-123', {
        expectedRevision: 8,
        quantity: 1,
        targetPosition: 2,
      })
      .subscribe();

    const request = http.expectOne('/api/v1/orcamentos/91/kits/44');
    expect(request.request.method).toBe('POST');
    expect(request.request.headers.get('Idempotency-Key')).toBe('kit-request-123');
    expect(request.request.body).toEqual({ expectedRevision: 8, quantity: 1, targetPosition: 2 });
    expect(request.request.body.unitPrice).toBeUndefined();
    expect(request.request.body.tenantId).toBeUndefined();
    request.flush({ groups: [] });
  });

  it('updates group metadata with revision and no browser-controlled tenant', () => {
    service
      .updateGroup(91, 15, {
        expectedRevision: 4,
        title: 'Freios',
        customerDescription: 'Revisão completa',
        internalNote: 'Somente equipe',
        recommended: false,
        visibility: 'CUSTOMER_VISIBLE',
      })
      .subscribe();

    const request = http.expectOne('/api/v1/orcamentos/91/composition/groups/15');
    expect(request.request.method).toBe('PUT');
    expect(request.request.body.expectedRevision).toBe(4);
    expect(request.request.body.tenantId).toBeUndefined();
    expect(request.request.body.empresaId).toBeUndefined();
    request.flush({ groups: [] });
  });

  it('reorders groups by complete ids and explicit revision', () => {
    service.reorderGroups(91, 4, [18, 15]).subscribe();

    const request = http.expectOne('/api/v1/orcamentos/91/composition/groups/reorder');
    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toEqual({ expectedRevision: 4, orderedIds: [18, 15] });
    request.flush({ groups: [] });
  });

  it('updates only item quantity while price remains server-owned', () => {
    service.updateLine(91, 15, 22, 4, 2.5).subscribe();

    const request = http.expectOne('/api/v1/orcamentos/91/composition/groups/15/items/22');
    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toEqual({ expectedRevision: 4, quantity: 2.5 });
    expect(request.request.body.unitPrice).toBeUndefined();
    expect(request.request.body.discountAmount).toBeUndefined();
    request.flush({ groups: [] });
  });

  it('sends revision as a query parameter when deleting an item', () => {
    service.deleteLine(91, 15, 22, 4).subscribe();

    const request = http.expectOne(req => req.url.endsWith('/groups/15/items/22'));
    expect(request.request.method).toBe('DELETE');
    expect(request.request.params.get('expectedRevision')).toBe('4');
    expect(request.request.params.has('tenantId')).toBeFalse();
    request.flush({ groups: [] });
  });
});
