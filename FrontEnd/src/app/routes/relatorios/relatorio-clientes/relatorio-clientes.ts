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

  // ─── Tipo de Relatório ───
  tipoRelatorio: 'CADASTRADOS' | 'VISITAS' | 'INATIVOS' = 'CADASTRADOS';
  tipoRelatorioOptions = [
    { label: 'Clientes Cadastrados', value: 'CADASTRADOS' },
    { label: 'Visitas de Clientes', value: 'VISITAS' },
    { label: 'Clientes Inativos / Sem Visitas (90 dias)', value: 'INATIVOS' }
  ];

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
  clientes: any[] = [];
  isLoading  = false;
  pdfLoading = false;
  totalElements = 0;

  // ─── Stats ───
  stats: ResumoStats = { total: 0, ativos: 0, inativos: 0, bloqueados: 0, pessoaFisica: 0, pessoaJuridica: 0 };
  totalValorVisitas = 0;

  // Labels para template
  TipoClienteLabels   = TipoClienteLabels;
  StatusClienteLabels = StatusClienteLabels;
  OrigemClienteLabels = OrigemClienteLabels;
  TipoCliente   = TipoCliente;
  StatusCliente = StatusCliente;

  dataGeracao = new Date();

  ngOnInit() {
    this.configurarDatasPadrao();
    this.gerarRelatorio();
  }

  configurarDatasPadrao() {
    const hoje = new Date();
    const ano = hoje.getFullYear();
    const mes = hoje.getMonth();
    
    const primeiroDia = new Date(ano, mes, 1);
    const ultimoDia = new Date(ano, mes + 1, 0);

    const formatarData = (d: Date) => {
      const day = String(d.getDate()).padStart(2, '0');
      const month = String(d.getMonth() + 1).padStart(2, '0');
      return `${d.getFullYear()}-${month}-${day}`;
    };

    this.dataInicio = formatarData(primeiroDia);
    this.dataFim = formatarData(ultimoDia);
  }

  get periodoFormatado() {
    if (!this.dataInicio || !this.dataFim) return '';
    
    const formatarParaExibicao = (dataStr: string) => {
      const partes = dataStr.split('-');
      if (partes.length !== 3) return '';
      const dia = parseInt(partes[2], 10);
      const mes = parseInt(partes[1], 10);
      const ano = partes[0];
      return `${dia}/${mes}/${ano}`;
    };

    const ini = formatarParaExibicao(this.dataInicio);
    const fim = formatarParaExibicao(this.dataFim);
    return `NO PERÍODO DE ${ini} ATÉ ${fim}`;
  }

  gerarRelatorio() {
    this.isLoading = true;
    const params: any = {
      tipoRelatorio: this.tipoRelatorio
    };
    if (this.filtroStatus) params['status'] = this.filtroStatus;
    if (this.filtroTipo) params['tipoCliente'] = this.filtroTipo;
    if (this.filtroOrigem) params['origemCliente'] = this.filtroOrigem;
    if (this.filtroBusca.trim()) params['busca'] = this.filtroBusca.trim();
    if (this.dataInicio) params['dataInicio'] = this.dataInicio;
    if (this.dataFim) params['dataFim'] = this.dataFim;

    this.relatoriosService.getClientesDados(params).pipe(
      finalize(() => { this.isLoading = false; this.cdr.detectChanges(); })
    ).subscribe({
      next: (res: any[]) => {
        this.clientes = res || [];
        this.totalElements = res.length;
        this.calcularStats();
        this.dataGeracao = new Date();
      },
      error: () => this.toast.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar dados do relatório.' })
    });
  }

  imprimirJasper() {
    this.pdfLoading = true;
    const params: any = {
      tipoRelatorio: this.tipoRelatorio
    };
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
    this.totalValorVisitas = c.reduce((sum, item) => sum + (item.valorGastoOS || 0), 0);
    this.stats = {
      total: this.totalElements,
      ativos:         c.filter(x => x.status?.toUpperCase().includes('ATIVO')).length,
      inativos:       c.filter(x => x.status?.toUpperCase().includes('INATIVO')).length,
      bloqueados:     c.filter(x => x.status?.toUpperCase().includes('BLOQUEADO')).length,
      pessoaFisica:   c.filter(x => (x.documento || '').replace(/\D/g, '').length <= 11).length,
      pessoaJuridica: c.filter(x => (x.documento || '').replace(/\D/g, '').length > 11).length,
    };
  }

  imprimir() { window.print(); }

  exportarCSV() {
    let headers: string[] = [];
    let rows: any[][] = [];

    if (this.tipoRelatorio === 'VISITAS') {
      headers = ['Nome', 'Endereço', 'Telefones', 'Nº OS', 'OS (Serviços)', 'Data OS', 'Valor Gasto'];
      rows = this.clientes.map(c => [
        c.nome || '',
        c.endereco || '',
        c.telefones || '',
        c.numeroOSVisita || '',
        c.osDescricao || '',
        c.dataOS || '',
        c.valorGastoOS || 0
      ]);
    } else {
      headers = ['Nome', 'Endereço', 'Telefones', 'Ticket Médio', 'Última Visita', 'Nº de OS', 'Data Cadastro'];
      rows = this.clientes.map(c => [
        c.nome || '',
        c.endereco || '',
        c.telefones || '',
        c.ticketMedio || 0,
        c.ultimaVisita || '',
        c.numeroOS || 0,
        c.dataCadastro || ''
      ]);
    }

    const csv = [headers, ...rows].map(r => r.map(v => `"${v}"`).join(',')).join('\n');
    const blob = new Blob(['\uFEFF' + csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `relatorio-clientes-${this.tipoRelatorio.toLowerCase()}-${this.datePipe.transform(new Date(), 'yyyyMMdd')}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }

  limparFiltros() {
    this.filtroStatus = null; this.filtroTipo = null; this.filtroOrigem = null;
    this.filtroBusca = '';
    this.configurarDatasPadrao();
    this.gerarRelatorio();
  }

  isPessoaJuridica(documento?: string): boolean {
    if (!documento) return false;
    return documento.replace(/\D/g, '').length > 11;
  }

  getStatusClass(status: string) {
    if (!status) return 'badge-secondary';
    const s = status.toUpperCase();
    if (s.includes('ATIVO')) return 'badge-success';
    if (s.includes('INATIVO')) return 'badge-secondary';
    if (s.includes('BLOQUEADO')) return 'badge-danger';
    return 'badge-secondary';
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
