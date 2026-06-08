import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-departamento-item-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, MatIconModule],
  templateUrl: './departamento-item-dialog.html',
})
export class DepartamentoItemDialog implements OnInit {
  descricao = '';
  isEdit = false;

  constructor(private ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

  ngOnInit() {
    if (this.config?.data && this.config.data.descricao) {
      this.isEdit = true;
      this.descricao = this.config.data.descricao;
    }
  }

  cancelar(): void {
    this.ref.close();
  }

  salvar(): void {
    const desc = this.descricao ? this.descricao.trim() : '';
    if (!desc) {
      // should be disabled in UI, but keep safe
      return;
    }
    if (desc.length < 2) {
      // we don't have messageService here, but we can return validation detail or handle it in parent component. Let's return it so parent can show toast.
      this.ref.close({ error: 'A descrição do departamento deve ter pelo menos 2 caracteres.' });
      return;
    }
    this.ref.close({ descricao: desc });
  }
}
