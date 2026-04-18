import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map, of } from 'rxjs';

import { Menu } from '@core';
import { Token, User } from './interface';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  protected readonly http = inject(HttpClient);

  login(username: string, password: string, rememberMe = false) {
    const tenantId = localStorage.getItem('tenantId') || '1';
    return this.http.post<Token>(
      '/api/auth/login',
      { email: username, senha: password },
      { headers: { 'X-Tenant-Id': tenantId } }
    ).pipe(
      map((response: any) => {
        // Converter camelCase do backend para snake_case esperado pelo frontend
        return {
          access_token: response.accessToken || response.access_token,
          refresh_token: response.refreshToken || response.refresh_token,
          token_type: 'Bearer',
          expires_in: response.expiraEm ? Math.floor((new Date(response.expiraEm).getTime() - Date.now()) / 1000) : 86400
        } as Token;
      })
    );
  }

  refresh(params: Record<string, any>) {
    const tenantId = localStorage.getItem('tenantId') || '1';
    const refreshToken = params.refresh_token || params.refreshToken;
    return this.http.post<Token>('/api/auth/refresh', { refreshToken }, { headers: { 'X-Tenant-Id': tenantId } }).pipe(
      map((response: any) => {
        // Converter camelCase do backend para snake_case esperado pelo frontend
        return {
          access_token: response.accessToken || response.access_token,
          refresh_token: response.refreshToken || response.refresh_token,
          token_type: 'Bearer',
          expires_in: response.expiraEm ? Math.floor((new Date(response.expiraEm).getTime() - Date.now()) / 1000) : 86400
        } as Token;
      })
    );
  }

  logout() {
    return this.http.post<any>('/api/auth/logout', {});
  }

  user() {
    return this.http.get<User>('/api/usuarios/me');
  }

  menu() {
    return this.http.get<{ menu: Menu[] }>('/user/menu').pipe(map(res => res.menu));
  }
}
