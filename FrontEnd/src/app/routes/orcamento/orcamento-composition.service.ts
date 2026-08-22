import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export type CompositionLineType = 'PART' | 'LABOR' | 'FEE' | 'SUBLET' | 'DISCOUNT' | 'NOTE';
export type AvailabilityStatus = 'AVAILABLE' | 'PARTIAL' | 'NEEDED' | 'NOT_APPLICABLE';

export interface CompositionLine {
  id: number;
  lineType: CompositionLineType;
  catalogItemId: number | null;
  catalogVersion: number | null;
  source: 'PRODUCT_CATALOG' | 'SERVICE_CATALOG' | 'KIT' | 'MANUAL';
  description: string;
  reference: string | null;
  quantity: number;
  unitPrice: number;
  discountAmount: number;
  totalAmount: number;
  availabilityStatus: AvailabilityStatus;
  position: number;
}

export interface CompositionGroup {
  id: number;
  title: string;
  customerDescription: string | null;
  internalNote: string | null;
  recommended: boolean;
  visibility: 'CUSTOMER_VISIBLE' | 'INTERNAL_ONLY';
  position: number;
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
  groups: CompositionGroup[];
}

export interface CatalogSearchItem {
  id: number;
  lineType: 'PART' | 'LABOR';
  description: string;
  reference: string | null;
  suggestedPrice: number;
  availabilityStatus: AvailabilityStatus;
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

  searchCatalog(query: string): Observable<CatalogSearchResponse> {
    return this.http.get<CatalogSearchResponse>(`${this.baseUrl}/catalog`, {
      params: new HttpParams().set('q', query),
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

