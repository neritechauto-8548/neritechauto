import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';
import { MessageService } from 'primeng/api';

import { StatusVeiculo, VeiculoResponse } from '../models/veiculo.models';
import { VeiculoService } from './veiculo.service';
import { Veiculo } from './veiculo';

describe('Veiculo contextual actions', () => {
  let component: Veiculo;
  let router: jasmine.SpyObj<Router>;
  let permissions: NgxPermissionsService;

  const vehicle: VeiculoResponse = {
    id: 9,
    clienteId: 42,
    clienteNome: 'Cliente Teste',
    placa: 'ABC1D23',
    status: StatusVeiculo.ATIVO,
  };

  beforeEach(() => {
    router = jasmine.createSpyObj<Router>('Router', ['navigate']);
    TestBed.configureTestingModule({
      imports: [NgxPermissionsModule.forRoot()],
      providers: [
        MessageService,
        { provide: Router, useValue: router },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParamMap: { get: () => null } } },
        },
        { provide: VeiculoService, useValue: jasmine.createSpyObj<VeiculoService>('VeiculoService', ['list']) },
      ],
    });

    permissions = TestBed.inject(NgxPermissionsService);
    permissions.loadPermissions(['VEICULO_EDITAR', 'OS_INCLUIR', 'GERAL_AGENDAMENTO_EDITAR']);
    component = TestBed.runInInjectionContext(() => new Veiculo());
  });

  afterEach(() => permissions.flushPermissions());

  it('uses the canonical edit route', () => {
    component.editarVeiculo(vehicle);
    expect(router.navigate).toHaveBeenCalledOnceWith(['/veiculos', 9, 'editar']);
  });

  it('opens the canonical vehicle passport for every readable vehicle', () => {
    component.abrirPassaporte(vehicle);
    expect(router.navigate).toHaveBeenCalledOnceWith(['/veiculos', 9]);
  });

  it('opens vehicle links and revisions from canonical routes', () => {
    component.abrirVinculos(vehicle);
    expect(router.navigate).toHaveBeenCalledWith(['/veiculos', 9, 'vinculos']);

    component.abrirRevisoes(vehicle);
    expect(router.navigate).toHaveBeenCalledWith(['/veiculos', 9, 'revisoes']);
  });

  it('preserves customer and vehicle context when starting an estimate', () => {
    component.criarOrcamento(vehicle);
    expect(router.navigate).toHaveBeenCalledOnceWith(
      ['/orcamentos/novo'],
      { queryParams: { clienteId: 42, veiculoId: 9 } }
    );
  });

  it('preserves customer and vehicle context when scheduling', () => {
    component.agendar(vehicle);
    expect(router.navigate).toHaveBeenCalledOnceWith(
      ['/agenda/novo'],
      { queryParams: { clienteId: 42, veiculoId: 9 } }
    );
  });

  it('does not expose operational actions for an inactive vehicle', () => {
    const inactive = { ...vehicle, status: StatusVeiculo.INATIVO };
    const labels = component.menuItemsFor(inactive).map(item => item.label);

    expect(labels).toContain('Abrir passaporte');
    expect(labels).toContain('Histórico de vínculos');
    expect(labels).toContain('Próximas revisões');
    expect(labels).not.toContain('Novo orçamento');
    expect(labels).not.toContain('Agendar serviço');
  });
});
