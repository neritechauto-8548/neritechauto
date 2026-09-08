import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { PaginaPagamentosOS, ResumoFaturaOS } from './os-finance.models';

@Injectable({ providedIn: 'root' })
export class FinanceiroOrdemServicoService {
  private readonly http = inject(HttpClient);
  private readonly urlFaturas = `${environment.baseUrl}/v1/financeiro/faturas`;
  private readonly urlPagamentos = `${environment.baseUrl}/v1/financeiro/pagamentos`;

  buscarFaturaPorOrdem(ordemServicoId: number): Observable<ResumoFaturaOS | null> {
    return this.http.get<ResumoFaturaOS | null>(`${this.urlFaturas}/ordem-servico/${ordemServicoId}`);
  }

  listarPagamentosPorOrdem(ordemServicoId: number): Observable<PaginaPagamentosOS> {
    const parametros = new HttpParams().set('page', '0').set('size', '20').set('sort', 'dataPagamento,desc');
    return this.http.get<PaginaPagamentosOS>(`${this.urlPagamentos}/ordem-servico/${ordemServicoId}`, { params: parametros });
  }
}
