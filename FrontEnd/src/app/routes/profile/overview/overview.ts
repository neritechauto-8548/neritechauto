import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

import { AppSettings, SettingsService } from '@core';
import { AuthService } from '@core/authentication/auth.service';
import { User } from '@core/authentication/interface';
import { ProfileAvatarService } from '../../../theme/widgets/profile-avatar.service';
import { environment } from '@env/environment';

// Customizer Imports
import { MatRadioModule } from '@angular/material/radio';
import { MatButtonModule } from '@angular/material/button';

// PrimeNG Imports
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { PasswordModule } from 'primeng/password';
import { FileUploadModule } from 'primeng/fileupload';

@Component({
  selector: 'app-profile-overview',
  templateUrl: './overview.html',
  styleUrl: './overview.scss',
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    FormsModule, 
    MatRadioModule, 
    MatButtonModule,
    ToastModule,
    InputTextModule,
    ButtonModule,
    PasswordModule,
    FileUploadModule
  ],
  providers: [MessageService],
})
export class ProfileOverview implements OnInit, OnDestroy {
  private readonly fb = inject(FormBuilder);
  private readonly settings = inject(SettingsService);
  private readonly authService = inject(AuthService);
  private readonly messageService = inject(MessageService);
  readonly avatar = inject(ProfileAvatarService);
  readonly uxPreview = environment.uxPreview;
  private userSubscription = Subscription.EMPTY;

  // System Settings (Theme, Header, Nav)
  settingsForm = this.fb.nonNullable.group<AppSettings>(this.settings.options);
  private settingsSubscription = Subscription.EMPTY;

  // Profile Form
  profileForm!: FormGroup;
  
  // Password Form
  passwordForm!: FormGroup;

  user: User = {};

  get avatarSrc(): string {
    return this.avatar.src(this.user);
  }

  onAvatarError(): void {
    this.avatar.reportImageError(this.avatarSrc);
  }

  onPhotoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.item(0);
    if (!file) return;
    const error = this.avatar.selectLocalPreview(file);
    if (error) {
      this.messageService.add({ severity: 'warn', summary: 'Foto de perfil', detail: error });
    } else {
      this.messageService.add({
        severity: 'info', summary: 'Prévia atualizada',
        detail: 'A foto será exibida apenas nesta sessão de homologação, sem gravação no servidor.'
      });
    }
    input.value = '';
  }

  ngOnInit() {
    this.userSubscription = this.authService.user().subscribe(u => {
      this.user = u;
      this.profileForm = this.fb.group({
        name: [u.name || ''],
        email: [u.email || ''],
      });
    });

    this.passwordForm = this.fb.group({
      currentPassword: [''],
      newPassword: [''],
      confirmPassword: ['']
    });

    this.settingsSubscription = this.settingsForm.valueChanges.subscribe(value => {
      this.settings.setOptions(this.settingsForm.getRawValue());
      const theme = this.settingsForm.get('theme')?.value;
      if (theme) {
        this.settings.setTheme(theme);
      }
      // Preferência local aplicada; não indicar persistência em backend.
    });
  }

  ngOnDestroy() {
    this.settingsSubscription.unsubscribe();
    this.userSubscription.unsubscribe();
  }

  saveProfile() {
    this.messageService.add({severity:'info', summary:'Indisponível', detail:'A edição do perfil necessita de integração segura com o servidor.'});
  }

  savePassword() {
    const { newPassword, confirmPassword } = this.passwordForm.value;
    if (newPassword !== confirmPassword) {
      this.messageService.add({severity:'error', summary:'Erro', detail:'As senhas não coincidem!'});
      return;
    }
    this.messageService.add({severity:'info', summary:'Indisponível', detail:'A atualização de senha ainda não possui fluxo autenticado de confirmação.'});
  }

}

