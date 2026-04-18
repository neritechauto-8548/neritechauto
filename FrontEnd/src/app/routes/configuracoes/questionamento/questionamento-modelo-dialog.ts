import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-questionamento-modelo-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, MatIconModule],
  templateUrl: './questionamento-modelo-dialog.html',
})
export class QuestionamentoModeloDialog implements OnInit {
  titulo = '';
  isEdit = false;

  constructor(private ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

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
    if (valor) {
      this.ref.close({ titulo: valor });
    }
  }
}
