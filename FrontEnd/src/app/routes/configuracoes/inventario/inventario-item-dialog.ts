import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { MatIconModule } from '@angular/material/icon';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { TipoInventario, StatusInventario } from './inventario.service';

@Component({
  selector: 'inventario-item-dialog',
  standalone: true,
  templateUrl: './inventario-item-dialog.html',
  styleUrls: ['./inventario-item-dialog.scss'],
  imports: [CommonModule, FormsModule, InputTextModule, SelectModule, TextareaModule, ButtonModule, MatIconModule],
})
export class InventarioItemDialog implements OnInit {
  constructor(public ref: DynamicDialogRef, public config: DynamicDialogConfig) {}

  isEdit = false;

  form = {
    codigo: '',
    descricao: '',
    tipoInventario: 'GERAL' as TipoInventario,
    dataInicio: new Date().toISOString().slice(0, 10),
    dataFim: '',
    status: 'PLANEJADO' as StatusInventario,
    observacoes: '',
  };

  tipoOptions = [
    { label: 'GERAL', value: 'GERAL' },
    { label: 'PARCIAL', value: 'PARCIAL' },
    { label: 'CICLICO', value: 'CICLICO' },
    { label: 'EMERGENCIAL', value: 'EMERGENCIAL' },
  ];

  statusOptions = [
    { label: 'PLANEJADO', value: 'PLANEJADO' },
    { label: 'EM_ANDAMENTO', value: 'EM_ANDAMENTO' },
    { label: 'FINALIZADO', value: 'FINALIZADO' },
    { label: 'CANCELADO', value: 'CANCELADO' },
  ];

  cancelar() {
    this.ref.close(null);
  }

  ngOnInit() {
    if (this.config.data && this.config.data.codigo) {
      this.isEdit = true;
      this.form = { ...this.form, ...this.config.data };
    }
  }

  adicionar() {
    this.ref.close(this.form);
  }
}
