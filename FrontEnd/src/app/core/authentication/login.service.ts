import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map } from 'rxjs';

import { Menu } from '@core';
import { Token, User } from './interface';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  protected readonly http = inject(HttpClient);
  private readonly authFeedback = { headers: new HttpHeaders({ 'X-Skip-Toast': 'true' }) };

  login(username: string, password: string, rememberMe = false) {
    return this.http.post<Token>('/auth/login', {
      email: username.trim(), senha: password,
    }, this.authFeedback).pipe(map(response => this.normalizeToken(response)));
  }

  refresh(params: Record<string, any>) {
    const refreshToken = params.refresh_token || params.refreshToken;
    return this.http.post<Token>('/auth/refresh', { refreshToken }).pipe(
      map(response => this.normalizeToken(response))
    );
  }

  private normalizeToken(response: any): Token {
    const accessToken = response?.accessToken || response?.access_token;
    if (typeof accessToken !== 'string' || !accessToken.trim()) {
      throw new HttpErrorResponse({ status: 502, statusText: 'Resposta de autenticação inválida' });
    }
    const expiration = Number(response?.expiresIn);
    const expiresIn = Number.isFinite(expiration) && expiration > 0
      ? Math.floor(expiration / 1000)
      : response?.expiraEm
        ? Math.max(0, Math.floor((new Date(response.expiraEm).getTime() - Date.now()) / 1000))
        : undefined;
    return {
      access_token: accessToken,
      refresh_token: response?.refreshToken || response?.refresh_token,
      token_type: 'Bearer',
      ...(expiresIn !== undefined ? { expires_in: expiresIn } : {}),
    } as Token;
  }

  logout() {
    return this.http.post<any>('/auth/logout', {});
  }

  recoverPassword(email: string) {
    return this.http.post<any>('/auth/recover-password', { email }, this.authFeedback);
  }

  resetPassword(params: any) {
    return this.http.post<any>('/auth/reset-password', params, this.authFeedback);
  }

  user() {
    return this.http.get<User>('/usuarios/me').pipe(
      map((user: any) => {
        return {
          ...user,
          name: user.nomeCompleto || user.name,
          avatar: user.avatarUrl || user.avatar
        } as User;
      })
    );
  }

  menu() {
    // Busca o arquivo estático da própria URL do frontend, ignorando o BaseUrlInterceptor
    const menuUrl = window.location.origin + '/data/menu.json';
    return this.http.get<any>(menuUrl).pipe(map(res => res.menu || res.response?.menu));
  }
}
