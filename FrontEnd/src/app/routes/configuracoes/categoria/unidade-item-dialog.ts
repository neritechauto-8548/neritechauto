import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { UnidadeMedidaRequest } from './unidade-medida.service';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'unidade-item-dialog',
  standalone: true,
  templateUrl: './unidade-item-dialog.html',
  styleUrls: ['./unidade-item-dialog.scss'],
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, CheckboxModule, MatIconModule, ToastModule],
  providers: [MessageService],
})
export class UnidadeItemDialog implements OnInit {
  constructor(
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private messageService: MessageService
  ) {}

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
    const nome = (this.form.nome || '').trim();
    const sigla = (this.form.sigla || '').trim();

    if (!nome) {
      this.messageService.add({ severity: 'warn', summary: 'Campo obrigatório', detail: 'O nome da unidade de medida é obrigatório.' });
      return;
    }
    if (nome.length < 2) {
      this.messageService.add({ severity: 'warn', summary: 'Campo inválido', detail: 'O nome deve ter pelo menos 2 caracteres.' });
      return;
    }
    if (nome.length > 50) {
      this.messageService.add({ severity: 'warn', summary: 'Campo inválido', detail: 'O nome não pode exceder 50 caracteres.' });
      return;
    }

    if (!sigla) {
      this.messageService.add({ severity: 'warn', summary: 'Campo obrigatório', detail: 'A sigla da unidade de medida é obrigatória.' });
      return;
    }
    if (sigla.length > 10) {
      this.messageService.add({ severity: 'warn', summary: 'Campo inválido', detail: 'A sigla não pode exceder 10 caracteres.' });
      return;
    }

    const payload = {
      nome,
      sigla: sigla.toUpperCase(),
      ativo: this.form.ativo,
    };
    this.ref.close(payload);
  }
}
