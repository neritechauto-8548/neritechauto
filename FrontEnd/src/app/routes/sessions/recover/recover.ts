import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';
import { LoginService } from '@core/authentication/login.service';
import { HotToastService } from '@ngxpert/hot-toast';

@Component({
  selector: 'app-recover',
  templateUrl: './recover.html',
  styleUrl: './recover.scss',
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
export class Recover {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly loginService = inject(LoginService);
  private readonly toast = inject(HotToastService);

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
        this.toast.success('Se este e-mail estiver cadastrado, você receberá um link em breve.', {
          duration: 5000,
          position: 'top-center'
        });
      },
      error: () => {
        this.isSubmitting = false;
        // Mesmo em erro de e-mail não encontrado, por segurança, podemos mostrar a mesma mensagem ou algo neutro
        this.toast.error('Ocorreu um erro ao processar sua solicitação. Tente novamente.', {
          duration: 5000,
        });
      }
    });
  }
}
