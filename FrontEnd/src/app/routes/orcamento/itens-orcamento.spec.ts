import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import {
  BudgetComposition,
  CatalogSearchItem,
  CompositionLine,
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
    commercialPermissions: {
      canEditPackagePrice: true,
      canEditUnitPrice: true,
      canApplyDiscount: true,
      canApproveDiscount: false,
      canViewCost: false,
      discountAuthorityPercent: 5,
    },
    groups: [
      {
        id: 15,
        title: 'Sistema de freios',
        customerDescription: 'Revisão segura',
        internalNote: null,
        kitOriginId: null,
        kitOriginVersion: null,
        recommended: false,
        visibility: 'CUSTOMER_VISIBLE',
        position: 0,
        packagePrice: null,
        packageDistributionMethod: null,
        packageOriginalSubtotal: null,
        packageAdjustmentAmount: null,
        packagePriceSourceType: null,
        packagePriceSourceId: null,
        packagePriceSourceVersion: null,
        packageAppliedAt: null,
        packageOverrideReason: null,
        packageAuthorityStatus: null,
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
        'instantiateKit',
        'updateGroup',
        'duplicateGroup',
        'deleteGroup',
        'reorderGroups',
        'updateLine',
        'updatePackagePrice',
        'updateLineCommercial',
        'decideDiscount',
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
      itemCount: 1,
      catalogVersion: 3,
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
      itemCount: 1,
      catalogVersion: 3,
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

  it('instantiates a kit at the end with a unique idempotency key and no client price', () => {
    compositionService.instantiateKit.and.returnValue(
      of({ ...composition, revision: 5, groupCount: 2 })
    );
    component.ngOnInit();

    component.addCatalogItem({
      id: 44,
      lineType: 'KIT',
      description: 'Revisão 40 mil km',
      reference: 'KIT-40K',
      suggestedPrice: 890,
      availabilityStatus: 'AVAILABLE',
      itemCount: 4,
      catalogVersion: 6,
    });

    expect(compositionService.instantiateKit).toHaveBeenCalledWith(
      91,
      44,
      jasmine.stringMatching(/^kit-/),
      { expectedRevision: 4, quantity: 1, targetPosition: 1 }
    );
    expect(component.saveMessage).toContain('Kit v6');
  });

  it('saves a closed package price with an explicit distribution policy and audit reason', () => {
    compositionService.updatePackagePrice.and.returnValue(
      of({
        ...composition,
        revision: 5,
        groups: [
          {
            ...composition.groups[0],
            packagePrice: 220,
            packageDistributionMethod: 'WEIGHTED',
            packageOriginalSubtotal: 250,
            packageAdjustmentAmount: -30,
            subtotal: 220,
          },
        ],
      })
    );
    component.ngOnInit();
    component.startPackagePricing(composition.groups[0]);
    component.packagePriceForm.setValue({
      packagePrice: 220,
      distributionMethod: 'WEIGHTED',
      overrideReason: 'Condição negociada na recepção',
    });

    component.savePackagePrice(composition.groups[0]);

    expect(compositionService.updatePackagePrice).toHaveBeenCalledWith(91, 15, {
      expectedRevision: 4,
      packagePrice: 220,
      distributionMethod: 'WEIGHTED',
      priceSourceId: null,
      priceSourceVersion: null,
      overrideReason: 'Condição negociada na recepção',
    });
    expect(component.pricingGroupId).toBeNull();
    expect(component.saveMessage).toContain('distribuído');
  });

  it('sends quantity, price override and discount as one commercial mutation', () => {
    const line: CompositionLine = {
      id: 31,
      lineType: 'PART',
      catalogItemId: 22,
      catalogVersion: 3,
      source: 'PRODUCT_CATALOG',
      kitOriginId: null,
      kitOriginVersion: null,
      description: 'Pastilha de freio',
      reference: 'PST-22',
      quantity: 1,
      unitPrice: 100,
      grossAmount: 100,
      discountAmount: 0,
      discountType: 'NONE',
      discountValue: 0,
      discountReason: null,
      discountAuthorityStatus: 'NONE',
      discountAuthorityLimitPercent: null,
      discountApprovalRequestId: null,
      totalAmount: 100,
      allocatedPackageAmount: null,
      packageAdjustmentAmount: 0,
      priceSourceType: 'PRODUCT_CATALOG',
      priceSourceId: 22,
      priceSourceVersion: 3,
      priceAppliedAt: '2026-08-22T12:00:00Z',
      priceOverridden: false,
      priceOverrideReason: null,
      availabilityStatus: 'AVAILABLE',
      position: 0,
    };
    compositionService.updateLineCommercial.and.returnValue(
      of({ ...composition, revision: 5 })
    );
    component.ngOnInit();
    component.startEditLine(line);
    component.lineCommercialForm.setValue({
      quantity: 2,
      unitPrice: 95,
      priceOverrideReason: 'Ajuste negociado com cliente',
      discountType: 'PERCENT',
      discountValue: 4,
      discountReason: 'Fidelidade comprovada do cliente',
    });

    component.saveLine(15, line);

    expect(compositionService.updateLineCommercial).toHaveBeenCalledWith(91, 15, 31, {
      expectedRevision: 4,
      quantity: 2,
      unitPrice: 95,
      priceOverrideReason: 'Ajuste negociado com cliente',
      discountType: 'PERCENT',
      discountValue: 4,
      discountReason: 'Fidelidade comprovada do cliente',
    });
    expect(component.editingLineId).toBeNull();
  });

  it('blocks a commercial override locally when the audit reason is too short', () => {
    const line = {
      id: 31,
      quantity: 1,
      unitPrice: 100,
      discountType: 'NONE' as const,
      discountValue: 0,
      discountReason: null,
    } as CompositionLine;
    component.ngOnInit();
    component.startEditLine(line);
    component.lineCommercialForm.patchValue({ unitPrice: 95, priceOverrideReason: 'Ajuste' });

    component.saveLine(15, line);

    expect(compositionService.updateLineCommercial).not.toHaveBeenCalled();
    expect(component.mutationError).toContain('pelo menos 8 caracteres');
  });
});

