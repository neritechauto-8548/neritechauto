import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-entrada-nfe',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, DialogModule, MatIconModule],
  templateUrl: './entrada.html',

})
export class EntradaNfe {
  selectedFile: File | null = null;
  fileName = 'Nenhum arquivo escolhido';
  loadingVisible = false;
  errorVisible = false;
  errorMsg = '';

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    const f = input.files && input.files[0] ? input.files[0] : null;
    this.selectedFile = f;
    this.fileName = f ? f.name : 'Nenhum arquivo escolhido';
    if (f && !f.name.toLowerCase().endsWith('.xml')) {
      this.errorMsg = 'Atenção! Apenas o arquivo .xml da NFe é aceito.';
      this.errorVisible = true;
      this.selectedFile = null;
      this.fileName = 'Nenhum arquivo escolhido';
    }
  }

  enviar() {
    if (!this.selectedFile) {
      this.errorMsg = 'Selecione o XML da NFe para continuar.';
      this.errorVisible = true;
      return;
    }
    this.loadingVisible = true;
    setTimeout(() => {
      this.loadingVisible = false;
    }, 2000);
  }
}
