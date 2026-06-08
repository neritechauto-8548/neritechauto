import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ToggleSwitch } from 'primeng/toggleswitch';

@Component({
  selector: 'app-questionamento-modelo-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, MatIconModule, ToastModule, ToggleSwitch],
  templateUrl: './questionamento-modelo-dialog.html',
})
export class QuestionamentoModeloDialog implements OnInit {
  titulo = '';
  ativo = true;
  isEdit = false;
  private readonly messageService = inject(MessageService);

  constructor(private ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

  ngOnInit() {
    if (this.config?.data) {
      if (this.config.data.titulo) {
        this.titulo = this.config.data.titulo;
        this.isEdit = true;
      }
      if (this.config.data.ativo !== undefined) {
        this.ativo = !!this.config.data.ativo;
      }
    }
  }

  cancelar(): void {
    this.ref.close();
  }

  salvar(): void {
    const valor = (this.titulo || '').trim();
    if (!valor) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'O nome do questionário é obrigatório.' });
      return;
    }
    if (valor.length < 2) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'O nome do questionário deve ter pelo menos 2 caracteres.' });
      return;
    }
    if (valor.length > 255) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'O nome do questionário não pode exceder 255 caracteres.' });
      return;
    }
    this.ref.close({ titulo: valor, ativo: this.ativo });
  }
}
