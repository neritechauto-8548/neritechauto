import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, defer, throwError, map } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { environment } from '../../../../environments/environment';

export interface ComissaoLinha {
  id: number;
  empresaId: number;
  funcionarioId: number;
  funcionarioNome: string;
  ordemServicoId?: number;
  numeroOs?: string;
  placa?: string;
  clienteNome?: string;
  empresaNome?: string;
  dataAutorizacao?: string;
  dataFechamento?: string;
  valorLiquido?: number;
  faturaId?: number;
  faturaNumero?: string;
  itemFaturaDescricao?: string;
  dataReferencia: string;
  valorBase: number;
  percentualComissao: number;
  valorComissao: number;
  aprovado: boolean;
  pago: boolean;
  dataPagamento?: string;
  observacoes?: string;
}
export interface PlanoContaComissao { id: number; nome: string; codigo: string; aceitaLancamento: boolean; ativo: boolean; }
export interface OpcoesQuitacao { gerarContaPagar: boolean; liquidarContaPagar?: boolean; planoContaId?: number; tipoDocumento?: string; }
export interface ComissoesPage { content: ComissaoLinha[]; totalElements: number; totalPages: number; }
export interface FiltrosComissoes {
  nome?: string; ordemServicoId?: number; funcionarioId?: number; paga?: boolean;
  inicio?: string; fim?: string;
}
@Injectable({ providedIn: 'root' })
export class ComissaoFinanceiraService {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly base = environment.baseUrl + '/v1/financeiro/comissoes';
  private scoped<T>(action: (empresaId: number, headers: HttpHeaders) => Observable<T>): Observable<T> {
    return defer(() => {
      const empresaId = empresaIdAtual(this.storage);
      if (!empresaId) return throwError(() => new Error('Empresa não identificada'));
      return action(empresaId, new HttpHeaders({ 'X-Tenant-Id': String(empresaId) }));
    });
  }
  listar(filters: FiltrosComissoes, page = 0, size = 20): Observable<ComissoesPage> {
    return this.scoped((empresaId, headers) => {
      let params = new HttpParams().set('empresaId', String(empresaId))
        .set('page', String(page)).set('size', String(size)).set('sort', 'dataCompetencia,desc');
      for (const [key, value] of Object.entries(filters)) {
        if (value !== null && value !== undefined && value !== '') params = params.set(key, String(value));
      }
      return this.http.get<ComissoesPage>(this.base, { headers, params });
    });
  }
  aprovar(id: number): Observable<ComissaoLinha> {
    return this.scoped((empresaId, headers) =>
      this.http.post<ComissaoLinha>(this.base + '/' + id + '/aprovar', null, {
        headers, params: { empresaId }
      }));
  }
  listarPlanos(): Observable<PlanoContaComissao[]> {
    return this.scoped((empresaId, headers) => this.http.get<{ content: PlanoContaComissao[] }>(environment.baseUrl + '/v1/financeiro/plano-contas', {
      headers, params: new HttpParams().set('empresaId', empresaId).set('page', 0).set('size', 100)
    }).pipe(map(page => (page.content || []).filter(p => p.ativo && p.aceitaLancamento))));
  }
  quitar(id: number, dataPagamento: string, observacao?: string, opcoes?: OpcoesQuitacao): Observable<ComissaoLinha> {
    return this.scoped((empresaId, headers) =>
      this.http.post<ComissaoLinha>(this.base + '/' + id + '/quitar', {
        dataPagamento, observacao, gerarContaPagar: opcoes?.gerarContaPagar ?? false,
        liquidarContaPagar: opcoes?.liquidarContaPagar ?? false, planoContaId: opcoes?.planoContaId,
        tipoDocumento: opcoes?.tipoDocumento
      }, { headers, params: { empresaId } }));
  }
  estornar(id: number): Observable<ComissaoLinha> {
    return this.scoped((empresaId, headers) =>
      this.http.post<ComissaoLinha>(this.base + '/' + id + '/estornar', null, {
        headers, params: { empresaId }
      }));
  }
}
