import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { LoginService } from '@core/authentication/login.service';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs/operators';

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
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly loginService = inject(LoginService);
  private readonly messages = inject(MessageService);

  isSubmitting = false;
  showPassword = false;
  showConfirmation = false;
  readonly token = this.route.snapshot.queryParamMap.get('token') || '';
  linkError: string | null = this.token ? null : 'Link de recuperação inválido. Solicite um novo.';
  resetForm = this.fb.nonNullable.group({
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required],
  }, { validators: passwordMatchValidator });

  get password() { return this.resetForm.controls.password; }
  get confirmPassword() { return this.resetForm.controls.confirmPassword; }

  resetPassword() {
    if (this.isSubmitting || this.linkError) return;
    if (this.resetForm.invalid) {
      this.resetForm.markAllAsTouched();
      return;
    }
    this.isSubmitting = true;
    this.loginService.resetPassword({ token: this.token, novaSenha: this.password.value })
      .pipe(finalize(() => this.isSubmitting = false))
      .subscribe({
        next: () => {
          this.messages.add({ severity: 'success', summary: 'Senha atualizada', detail: 'Entre com sua nova senha.' });
          this.router.navigateByUrl('/auth/login');
        },
        error: (error: HttpErrorResponse) => {
          const message = typeof error.error?.message === 'string' ? error.error.message : '';
          if (error.status === 400 && /link|token/i.test(message)) {
            this.linkError = /expirou|expirado/i.test(message) ? 'Este link expirou. Solicite um novo.'
              : /utilizado/i.test(message) ? 'Este link já foi utilizado. Solicite um novo.'
              : 'Link de recuperação inválido. Solicite um novo.';
          } else if (error.status === 422) {
            this.password.setErrors({ remote: 'A senha não atende aos requisitos de segurança.' });
          } else {
            this.messages.add({ severity: 'error', summary: 'Não foi possível atualizar',
              detail: error.status === 0 ? 'Sem conexão com o servidor. Tente novamente.' : 'Tente novamente em instantes.' });
          }
        },
      });
  }
}
