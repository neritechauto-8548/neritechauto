import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';

import { StatusVeiculo } from '../../veiculo/models/veiculo.models';
import { StatusCliente, TipoCliente } from '../models/cliente.models';
import { CustomerDetailReadService } from './detalhe-cliente.service';
import { DetalheCliente } from './detalhe-cliente';

describe('DetalheCliente contextual actions', () => {
  let component: DetalheCliente;
  let router: jasmine.SpyObj<Router>;
  let permissions: NgxPermissionsService;

  beforeEach(() => {
    router = jasmine.createSpyObj<Router>('Router', ['navigate']);
    TestBed.configureTestingModule({
      imports: [NgxPermissionsModule.forRoot()],
      providers: [
        { provide: Router, useValue: router },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '42' } } } },
        {
          provide: CustomerDetailReadService,
          useValue: jasmine.createSpyObj<CustomerDetailReadService>('CustomerDetailReadService', [
            'getCustomer',
            'getContacts',
            'getAddresses',
            'getVehicles',
          ]),
        },
      ],
    });

    permissions = TestBed.inject(NgxPermissionsService);
    permissions.loadPermissions(['GERAL_USUARIO', 'OS_INCLUIR', 'GERAL_AGENDAMENTO_EDITAR']);
    component = TestBed.runInInjectionContext(() => new DetalheCliente());
    component.customer = {
      id: 42,
      displayName: 'Cliente Teste',
      type: TipoCliente.PESSOA_FISICA,
      status: StatusCliente.ATIVO,
      hasRelationshipNotes: false,
    };
  });

  afterEach(() => permissions.flushPermissions());

  it('opens a new estimate with the exact customer context', () => {
    component.createEstimate();

    expect(router.navigate).toHaveBeenCalledOnceWith(
      ['/orcamentos/novo'],
      { queryParams: { clienteId: 42 } }
    );
  });

  it('opens a new appointment with the exact customer context', () => {
    component.schedule();

    expect(router.navigate).toHaveBeenCalledOnceWith(
      ['/agenda/novo'],
      { queryParams: { clienteId: 42 } }
    );
  });

  it('opens a readable vehicle in its canonical passport', () => {
    component.openVehicle({
      id: 9,
      maskedPlate: 'ABC••23',
      status: StatusVeiculo.ATIVO,
    });

    expect(router.navigate).toHaveBeenCalledOnceWith(['/veiculos', 9]);
  });

  it('blocks creation actions for an inactive customer', () => {
    component.customer = {
      ...component.customer!,
      status: StatusCliente.INATIVO,
    };

    component.createEstimate();
    component.schedule();

    expect(router.navigate).not.toHaveBeenCalled();
  });
});
