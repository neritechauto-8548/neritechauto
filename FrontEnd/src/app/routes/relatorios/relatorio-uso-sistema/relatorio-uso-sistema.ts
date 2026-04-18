import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';

interface LogItem {
  date: string; // dd/MM/yyyy
  time: string; // HH:mm:ss
  employee: string;
  description: string;
}

@Component({
  selector: 'app-relatorio-uso-sistema',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, PanelModule, ButtonModule],
  templateUrl: './relatorio-uso-sistema.html',
  styleUrls: ['./relatorio-uso-sistema.scss'],
})
export class RelatorioUsoSistema {
  // Filtros
  dataInicial = '';
  dataFinal = '';
  funcionario = 'Todos';
  descricao = '';
  ordemServico = '';

  // Dados demo
  logs: LogItem[] = [
    { date: '02/11/2025', time: '10:13:56', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'Quitou conta de numero 2' },
    { date: '02/11/2025', time: '10:13:48', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'Cadastrou a conta avulsa 2: TESTSTSE' },
    { date: '02/11/2025', time: '10:09:32', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'Quitou conta de numero 1' },
    { date: '02/11/2025', time: '10:07:08', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'O Status da OS 2 foi alterado do status Aprovada para o status Entregue' },
    { date: '02/11/2025', time: '10:06:29', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'Cadastrou a conta 1: REFERENTE AO OS 2 Cliente: JUNIOR RUTIMAR DA SILVA' },
    { date: '02/11/2025', time: '10:05:30', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'Cadastrou Ordem de serviço 2  Para  Veiculo  EXE-0199, Cliente  1, Funcionario 1, Previsao de entrega com Data: 4/11/2025 e Hora:10:05, Descricao: teset, Reservico:NAO, Localizacao: ALINHAMENTO' },
    { date: '02/11/2025', time: '09:48:40', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'VENDA: 1 cancelada ou excluida!' },
    { date: '02/11/2025', time: '09:47:49', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'VENDA: 1 cancelada ou excluida!' },
    { date: '02/11/2025', time: '09:47:45', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'Cadastrou PRODUTO: 2 - AMORTECEDOR /' },
    { date: '02/11/2025', time: '09:36:03', employee: 'ALEXANDRE ROMULO ALBUQUERQUE NERI', description: 'MESTRE entrou no sistema (IP: 186.224.25.55)' },
  ];

  // Paginação
  page = 1;
  pageSize = 10;

  get filtered(): LogItem[] {
    return this.logs.filter(l => {
      const funcionarioOk = this.funcionario === 'Todos' || l.employee.includes(this.funcionario);
      const descOk = !this.descricao || l.description.toLowerCase().includes(this.descricao.toLowerCase());
      const osOk = !this.ordemServico || l.description.toLowerCase().includes(this.ordemServico.toLowerCase());
      // Datas demo: ignora se filtros vazios
      return funcionarioOk && descOk && osOk;
    });
  }

  get total(): number { return this.filtered.length; }
  get totalPages(): number { return Math.max(1, Math.ceil(this.total / this.pageSize)); }
  get pageItems(): LogItem[] {
    const start = (this.page - 1) * this.pageSize;
    return this.filtered.slice(start, start + this.pageSize);
  }

  buscar() { this.page = 1; }
  anterior() { if (this.page > 1) this.page--; }
  proxima() { if (this.page < this.totalPages) this.page++; }
}