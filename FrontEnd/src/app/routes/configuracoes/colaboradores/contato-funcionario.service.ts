import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { defer, Observable, throwError } from 'rxjs';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { environment } from '../../../../environments/environment';

export type TipoMeio = 'EMAIL' | 'WHATSAPP' | 'CELULAR' | 'TELEFONE';
export interface MeioContato { id?: number; tipo: TipoMeio; valor: string; }
export interface ContatoFuncionario {
  id: number; nome: string; sexo?: string; dataNascimento?: string;
  principal: boolean; meios: MeioContato[];
}
export type ContatoPayload = Omit<ContatoFuncionario, 'id'>;
@Injectable({ providedIn: 'root' })
export class ContatoFuncionarioService {
  private http = inject(HttpClient);
  private storage = inject(LocalStorageService);
  private base = environment.baseUrl + '/v1/rh/funcionarios';
  private escopo<T>(id: number, fn: (url: string, headers: HttpHeaders) => Observable<T>): Observable<T> {
    return defer(() => {
      const tenant = empresaIdAtual(this.storage);
      if (!tenant) return throwError(() => new Error('Empresa não identificada'));
      return fn(this.base + '/' + id + '/contatos', new HttpHeaders({ 'X-Tenant-Id': String(tenant) }));
    });
  }
  listar(funcionarioId: number): Observable<ContatoFuncionario[]> {
    return this.escopo(funcionarioId, (url, headers) => this.http.get<ContatoFuncionario[]>(url, { headers }));
  }
  criar(funcionarioId: number, data: ContatoPayload): Observable<ContatoFuncionario> {
    return this.escopo(funcionarioId, (url, headers) => this.http.post<ContatoFuncionario>(url, data, { headers }));
  }
  atualizar(funcionarioId: number, id: number, data: ContatoPayload): Observable<ContatoFuncionario> {
    return this.escopo(funcionarioId, (url, headers) => this.http.put<ContatoFuncionario>(url + '/' + id, data, { headers }));
  }
  excluir(funcionarioId: number, id: number): Observable<void> {
    return this.escopo(funcionarioId, (url, headers) => this.http.delete<void>(url + '/' + id, { headers }));
  }
}
