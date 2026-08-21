import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs/operators';

import { AuthService } from '@core/authentication';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.scss',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);
  private readonly messageService = inject(MessageService);

  isSubmitting = false;
  showPassword = false;

  readonly loginForm = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  get username() {
    return this.loginForm.get('username')!;
  }

  get password() {
    return this.loginForm.get('password')!;
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  login() {
    this.clearRemoteErrors();

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    this.auth
      .login(this.username.value.trim(), this.password.value)
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Acesso liberado',
            detail: 'Login realizado com sucesso.',
          });
          this.router.navigateByUrl('/home');
        },
        error: (errorRes: HttpErrorResponse) => this.handleLoginError(errorRes),
      });
  }

  private clearRemoteErrors() {
    for (const control of [this.username, this.password]) {
      if (!control.hasError('remote')) {
        continue;
      }

      const currentErrors = { ...(control.errors || {}) };
      delete currentErrors['remote'];
      control.setErrors(Object.keys(currentErrors).length ? currentErrors : null);
    }
  }

  private handleLoginError(errorRes: HttpErrorResponse) {
    if (errorRes.status === 401) {
      const message = 'E-mail ou senha inválidos.';
      this.username.setErrors({ ...(this.username.errors || {}), remote: message });
      this.password.setErrors({ ...(this.password.errors || {}), remote: message });
      return;
    }

    if (errorRes.status === 422) {
      this.applyValidationErrors(errorRes.error?.errors);
      this.messageService.add({
        severity: 'error',
        summary: 'Verifique os dados',
        detail: 'Revise os campos destacados e tente novamente.',
      });
      return;
    }

    this.messageService.add({
      severity: 'error',
      summary: 'Não foi possível entrar',
      detail: 'Não foi possível concluir o acesso agora. Tente novamente.',
    });
  }

  private applyValidationErrors(errors: unknown) {
    if (Array.isArray(errors)) {
      errors.forEach(error => {
        const [field, ...messageParts] = String(error).split(':');
        this.setRemoteError(field?.trim(), messageParts.join(':').trim() || 'Valor inválido.');
      });
      return;
    }

    if (errors && typeof errors === 'object') {
      Object.entries(errors as Record<string, unknown>).forEach(([field, message]) => {
        const detail = Array.isArray(message) ? String(message[0]) : String(message);
        this.setRemoteError(field, detail);
      });
    }
  }

  private setRemoteError(field: string | undefined, message: string) {
    const control = field === 'email' ? this.username : field === 'senha' ? this.password : undefined;
    const target = control || this.username;
    target.setErrors({ ...(target.errors || {}), remote: message });
  }
}
