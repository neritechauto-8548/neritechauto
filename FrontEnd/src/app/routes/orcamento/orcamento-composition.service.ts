import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export type CompositionLineType = 'PART' | 'LABOR' | 'FEE' | 'SUBLET' | 'DISCOUNT' | 'NOTE';
export type AvailabilityStatus = 'AVAILABLE' | 'PARTIAL' | 'NEEDED' | 'NOT_APPLICABLE';
export type DiscountType = 'NONE' | 'FIXED' | 'PERCENT';
export type DiscountAuthorityStatus = 'NONE' | 'APPROVED' | 'PENDING_APPROVAL' | 'REJECTED';
export type PackageDistributionMethod = 'WEIGHTED' | 'LABOR_FIRST' | 'POLICY';

export interface CompositionLine {
  id: number;
  lineType: CompositionLineType;
  catalogItemId: number | null;
  catalogVersion: number | null;
  source: 'PRODUCT_CATALOG' | 'SERVICE_CATALOG' | 'KIT' | 'MANUAL';
  kitOriginId: number | null;
  kitOriginVersion: number | null;
  description: string;
  reference: string | null;
  quantity: number;
  unitPrice: number;
  grossAmount: number;
  discountAmount: number;
  discountType: DiscountType;
  discountValue: number;
  discountReason: string | null;
  discountAuthorityStatus: DiscountAuthorityStatus;
  discountAuthorityLimitPercent: number | null;
  discountApprovalRequestId: number | null;
  totalAmount: number;
  allocatedPackageAmount: number | null;
  packageAdjustmentAmount: number;
  priceSourceType: string;
  priceSourceId: number | null;
  priceSourceVersion: number | null;
  priceAppliedAt: string | null;
  priceOverridden: boolean;
  priceOverrideReason: string | null;
  availabilityStatus: AvailabilityStatus;
  position: number;
}

export interface CompositionGroup {
  id: number;
  title: string;
  customerDescription: string | null;
  internalNote: string | null;
  kitOriginId: number | null;
  kitOriginVersion: number | null;
  recommended: boolean;
  visibility: 'CUSTOMER_VISIBLE' | 'INTERNAL_ONLY';
  position: number;
  packagePrice: number | null;
  packageDistributionMethod: PackageDistributionMethod | null;
  packageOriginalSubtotal: number | null;
  packageAdjustmentAmount: number | null;
  packagePriceSourceType: string | null;
  packagePriceSourceId: number | null;
  packagePriceSourceVersion: number | null;
  packageAppliedAt: string | null;
  packageOverrideReason: string | null;
  packageAuthorityStatus: 'APPROVED' | 'PENDING_APPROVAL' | null;
  subtotal: number;
  lines: CompositionLine[];
}

export interface BudgetComposition {
  budgetId: number;
  revision: number;
  calculationStatus: 'EMPTY' | 'CURRENT' | 'PENDING' | 'ERROR';
  currency: 'BRL';
  requiredTotal: number;
  recommendedTotal: number;
  partsTotal: number;
  laborTotal: number;
  groupCount: number;
  lineCount: number;
  canReview: boolean;
  blockers: string[];
  commercialPermissions: CommercialPermissions;
  groups: CompositionGroup[];
}

export interface CommercialPermissions {
  canEditPackagePrice: boolean;
  canEditUnitPrice: boolean;
  canApplyDiscount: boolean;
  canApproveDiscount: boolean;
  canViewCost: boolean;
  discountAuthorityPercent: number;
}

export interface CatalogSearchItem {
  id: number;
  lineType: 'PART' | 'LABOR' | 'KIT';
  description: string;
  reference: string | null;
  suggestedPrice: number;
  availabilityStatus: AvailabilityStatus;
  itemCount: number;
  catalogVersion: number | null;
}

export interface CatalogSearchResponse {
  query: string;
  items: CatalogSearchItem[];
  truncated: boolean;
}

@Injectable({ providedIn: 'root' })
export class OrcamentoCompositionService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.baseUrl}/v1/orcamentos`;

  get(budgetId: number): Observable<BudgetComposition> {
    return this.http.get<BudgetComposition>(`${this.baseUrl}/${budgetId}/composition`);
  }

  searchCatalog(query: string, type?: 'KIT'): Observable<CatalogSearchResponse> {
    let params = new HttpParams().set('q', query);
    if (type) params = params.set('type', type);
    return this.http.get<CatalogSearchResponse>(`${this.baseUrl}/catalog`, {
      params,
    });
  }

  createGroup(
    budgetId: number,
    request: {
      expectedRevision: number;
      title: string;
      customerDescription?: string | null;
      recommended: boolean;
      visibility: 'CUSTOMER_VISIBLE' | 'INTERNAL_ONLY';
    }
  ): Observable<BudgetComposition> {
    return this.http.post<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups`,
      request
    );
  }

  addCatalogItem(
    budgetId: number,
    groupId: number,
    request: {
      expectedRevision: number;
      lineType: 'PART' | 'LABOR';
      catalogItemId: number;
      quantity: number;
    }
  ): Observable<BudgetComposition> {
    return this.http.post<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}/items`,
      request
    );
  }

  instantiateKit(
    budgetId: number,
    kitId: number,
    idempotencyKey: string,
    request: {
      expectedRevision: number;
      quantity: number;
      targetPosition: number;
    }
  ): Observable<BudgetComposition> {
    return this.http.post<BudgetComposition>(`${this.baseUrl}/${budgetId}/kits/${kitId}`, request, {
      headers: { 'Idempotency-Key': idempotencyKey },
    });
  }

  updateGroup(
    budgetId: number,
    groupId: number,
    request: {
      expectedRevision: number;
      title: string;
      customerDescription: string | null;
      internalNote: string | null;
      recommended: boolean;
      visibility: 'CUSTOMER_VISIBLE' | 'INTERNAL_ONLY';
    }
  ): Observable<BudgetComposition> {
    return this.http.put<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}`,
      request
    );
  }

  duplicateGroup(
    budgetId: number,
    groupId: number,
    expectedRevision: number
  ): Observable<BudgetComposition> {
    return this.http.post<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}/duplicate`,
      { expectedRevision }
    );
  }

  deleteGroup(
    budgetId: number,
    groupId: number,
    expectedRevision: number
  ): Observable<BudgetComposition> {
    return this.http.delete<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}`,
      { params: new HttpParams().set('expectedRevision', expectedRevision) }
    );
  }

  reorderGroups(
    budgetId: number,
    expectedRevision: number,
    orderedIds: number[]
  ): Observable<BudgetComposition> {
    return this.http.put<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/reorder`,
      {
        expectedRevision,
        orderedIds,
      }
    );
  }

  updateLine(
    budgetId: number,
    groupId: number,
    itemId: number,
    expectedRevision: number,
    quantity: number
  ): Observable<BudgetComposition> {
    return this.http.put<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}/items/${itemId}`,
      { expectedRevision, quantity }
    );
  }

  updatePackagePrice(
    budgetId: number,
    groupId: number,
    request: {
      expectedRevision: number;
      packagePrice: number | null;
      distributionMethod: PackageDistributionMethod | null;
      priceSourceId: number | null;
      priceSourceVersion: number | null;
      overrideReason: string | null;
    }
  ): Observable<BudgetComposition> {
    return this.http.put<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}/package-price`,
      request
    );
  }

  updateLineCommercial(
    budgetId: number,
    groupId: number,
    itemId: number,
    request: {
      expectedRevision: number;
      quantity: number;
      unitPrice: number;
      priceOverrideReason: string | null;
      discountType: DiscountType;
      discountValue: number;
      discountReason: string | null;
    }
  ): Observable<BudgetComposition> {
    return this.http.put<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}/items/${itemId}/commercial`,
      request
    );
  }

  decideDiscount(
    budgetId: number,
    approvalId: number,
    request: { expectedRevision: number; decision: 'APPROVE' | 'REJECT'; reason: string }
  ): Observable<BudgetComposition> {
    return this.http.post<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/discount-approvals/${approvalId}/decision`,
      request
    );
  }

  duplicateLine(
    budgetId: number,
    groupId: number,
    itemId: number,
    expectedRevision: number
  ): Observable<BudgetComposition> {
    return this.http.post<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}/items/${itemId}/duplicate`,
      { expectedRevision }
    );
  }

  deleteLine(
    budgetId: number,
    groupId: number,
    itemId: number,
    expectedRevision: number
  ): Observable<BudgetComposition> {
    return this.http.delete<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}/items/${itemId}`,
      { params: new HttpParams().set('expectedRevision', expectedRevision) }
    );
  }

  reorderLines(
    budgetId: number,
    groupId: number,
    expectedRevision: number,
    orderedIds: number[]
  ): Observable<BudgetComposition> {
    return this.http.put<BudgetComposition>(
      `${this.baseUrl}/${budgetId}/composition/groups/${groupId}/items/reorder`,
      { expectedRevision, orderedIds }
    );
  }
}

