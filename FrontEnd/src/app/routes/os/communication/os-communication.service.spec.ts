import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { OsCommunicationService } from './os-communication.service';

describe('OsCommunicationService contract', () => {
  let service: OsCommunicationService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [OsCommunicationService, provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(OsCommunicationService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('envia usando o cadastro do cliente sem tenant no navegador', () => {
    service.sendEmail(42).subscribe();
    const request = http.expectOne(req => req.url.endsWith('/v1/ordens-servico/42/enviar-email'));
    expect(request.request.method).toBe('POST');
    expect(request.request.params.keys()).toEqual([]);
    expect(request.request.body).toBeNull();
    request.flush(null);
  });

  it('envia apenas emailDestino quando o operador informa endereço alternativo', () => {
    service.sendEmail(42, 'cliente@exemplo.com').subscribe();
    const request = http.expectOne(req => req.url.endsWith('/v1/ordens-servico/42/enviar-email'));
    expect(request.request.params.get('emailDestino')).toBe('cliente@exemplo.com');
    expect(request.request.params.has('empresaId')).toBeFalse();
    expect(request.request.params.has('tenantId')).toBeFalse();
    request.flush(null);
  });
});
