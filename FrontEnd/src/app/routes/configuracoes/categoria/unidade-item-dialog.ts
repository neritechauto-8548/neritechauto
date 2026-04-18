import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { UnidadeMedidaRequest } from './unidade-medida.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'unidade-item-dialog',
  standalone: true,
  templateUrl: './unidade-item-dialog.html',
  styleUrls: ['./unidade-item-dialog.scss'],
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, CheckboxModule, MatIconModule],
})
export class UnidadeItemDialog implements OnInit {
  constructor(public ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

  form: UnidadeMedidaRequest = {
    nome: '',
    sigla: '',
    ativo: true,
  };

  isEdit = false;

  ngOnInit() {
    if (this.config.data) {
        this.isEdit = true;
        this.form = { ...this.config.data };
    }
  }

  cancelar() {
    this.ref.close(null);
  }

  confirmar() {
    const payload = {
      nome: this.form.nome,
      sigla: this.form.sigla,
      ativo: this.form.ativo,
    };
    this.ref.close(payload);
  }
}
