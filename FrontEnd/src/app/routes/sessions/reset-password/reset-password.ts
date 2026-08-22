import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { LoginService } from '@core/authentication/login.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { finalize } from 'rxjs/operators';

export const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;
  return password && confirmPassword && password !== confirmPassword ? { mismatch: true } : null;
};

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.scss',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
})
export class ResetPassword implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly loginService = inject(LoginService);
  private readonly toast = inject(HotToastService);

  isSubmitting = false;
  invalidToken = false;
  passwordUpdated = false;
  showPassword = false;
  showConfirmPassword = false;
  token = '';

  readonly resetForm = this.fb.nonNullable.group(
    {
      // Mantém o frontend alinhado ao contrato atual do backend (@Size(min = 6)).
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
    },
    { validators: [passwordMatchValidator] }
  );

  get password() {
    return this.resetForm.get('password')!;
  }

  get confirmPassword() {
    return this.resetForm.get('confirmPassword')!;
  }

  ngOnInit() {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    this.invalidToken = !this.token;
  }

  togglePasswordVisibility(field: 'password' | 'confirmPassword') {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
      return;
    }

    this.showConfirmPassword = !this.showConfirmPassword;
  }

  resetPassword() {
    if (!this.token || this.invalidToken || this.passwordUpdated) {
      return;
    }

    if (this.resetForm.invalid) {
      this.resetForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    this.loginService
      .resetPassword({ token: this.token, novaSenha: this.password.value })
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => {
          this.passwordUpdated = true;
          this.resetForm.disable();
          this.toast.success('Senha atualizada com segurança.', { duration: 5000 });
        },
        error: (error: HttpErrorResponse) => this.handleResetError(error),
      });
  }

  private handleResetError(error: HttpErrorResponse) {
    if ([400, 401, 404, 410].includes(error.status)) {
      this.invalidToken = true;
      this.resetForm.disable();
      this.toast.error('Este link de recuperação é inválido, já foi utilizado ou expirou.', {
        duration: 6500,
      });
      return;
    }

    if (error.status === 429) {
      this.toast.error('Muitas tentativas. Tente novamente em alguns instantes.', { duration: 6000 });
      return;
    }

    this.toast.error('Não foi possível atualizar a senha agora. Tente novamente mais tarde.', {
      duration: 6000,
    });
  }
}
