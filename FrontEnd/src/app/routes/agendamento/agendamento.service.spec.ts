import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { AgendamentoRequest, AgendamentoService } from './agendamento.service';

describe('AgendamentoService', () => {
  let service: AgendamentoService;
  let http: HttpTestingController;

  const payload: AgendamentoRequest = {
    clienteId: 42,
    veiculoId: 8,
    dataAgendamento: '2026-08-25',
    horaInicio: '09:00:00',
    horaFim: '10:00:00',
    duracaoEstimadaMinutos: 60,
    status: 'AGENDADO',
    canalAgendamento: 'PRESENCIAL',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AgendamentoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('creates an appointment without browser-controlled tenant authority', () => {
    service.create(payload).subscribe();

    const request = http.expectOne('/api/v1/agendamentos');
    expect(request.request.method).toBe('POST');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    expect(request.request.body['tenantId']).toBeUndefined();
    expect(request.request.body['empresaId']).toBeUndefined();
    expect(request.request.body).toEqual(payload);
    request.flush({ id: 10 });
  });

  it('updates an appointment without adding tenant authority', () => {
    service.update(10, payload).subscribe();

    const request = http.expectOne('/api/v1/agendamentos/10');
    expect(request.request.method).toBe('PUT');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    expect(request.request.body['tenantId']).toBeUndefined();
    expect(request.request.body['empresaId']).toBeUndefined();
    request.flush({ id: 10 });
  });

  it('loads only minimized vehicles for the selected customer', () => {
    service.listVehiclesForCustomer(42).subscribe();

    const request = http.expectOne(req => req.url === '/api/v1/veiculos/resumo');
    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('clienteId')).toBe('42');
    expect(request.request.params.has('tenantId')).toBeFalse();
    expect(request.request.params.has('empresaId')).toBeFalse();
    request.flush([
      {
        id: 8,
        marcaNome: 'Volkswagen',
        modeloNome: 'Polo',
        maskedPlate: 'ABC••23',
        status: 'ATIVO',
      },
    ]);
  });
});
