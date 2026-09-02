import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@core';
import { NeriTechIcon } from '../../../shared/components';
import { OsCommunicationService } from './os-communication.service';

@Component({
  selector: 'os-communication-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, NeriTechIcon],
  templateUrl: './os-communication-panel.html',
  styleUrl: './os-communication-panel.scss',
})
export class OsCommunicationPanel {
  private readonly service = inject(OsCommunicationService);
  private readonly auth = inject(AuthService);
  private readonly cdr = inject(ChangeDetectorRef);

  @Input({ required: true }) osId!: number;

  email = '';
  sending = false;
  message = '';
  success = false;

  get canSend(): boolean {
    const user = this.auth.snapshot();
    const permissions = (user.permissions ?? []).map(value => String(value));
    const roles = (user.roles ?? []).map(value => String(value));
    return permissions.includes('OS_EDITAR') || roles.includes('ADMIN') || roles.includes('ROLE_ADMIN');
  }

  send(): void {
    if (!this.canSend || this.sending || !Number.isInteger(this.osId) || this.osId <= 0) return;
    this.sending = true;
    this.message = '';
    this.success = false;
    this.service.sendEmail(this.osId, this.email).subscribe({
      next: () => {
        this.sending = false;
        this.success = true;
        this.message = this.email.trim()
          ? `E-mail enviado para ${this.email.trim()}.`
          : 'E-mail enviado para o endereço cadastrado do cliente.';
        this.cdr.markForCheck();
      },
      error: error => {
        this.sending = false;
        this.success = false;
        this.message = error?.error?.message || error?.message || 'Não foi possível enviar a Ordem de Serviço por e-mail.';
        this.cdr.markForCheck();
      },
    });
  }
}
