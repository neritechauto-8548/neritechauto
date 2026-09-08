import { Location } from '@angular/common';
import { NgForm } from '@angular/forms';
import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';
import { MessageService } from 'primeng/api';
import { Subject, of } from 'rxjs';

import { ClientesService } from '../../cliente/cliente/cliente.service';
import { StatusCliente } from '../../cliente/models/cliente.models';
import { StatusVeiculo, VeiculoResponse } from '../models/veiculo.models';
import { VeiculoService } from '../veiculo/veiculo.service';
import { CadastroVeiculo } from './cadastro-veiculo';

describe('CadastroVeiculo', () => {
  let component: CadastroVeiculo;
  let router: jasmine.SpyObj<Router>;
  let vehicleService: jasmine.SpyObj<VeiculoService>;
  let confirmation: jasmine.SpyObj<ConfirmationService>;
  let permissions: NgxPermissionsService;

  const response: VeiculoResponse = {
    id: 9,
    clienteId: 42,
    clienteNome: 'Cliente Teste',
    placa: 'ABC1D23',
    quilometragemAtual: 1000,
    status: StatusVeiculo.ATIVO,
  };

  beforeEach(() => {
    router = jasmine.createSpyObj<Router>('Router', ['navigate']);
    vehicleService = jasmine.createSpyObj<VeiculoService>('VeiculoService', [
      'create',
      'update',
      'deactivate',
      'reactivate',
    ]);
    confirmation = jasmine.createSpyObj<ConfirmationService>('ConfirmationService', ['confirm']);

    TestBed.configureTestingModule({
      imports: [NgxPermissionsModule.forRoot()],
      providers: [
        MessageService,
        { provide: Router, useValue: router },
        { provide: Location, useValue: jasmine.createSpyObj<Location>('Location', ['back']) },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { queryParamMap: convertToParamMap({}) },
            paramMap: of(convertToParamMap({})),
            queryParamMap: of(convertToParamMap({})),
          },
        },
        { provide: VeiculoService, useValue: vehicleService },
        { provide: ClientesService, useValue: jasmine.createSpyObj<ClientesService>('ClientesService', ['list']) },
        { provide: ConfirmationService, useValue: confirmation },
      ],
    });

    permissions = TestBed.inject(NgxPermissionsService);
    permissions.loadPermissions(['VEICULO_CRIAR', 'VEICULO_EDITAR', 'VEICULO_EXCLUIR', 'OS_INCLUIR']);
    component = TestBed.runInInjectionContext(() => new CadastroVeiculo());
    component.selectedCliente = {
      id: 42,
      nome: 'Cliente Teste',
      cpfCnpj: '***.***.***-00',
      status: StatusCliente.ATIVO,
    };
    component.form = {
      clienteId: 42,
      placa: 'abc-1d23',
      quilometragemAtual: 1000,
      status: StatusVeiculo.ATIVO,
    };
  });

  afterEach(() => permissions.flushPermissions());

  it('navigates to an estimate only after persistence succeeds', () => {
    const request = new Subject<VeiculoResponse>();
    vehicleService.create.and.returnValue(request);

    component.salvar('budget');
    expect(router.navigate).not.toHaveBeenCalled();

    request.next(response);
    request.complete();

    expect(router.navigate).toHaveBeenCalledOnceWith(
      ['/orcamentos/novo'],
      { queryParams: { clienteId: 42, veiculoId: 9 } }
    );
  });

  it('blocks odometer regression until an auditable correction contract exists', () => {
    component.id = 9;
    component['originalOdometer'] = 1500;
    component.form.quilometragemAtual = 1000;

    component.salvar();

    expect(vehicleService.update).not.toHaveBeenCalled();
    expect(component.validationErrors.join(' ')).toContain('regressão do odômetro');
  });

  it('uses the logical deactivation endpoint and preserves history semantics', () => {
    component.id = 9;
    confirmation.confirm.and.returnValue(of(true));
    vehicleService.deactivate.and.returnValue(of({ ...response, status: StatusVeiculo.INATIVO }));

    component.inativarVeiculo();

    expect(vehicleService.deactivate).toHaveBeenCalledOnceWith(9);
    expect(component.form.status).toBe(StatusVeiculo.INATIVO);
  });

  it('keeps inactivation behind the explicit auditable lifecycle action', () => {
    const hasInactiveStatus = () =>
      component.statusOptions.some(option => option.value === StatusVeiculo.INATIVO);

    expect(hasInactiveStatus()).toBeFalse();

    component.form.status = StatusVeiculo.INATIVO;

    expect(hasInactiveStatus()).toBeTrue();
  });

  it('asks before leaving a dirty form', () => {
    component.vehicleForm = { dirty: true } as NgForm;
    confirmation.confirm.and.returnValue(of(false));

    const decision = component.canLeave();

    expect(typeof decision).not.toBe('boolean');
    expect(confirmation.confirm).toHaveBeenCalled();
  });
});
