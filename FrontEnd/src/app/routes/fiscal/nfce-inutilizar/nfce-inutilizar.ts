import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-nfce-inutilizar-numeracao',
  standalone: true,
  imports: [CommonModule, FormsModule, PanelModule, ButtonModule, DialogModule],
  templateUrl: './nfce-inutilizar.html',

})
export class NfceInutilizarNumeracao {
  formVisible = false;
  confirmVisible = false;
  loadingVisible = false;

  serie = '';
  numeroInicial = '';
  numeroFinal = '';
  justificativa = '';

  openForm() { this.formVisible = true; }
  closeForm() { this.formVisible = false; }

  solicitarInutilizacao() {
    if (!this.serie || !this.numeroInicial || !this.numeroFinal || !this.justificativa) {
      return; // simples validação
    }
    this.confirmVisible = true;
  }

  confirmar() {
    this.confirmVisible = false;
    this.formVisible = false;
    this.loadingVisible = true;
    setTimeout(() => { this.loadingVisible = false; }, 2000);
  }

  cancelarConfirmacao() { this.confirmVisible = false; }
}
