import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-inutilizar-numeracao',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, DialogModule, InputTextModule, TableModule, TagModule, MatIconModule],
  templateUrl: './inutilizar-numeracao.html',

})
export class InutilizarNumeracao {
  formVisible = false;
  confirmVisible = false;
  loadingVisible = false;

  serie = '1';
  numeroInicial: number | null = null;
  numeroFinal: number | null = null;
  justificativa = '';

  solicitacoes: any[] = [];

  abrirFormulario() { this.formVisible = true; }
  fecharFormulario() { this.formVisible = false; }

  solicitarInutilizacao() { this.confirmVisible = true; }

  confirmarInutilizacao() {
    this.confirmVisible = false;
    this.formVisible = false;
    this.loadingVisible = true;
    setTimeout(() => {
      this.loadingVisible = false;
    }, 2000);
  }
}
