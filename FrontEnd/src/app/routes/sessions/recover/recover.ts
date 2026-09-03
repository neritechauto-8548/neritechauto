import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { LoginService } from '@core/authentication/login.service';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-recover',
  templateUrl: './recover.html',
  styleUrl: './recover.scss',
  providers: [MessageService],
  imports: [CommonModule, ReactiveFormsModule, RouterLink, ToastModule],
})
export class Recover {
  private readonly fb = inject(FormBuilder);
  private readonly loginService = inject(LoginService);
  private readonly messageService = inject(MessageService);

  isSubmitting = false;
  emailSent = false;

  readonly recoverForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
  });

  get email() {
    return this.recoverForm.get('email')!;
  }

  recover() {
    if (this.recoverForm.invalid) {
      this.recoverForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    this.loginService
      .recoverPassword(this.email.value.trim())
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => this.completeRecoveryRequest(),
        error: (error: HttpErrorResponse) => this.handleRecoveryError(error),
      });
  }

  private completeRecoveryRequest() {
    this.emailSent = true;
    this.messageService.add({
      severity: 'success',
      summary: 'Solicitação recebida',
      detail: 'Se a conta estiver elegível, as instruções serão enviadas para o e-mail informado.',
      life: 5000,
    });
  }

  private handleRecoveryError(error: HttpErrorResponse) {
    if (error.status === 429) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Muitas tentativas',
        detail: 'Aguarde alguns instantes antes de solicitar uma nova recuperação.',
        life: 6000,
      });
      return;
    }

    this.messageService.add({
      severity: 'error',
      summary: 'Não foi possível concluir',
      detail: 'Não foi possível processar a solicitação agora. Tente novamente mais tarde.',
      life: 5000,
    });
  }
}
