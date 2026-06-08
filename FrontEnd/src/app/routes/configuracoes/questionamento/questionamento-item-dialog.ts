import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { TipoPergunta, PerguntaItem } from './questionamento';

@Component({
  selector: 'app-questionamento-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, Select, MatIconModule, ToastModule],
  templateUrl: './questionamento-item-dialog.html',
})
export class QuestionamentoItemDialog implements OnInit {
  descricao = '';
  tipo: TipoPergunta = 'avaliacao';
  isEdit = false;
  private readonly messageService = inject(MessageService);

  tipos = [
    { label: 'Avaliação 1 a 10', value: 'avaliacao' },
    { label: 'Sim ou Não', value: 'simnao' },
    { label: 'Aberta (Texto)', value: 'aberta' },
  ];

  constructor(private ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

  ngOnInit() {
    if (this.config?.data) {
      this.descricao = this.config.data.texto || '';
      this.tipo = this.config.data.tipo || 'avaliacao';
      this.isEdit = !!this.config.data.texto;
    }
  }

  cancelar(): void {
    this.ref.close();
  }

  salvar(): void {
    const value = this.descricao.trim();
    if (!value) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'A descrição da pergunta é obrigatória.' });
      return;
    }
    if (value.length < 2) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'A descrição da pergunta deve ter pelo menos 2 caracteres.' });
      return;
    }
    if (value.length > 255) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'A descrição da pergunta não pode exceder 255 caracteres.' });
      return;
    }
    if (!this.tipo) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'O tipo de resposta da pergunta é obrigatório.' });
      return;
    }
    const result: PerguntaItem = { texto: value, tipo: this.tipo };
    this.ref.close(result);
  }
}
