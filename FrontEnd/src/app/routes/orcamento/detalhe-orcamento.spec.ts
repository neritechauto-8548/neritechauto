import { Location } from '@angular/common';
import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { DetalheOrcamentoComponent } from './detalhe-orcamento';
import { OrcamentoListItem, OrcamentoListService } from './orcamento-list.service';

describe('DetalheOrcamentoComponent', () => {
  let component: DetalheOrcamentoComponent;
  let service: jasmine.SpyObj<OrcamentoListService>;
  let router: jasmine.SpyObj<Router>;

  const budget: OrcamentoListItem = {
    id: 91,
    numero: 'ORC-00091',
    versaoAtual: 2,
    cliente: { id: 10, nome: 'Cliente Teste' },
    veiculo: { id: 20, descricao: 'Neri One', placa: 'ABC1D23' },
    status: 'AGUARDANDO_APROVACAO',
    total: { currency: 'BRL', amount: 1250.5 },
    validadeEm: null,
    responsavelId: null,
    comunicacaoStatus: null,
    proximaAcao: 'ACOMPANHAR_APROVACAO',
    criadoEm: '2026-08-22T10:00:00',
    atualizadoEm: '2026-08-22T11:00:00',
    allowedActions: ['OPEN'],
  };

  beforeEach(() => {
    service = jasmine.createSpyObj<OrcamentoListService>('OrcamentoListService', ['getById']);
    router = jasmine.createSpyObj<Router>('Router', ['navigate', 'navigateByUrl']);
    service.getById.and.returnValue(of(budget));

    TestBed.configureTestingModule({
      providers: [
        { provide: OrcamentoListService, useValue: service },
        { provide: Router, useValue: router },
        { provide: Location, useValue: jasmine.createSpyObj<Location>('Location', ['back']) },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ id: 91 }) } },
        },
      ],
    });

    component = TestBed.runInInjectionContext(() => new DetalheOrcamentoComponent());
  });

  it('loads the canonical minimized snapshot and derives read-only labels', () => {
    component.ngOnInit();

    expect(service.getById).toHaveBeenCalledOnceWith(91);
    expect(component.budget).toEqual(budget);
    expect(component.versionLabel).toBe('Versão 2');
    expect(component.nextActionLabel()).toBe('Acompanhar aprovação');
    expect(component.hasMutableCapability).toBeFalse();
  });

  it('opens canonical customer and vehicle routes without tenant parameters', () => {
    component.ngOnInit();

    component.abrirCliente();
    component.abrirVeiculo();

    expect(router.navigate).toHaveBeenCalledWith(['/clientes', 10]);
    expect(router.navigate).toHaveBeenCalledWith(['/veiculos', 20]);
  });

  it('keeps unavailable tabs disabled and exposes no silent mutation commands', () => {
    component.ngOnInit();

    expect(component.tabs.filter(tab => tab.active).map(tab => tab.label)).toEqual(['Resumo']);
    expect('salvar' in component).toBeFalse();
    expect('cancelar' in component).toBeFalse();
    expect('converter' in component).toBeFalse();
  });

  it('renders a neutral forbidden state without retaining the previous snapshot', () => {
    service.getById.and.returnValue(throwError(() => ({ status: 403 })));

    component.ngOnInit();

    expect(component.budget).toBeNull();
    expect(component.loadError).toBeTrue();
    expect(component.forbidden).toBeTrue();
  });
});
