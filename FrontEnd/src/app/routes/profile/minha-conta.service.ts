import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UsuarioResponse } from '../configuracoes/permissoes/usuario.models';

@Injectable({ providedIn: 'root' })
export class MinhaContaService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.baseUrl}/usuarios`;
  obterMinhaConta(): Observable<UsuarioResponse> { return this.http.get<UsuarioResponse>(`${this.base}/me`); }
  atualizarEmail(email: string, senhaAtual: string): Observable<UsuarioResponse> {
    return this.http.patch<UsuarioResponse>(`${this.base}/me/email`, { email, senhaAtual });
  }
  alterarSenha(senhaAtual: string, novaSenha: string): Observable<void> {
    return this.http.post<void>(`${environment.baseUrl}/auth/change-my-password`, { senhaAtual, novaSenha });
  }
}
