import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';

export interface HistoricoPage<T> {
  content: T[]; totalElements: number; totalPages: number; number: number; size: number; last?: boolean;
}
export interface HistoricoVeiculo {
  id: number; clienteId: number; cliente: string; documento?: string;
  placa: string; modelo?: string; marca?: string;
  anoFabricacao?: number; anoModelo?: number; combustivel?: string; cilindrada?: number;
}
export interface HistoricoItem {
  id: number; tipo: 'SERVICO' | 'PRODUTO'; descricao: string; quantidade?: number;
  valorUnitario?: number; desconto?: number; valorFinal?: number; comissionadoId?: number;
}
export interface HistoricoOS {
  id: number; numero: string; dataAbertura?: string; dataEntrega?: string;
  status: string; autorizada: boolean; problema?: string; solucao?: string;
  valorTotal?: number; itens: HistoricoItem[];
}
export interface HistoricoRelatorioOpcoes {
  veiculoId?: number; busca?: string; soAutorizadas: boolean;
  mostrarComissionado: boolean; mostrarValores: boolean;
}

@Injectable({providedIn: 'root'})
export class HistoricoVeiculosService {
  private readonly http = inject(HttpClient);
  private readonly url = environment.baseUrl + '/v1/historicos-veiculos';
  listar(busca = '', ordem = 'AZ', page = 0, size = 10): Observable<HistoricoPage<HistoricoVeiculo>> {
    return this.http.get<HistoricoPage<HistoricoVeiculo>>(this.url, {
      params: new HttpParams().set('busca', busca).set('ordem', ordem)
        .set('page', page).set('size', size)
    });
  }
  veiculo(id: number): Observable<HistoricoVeiculo> {
    return this.http.get<HistoricoVeiculo>(this.url + '/' + id);
  }
  ordens(id: number, busca = '', ordenar = 'OS', incluirAutorizados = false, page = 0,
    size = 10): Observable<HistoricoPage<HistoricoOS>> {
    return this.http.get<HistoricoPage<HistoricoOS>>(this.url + '/' + id + '/ordens', {
      params: new HttpParams().set('busca', busca).set('ordenar', ordenar)
        .set('incluirAutorizados', incluirAutorizados).set('page', page).set('size', size)
    });
  }
  baixarRelatorio(options: HistoricoRelatorioOpcoes): Observable<Blob> {
    let params = new HttpParams().set('soAutorizadas', options.soAutorizadas)
      .set('mostrarComissionado', options.mostrarComissionado)
      .set('mostrarValores', options.mostrarValores);
    if(options.veiculoId) params = params.set('veiculoId', options.veiculoId);
    if(options.busca?.trim()) params = params.set('busca', options.busca.trim());
    return this.http.get(this.url + '/relatorio', { params, responseType: 'blob' });
  }
}
