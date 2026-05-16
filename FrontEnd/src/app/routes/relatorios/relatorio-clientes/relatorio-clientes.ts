import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs';

import { ClientesService, ClienteResponseDTO, Page } from '../../cliente/cliente/cliente.service';
import {
  TipoCliente, StatusCliente, OrigemCliente,
  TipoClienteLabels, StatusClienteLabels, OrigemClienteLabels,
  getTipoClienteOptions, getStatusClienteOptions, getOrigemClienteOptions
} from '../../cliente/models/cliente.models';
import { AuthService } from '@core/authentication';
import { RelatoriosService } from '../relatorios.service';

interface ResumoStats {
  total: number; ativos: number; inativos: number;
  bloqueados: number; pessoaFisica: number; pessoaJuridica: number;
}

@Component({
  selector: 'app-relatorio-clientes',
  standalone: true,
  templateUrl: './relatorio-clientes.html',
  styleUrls: ['./relatorio-clientes.scss'],
  imports: [CommonModule, FormsModule, MatIconModule, MatButtonModule, ToastModule, SelectModule],
  providers: [MessageService, DatePipe],
})
export class RelatorioClientes implements OnInit {
  private readonly clientesService = inject(ClientesService);
  private readonly auth = inject(AuthService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly datePipe = inject(DatePipe);
  private readonly toast = inject(MessageService);
  private readonly relatoriosService = inject(RelatoriosService);

  // Empresa info (para o PDF)
  nomeEmpresa = 'NeriTech Auto';

  // ─── Filtros básicos ───
  filtroStatus: StatusCliente | null = null;
  filtroTipo: TipoCliente | null = null;
  filtroOrigem: OrigemCliente | null = null;
  filtroBusca = '';

  // ─── Filtros avançados ───
  dataInicio = '';   // YYYY-MM-DD
  dataFim    = '';   // YYYY-MM-DD

  // ─── Opções dos selects ───
  statusOptions  = [{ label: 'Todos os Status', value: null }, ...getStatusClienteOptions()];
  tipoOptions    = [{ label: 'Todos os Tipos',  value: null }, ...getTipoClienteOptions()];
  origemOptions  = [{ label: 'Todas as Origens',value: null }, ...getOrigemClienteOptions()];

  // ─── Dados ───
  clientes: ClienteResponseDTO[] = [];
  isLoading  = false;
  pdfLoading = false;
  totalElements = 0;

  // ─── Stats ───
  stats: ResumoStats = { total: 0, ativos: 0, inativos: 0, bloqueados: 0, pessoaFisica: 0, pessoaJuridica: 0 };

  // Labels para template
  TipoClienteLabels   = TipoClienteLabels;
  StatusClienteLabels = StatusClienteLabels;
  OrigemClienteLabels = OrigemClienteLabels;
  TipoCliente   = TipoCliente;
  StatusCliente = StatusCliente;

  dataGeracao = new Date();

  ngOnInit() { this.gerarRelatorio(); }

  gerarRelatorio() {
    this.isLoading = true;
    const filters: any = { page: 1, size: 200, sort: 'nomeCompleto,asc' };
    if (this.filtroStatus) filters['status']       = this.filtroStatus;
    if (this.filtroTipo)   filters['tipoCliente']  = this.filtroTipo;
    if (this.filtroOrigem) filters['origemCliente']= this.filtroOrigem;
    if (this.filtroBusca.trim()) filters['nomeCompleto'] = this.filtroBusca.trim();
    // Nota: dataInicio/dataFim/mesAniversario não são suportados pelo endpoint /clientes (paginado)
    // — são passados apenas para o PDF Jasper.

    this.clientesService.list(filters).pipe(
      finalize(() => { this.isLoading = false; this.cdr.detectChanges(); })
    ).subscribe({
      next: (res: Page<ClienteResponseDTO>) => {
        this.clientes = res.content || [];
        this.totalElements = res.totalElements;
        this.calcularStats();
        this.dataGeracao = new Date();
      },
      error: () => this.toast.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar clientes.' })
    });
  }

  imprimirJasper() {
    this.pdfLoading = true;
    const params: any = {};
    if (this.filtroStatus)  params['status']         = this.filtroStatus;
    if (this.filtroTipo)    params['tipoCliente']     = this.filtroTipo;
    if (this.filtroOrigem)  params['origemCliente']   = this.filtroOrigem;
    if (this.filtroBusca.trim()) params['busca']      = this.filtroBusca.trim();
    if (this.dataInicio)    params['dataInicio']      = this.dataInicio;
    if (this.dataFim)       params['dataFim']         = this.dataFim;

    this.relatoriosService.gerarRelatorio('clientes', params)
      .pipe(finalize(() => { this.pdfLoading = false; this.cdr.detectChanges(); }))
      .subscribe({
        next: blob => this.relatoriosService.abrirBlobEmNovaAba(blob),
        error: () => this.toast.add({
          severity: 'error', summary: 'Erro ao gerar PDF',
          detail: 'Não foi possível gerar o PDF. Verifique se o backend está online.'
        })
      });
  }

  private calcularStats() {
    const c = this.clientes;
    this.stats = {
      total: this.totalElements,
      ativos:         c.filter(x => x.status === StatusCliente.ATIVO).length,
      inativos:       c.filter(x => x.status === StatusCliente.INATIVO).length,
      bloqueados:     c.filter(x => x.status === StatusCliente.BLOQUEADO).length,
      pessoaFisica:   c.filter(x => x.tipoCliente === TipoCliente.PESSOA_FISICA).length,
      pessoaJuridica: c.filter(x => x.tipoCliente === TipoCliente.PESSOA_JURIDICA).length,
    };
  }

  imprimir() { window.print(); }

  exportarCSV() {
    const headers = ['Nome/Razão Social','CPF/CNPJ','Tipo','Status','E-mail','Origem'];
    const rows = this.clientes.map(c => [
      this.getNome(c), c.cpf || c.cnpj || '',
      TipoClienteLabels[c.tipoCliente as TipoCliente] || c.tipoCliente,
      StatusClienteLabels[c.status as StatusCliente] || c.status || '',
      c.email || '',
      OrigemClienteLabels[c.origemCliente as OrigemCliente] || c.origemCliente || ''
    ]);
    const csv = [headers,...rows].map(r => r.map(v => `"${v}"`).join(',')).join('\n');
    const blob = new Blob(['\uFEFF'+csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `clientes-${this.datePipe.transform(new Date(),'yyyyMMdd')}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }

  limparFiltros() {
    this.filtroStatus = null; this.filtroTipo = null; this.filtroOrigem = null;
    this.filtroBusca = ''; this.dataInicio = ''; this.dataFim = '';
    this.gerarRelatorio();
  }

  getNome(c: ClienteResponseDTO): string { return c.nomeCompleto || c.nomeFantasia || c.razaoSocial || '—'; }
  getDoc (c: ClienteResponseDTO): string { return c.cpf || c.cnpj || '—'; }

  getStatusLabel(status?: string): string {
    if (!status) return '—';
    return StatusClienteLabels[status as StatusCliente] || status;
  }
  getOrigemLabel(origem?: string): string {
    if (!origem) return '—';
    return OrigemClienteLabels[origem as OrigemCliente] || origem;
  }
  getStatusClass(status: string) {
    const m: Record<string,string> = { ATIVO:'badge-success', INATIVO:'badge-secondary', BLOQUEADO:'badge-danger' };
    return m[status] || 'badge-secondary';
  }

  get dataGeracaoFormatada() { return this.datePipe.transform(this.dataGeracao,'dd/MM/yyyy HH:mm') || ''; }
  get filtrosAtivos() {
    const p: string[] = [];
    if (this.filtroStatus)  p.push(`Status: ${StatusClienteLabels[this.filtroStatus]}`);
    if (this.filtroTipo)    p.push(`Tipo: ${TipoClienteLabels[this.filtroTipo]}`);
    if (this.filtroOrigem)  p.push(`Origem: ${OrigemClienteLabels[this.filtroOrigem]}`);
    if (this.filtroBusca)   p.push(`"${this.filtroBusca}"`);
    if (this.dataInicio)    p.push(`De: ${this.datePipe.transform(this.dataInicio,'dd/MM/yyyy')}`);
    if (this.dataFim)       p.push(`Até: ${this.datePipe.transform(this.dataFim,'dd/MM/yyyy')}`);
    return p.length ? p.join(' · ') : 'Todos os clientes';
  }
}
