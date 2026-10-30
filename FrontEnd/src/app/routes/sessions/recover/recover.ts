import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { LoginService } from '@core/authentication/login.service';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import { finalize, timeout } from 'rxjs';

@Component({
  selector: 'app-recover',
  templateUrl: './recover.html',
  styleUrl: './recover.scss',
  imports: [CommonModule, ReactiveFormsModule, RouterLink, NeriTechIcon],
})
export class Recover {
  private readonly fb = inject(FormBuilder);
  private readonly loginService = inject(LoginService);

  isSubmitting = false;
  emailSent = false;
  errorMessage = '';
  showLoginHelp = false;

  recoverForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
  });

  get email() { return this.recoverForm.controls.email; }

  recover() {
    if (this.isSubmitting || this.emailSent) return;
    this.errorMessage = '';
    this.email.setValue(this.email.value.trim(), { emitEvent: false });
    if (this.recoverForm.invalid) {
      this.recoverForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.loginService.recoverPassword(this.email.value)
      .pipe(timeout(15000), finalize(() => this.isSubmitting = false))
      .subscribe({
        next: () => {
          // Never disclose whether this email is registered or sent.
          this.emailSent = true;
        },
        error: (error: HttpErrorResponse | Error) => {
          if (error instanceof HttpErrorResponse && error.status === 429) {
            this.errorMessage = 'Muitas solicitações deste dispositivo. Aguarde alguns minutos e tente novamente.';
          } else if (error instanceof HttpErrorResponse && error.status === 503) {
            this.errorMessage = 'O serviço de recuperação está indisponível no momento. Tente novamente mais tarde ou contate o suporte.';
          } else if (error instanceof HttpErrorResponse && error.status === 0) {
            this.errorMessage = 'Sem conexão com o servidor. Verifique sua rede e tente novamente.';
          } else {
            this.errorMessage = 'Não foi possível processar a solicitação agora. Tente novamente em instantes.';
          }
        },
      });
  }
}
