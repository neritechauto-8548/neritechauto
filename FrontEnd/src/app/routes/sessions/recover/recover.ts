import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';

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

  isSubmitting = false;

  recoverForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
  });

  get email() {
    return this.recoverForm.get('email')!;
  }

  recover() {
    this.isSubmitting = true;
    // TODO: Integrar com o serviço real de recuperação de senha
    setTimeout(() => {
      this.isSubmitting = false;
      // Após enviar, opcionalmente redirecionar para login
      this.router.navigateByUrl('/auth/login');
    }, 600);
  }
}
