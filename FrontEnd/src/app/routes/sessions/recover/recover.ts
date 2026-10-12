import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { LoginService } from '@core/authentication/login.service';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import { MessageService } from 'primeng/api';


@Component({
  selector: 'app-recover',
  templateUrl: './recover.html',
  styleUrl: './recover.scss',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    NeriTechIcon,
  ],
})
export class Recover {
  private readonly fb = inject(FormBuilder);
  private readonly loginService = inject(LoginService);
  private readonly messageService = inject(MessageService);

  isSubmitting = false;
  emailSent = false;


  recoverForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
  });

  get email() {
    return this.recoverForm.get('email')!;
  }

  recover() {
    if (this.isSubmitting) return;
    if (this.recoverForm.invalid) {
      this.recoverForm.markAllAsTouched();
      return;
    }
    this.isSubmitting = true;
    
    this.loginService.recoverPassword(this.email.value).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.emailSent = true;
      },
      error: () => {
        this.isSubmitting = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: 'Ocorreu um erro ao processar sua solicitação. Tente novamente.',
          life: 5000
        });
      }
    });
  }
}
