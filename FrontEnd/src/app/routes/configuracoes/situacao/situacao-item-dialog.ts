import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ColorPicker } from 'primeng/colorpicker';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'situacao-item-dialog',
  standalone: true,
  templateUrl: './situacao-item-dialog.html',
  styleUrls: ['./situacao-item-dialog.scss'],
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, ColorPicker, MatIconModule],
})
export class SituacaoItemDialog {
  isEdit = false;
  private readonly messageService = inject(MessageService);

  constructor(public ref: DynamicDialogRef, public config: DynamicDialogConfig) {
    if (config?.data) {
      this.isEdit = !!config.data.id;
      this.form = {
        nmSituacao: config.data.nmSituacao ?? '',
        dsSituacao: config.data.dsSituacao ?? '',
        corSituacao: config.data.corSituacao ?? '#ff0000',
      };
    }
  }

  form = {
    nmSituacao: '',
    dsSituacao: '',
    corSituacao: '#2563EB',
  };

  cancelar() {
    this.ref.close(null);
  }

  salvar() {
    const nome = this.form.nmSituacao?.trim();
    if (!nome) {
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'O nome da situação é obrigatório.' });
      return;
    }
    if (nome.length < 2) {
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'O nome da situação deve ter pelo menos 2 caracteres.' });
      return;
    }
    if (nome.length > 255) {
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'O nome da situação não pode exceder 255 caracteres.' });
      return;
    }
    if (this.form.dsSituacao && this.form.dsSituacao.trim().length > 1000) {
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'A explicação não pode exceder 1000 caracteres.' });
      return;
    }

    // Normalizar a cor para garantir o prefixo '#'
    let cor = this.form.corSituacao?.trim() || '#2563EB';
    if (!cor.startsWith('#')) {
      cor = '#' + cor;
    }
    this.form.corSituacao = cor;

    this.ref.close(this.form);
  }
}
