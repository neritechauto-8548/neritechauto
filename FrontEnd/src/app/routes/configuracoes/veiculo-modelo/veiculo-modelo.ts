import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AccordionModule } from 'primeng/accordion';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import { PageHeader } from '@shared/components/page-header/page-header';
import { VeiculoMarcaDialog } from './veiculo-marca-dialog';
import { VeiculoModeloDialog } from './veiculo-modelo-dialog';

interface ModeloVeiculo {
  nome: string;
}

interface VeiculoMarca {
  nome: string;
  modelos: ModeloVeiculo[];
}

@Component({
  selector: 'app-veiculo-modelo',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, AccordionModule, DynamicDialogModule, PageHeader],
  providers: [DialogService],
  templateUrl: './veiculo-modelo.html',
  styleUrls: ['./veiculo-modelo.scss'],
})
export class VeiculoModelo {
  marcas: VeiculoMarca[] = [
    {
      nome: 'ACURA',
      modelos: [
        { nome: 'INTEGRA' },
        { nome: 'LEGEND' },
        { nome: 'MDX' }
      ]
    },
    {
      nome: 'AUDI',
      modelos: [
        { nome: 'A3' },
        { nome: 'A4' },
        { nome: 'Q5' }
      ]
    },
    {
      nome: 'BMW',
      modelos: [
        { nome: 'X1' },
        { nome: 'X3' },
        { nome: 'X5' }
      ]
    }
  ];

  constructor(private dialogService: DialogService) {}

  novaMarca(): void {
    const ref = this.dialogService.open(VeiculoMarcaDialog, {
      header: 'Nova Marca',
      width: '600px',
    });

    ref?.onClose?.subscribe((nome: string | undefined) => {
      const value = nome?.trim();
      if (value) {
        this.marcas = [...this.marcas, { nome: value, modelos: [] }];
      }
    });
  }

  editarMarca(index: number): void {
    const marca = this.marcas[index];
    const ref = this.dialogService.open(VeiculoMarcaDialog, {
      header: 'Editar Marca',
      width: '600px',
      data: { nome: marca.nome }
    });

    ref?.onClose?.subscribe((nome: string | undefined) => {
      const value = nome?.trim();
      if (value) {
        this.marcas[index].nome = value;
      }
    });
  }

  removerMarca(index: number): void {
    this.marcas = this.marcas.filter((_, i) => i !== index);
  }

  novoModelo(marcaIndex: number): void {
    const ref = this.dialogService.open(VeiculoModeloDialog, {
      header: 'Novo Modelo',
      width: '600px',
    });

    ref?.onClose?.subscribe((nome: string | undefined) => {
      const value = nome?.trim();
      if (value) {
        this.marcas[marcaIndex].modelos = [...this.marcas[marcaIndex].modelos, { nome: value }];
      }
    });
  }

  editarModelo(marcaIndex: number, modeloIndex: number): void {
    const modelo = this.marcas[marcaIndex].modelos[modeloIndex];
    const ref = this.dialogService.open(VeiculoModeloDialog, {
      header: 'Editar Modelo',
      width: '600px',
      data: { nome: modelo.nome }
    });

    ref?.onClose?.subscribe((nome: string | undefined) => {
      const value = nome?.trim();
      if (value) {
        this.marcas[marcaIndex].modelos[modeloIndex].nome = value;
      }
    });
  }

  removerModelo(marcaIndex: number, modeloIndex: number): void {
    this.marcas[marcaIndex].modelos = this.marcas[marcaIndex].modelos.filter((_, i) => i !== modeloIndex);
  }
}