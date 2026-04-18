import { Component } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';

@Component({
  standalone: true,
  selector: 'app-transferencia',
  imports: [CommonModule, FormsModule, CardModule, SelectModule, InputTextModule, ButtonModule, DecimalPipe],
  templateUrl: './transferencia.html',
  styleUrls: ['./transferencia.scss'],
})
export class TransferenciaComponent {
  contas = [
    { label: 'BANCO', value: 'BANCO' },
    { label: 'CAIXA', value: 'CAIXA' },
    { label: 'CARTAO', value: 'CARTAO' },
  ];

  origem = 'BANCO';
  destino = 'BANCO';
  valorTransferencia = 0;

  valorAtualOrigem = -4902.54;
  valorAtualDestino = -4902.54;

  get valorFinalOrigem(): number {
    return this.valorAtualOrigem - this.valorTransferencia;
  }
  get valorFinalDestino(): number {
    return this.valorAtualDestino + this.valorTransferencia;
  }

  fazerTransferencia(): void {
    if (!this.origem || !this.destino || this.valorTransferencia <= 0) {
      alert('Informe origem, destino e um valor de transferência válido.');
      return;
    }
    // Simula a transferência apenas na UI
    this.valorAtualOrigem = this.valorFinalOrigem;
    this.valorAtualDestino = this.valorFinalDestino;
    this.valorTransferencia = 0;
    alert('Transferência realizada com sucesso.');
  }
}