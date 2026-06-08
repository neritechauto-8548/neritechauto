import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'localizacao-item-dialog',
  standalone: true,
  templateUrl: './localizacao-item-dialog.html',
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule],
})
export class LocalizacaoItemDialog implements OnInit {
  descricao = '';
  private readonly messageService = inject(MessageService);

  constructor(
    private ref: DynamicDialogRef<LocalizacaoItemDialog>,
    private config: DynamicDialogConfig
  ) {}

  ngOnInit() {
    if (this.config.data) {
      this.descricao = this.config.data.descricao || '';
    }
  }

  cancelar() {
    this.ref.close();
  }

  salvar() {
    const desc = this.descricao?.trim();
    if (!desc) {
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'A descrição da localização é obrigatória.' });
      return;
    }
    if (desc.length < 2) {
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'A descrição da localização deve ter pelo menos 2 caracteres.' });
      return;
    }
    if (desc.length > 255) {
      this.messageService.add({ severity: 'error', summary: 'Erro de Validação', detail: 'A descrição da localização não pode exceder 255 caracteres.' });
      return;
    }

    this.ref.close({ descricao: desc });
  }
}
