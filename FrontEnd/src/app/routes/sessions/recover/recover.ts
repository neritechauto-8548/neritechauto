import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';
import { LoginService } from '@core/authentication/login.service';
import { MessageService } from 'primeng/api';

import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-recover',
  templateUrl: './recover.html',
  styleUrl: './recover.scss',
  providers: [MessageService],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ToastModule,
    TranslateModule,
  ],
})
export class Recover {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
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
    this.isSubmitting = true;
    
    this.loginService.recoverPassword(this.email.value).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.emailSent = true;
        this.messageService.add({
          severity: 'success',
          summary: 'E-mail Enviado',
          detail: 'Se este e-mail estiver cadastrado, você receberá um link em breve.',
          life: 5000
        });
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
