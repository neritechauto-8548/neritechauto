import { CommonModule, Location } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { LoginService } from '@core/authentication/login.service';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import { finalize, timeout } from 'rxjs';

export const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password');
  const confirmation = control.get('confirmPassword');
  return password && confirmation && password.value !== confirmation.value ? { mismatch: true } : null;
};

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.scss',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, NeriTechIcon],
})
export class ResetPassword {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly location = inject(Location);
  private readonly loginService = inject(LoginService);

  isSubmitting = false;
  showPassword = false;
  showConfirmation = false;
  completed = false;
  errorMessage = '';
  // Query-string links issued earlier remain valid; new links use #token
  // so the credential is never sent to the frontend server as a request URL.
  private readonly token = this.route.snapshot.queryParamMap.get('token')
    || new URLSearchParams(this.route.snapshot.fragment || '').get('token') || '';
  linkError: string | null = this.token ? null : 'Link de recuperação inválido. Solicite um novo.';
  resetForm = this.fb.nonNullable.group({
    password: ['', [Validators.required, Validators.minLength(12), Validators.maxLength(128)]],
    confirmPassword: ['', Validators.required],
  }, { validators: passwordMatchValidator });

  constructor() {
    // Do not keep the recovery credential in the visible URL or history.
    if (this.token) this.location.replaceState('/auth/reset-password');
  }

  get password() { return this.resetForm.controls.password; }
  get confirmPassword() { return this.resetForm.controls.confirmPassword; }

  resetPassword() {
    if (this.isSubmitting || this.completed || this.linkError) return;
    this.errorMessage = '';
    if (this.resetForm.invalid) {
      this.resetForm.markAllAsTouched();
      return;
    }
    this.isSubmitting = true;
    this.loginService.resetPassword({ token: this.token, novaSenha: this.password.value })
      .pipe(timeout(15000), finalize(() => this.isSubmitting = false))
      .subscribe({
        next: () => {
          // Only the confirmed backend response can show a successful reset.
          this.completed = true;
          this.resetForm.reset();
          this.showPassword = false;
          this.showConfirmation = false;
        },
        error: (error: HttpErrorResponse | Error) => {
          if (!(error instanceof HttpErrorResponse)) {
            this.errorMessage = 'O servidor não respondeu a tempo. Verifique a conexão antes de tentar novamente.';
            return;
          }
          const message = typeof error.error?.message === 'string' ? error.error.message : '';
          if ((error.status === 400 && /link|token|expir|utiliz/i.test(message))
              || error.status === 409 || error.status === 410) {
            this.linkError = /expir/i.test(message) ? 'Este link expirou. Solicite um novo.'
              : /utiliz/i.test(message) || error.status === 409
                ? 'Este link já foi utilizado. Solicite um novo.'
                : 'Link de recuperação inválido. Solicite um novo.';
          } else if (error.status === 422) {
            this.password.setErrors({ remote: 'Use uma senha segura de 12 a 128 caracteres, diferente da anterior.' });
            this.password.markAsTouched();
            this.errorMessage = 'A nova senha não atende à política de segurança.';
          } else if (error.status === 0) {
            this.errorMessage = 'Sem conexão com o servidor. Verifique sua rede antes de tentar novamente.';
          } else if (error.status === 429) {
            this.errorMessage = 'Muitas tentativas. Aguarde alguns minutos antes de tentar novamente.';
          } else {
            this.errorMessage = 'Não foi possível confirmar a atualização. Tente novamente ou solicite outro link.';
          }
        },
      });
  }
}
