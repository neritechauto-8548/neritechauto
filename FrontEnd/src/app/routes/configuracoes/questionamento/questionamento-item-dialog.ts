import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';
import { TipoPergunta, PerguntaItem } from './questionamento';

@Component({
  selector: 'app-questionamento-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, Select, MatIconModule],
  templateUrl: './questionamento-item-dialog.html',
})
export class QuestionamentoItemDialog implements OnInit {
  descricao = '';
  tipo: TipoPergunta = 'avaliacao';
  isEdit = false;

  tipos = [
    { label: 'Avaliação 1 a 10', value: 'avaliacao' },
    { label: 'Sim ou Não', value: 'simnao' },
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
    if (value) {
      const result: PerguntaItem = { texto: value, tipo: this.tipo };
      this.ref.close(result);
    }
  }
}
