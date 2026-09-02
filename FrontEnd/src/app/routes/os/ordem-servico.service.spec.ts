import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from '@core';

import { TipoOS } from './models/os.models';
import { OrdemServicoService } from './ordem-servico.service';

describe('OrdemServicoService tenancy contract', () => {
  let service: OrdemServicoService;
  let http: HttpTestingController;
  const auth = {
    snapshot: jasmine.createSpy('snapshot'),
  };

  beforeEach(() => {
    auth.snapshot.calls.reset();
    auth.snapshot.and.returnValue({ empresaId: 42 });

    TestBed.configureTestingModule({
      providers: [
        OrdemServicoService,
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: AuthService, useValue: auth },
      ],
    });

    service = TestBed.inject(OrdemServicoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('deriva a listagem da empresa autenticada, sem localStorage ou tenant default', () => {
    service.list({ page: 0, size: 25, tipo: 'SERVICO' }).subscribe();

    const request = http.expectOne(req => req.url.endsWith('/v1/ordens-servico/empresa/42'));
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('page')).toBe('0');
    expect(request.request.params.get('size')).toBe('25');
    expect(request.request.params.get('tipo')).toBe('SERVICO');

    request.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 25 });
  });

  it('sobrescreve qualquer contexto de tenant no create com a empresa autenticada', () => {
    service.create({
      numeroOS: 'OS-100',
      tipoOS: TipoOS.MANUTENCAO,
      valorTotal: 0,
    }).subscribe();

    const request = http.expectOne(req => req.url.endsWith('/v1/ordens-servico'));
    expect(request.request.method).toBe('POST');
    expect(request.request.body.empresaId).toBe(42);
    expect(request.request.body.numeroOS).toBe('OS-100');

    request.flush({ id: 100, empresaId: 42, numeroOS: 'OS-100', tipoOS: TipoOS.MANUTENCAO, valorTotal: 0 });
  });

  it('falha fechado quando a sessão não possui empresa válida', () => {
    auth.snapshot.and.returnValue({ empresaId: null });

    expect(() => service.list()).toThrowError(/Empresa autenticada não disponível/);
    http.expectNone(() => true);
  });
});
