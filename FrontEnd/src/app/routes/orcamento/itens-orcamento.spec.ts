import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import {
  BudgetComposition,
  CatalogSearchItem,
  OrcamentoCompositionService,
} from './orcamento-composition.service';
import { ItensOrcamentoComponent } from './itens-orcamento';
import { OrcamentoListItem, OrcamentoListService } from './orcamento-list.service';

describe('ItensOrcamentoComponent', () => {
  let component: ItensOrcamentoComponent;
  let compositionService: jasmine.SpyObj<OrcamentoCompositionService>;
  let budgetService: jasmine.SpyObj<OrcamentoListService>;

  const budget: OrcamentoListItem = {
    id: 91,
    numero: 'ORC-00091',
    versaoAtual: 1,
    cliente: { id: 10, nome: 'Cliente Teste' },
    veiculo: { id: 20, descricao: 'Neri One', placa: 'ABC1D23' },
    status: 'RASCUNHO',
    total: { currency: 'BRL', amount: 0 },
    validadeEm: null,
    responsavelId: null,
    comunicacaoStatus: null,
    proximaAcao: 'CONTINUAR_EDICAO',
    criadoEm: null,
    atualizadoEm: null,
    allowedActions: ['OPEN'],
  };

  const composition: BudgetComposition = {
    budgetId: 91,
    revision: 4,
    calculationStatus: 'CURRENT',
    currency: 'BRL',
    requiredTotal: 250,
    recommendedTotal: 0,
    partsTotal: 100,
    laborTotal: 150,
    groupCount: 1,
    lineCount: 1,
    canReview: true,
    blockers: [],
    groups: [
      {
        id: 15,
        title: 'Sistema de freios',
        customerDescription: 'Revisão segura',
        internalNote: null,
        recommended: false,
        visibility: 'CUSTOMER_VISIBLE',
        position: 0,
        subtotal: 250,
        lines: [],
      },
    ],
  };

  beforeEach(() => {
    compositionService = jasmine.createSpyObj<OrcamentoCompositionService>(
      'OrcamentoCompositionService',
      [
        'get',
        'searchCatalog',
        'createGroup',
        'addCatalogItem',
        'updateGroup',
        'duplicateGroup',
        'deleteGroup',
        'reorderGroups',
        'updateLine',
        'duplicateLine',
        'deleteLine',
        'reorderLines',
      ]
    );
    budgetService = jasmine.createSpyObj<OrcamentoListService>('OrcamentoListService', ['getById']);
    compositionService.get.and.returnValue(of(composition));
    budgetService.getById.and.returnValue(of(budget));

    TestBed.configureTestingModule({
      providers: [
        { provide: OrcamentoCompositionService, useValue: compositionService },
        { provide: OrcamentoListService, useValue: budgetService },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap({ id: 91 }) } },
        },
      ],
    });

    component = TestBed.runInInjectionContext(() => new ItensOrcamentoComponent());
  });

  it('loads budget and canonical composition together and selects the first group', () => {
    component.ngOnInit();

    expect(budgetService.getById).toHaveBeenCalledOnceWith(91);
    expect(compositionService.get).toHaveBeenCalledOnceWith(91);
    expect(component.budget).toEqual(budget);
    expect(component.composition).toEqual(composition);
    expect(component.selectedGroupId).toBe(15);
    expect(component.isLoading).toBeFalse();
  });

  it('creates a group against the current server revision', () => {
    const updated = { ...composition, revision: 5, groupCount: 2 };
    compositionService.createGroup.and.returnValue(of(updated));
    component.ngOnInit();
    component.groupForm.setValue({
      title: 'Troca de fluido',
      customerDescription: 'Renova o fluido do sistema',
      recommended: true,
    });

    component.createGroup();

    expect(compositionService.createGroup).toHaveBeenCalledWith(91, {
      expectedRevision: 4,
      title: 'Troca de fluido',
      customerDescription: 'Renova o fluido do sistema',
      recommended: true,
      visibility: 'CUSTOMER_VISIBLE',
    });
    expect(component.composition?.revision).toBe(5);
  });

  it('adds only catalog identity and quantity, leaving canonical price to the server', () => {
    compositionService.addCatalogItem.and.returnValue(of({ ...composition, revision: 5 }));
    component.ngOnInit();
    const item: CatalogSearchItem = {
      id: 22,
      lineType: 'PART',
      description: 'Pastilha de freio',
      reference: 'PST-22',
      suggestedPrice: 99,
      availabilityStatus: 'AVAILABLE',
    };

    component.addCatalogItem(item);

    expect(compositionService.addCatalogItem).toHaveBeenCalledWith(91, 15, {
      expectedRevision: 4,
      lineType: 'PART',
      catalogItemId: 22,
      quantity: 1,
    });
  });

  it('preserves the previous composition and exposes reload guidance on conflict', () => {
    compositionService.addCatalogItem.and.returnValue(throwError(() => ({ status: 409 })));
    component.ngOnInit();

    component.addCatalogItem({
      id: 22,
      lineType: 'PART',
      description: 'Pastilha de freio',
      reference: 'PST-22',
      suggestedPrice: 99,
      availabilityStatus: 'AVAILABLE',
    });

    expect(component.composition).toEqual(composition);
    expect(component.conflict).toBeTrue();
    expect(component.mutationError).toContain('Recarregue');
  });

  it('updates group metadata against the current revision without sending prices', () => {
    compositionService.updateGroup.and.returnValue(of({ ...composition, revision: 5 }));
    component.ngOnInit();
    component.startEditGroup(composition.groups[0]);
    component.editGroupForm.patchValue({
      title: 'Freios dianteiros',
      internalNote: 'Validar ruído no teste de rodagem',
      visibility: 'INTERNAL_ONLY',
    });

    component.saveGroup(15);

    expect(compositionService.updateGroup).toHaveBeenCalledWith(91, 15, {
      expectedRevision: 4,
      title: 'Freios dianteiros',
      customerDescription: 'Revisão segura',
      internalNote: 'Validar ruído no teste de rodagem',
      recommended: false,
      visibility: 'INTERNAL_ONLY',
    });
    expect(component.editingGroupId).toBeNull();
  });

  it('moves a group with a complete ordered id list as the accessible drag alternative', () => {
    const secondGroup = { ...composition.groups[0], id: 16, title: 'Suspensão', position: 1 };
    const twoGroups = {
      ...composition,
      groups: [composition.groups[0], secondGroup],
      groupCount: 2,
    };
    compositionService.get.and.returnValue(of(twoGroups));
    compositionService.reorderGroups.and.returnValue(
      of({ ...twoGroups, revision: 5, groups: [secondGroup, composition.groups[0]] })
    );
    component.ngOnInit();

    component.moveGroup(0, 1);

    expect(compositionService.reorderGroups).toHaveBeenCalledWith(91, 4, [16, 15]);
  });

  it('requires explicit confirmation before deleting a group', () => {
    compositionService.deleteGroup.and.returnValue(
      of({ ...composition, revision: 5, groups: [], groupCount: 0 })
    );
    component.ngOnInit();

    component.requestDeleteGroup(composition.groups[0]);
    expect(compositionService.deleteGroup).not.toHaveBeenCalled();

    component.confirmDeletion();
    expect(compositionService.deleteGroup).toHaveBeenCalledOnceWith(91, 15, 4);
    expect(component.pendingDeletion).toBeNull();
  });
});

