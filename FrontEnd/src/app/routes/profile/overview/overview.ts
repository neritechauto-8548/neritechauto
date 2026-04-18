import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

import { AppSettings, SettingsService } from '@core';
import { AuthService } from '@core/authentication/auth.service';
import { User } from '@core/authentication/interface';

// Customizer Imports
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatRadioModule } from '@angular/material/radio';
import { MatDividerModule } from '@angular/material/divider';
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
    MatSlideToggleModule, 
    MatRadioModule, 
    MatDividerModule,
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

  // System Settings (Theme, Header, Nav)
  settingsForm = this.fb.nonNullable.group<AppSettings>(this.settings.options);
  private settingsSubscription = Subscription.EMPTY;

  // Profile Form
  profileForm!: FormGroup;
  
  // Password Form
  passwordForm!: FormGroup;

  user: User = {};

  ngOnInit() {
    this.authService.user().subscribe(u => {
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
      this.messageService.add({severity:'success', summary:'Sucesso', detail:'Configurações de sistema atualizadas com sucesso!'});
    });
  }

  ngOnDestroy() {
    this.settingsSubscription.unsubscribe();
  }

  saveProfile() {
    if (this.profileForm.valid) {
      this.messageService.add({severity:'success', summary:'Sucesso', detail:'Perfil atualizado com sucesso!'});
    }
  }

  savePassword() {
    const { newPassword, confirmPassword } = this.passwordForm.value;
    if (newPassword !== confirmPassword) {
      this.messageService.add({severity:'error', summary:'Erro', detail:'As senhas não coincidem!'});
      return;
    }
    this.messageService.add({severity:'success', summary:'Sucesso', detail:'Senha alterada com sucesso!'});
    this.passwordForm.reset();
  }

  onPhotoUpload(event: any) {
    this.messageService.add({severity:'success', summary:'Sucesso', detail:'Foto de perfil atualizada!'});
  }

  get isHeaderPosAbove() {
    return this.settingsForm.get('headerPos')?.value === 'above';
  }

  get isNavPosTop() {
    return this.settingsForm.get('navPos')?.value === 'top';
  }

  get isShowHeader() {
    return this.settingsForm.get('showHeader')?.value === true;
  }
}

