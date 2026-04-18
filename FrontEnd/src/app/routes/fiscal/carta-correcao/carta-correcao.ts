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
  selector: 'app-carta-correcao',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, DialogModule, InputTextModule, TableModule, TagModule, MatIconModule],
  templateUrl: './carta-correcao.html',

})
export class CartaCorrecao {
  formVisible = false;
  confirmVisible = false;
  errorVisible = false;
  loadingVisible = false;

  notasOptions = ['000001', '000002', '000003'];
  numeroNota = this.notasOptions[0];
  numeroCarta = '';
  descricao = '';

  cartasEmitidas: any[] = [];

  abrirFormulario() { this.formVisible = true; }
  fecharFormulario() { this.formVisible = false; }

  enviar() {
    if (!this.descricao || this.descricao.trim().length < 15) {
      this.errorVisible = true;
      return;
    }
    this.confirmVisible = true;
  }

  confirmarEnvio() {
    this.confirmVisible = false;
    this.formVisible = false;
    this.loadingVisible = true;
    setTimeout(() => {
      this.loadingVisible = false;
    }, 2000);
  }
}
