import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'categoria-item-dialog',
  standalone: true,
  templateUrl: './categoria-item-dialog.html',
  styleUrls: ['./categoria-item-dialog.scss'],
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, CheckboxModule, MatIconModule],
})
export class CategoriaItemDialog implements OnInit {
  constructor(public ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

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
    const payload = {
      nome: this.form.nome,
      ativo: this.form.ativo
    };
    this.ref.close(payload);
  }
}
