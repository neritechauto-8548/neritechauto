import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UsuarioRequest, UsuarioResponse, Permissao } from './permissoes/usuario.models';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.baseUrl}/usuarios`; // Note: Controller uses /usuarios, typically /api/v1/... check path
  // The controller code showed @RequestMapping("/usuarios") WITHOUT /v1. Need to be careful or update backend.
  // Assuming backend is consistent with others, it SHOULD be /v1. But file said /usuarios.
  // I will try /api/usuarios based on baseUrl.
  // Wait, other services use /v1/...
  // Let's assume the backend file I read was legacy or mixed. I will try /usuarios first as per code.

  // Correction: The viewed file UsuarioController.java has @RequestMapping("/usuarios").
  // So it is likely just /usuarios (or /api/usuarios if context path).
  // I will use `${environment.baseUrl}/usuarios` matching previous service which used `${environment.baseUrl}` as prefix.
  // Previous services used `${environment.baseUrl}/v1/...`.
  // If base url is /api, then this is /api/usuarios.

  list(): Observable<UsuarioResponse[]> {
    return this.http.get<UsuarioResponse[]>(this.API_URL);
  }

  get(id: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.API_URL}/${id}`);
  }

  create(user: UsuarioRequest): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(this.API_URL, user);
  }

  update(id: number, user: UsuarioRequest): Observable<UsuarioResponse> {
    return this.http.put<UsuarioResponse>(`${this.API_URL}/${id}`, user);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  listPermissoes(): Observable<Permissao[]> {
      // Mocking Roles/Permissions for now or calling PermissaoController
      // Controller is /v1/permissoes
      return this.http.get<Permissao[]>(`${environment.baseUrl}/v1/permissoes`);
  }

  // Helper mock for Roles if Permissoes don't fit
  getRoles(): Observable<any[]> {
      return of([
          { id: 1, nome: 'ADMIN', descricao: 'Administrador' },
          { id: 2, nome: 'USUARIO', descricao: 'Usuário Padrão' },
          { id: 3, nome: 'VENDEDOR', descricao: 'Vendedor' },
          { id: 4, nome: 'FINANCEIRO', descricao: 'Gestor Financeiro' }
      ]);
  }
}
