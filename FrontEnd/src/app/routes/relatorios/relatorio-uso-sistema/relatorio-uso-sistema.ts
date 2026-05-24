import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { SelectModule } from 'primeng/select';
import { RelatoriosService } from '../relatorios.service';

interface LogItem {
  date: string; // dd/MM/yyyy
  time: string; // HH:mm:ss
  employee: string;
  description: string;
}

@Component({
  selector: 'app-relatorio-uso-sistema',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, MatIconModule, SelectModule],
  templateUrl: './relatorio-uso-sistema.html',
  styleUrls: ['./relatorio-uso-sistema.scss'],
})
export class RelatorioUsoSistema implements OnInit {
  private readonly relatoriosService = inject(RelatoriosService);

  // Filtros
  dataInicial = '';
  dataFinal = '';
  funcionario = 'Todos';
  descricao = '';
  ordemServico = '';

  // Opções dos selects
  funcionarioOptions = [
    { label: 'Todos os Funcionários', value: 'Todos' }
  ];

  // Dados
  logs: LogItem[] = [];

  // Stats
  stats = {
    total: 0,
    criacoes: 0,
    alteracoes: 0,
    exclusoes: 0
  };

  // Paginação
  page = 1;
  pageSize = 10;
  paginaDestino = 1;

  ngOnInit() {
    this.carregarLogs();
  }

  carregarLogs() {
    this.relatoriosService.getLogsAlteracoes().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          // Ordenar decrescente pela data/hora
          const sortedData = [...data].sort((a, b) => {
            const dateA = a.dataAlteracao ? new Date(a.dataAlteracao).getTime() : 0;
            const dateB = b.dataAlteracao ? new Date(b.dataAlteracao).getTime() : 0;
            return dateB - dateA;
          });

          this.logs = sortedData.map(item => {
            const dataString = item.dataAlteracao;
            let datePart = '—';
            let timePart = '—';
            if (dataString) {
              const parts = dataString.split('T');
              if (parts.length >= 1) {
                const dateSub = parts[0].split('-');
                if (dateSub.length === 3) {
                  datePart = `${dateSub[2]}/${dateSub[1]}/${dateSub[0]}`;
                }
              }
              if (parts.length >= 2) {
                timePart = parts[1].substring(0, 8);
              }
            }
            
            let desc = item.motivoAlteracao || item.contextoOperacao;
            if (!desc) {
              const operacaoLbl = item.operacao === 'CREATE' ? 'Cadastrou' : (item.operacao === 'DELETE' ? 'Excluiu' : 'Alterou');
              const tabelaLbl = item.tabelaAfetada || 'registro';
              desc = `${operacaoLbl} ${tabelaLbl} (ID: ${item.registroId})`;
            }

            return {
              date: datePart,
              time: timePart,
              employee: item.usuarioNome || `Usuário ID: ${item.usuarioResponsavel}`,
              description: desc
            };
          });

          // Popular funcionários dinamicamente
          const uniqueEmployees = Array.from(new Set(this.logs.map(x => x.employee).filter(Boolean)));
          this.funcionarioOptions = [
            { label: 'Todos os Funcionários', value: 'Todos' },
            ...uniqueEmployees.map(emp => ({ label: emp, value: emp }))
          ];
        } else {
          this.logs = [];
          this.funcionarioOptions = [
            { label: 'Todos os Funcionários', value: 'Todos' }
          ];
        }
        this.calcularStats();
        this.page = 1;
        this.paginaDestino = 1;
      },
      error: () => {
        this.logs = [];
        this.funcionarioOptions = [
          { label: 'Todos os Funcionários', value: 'Todos' }
        ];
        this.calcularStats();
      }
    });
  }

  calcularStats() {
    const all = this.logs;
    this.stats = {
      total: all.length,
      criacoes: all.filter(x => x.description.toLowerCase().includes('cadastrou') || x.description.toLowerCase().includes('criou') || x.description.toUpperCase().includes('CREATE')).length,
      alteracoes: all.filter(x => x.description.toLowerCase().includes('alterou') || x.description.toLowerCase().includes('quitou') || x.description.toUpperCase().includes('UPDATE')).length,
      exclusoes: all.filter(x => x.description.toLowerCase().includes('excluiu') || x.description.toLowerCase().includes('cancelada') || x.description.toUpperCase().includes('DELETE')).length
    };
  }

  get filtered(): LogItem[] {
    return this.logs.filter(l => {
      const funcionarioOk = this.funcionario === 'Todos' || l.employee.toUpperCase().includes(this.funcionario.toUpperCase());
      const descOk = !this.descricao || l.description.toLowerCase().includes(this.descricao.toLowerCase());
      const osOk = !this.ordemServico || l.description.toLowerCase().includes(this.ordemServico.toLowerCase());
      
      // Filtros de data
      if (this.dataInicial) {
        const parts = l.date.split('/');
        const logDate = new Date(Number(parts[2]), Number(parts[1]) - 1, Number(parts[0]));
        const filterParts = this.dataInicial.split('-');
        let iniDate: Date;
        if (filterParts.length === 3) {
          if (filterParts[0].length === 4) {
            iniDate = new Date(Number(filterParts[0]), Number(filterParts[1]) - 1, Number(filterParts[2]));
          } else {
            iniDate = new Date(Number(filterParts[2]), Number(filterParts[1]) - 1, Number(filterParts[0]));
          }
          if (logDate < iniDate) return false;
        }
      }
      
      if (this.dataFinal) {
        const parts = l.date.split('/');
        const logDate = new Date(Number(parts[2]), Number(parts[1]) - 1, Number(parts[0]));
        const filterParts = this.dataFinal.split('-');
        let fimDate: Date;
        if (filterParts.length === 3) {
          if (filterParts[0].length === 4) {
            fimDate = new Date(Number(filterParts[0]), Number(filterParts[1]) - 1, Number(filterParts[2]));
          } else {
            fimDate = new Date(Number(filterParts[2]), Number(filterParts[1]) - 1, Number(filterParts[0]));
          }
          if (logDate > fimDate) return false;
        }
      }

      return funcionarioOk && descOk && osOk;
    });
  }

  get total(): number { return this.filtered.length; }
  get totalPages(): number { return Math.max(1, Math.ceil(this.total / this.pageSize)); }
  get pageItems(): LogItem[] {
    const start = (this.page - 1) * this.pageSize;
    return this.filtered.slice(start, start + this.pageSize);
  }

  buscar() { 
    this.page = 1; 
    this.paginaDestino = 1;
  }
  
  limparFiltros() {
    this.dataInicial = '';
    this.dataFinal = '';
    this.funcionario = 'Todos';
    this.descricao = '';
    this.ordemServico = '';
    this.buscar();
  }

  anterior() { if (this.page > 1) { this.page--; this.paginaDestino = this.page; } }
  proxima() { if (this.page < this.totalPages) { this.page++; this.paginaDestino = this.page; } }
  
  irParaPagina() {
    const p = Number(this.paginaDestino);
    if (p >= 1 && p <= this.totalPages) {
      this.page = p;
    }
  }
}