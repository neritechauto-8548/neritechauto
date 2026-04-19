import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormsModule, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { HotToastService } from '@ngxpert/hot-toast';
import { TranslateModule } from '@ngx-translate/core';
import { LoginService } from '@core/authentication/login.service';

export const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password');
  const confirmPassword = control.get('confirmPassword');
  return password && confirmPassword && password.value !== confirmPassword.value ? { mismatch: true } : null;
};

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.scss',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,

    TranslateModule,
  ],
})
export class ResetPassword implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly loginService = inject(LoginService);
  private readonly toast = inject(HotToastService);

  isSubmitting = false;
  token = '';

  resetForm = this.fb.nonNullable.group({
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', [Validators.required]],
  }, { validators: [passwordMatchValidator] });

  get password() {
    return this.resetForm.get('password')!;
  }

  get confirmPassword() {
    return this.resetForm.get('confirmPassword')!;
  }

  ngOnInit() {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    if (!this.token) {
        this.toast.error('Token de recuperação ausente ou inválido.', { duration: 5000 });
        this.router.navigateByUrl('/auth/login');
    }
  }

  resetPassword() {
    this.isSubmitting = true;
    
    this.loginService.resetPassword({
        token: this.token,
        novaSenha: this.password.value
    }).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.toast.success('Senha atualizada com sucesso! Agora você já pode logar.', {
          duration: 5000,
        });
        this.router.navigateByUrl('/auth/login');
      },
      error: (err) => {
        this.isSubmitting = false;
        const msg = err.error?.message || 'Erro ao redefinir senha. O link pode ter expirado.';
        this.toast.error(msg, {
          duration: 7000,
        });
      }
    });
  }
}
