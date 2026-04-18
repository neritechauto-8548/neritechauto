import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';

@Component({
  selector: 'app-veiculo-marca-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule],
  templateUrl: './veiculo-marca-dialog.html',
})
export class VeiculoMarcaDialog {
  nome = '';

  constructor(
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig
  ) {
    if (this.config.data?.nome) {
      this.nome = this.config.data.nome;
    }
  }

  fechar(): void {
    this.ref.close();
  }

  incluir(): void {
    if (this.nome.trim()) {
      this.ref.close(this.nome.trim());
    }
  }
}