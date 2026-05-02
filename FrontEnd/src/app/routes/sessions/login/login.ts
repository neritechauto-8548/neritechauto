import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { MtxButtonModule } from '@ng-matero/extensions/button';
import { TranslateModule } from '@ngx-translate/core';
import { filter } from 'rxjs/operators';

import { AuthService } from '@core/authentication';
import { LocalStorageService } from '@shared/services/storage.service';

import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.scss',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    TranslateModule,
    ToastModule,
  ],
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);
  private readonly storage = inject(LocalStorageService);
  private readonly messageService = inject(MessageService);

  isSubmitting = false;
  showPassword = false;

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  loginForm = this.fb.nonNullable.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
    rememberMe: [true],
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

  constructor() {}

  login() {
    this.isSubmitting = true;

    this.auth
      .login(this.username.value, this.password.value, this.rememberMe.value)
      .pipe(filter(authenticated => authenticated))
      .subscribe({
        next: () => {
          if (this.rememberMe.value) {
            this.storage.set('loginDefaults', { username: this.username.value, password: this.password.value });
          }
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Login realizado com sucesso!' });
          this.router.navigateByUrl('/');
        },
        error: (errorRes: HttpErrorResponse) => {
          if (errorRes.status === 401) {
            const form = this.loginForm;
            const message = errorRes.error?.message || 'Credenciais inválidas';
            form.get('username')?.setErrors({ remote: message });
            form.get('password')?.setErrors({ remote: message });
          } else if (errorRes.status === 422) {
            const form = this.loginForm;
            const errors = errorRes.error?.errors;
            if (Array.isArray(errors)) {
              errors.forEach((err: string) => {
                const parts = String(err).split(':');
                const field = parts[0]?.trim();
                const message = parts.slice(1).join(':').trim() || err;
                const controlKey = field === 'email' ? 'username' : field === 'senha' ? 'password' : undefined;
                if (controlKey && form.get(controlKey)) {
                  form.get(controlKey)?.setErrors({ remote: message });
                } else {
                  form.get('username')?.setErrors({ remote: message });
                }
              });
            } else if (errors && typeof errors === 'object') {
              Object.keys(errors).forEach(key => {
                const controlKey = key === 'email' ? 'username' : key;
                const msg = Array.isArray(errors[key]) ? errors[key][0] : errors[key];
                form.get(controlKey)?.setErrors({ remote: msg });
              });
            }
            this.messageService.add({ 
              severity: 'error', 
              summary: 'Erro de validação', 
              detail: 'Por favor, verifique os campos destacados.' 
            });
          } else {
            this.messageService.add({ 
              severity: 'error', 
              summary: 'Erro de Login', 
              detail: errorRes.error?.message || 'E-mail ou senha incorretos.' 
            });
          }
          this.isSubmitting = false;
        },
      });
  }
}
