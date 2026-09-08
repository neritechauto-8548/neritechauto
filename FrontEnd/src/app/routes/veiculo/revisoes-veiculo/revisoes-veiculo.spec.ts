import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { ClientesService } from '../../cliente/cliente/cliente.service';
import { StatusCliente, TipoCliente } from '../../cliente/models/cliente.models';
import { StatusVeiculo, VeiculoResponse } from '../models/veiculo.models';
import { VeiculoService } from '../veiculo/veiculo.service';
import { RevisoesVeiculo } from './revisoes-veiculo';

describe('RevisoesVeiculo', () => {
  let component: RevisoesVeiculo;
  let router: jasmine.SpyObj<Router>;
  let vehicleService: jasmine.SpyObj<VeiculoService>;
  let customerService: jasmine.SpyObj<ClientesService>;

  const vehicle: VeiculoResponse = {
    id: 9,
    clienteId: 42,
    clienteNome: 'Cliente Teste',
    placa: 'ABC1D23',
    marcaNome: 'Neri',
    modeloNome: 'One',
    quilometragemAtual: 12500,
    proximaRevisaoKm: 20000,
    proximaRevisaoData: '2026-12-10',
    status: StatusVeiculo.ATIVO,
  };

  beforeEach(() => {
    router = jasmine.createSpyObj<Router>('Router', ['navigate']);
    vehicleService = jasmine.createSpyObj<VeiculoService>('VeiculoService', ['getById']);
    customerService = jasmine.createSpyObj<ClientesService>('ClientesService', ['getSummary']);
    vehicleService.getById.and.returnValue(of(vehicle));
    customerService.getSummary.and.returnValue(of({
      id: 42,
      displayName: 'Cliente Teste',
      type: TipoCliente.PESSOA_FISICA,
      status: StatusCliente.ATIVO,
      maskedTaxId: '***.***.***-00',
      hasRelationshipNotes: false,
    }));

    TestBed.configureTestingModule({
      providers: [
        { provide: Router, useValue: router },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ id: 9 }) } },
        },
        { provide: VeiculoService, useValue: vehicleService },
        { provide: ClientesService, useValue: customerService },
      ],
    });

    component = TestBed.runInInjectionContext(() => new RevisoesVeiculo());
  });

  it('loads only canonical vehicle and minimized customer sources', () => {
    component.ngOnInit();

    expect(vehicleService.getById).toHaveBeenCalledOnceWith(9);
    expect(customerService.getSummary).toHaveBeenCalledOnceWith(42);
    expect(component.hasDateParameter).toBeTrue();
    expect(component.hasOdometerParameter).toBeTrue();
  });

  it('does not calculate recommendation counts from registration parameters', () => {
    component.ngOnInit();

    expect(component.attentionCards.every(card => !('count' in card))).toBeTrue();
    expect('recommendations' in component).toBeFalse();
  });

  it('keeps the screen usable when the current customer summary fails', () => {
    customerService.getSummary.and.returnValue(throwError(() => new Error('unavailable')));

    component.ngOnInit();

    expect(component.fatalError).toBeFalse();
    expect(component.customerFailed).toBeTrue();
    expect(component.vehicle).toEqual(vehicle);
  });

  it('does not expose conversions without recommendation lineage', () => {
    expect('createEstimate' in component).toBeFalse();
    expect('schedule' in component).toBeFalse();
    expect('dismiss' in component).toBeFalse();
  });

  it('returns to the canonical passport route', () => {
    component.ngOnInit();

    component.backToPassport();

    expect(router.navigate).toHaveBeenCalledOnceWith(['/veiculos', 9]);
  });
});
