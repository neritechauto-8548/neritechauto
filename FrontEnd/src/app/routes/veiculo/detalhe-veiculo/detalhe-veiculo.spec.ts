import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';
import { of, throwError } from 'rxjs';

import { ClientesService } from '../../cliente/cliente/cliente.service';
import { StatusCliente, TipoCliente } from '../../cliente/models/cliente.models';
import { StatusVeiculo, VeiculoResponse } from '../models/veiculo.models';
import { VeiculoService } from '../veiculo/veiculo.service';
import { DetalheVeiculo } from './detalhe-veiculo';

describe('DetalheVeiculo', () => {
  let component: DetalheVeiculo;
  let router: jasmine.SpyObj<Router>;
  let vehicleService: jasmine.SpyObj<VeiculoService>;
  let customerService: jasmine.SpyObj<ClientesService>;
  let permissions: NgxPermissionsService;

  const vehicle: VeiculoResponse = {
    id: 9,
    clienteId: 42,
    clienteNome: 'Cliente Teste',
    placa: 'ABC1D23',
    marcaNome: 'Neri',
    modeloNome: 'One',
    anoFabricacao: 2025,
    anoModelo: 2026,
    quilometragemAtual: 12500,
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
      imports: [NgxPermissionsModule.forRoot()],
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

    permissions = TestBed.inject(NgxPermissionsService);
    permissions.loadPermissions(['VEICULO_EDITAR', 'OS_INCLUIR', 'GERAL_AGENDAMENTO_EDITAR']);
    component = TestBed.runInInjectionContext(() => new DetalheVeiculo());
  });

  afterEach(() => permissions.flushPermissions());

  it('loads only canonical vehicle and minimized current-customer sources', () => {
    component.ngOnInit();

    expect(vehicleService.getById).toHaveBeenCalledOnceWith(9);
    expect(customerService.getSummary).toHaveBeenCalledOnceWith(42);
    expect(component.vehicle).toEqual(vehicle);
    expect(component.currentCustomer?.maskedTaxId).toBe('***.***.***-00');
  });

  it('keeps the passport usable when the customer projection fails', () => {
    customerService.getSummary.and.returnValue(throwError(() => new Error('unavailable')));

    component.ngOnInit();

    expect(component.fatalError).toBeFalse();
    expect(component.customerFailed).toBeTrue();
    expect(component.vehicle).toEqual(vehicle);
  });

  it('preserves vehicle and customer context when starting an estimate', () => {
    component.ngOnInit();

    component.createEstimate();

    expect(router.navigate).toHaveBeenCalledOnceWith(
      ['/orcamentos/novo'],
      { queryParams: { clienteId: 42, veiculoId: 9 } }
    );
  });

  it('opens the canonical temporal-links view', () => {
    component.ngOnInit();

    component.openLinks();

    expect(router.navigate).toHaveBeenCalledOnceWith(['/veiculos', 9, 'vinculos']);
  });

  it('opens the canonical recommendations view', () => {
    component.ngOnInit();

    component.openRevisions();

    expect(router.navigate).toHaveBeenCalledOnceWith(['/veiculos', 9, 'revisoes']);
  });

  it('blocks incompatible operational CTAs for an inactive vehicle', () => {
    vehicleService.getById.and.returnValue(of({ ...vehicle, status: StatusVeiculo.INATIVO }));

    component.ngOnInit();

    expect(component.canCreateEstimate).toBeFalse();
    expect(component.canSchedule).toBeFalse();
    expect(component.alerts.some(alert => alert.title === 'Veículo inativo')).toBeTrue();
  });

  it('supports arrow-key navigation across tabs', () => {
    const event = {
      key: 'ArrowRight',
      preventDefault: jasmine.createSpy('preventDefault'),
      currentTarget: { parentElement: null },
    } as unknown as KeyboardEvent;

    component.onTabKeydown(event, 0);

    expect(component.activeTab).toBe('ficha');
    expect(event.preventDefault).toHaveBeenCalled();
  });
});
