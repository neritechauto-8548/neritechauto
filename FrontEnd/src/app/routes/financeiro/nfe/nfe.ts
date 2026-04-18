import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { HotToastService } from '@ngxpert/hot-toast';
import { NfeService } from '../nfe.service';
import { NfeResponse, StatusNfe } from '../models/nfe.models';

@Component({
  standalone: true,
  selector: 'app-nfe',
  imports: [
    CommonModule,
    FormsModule,
    PanelModule,
    ButtonModule,
    TableModule,
    TagModule,
    TooltipModule,
    DialogModule,
    InputTextModule
  ],
  templateUrl: './nfe.html',
})
export class NfeComponent implements OnInit {
  private service = inject(NfeService);
  private toast = inject(HotToastService);

  loading = false;
  notas: NfeResponse[] = [];

  // Dialog
  detalheVisible = false;
  notaSelecionada: NfeResponse | null = null;

  ngOnInit() {
      this.carregarNotas();
  }

  carregarNotas() {
      this.loading = true;
      this.service.list({ empresaId: 1, page: 0, size: 50 }).subscribe({
          next: (page) => {
              this.notas = page.content;
              this.loading = false;
          },
          error: (err) => {
              console.error(err);
              this.toast.error('Erro ao listar NFes');
              this.loading = false;
          }
      });
  }

  getStatusSeverity(status: StatusNfe): 'success' | 'info' | 'warn' | 'danger' | 'secondary' | 'contrast' | undefined {
      switch(status) {
          case StatusNfe.AUTORIZADA: return 'success';
          case StatusNfe.EM_DIGITACAO: return 'warn';
          case StatusNfe.CANCELADA: return 'danger';
          case StatusNfe.REJEITADA: return 'danger';
          case StatusNfe.TRANSMITIDA: return 'info';
          default: return 'secondary';
      }
  }

  visualizar(nota: NfeResponse) {
      this.notaSelecionada = nota;
      this.detalheVisible = true;
  }

  imprimir(nota: NfeResponse) {
      this.toast.info('Impressão do DANFE em desenvolvimento');
  }
}
