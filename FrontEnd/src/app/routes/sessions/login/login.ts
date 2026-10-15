import { HttpErrorResponse } from '@angular/common/http';
import { EMPTY, catchError, filter, map, switchMap, take, timeout, throwError } from 'rxjs';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { AuthService } from '@core/authentication';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import { LocalStorageService } from '@shared/services/storage.service';

import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.scss',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    NeriTechIcon,
  ],
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly auth = inject(AuthService);
  private readonly storage = inject(LocalStorageService);
  private readonly messageService = inject(MessageService);

  isSubmitting = false;
  showPassword = false;
  loginError = '';

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  loginForm = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
    rememberMe: [false],
  });

  get username() {
    return this.loginForm.get('username')!;
  }

  get password() {
    return this.loginForm.get('password')!;
  }

  get rememberMe() {
    return this.loginForm.get('rememberMe')!;
  }

  constructor() {
    const remembered = this.storage.get('loginDefaults') as { username?: string };
    if (remembered?.username) this.loginForm.patchValue({ username: remembered.username, rememberMe: true });
  }

  login() {
    if (this.isSubmitting) return;
    this.loginError = '';
    const email = this.username.value.trim();
    this.username.setValue(email, { emitEvent: false });
    for (const control of [this.username, this.password]) {
      if (control.hasError('remote')) control.updateValueAndValidity({ emitEvent: false });
    }

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.auth.login(email, this.password.value, this.rememberMe.value).pipe(
      switchMap(authenticated => {
        if (!authenticated) {
          return throwError(() => new HttpErrorResponse({
            status: 502, statusText: 'Resposta de autenticação inválida',
          }));
        }
        // The JWT alone is not authorization: require a real /usuarios/me
        // response with an active tenant before entering an authenticated page.
        return this.auth.user().pipe(
          filter(profile => !!profile && !!profile.id && !!(profile as any).empresaId),
          take(1),
          timeout(10000),
          map(() => true)
        );
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          const message = 'E-mail ou senha inválidos.';
          this.username.setErrors({ remote: message });
          this.password.setErrors({ remote: message });
          this.loginError = message;
        } else if (error.status === 429) {
          this.loginError = 'Muitas tentativas de acesso. Aguarde 15 minutos antes de tentar novamente.';
        } else if (error.status === 403) {
          this.loginError = 'Acesso não permitido. Entre em contato com o administrador da oficina.';
        } else if (error.status === 422) {
          const errors = error.error?.errors;
          if (errors && typeof errors === 'object' && !Array.isArray(errors)) {
            for (const [field, detail] of Object.entries(errors)) {
              const key = field === 'email' ? 'username' : field === 'senha' ? 'password' : field;
              const control = this.loginForm.get(key);
              if (control) control.setErrors({ remote: String(detail) });
            }
          }
          this.loginError = 'Confira os dados informados e tente novamente.';
        } else if (error.status === 0) {
          this.loginError = 'Sem conexão com o servidor. Verifique sua rede e tente novamente.';
        } else if (error.status === 409) {
          this.loginError = 'Não foi possível iniciar a sessão. Atualize a página e tente novamente.';
        } else {
          this.loginError = 'Não foi possível validar sua sessão. Tente novamente em instantes.';
        }
        return EMPTY;
      }),
      finalize(() => this.isSubmitting = false)
    ).subscribe(() => {
      // Never retain the password after successful authentication.
      this.password.setValue('', { emitEvent: false });
      if (this.rememberMe.value) {
        this.storage.set('loginDefaults', { username: email });
      } else {
        this.storage.remove('loginDefaults');
      }
      const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
      const destination = returnUrl?.startsWith('/') && !returnUrl.startsWith('//') &&
        !returnUrl.startsWith('/auth/') && !returnUrl.includes('\\')
          ? returnUrl : '/home';
      this.router.navigateByUrl(destination);
    });
  }
}
