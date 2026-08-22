import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { ClientesService } from '../../cliente/cliente/cliente.service';
import { StatusCliente, TipoCliente } from '../../cliente/models/cliente.models';
import { StatusVeiculo, VeiculoResponse } from '../models/veiculo.models';
import { VeiculoService } from '../veiculo/veiculo.service';
import { VinculosVeiculo } from './vinculos-veiculo';

describe('VinculosVeiculo', () => {
  let component: VinculosVeiculo;
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

    component = TestBed.runInInjectionContext(() => new VinculosVeiculo());
  });

  it('uses only the canonical vehicle and minimized customer read models', () => {
    component.ngOnInit();

    expect(vehicleService.getById).toHaveBeenCalledOnceWith(9);
    expect(customerService.getSummary).toHaveBeenCalledOnceWith(42);
    expect(component.currentCustomer?.maskedTaxId).toBe('***.***.***-00');
  });

  it('keeps the ledger usable when the customer summary fails', () => {
    customerService.getSummary.and.returnValue(throwError(() => new Error('unavailable')));

    component.ngOnInit();

    expect(component.fatalError).toBeFalse();
    expect(component.customerFailed).toBeTrue();
    expect(component.hasCurrentLink).toBeTrue();
  });

  it('does not infer a historical link when the vehicle has no current customer', () => {
    vehicleService.getById.and.returnValue(of({
      ...vehicle,
      clienteId: 0,
      clienteNome: undefined,
    }));

    component.ngOnInit();

    expect(component.hasCurrentLink).toBeFalse();
    expect(customerService.getSummary).not.toHaveBeenCalled();
  });

  it('returns to the canonical passport route', () => {
    component.ngOnInit();

    component.backToPassport();

    expect(router.navigate).toHaveBeenCalledOnceWith(['/veiculos', 9]);
  });

  it('never exposes an unbacked link-mutation method', () => {
    expect('changeLink' in component).toBeFalse();
    expect('closeLink' in component).toBeFalse();
  });
});
