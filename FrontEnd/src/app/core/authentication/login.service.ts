import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map } from 'rxjs';

import { Menu } from '@core';
import { Token, User } from './interface';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  protected readonly http = inject(HttpClient);

  login(username: string, password: string, rememberMe = false) {
    return this.http.post<Token>('/auth/login', { email: username, senha: password }).pipe(
      map((response: any) => ({
        access_token: response.accessToken || response.access_token,
        refresh_token: response.refreshToken || response.refresh_token,
        token_type: 'Bearer',
        expires_in: response.expiraEm
          ? Math.floor((new Date(response.expiraEm).getTime() - Date.now()) / 1000)
          : 86400,
      } as Token))
    );
  }

  refresh(params: Record<string, any>) {
    const refreshToken = params.refresh_token || params.refreshToken;
    return this.http.post<Token>('/auth/refresh', { refreshToken }).pipe(
      map((response: any) => ({
        access_token: response.accessToken || response.access_token,
        refresh_token: response.refreshToken || response.refresh_token,
        token_type: 'Bearer',
        expires_in: response.expiraEm
          ? Math.floor((new Date(response.expiraEm).getTime() - Date.now()) / 1000)
          : 86400,
      } as Token))
    );
  }

  logout() {
    return this.http.post<any>('/auth/logout', {});
  }

  recoverPassword(email: string) {
    return this.http.post<any>('/auth/recover-password', { email });
  }

  resetPassword(params: any) {
    return this.http.post<any>('/auth/reset-password', params);
  }

  user() {
    return this.http.get<User>('/usuarios/me').pipe(
      map((user: any) => ({
        ...user,
        name: user.nomeCompleto || user.name,
        avatar: user.avatarUrl || user.avatar,
      } as User))
    );
  }

  menu() {
    const menuUrl = window.location.origin + '/data/menu.json';
    return this.http.get<any>(menuUrl).pipe(map(res => res.menu || res.response?.menu));
  }
}
