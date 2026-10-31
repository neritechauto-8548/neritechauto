import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { LocalStorageService } from '@shared/services/storage.service';

export interface AreaRecurso {
  id: number; descricao: string; ativo: boolean; versao: number;
  subareas?: SubareaRecurso[];
}
export interface SubareaRecurso { id?: number; descricao: string; ativo: boolean; }
export interface ServicoRecurso {
  id: number; descricao: string; codigoTempario: string; areaId: number;
  area: string; unidade: string; tempo: number; vidaUtilDias: number | null;
  kmManutencao: number | null; nbs: string | null; observacao: string | null;
  preco: number; custoFixo: number; comissao: number; ativo: boolean;
}
export interface AcessorioRecurso {
  id: number; descricao: string; checklist: boolean; padrao: boolean;
  ativo: boolean; versao: number;
}
export interface AplicacaoRecurso {
  id: number; marcaId: number; marca: string; modeloId: number;
  modelo: string; tempo: number;
}
export interface OpcaoRecurso { id: number; nome: string; }

@Injectable({ providedIn: 'root' })
export class RecursosApi {
  private readonly http = inject(HttpClient);
  private readonly storage = inject(LocalStorageService);
  private readonly url = `${environment.baseUrl}/v1/recursos`;

  private get options() {
    const tenant = this.storage.has('tenantId') ? this.storage.get('tenantId') : null;
    if (!tenant || !/^\d+$/.test(String(tenant))) throw new Error('Empresa autenticada ausente.');
    return { headers: { 'X-Tenant-Id': String(tenant) } };
  }
  private params(args: Record<string, unknown>): HttpParams {
    let p = new HttpParams();
    for (const [key, value] of Object.entries(args)) {
      if (value !== null && value !== undefined && String(value).trim() !== '') p = p.set(key, String(value));
    }
    return p;
  }

  areas(args: Record<string, unknown> = {}): Observable<AreaRecurso[]> {
    return this.http.get<AreaRecurso[]>(`${this.url}/areas`, { ...this.options, params: this.params(args) });
  }
  area(id: number): Observable<AreaRecurso> {
    return this.http.get<AreaRecurso>(`${this.url}/areas/${id}`, this.options);
  }
  criarArea(data: Partial<AreaRecurso>) {
    return this.http.post<AreaRecurso>(`${this.url}/areas`, data, this.options);
  }
  atualizarArea(id: number, data: Partial<AreaRecurso>) {
    return this.http.put<AreaRecurso>(`${this.url}/areas/${id}`, data, this.options);
  }
  excluirArea(id: number) { return this.http.delete<void>(`${this.url}/areas/${id}`, this.options); }

  servicos(args: Record<string, unknown> = {}): Observable<ServicoRecurso[]> {
    return this.http.get<ServicoRecurso[]>(`${this.url}/servicos`, { ...this.options, params: this.params(args) });
  }
  servico(id: number) {
    return this.http.get<ServicoRecurso>(`${this.url}/servicos/${id}`, this.options);
  }
  criarServico(data: Partial<ServicoRecurso>) {
    return this.http.post<ServicoRecurso>(`${this.url}/servicos`, data, this.options);
  }
  atualizarServico(id: number, data: Partial<ServicoRecurso>) {
    return this.http.put<ServicoRecurso>(`${this.url}/servicos/${id}`, data, this.options);
  }
  excluirServico(id: number) { return this.http.delete<void>(`${this.url}/servicos/${id}`, this.options); }
  alterarPrecos(data: { operacao: string; valor: number; areaId: number | null }) {
    return this.http.post<{ alterados: number }>(`${this.url}/servicos/precos-lote`, data, this.options);
  }
  importarServicos(linhas: unknown[]) {
    return this.http.post<{ importados: number }>(`${this.url}/servicos/importar`, linhas, this.options);
  }
  marcas() { return this.http.get<OpcaoRecurso[]>(`${this.url}/marcas`, this.options); }
  modelos(marcaId: number) {
    return this.http.get<OpcaoRecurso[]>(`${this.url}/modelos`, { ...this.options, params: this.params({ marcaId }) });
  }
  aplicacoes(id: number) {
    return this.http.get<AplicacaoRecurso[]>(`${this.url}/servicos/${id}/aplicacoes`, this.options);
  }
  adicionarAplicacao(id: number, data: { marcaId: number; modeloId: number; tempo: number }) {
    return this.http.post<void>(`${this.url}/servicos/${id}/aplicacoes`, data, this.options);
  }
  alterarAplicacao(id: number, aplicacaoId: number, data: { tempo: number }) {
    return this.http.put<void>(`${this.url}/servicos/${id}/aplicacoes/${aplicacaoId}`, data, this.options);
  }
  removerAplicacao(id: number, aplicacaoId: number) {
    return this.http.delete<void>(`${this.url}/servicos/${id}/aplicacoes/${aplicacaoId}`, this.options);
  }

  acessorios(args: Record<string, unknown> = {}): Observable<AcessorioRecurso[]> {
    return this.http.get<AcessorioRecurso[]>(`${this.url}/acessorios`, { ...this.options, params: this.params(args) });
  }
  criarAcessorio(data: Partial<AcessorioRecurso>) {
    return this.http.post<AcessorioRecurso>(`${this.url}/acessorios`, data, this.options);
  }
  atualizarAcessorio(id: number, data: Partial<AcessorioRecurso>) {
    return this.http.put<AcessorioRecurso>(`${this.url}/acessorios/${id}`, data, this.options);
  }
  excluirAcessorio(id: number) { return this.http.delete<void>(`${this.url}/acessorios/${id}`, this.options); }
}
