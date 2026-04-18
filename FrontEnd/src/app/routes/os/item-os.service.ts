import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ItemOSProdutoRequest, ItemOSProdutoResponse, ItemOSServicoRequest, ItemOSServicoResponse } from './models/os.models';

@Injectable({ providedIn: 'root' })
export class ItemOSService {
  private readonly http = inject(HttpClient);
  private readonly baseUrlProdutos = `${environment.baseUrl}/v1/itens-os-produtos`;
  private readonly baseUrlServicos = `${environment.baseUrl}/v1/itens-os-servicos`;

  // === PRODUTOS ===

  listProdutos(ordemServicoId: number): Observable<ItemOSProdutoResponse[]> {
    return this.http.get<ItemOSProdutoResponse[]>(`${this.baseUrlProdutos}/ordem-servico/${ordemServicoId}`);
  }

  addProduto(item: ItemOSProdutoRequest): Observable<ItemOSProdutoResponse> {
    return this.http.post<ItemOSProdutoResponse>(this.baseUrlProdutos, item);
  }

  updateProduto(id: number, item: ItemOSProdutoRequest): Observable<ItemOSProdutoResponse> {
    return this.http.put<ItemOSProdutoResponse>(`${this.baseUrlProdutos}/${id}`, item);
  }

  deleteProduto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrlProdutos}/${id}`);
  }

  // === SERVIÇOS ===

  listServicos(ordemServicoId: number): Observable<ItemOSServicoResponse[]> {
    return this.http.get<ItemOSServicoResponse[]>(`${this.baseUrlServicos}/ordem-servico/${ordemServicoId}`);
  }

  addServico(item: ItemOSServicoRequest): Observable<ItemOSServicoResponse> {
    return this.http.post<ItemOSServicoResponse>(this.baseUrlServicos, item);
  }

  updateServico(id: number, item: ItemOSServicoRequest): Observable<ItemOSServicoResponse> {
    return this.http.put<ItemOSServicoResponse>(`${this.baseUrlServicos}/${id}`, item);
  }

  deleteServico(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrlServicos}/${id}`);
  }
}
