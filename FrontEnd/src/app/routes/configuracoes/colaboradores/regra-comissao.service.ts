import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

export interface RegraComissao {
  id?: number;
  funcionarioId: number;
  setorId?: number;
  setorNome?: string;
  percentual: number;
  sobreServico: string;
  sobreProdutos: string;
  faturamentoGeral: string;
  ativo: boolean;
  dataInicio?: string | Date;
  dataFinal?: string | Date;
}

@Injectable({
  providedIn: 'root'
})
export class RegraComissaoService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.baseUrl}/v1/rh/regras-comissao`;

  listarPorFuncionario(funcionarioId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/funcionario/${funcionarioId}`);
  }

  criar(regra: RegraComissao): Observable<RegraComissao> {
    return this.http.post<RegraComissao>(this.apiUrl, regra);
  }

  atualizar(id: number, regra: RegraComissao): Observable<RegraComissao> {
    return this.http.put<RegraComissao>(`${this.apiUrl}/${id}`, regra);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
