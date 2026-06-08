import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-checklist-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, MatIconModule],
  templateUrl: './checklist-item-dialog.html',
})
export class ChecklistItemDialog implements OnInit {
  descricao = '';
  isEdit = false;

  constructor(
    private ref: DynamicDialogRef, 
    public config: DynamicDialogConfig,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    if (this.config?.data && this.config.data.descricao) {
      this.descricao = this.config.data.descricao;
      this.isEdit = true;
    }
  }

  cancelar(): void {
    this.ref.close();
  }

  salvar(): void {
    const value = this.descricao.trim();
    if (!value) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'A descrição do item de checklist é obrigatória.' });
      return;
    }
    if (value.length < 2) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'A descrição do item de checklist deve ter pelo menos 2 caracteres.' });
      return;
    }
    this.ref.close(value);
  }
}
