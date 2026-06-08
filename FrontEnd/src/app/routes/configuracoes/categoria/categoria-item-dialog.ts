import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'categoria-item-dialog',
  standalone: true,
  templateUrl: './categoria-item-dialog.html',
  styleUrls: ['./categoria-item-dialog.scss'],
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, CheckboxModule, MatIconModule, ToastModule],
  providers: [MessageService],
})
export class CategoriaItemDialog implements OnInit {
  constructor(
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private messageService: MessageService
  ) {}

  form: any = {
    id: null,
    nome: '',
    empresaId: 0,
    ativo: true
  };

  ngOnInit() {
    if (this.config.data) {
      this.form = { ...this.config.data };
    }
  }

  cancelar() {
    this.ref.close(null);
  }

  adicionar() {
    const nome = (this.form.nome || '').trim();

    if (!nome) {
      this.messageService.add({ severity: 'warn', summary: 'Campo obrigatório', detail: 'O nome da categoria é obrigatório.' });
      return;
    }
    if (nome.length < 2) {
      this.messageService.add({ severity: 'warn', summary: 'Campo inválido', detail: 'O nome da categoria deve ter pelo menos 2 caracteres.' });
      return;
    }
    if (nome.length > 100) {
      this.messageService.add({ severity: 'warn', summary: 'Campo inválido', detail: 'O nome da categoria não pode exceder 100 caracteres.' });
      return;
    }

    const payload = {
      nome,
      ativo: this.form.ativo
    };
    this.ref.close(payload);
  }
}
