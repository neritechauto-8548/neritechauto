import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, Pipe, PipeTransform } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

// Angular Material
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';

// PrimeNG
import { InputTextModule } from 'primeng/inputtext';

// Shared
import { OrdemServicoService } from '../ordem-servico.service';
import { OrdemServicoResponse } from '../models/os.models';

@Pipe({ name: 'min', standalone: true })
export class MinPipe implements PipeTransform {
  transform(values: number[]): number {
    return Math.min(...values);
  }
}

@Component({
  selector: 'ordem-servico',
  standalone: true,
  templateUrl: './ordem-servico.html',
  styleUrls: ['./ordem-servico.scss'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    InputTextModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    MinPipe,
  ],
})
export class OrdemServico implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly osService = inject(OrdemServicoService);

  // Filtro
  search = '';
  statusParam: string | null = null;

  // Dados
  orders: any[] = [];
  currentRow: any = null;

  // Paginação
  loading = false;
  totalItems = 0;
  first = 0;
  rows = 15;

  ngOnInit() {
    const qp = this.route.snapshot.queryParamMap;
    this.statusParam = qp.get('status');
    this.load();
  }

  onBuscar() {
    this.first = 0;
    this.load();
  }

  setCurrent(row: any) {
    this.currentRow = row;
  }

  goPrev() {
    if (this.first >= this.rows) {
      this.first -= this.rows;
      this.load();
    }
  }

  goNext() {
    if (this.first + this.rows < this.totalItems) {
      this.first += this.rows;
      this.load();
    }
  }

  onCadastrar() {
    this.router.navigate(['/os/cadastro']);
  }

  onCadastrarOrcamento() {
    this.router.navigate(['/os/cadastro'], { queryParams: { tipo: 'ORCAMENTO' } });
  }

  visualizarOS(row: any) {
    const numero = row?.os ?? row?.id ?? null;
    const url = numero ? `/os/visualizar-os/${numero}` : '/os/visualizar-os';
    setTimeout(() => this.router.navigateByUrl(url), 0);
  }

  editarOS(row: any) {
    const id = row?.os ?? row?.id ?? null;
    if (id) this.router.navigate(['/os/cadastro', id]);
  }

  getStatusClass(statusNome: string, cor?: string): string {
    if (cor) return '';
    const n = (statusNome || '').toLowerCase();
    if (n.includes('aberta') || n.includes('aberto')) return 'bg-blue-50 text-blue-700 border-blue-200';
    if (n.includes('andamento')) return 'bg-amber-50 text-amber-700 border-amber-200';
    if (n.includes('conclu') || n.includes('finaliz')) return 'bg-emerald-50 text-emerald-700 border-emerald-200';
    if (n.includes('cancel')) return 'bg-red-50 text-red-700 border-red-200';
    if (n.includes('orçamento') || n.includes('orcamento')) return 'bg-indigo-50 text-indigo-700 border-indigo-200';
    return 'bg-slate-100 text-slate-600 border-slate-200';
  }

  private load() {
    this.loading = true;
    const page = Math.floor(this.first / this.rows);
    const params: any = { page, size: this.rows, sort: 'numeroOS,desc', search: this.search };
    if (this.statusParam) params.status = this.statusParam;

    this.osService.list(params).subscribe({
      next: (res) => {
        let list = res?.content || [];
        if (this.statusParam) {
          const target = this.statusParam.toUpperCase();
          list = list.filter(o => (o.statusNome || '').toUpperCase() === target);
        }
        this.orders = list.map(o => this.mapOS(o));
        this.totalItems = res?.totalElements || list.length;
        this.loading = false;
      },
      error: () => {
        this.orders = [];
        this.totalItems = 0;
        this.loading = false;
      },
    });
  }

  private mapOS(o: OrdemServicoResponse) {
    let data = '', hora = '';
    if (o?.dataAbertura) {
      const date = new Date(o.dataAbertura);
      data = date.toLocaleDateString('pt-BR');
      hora = date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    }
    return {
      os: o?.id ?? 0,
      numeroOS: o?.numeroOS || '',
      placa: o?.placaVeiculo || '',
      cliente: o?.nomeCliente || 'Cliente N/A',
      veiculo: o?.nomeVeiculo || '',
      data, hora,
      statusNome: o?.statusNome || '',
      statusCor: o?.statusCor || ''
    };
  }
}
