import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-checklist-modelo-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, MatIconModule],
  templateUrl: './checklist-modelo-dialog.html',
})
export class ChecklistModeloDialog implements OnInit {
  titulo = '';
  isEdit = false;

  constructor(
    private ref: DynamicDialogRef, 
    public config: DynamicDialogConfig,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    if (this.config?.data && this.config.data.titulo) {
      this.titulo = this.config.data.titulo;
      this.isEdit = true;
    }
  }

  cancelar(): void {
    this.ref.close();
  }

  salvar(): void {
    const valor = (this.titulo || '').trim();
    if (!valor) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O título do checklist é obrigatório.' });
      return;
    }
    if (valor.length < 2) {
      this.messageService.add({ severity: 'error', summary: 'Validação', detail: 'O título do checklist deve ter pelo menos 2 caracteres.' });
      return;
    }
    this.ref.close({ titulo: valor });
  }
}
