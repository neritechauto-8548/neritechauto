import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { OrcamentoDraftService } from './orcamento-draft.service';

describe('OrcamentoDraftService', () => {
  let service: OrcamentoDraftService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(OrcamentoDraftService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('creates a draft without browser-controlled tenant or commercial number', () => {
    service.create({
      clienteId: 15,
      veiculoId: 8,
      quilometragemEntrada: 32000,
      relatoCliente: 'Ruído ao frear',
    }).subscribe();

    const request = http.expectOne('/api/v1/orcamentos');
    expect(request.request.method).toBe('POST');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    expect(request.request.body['tenantId']).toBeUndefined();
    expect(request.request.body['empresaId']).toBeUndefined();
    expect(request.request.body['numeroOS']).toBeUndefined();
    expect(request.request.body['numeroOrcamento']).toBeUndefined();
    expect(request.request.body).toEqual({
      clienteId: 15,
      veiculoId: 8,
      quilometragemEntrada: 32000,
      relatoCliente: 'Ruído ao frear',
    });

    request.flush({
      id: 99,
      numeroOrcamento: 'ORC-20260821-ABC123',
      status: 'RASCUNHO',
      clienteId: 15,
      veiculoId: 8,
      criadoEm: '2026-08-21T14:30:00',
    });
  });
});
