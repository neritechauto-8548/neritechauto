import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';

@Component({
  selector: 'localizacao-item-dialog',
  standalone: true,
  templateUrl: './localizacao-item-dialog.html',
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule],
})
export class LocalizacaoItemDialog implements OnInit {
  descricao = '';

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
    if (this.descricao && this.descricao.trim()) {
      this.ref.close({ descricao: this.descricao.trim() });
    } else {
      this.ref.close();
    }
  }
}
