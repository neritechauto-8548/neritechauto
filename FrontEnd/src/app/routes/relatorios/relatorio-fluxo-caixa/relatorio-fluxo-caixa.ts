import { Component, ViewChild, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
// PrimeNG
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { Menu } from 'primeng/menu';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
// ApexCharts
import { NgApexchartsModule } from 'ng-apexcharts';

import { FinanceiroService } from '../../financeiro/financeiro.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-relatorio-fluxo-caixa',
  standalone: true,
  imports: [
    CommonModule, 
    RouterModule, 
    FormsModule, 
    PanelModule, 
    ButtonModule, 
    MenuModule,
    CardModule,
    TableModule,
    NgApexchartsModule
  ],
  templateUrl: './relatorio-fluxo-caixa.html',
  styleUrls: ['./relatorio-fluxo-caixa.scss'],
})
export class RelatorioFluxoCaixa implements OnInit {
  @ViewChild('menu') menu!: Menu;

  private readonly service = inject(FinanceiroService);

  period: 'Diário' | 'Mensal' = 'Mensal';
  year = new Date().getFullYear();
  monthIndex = new Date().getMonth(); // 0=Jan ... 11=Dez
  selectedAccountId: number | null = null;
  mode = 'PREVISTO E REALIZADO'; // 'PREVISTO E REALIZADO', 'Somente previsto', 'Somente realizado'

  isLoading = false;
  contasBancarias: any[] = [];
  days: number[] = [];
  months = [
    'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
    'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
  ];

  categories = [
    'Saldo do mês anterior',
    'Outras receitas',
    'Receitas de vendas',
    'Receitas de serviços',
    'Total de receitas',
    'Outras despesas',
    'Despesas com comissões',
    'Total de despesas',
    'Saldo líquido',
    'Saldo acumulado'
  ];

  // Matrices to store daily/monthly values: { [colKey]: { previsto: number, realizado: number } }
  dataMatrix: { [row: string]: { [colKey: string]: { previsto: number, realizado: number } } } = {};
  columnsList: string[] = []; // keys like 'day_X' (for daily view) or 'month_X' (for monthly view, where X is 0..11)
  columnsLabels: string[] = []; // display labels like '1', '2' or 'Janeiro', 'Fevereiro'

  public chartOptions: any;

  totals = { receitas: 0, despesas: 0, saldo: 0 };

  menuItems = [
    { label: 'Exportar Excel (CSV)', icon: 'pi pi-file-excel', command: () => this.exportExcel() },
    { label: 'Imprimir PDF', icon: 'pi pi-print', command: () => this.printPDF() },
  ];

  ngOnInit() {
    this.carregarContasBancarias();
    this.updateDays();
    this.buscar();
  }

  get isDaily() { return this.period === 'Diário'; }

  setPeriod(p: 'Diário' | 'Mensal') {
    this.period = p;
    this.updateDays();
    this.buscar();
  }

  setMonth(idx: number | string) {
    this.monthIndex = Number(idx);
    this.updateDays();
    this.buscar();
  }

  setYear(yr: number | string) {
    this.year = Number(yr);
    this.updateDays();
    this.buscar();
  }

  updateDays() {
    const count = new Date(this.year, this.monthIndex + 1, 0).getDate();
    this.days = Array.from({ length: count }, (_, i) => i + 1);
  }

  carregarContasBancarias() {
    this.service.listContasBancarias().subscribe({
      next: res => {
        const items = res?.content || res || [];
        this.contasBancarias = items.map((x: any) => ({
          label: x.nome || `${x.bancoNome} • ${x.agencia}/${x.conta}`,
          id: x.id
        }));
      },
      error: err => console.error('Erro ao carregar contas bancárias', err)
    });
  }

  formatBR(v: number) {
    return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }

  value(cat: string, colKey: string, type: 'previsto' | 'realizado'): number {
    return this.dataMatrix[cat]?.[colKey]?.[type] || 0;
  }

  isSumCategory(cat: string): boolean {
    return cat === 'Total de receitas' || cat === 'Total de despesas' || cat === 'Saldo líquido' || cat === 'Saldo acumulado';
  }

  buscar() {
    this.isLoading = true;

    // Calculate dates
    let dataInicioStr = '';
    let dataFimStr = '';

    if (this.isDaily) {
      const monthStr = String(this.monthIndex + 1).padStart(2, '0');
      const lastDay = new Date(this.year, this.monthIndex + 1, 0).getDate();
      dataInicioStr = `${this.year}-${monthStr}-01`;
      dataFimStr = `${this.year}-${monthStr}-${String(lastDay).padStart(2, '0')}`;
    } else {
      dataInicioStr = `${this.year}-01-01`;
      dataFimStr = `${this.year}-12-31`;
    }

    const queryParams: any = {
      dataInicio: dataInicioStr,
      dataFim: dataFimStr,
      size: 10000
    };
    if (this.selectedAccountId) {
      queryParams.contaBancariaId = this.selectedAccountId;
    }

    // Call services: listFluxoCaixa (Realizado) and listPagar/listReceber (Previsto)
    forkJoin({
      realizados: this.service.listFluxoCaixa(queryParams).pipe(catchError(() => of({ content: [] }))),
      pagar: this.service.listPagar({ size: 10000 }).pipe(catchError(() => of({ content: [] }))),
      receber: this.service.listReceber({ ...queryParams, status: 'ABERTO' }).pipe(catchError(() => of({ content: [] })))
    }).subscribe({
      next: res => {
        this.processData(res.realizados?.content || [], res.pagar?.content || [], res.receber?.content || []);
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  private processData(realizados: any[], pagar: any[], receber: any[]) {
    // Initialize matrix
    this.dataMatrix = {};
    for (const cat of this.categories) {
      this.dataMatrix[cat] = {};
    }

    // Setup columns
    this.columnsList = [];
    this.columnsLabels = [];

    if (this.isDaily) {
      for (const d of this.days) {
        const colKey = `day_${d}`;
        this.columnsList.push(colKey);
        this.columnsLabels.push(String(d));
      }
    } else {
      for (let i = 0; i < 12; i++) {
        const colKey = `month_${i}`;
        this.columnsList.push(colKey);
        this.columnsLabels.push(this.months[i]);
      }
    }

    // Initialize all cells to 0
    for (const cat of this.categories) {
      for (const colKey of this.columnsList) {
        this.dataMatrix[cat][colKey] = { previsto: 0, realizado: 0 };
      }
    }

    // 1. Process Realizados (Paid)
    for (const r of realizados) {
      const colKey = this.getColKey(r.dataMovimento || r.dataPagamento);
      if (!colKey || !this.dataMatrix['Outras receitas'][colKey]) continue;

      const isReceita = r.tipoMovimentacao === 'ENTRADA';
      const valor = Math.abs(r.valor || 0);

      if (isReceita) {
        const cat = this.mapReceitaCategory(r.planoContasNome, r.descricao);
        this.dataMatrix[cat][colKey].realizado += valor;
      } else {
        const cat = this.mapDespesaCategory(r.planoContasNome, r.descricao);
        this.dataMatrix[cat][colKey].realizado += valor;
      }
    }

    // 2. Process Previstos (Unpaid ContasPagar / ContasReceber)
    const unpaidPagar = pagar.filter((p: any) => 
      p.status !== 'PAGO' && p.status !== 'CANCELADO' && 
      (!this.selectedAccountId || p.contaBancariaId === this.selectedAccountId)
    );
    for (const p of unpaidPagar) {
      const colKey = this.getColKey(p.dataVencimento);
      if (!colKey || !this.dataMatrix['Outras despesas'][colKey]) continue;

      const valor = Math.abs(p.valorNominal || p.valor || 0);
      const cat = this.mapDespesaCategory(p.planoContasNome, p.descricao);
      this.dataMatrix[cat][colKey].previsto += valor;
    }

    const unpaidReceber = receber.filter((r: any) => 
      r.status !== 'PAGO' && r.status !== 'CANCELADO' &&
      (!this.selectedAccountId || r.contaBancariaId === this.selectedAccountId)
    );
    for (const r of unpaidReceber) {
      const colKey = this.getColKey(r.dataVencimento);
      if (!colKey || !this.dataMatrix['Outras receitas'][colKey]) continue;

      const valor = Math.abs(r.valorNominal || r.valor || 0);
      const cat = this.mapReceitaCategory(r.planoContasNome, r.descricao);
      this.dataMatrix[cat][colKey].previsto += valor;
    }

    // 3. Calculate Totals (Total Receitas, Total Despesas, Saldo Líquido)
    for (const colKey of this.columnsList) {
      // Total Receitas
      this.dataMatrix['Total de receitas'][colKey].realizado =
        this.dataMatrix['Outras receitas'][colKey].realizado +
        this.dataMatrix['Receitas de vendas'][colKey].realizado +
        this.dataMatrix['Receitas de serviços'][colKey].realizado;

      this.dataMatrix['Total de receitas'][colKey].previsto =
        this.dataMatrix['Outras receitas'][colKey].previsto +
        this.dataMatrix['Receitas de vendas'][colKey].previsto +
        this.dataMatrix['Receitas de serviços'][colKey].previsto;

      // Total Despesas
      this.dataMatrix['Total de despesas'][colKey].realizado =
        this.dataMatrix['Outras despesas'][colKey].realizado +
        this.dataMatrix['Despesas com comissões'][colKey].realizado;

      this.dataMatrix['Total de despesas'][colKey].previsto =
        this.dataMatrix['Outras despesas'][colKey].previsto +
        this.dataMatrix['Despesas com comissões'][colKey].previsto;

      // Saldo líquido
      this.dataMatrix['Saldo líquido'][colKey].realizado =
        this.dataMatrix['Total de receitas'][colKey].realizado -
        this.dataMatrix['Total de despesas'][colKey].realizado;

      this.dataMatrix['Saldo líquido'][colKey].previsto =
        this.dataMatrix['Total de receitas'][colKey].previsto -
        this.dataMatrix['Total de despesas'][colKey].previsto;
    }

    // 4. Calculate Saldo Anterior and Saldo Acumulado (Rollover balance)
    let startingBalance = 0;
    if (realizados.length > 0) {
      const sorted = [...realizados].sort((a, b) => (a.dataMovimento || '').localeCompare(b.dataMovimento || ''));
      const first = sorted[0];
      const val = Math.abs(first.valor || 0);
      const isReceita = first.tipoMovimentacao === 'ENTRADA';
      const act = first.saldoAtual || 0;
      startingBalance = isReceita ? (act - val) : (act + val);
    }

    let runningRealizado = startingBalance;
    let runningPrevisto = startingBalance;

    for (let i = 0; i < this.columnsList.length; i++) {
      const colKey = this.columnsList[i];

      // Saldo do mês anterior
      this.dataMatrix['Saldo do mês anterior'][colKey].realizado = runningRealizado;
      this.dataMatrix['Saldo do mês anterior'][colKey].previsto = runningPrevisto;

      // Saldo acumulado
      runningRealizado += this.dataMatrix['Saldo líquido'][colKey].realizado;
      runningPrevisto += this.dataMatrix['Saldo líquido'][colKey].previsto;

      this.dataMatrix['Saldo acumulado'][colKey].realizado = runningRealizado;
      this.dataMatrix['Saldo acumulado'][colKey].previsto = runningPrevisto;
    }

    // 5. Generate Chart coordinate points
    this.generateChartData();
  }

  private getColKey(dateStr: string | null): string | null {
    if (!dateStr) return null;
    try {
      const parts = dateStr.split('-');
      if (parts.length === 3) {
        const y = Number(parts[0]);
        const m = Number(parts[1]) - 1;
        const d = Number(parts[2]);

        if (this.isDaily) {
          if (y === this.year && m === this.monthIndex) {
            return `day_${d}`;
          }
        } else {
          if (y === this.year) {
            return `month_${m}`;
          }
        }
      }
    } catch {}
    return null;
  }

  private mapReceitaCategory(planoContas: string | null, descricao: string | null): string {
    const text = ((planoContas || '') + ' ' + (descricao || '')).toLowerCase();
    if (text.includes('venda')) return 'Receitas de vendas';
    if (text.includes('serviço') || text.includes('servico')) return 'Receitas de serviços';
    return 'Outras receitas';
  }

  private mapDespesaCategory(planoContas: string | null, descricao: string | null): string {
    const text = ((planoContas || '') + ' ' + (descricao || '')).toLowerCase();
    if (text.includes('comissão') || text.includes('comissao')) return 'Despesas com comissões';
    return 'Outras despesas';
  }

  private generateChartData() {
    const categories: string[] = [];
    const seriesReceitaRealizada: number[] = [];
    const seriesReceitaPrevista: number[] = [];
    const seriesDespesaRealizada: number[] = [];
    const seriesDespesaPrevista: number[] = [];
    const seriesSaldoRealizado: number[] = [];
    const seriesSaldoPrevisto: number[] = [];

    const colCount = this.columnsList.length;

    for (let i = 0; i < colCount; i++) {
      const colKey = this.columnsList[i];
      const label = this.columnsLabels[i];

      categories.push(label);

      const recR = this.dataMatrix['Total de receitas'][colKey].realizado;
      const recP = this.dataMatrix['Total de receitas'][colKey].previsto;
      const desR = this.dataMatrix['Total de despesas'][colKey].realizado;
      const desP = this.dataMatrix['Total de despesas'][colKey].previsto;
      const salR = this.dataMatrix['Saldo acumulado'][colKey].realizado;
      const salP = this.dataMatrix['Saldo acumulado'][colKey].previsto;

      seriesReceitaRealizada.push(recR);
      seriesReceitaPrevista.push(recP);
      seriesDespesaRealizada.push(desR);
      seriesDespesaPrevista.push(desP);
      seriesSaldoRealizado.push(salR);
      seriesSaldoPrevisto.push(salP);
    }

    const seriesData: any[] = [];
    const colorsData: string[] = [];
    const strokeWidths: number[] = [];
    const strokeDashes: number[] = [];

    if (this.mode === 'PREVISTO E REALIZADO' || this.mode === 'Somente realizado') {
      seriesData.push({ name: 'Receitas Realizadas', type: 'column', data: seriesReceitaRealizada });
      colorsData.push('#3b82f6'); // blue
      strokeWidths.push(0);
      strokeDashes.push(0);

      seriesData.push({ name: 'Despesas Realizadas', type: 'column', data: seriesDespesaRealizada });
      colorsData.push('#ef4444'); // red
      strokeWidths.push(0);
      strokeDashes.push(0);

      seriesData.push({ name: 'Saldo Realizado', type: 'line', data: seriesSaldoRealizado });
      colorsData.push('#10b981'); // green
      strokeWidths.push(3);
      strokeDashes.push(0);
    }

    if (this.mode === 'PREVISTO E REALIZADO' || this.mode === 'Somente previsto') {
      seriesData.push({ name: 'Receitas Previstas', type: 'column', data: seriesReceitaPrevista });
      colorsData.push('#93c5fd'); // light blue
      strokeWidths.push(0);
      strokeDashes.push(0);

      seriesData.push({ name: 'Despesas Previstas', type: 'column', data: seriesDespesaPrevista });
      colorsData.push('#fca5a5'); // light red
      strokeWidths.push(0);
      strokeDashes.push(0);

      seriesData.push({ name: 'Saldo Previsto', type: 'line', data: seriesSaldoPrevisto });
      colorsData.push('#34d399'); // light green
      strokeWidths.push(3);
      strokeDashes.push(5);
    }

    this.chartOptions = {
      series: seriesData,
      chart: {
        height: 380,
        type: 'line',
        stacked: false,
        toolbar: { show: true },
        zoom: { enabled: false }
      },
      colors: colorsData,
      stroke: {
        width: strokeWidths,
        dashArray: strokeDashes,
        curve: 'smooth'
      },
      plotOptions: {
        bar: {
          columnWidth: '55%',
          borderRadius: 3
        }
      },
      xaxis: {
        categories: categories,
        axisBorder: { show: false },
        axisTicks: { show: false }
      },
      yaxis: {
        labels: {
          formatter: (value: any) => this.formatBR(value)
        }
      },
      tooltip: {
        shared: true,
        intersect: false,
        y: {
          formatter: (value: any) => this.formatBR(value)
        }
      },
      legend: {
        position: 'bottom',
        horizontalAlign: 'center',
        fontSize: '13px',
        markers: { radius: 12 }
      },
      grid: {
        borderColor: '#f1f5f9',
        strokeDashArray: 4
      }
    };

    const lastColKey = this.columnsList[colCount - 1];
    const targetType = this.mode === 'Somente previsto' ? 'previsto' : 'realizado';
    this.totals = {
      receitas: this.columnsList.reduce((acc, c) => acc + this.dataMatrix['Total de receitas'][c][targetType], 0),
      despesas: this.columnsList.reduce((acc, c) => acc + this.dataMatrix['Total de despesas'][c][targetType], 0),
      saldo: lastColKey ? this.dataMatrix['Saldo acumulado'][lastColKey][targetType] : 0
    };
  }

  printPDF() {
    let dataInicioStr = '';
    let dataFimStr = '';

    if (this.isDaily) {
      const monthStr = String(this.monthIndex + 1).padStart(2, '0');
      const lastDay = new Date(this.year, this.monthIndex + 1, 0).getDate();
      dataInicioStr = `${this.year}-${monthStr}-01`;
      dataFimStr = `${this.year}-${monthStr}-${String(lastDay).padStart(2, '0')}`;
    } else {
      dataInicioStr = `${this.year}-01-01`;
      dataFimStr = `${this.year}-12-31`;
    }

    this.isLoading = true;
    this.service.imprimirFluxoCaixa({
      dataInicio: dataInicioStr,
      dataFim: dataFimStr,
      contaBancariaId: this.selectedAccountId || undefined
    }).subscribe({
      next: (blob: Blob) => {
        const blobUrl = URL.createObjectURL(blob);
        const iframe = document.createElement('iframe');
        iframe.style.display = 'none';
        iframe.src = blobUrl;
        document.body.appendChild(iframe);
        iframe.contentWindow?.print();
        this.isLoading = false;
      },
      error: err => {
        console.error('Erro ao gerar relatório', err);
        alert('Não foi possível gerar a impressão do relatório. Verifique se o backend está ativo e configurado.');
        this.isLoading = false;
      }
    });
  }

  exportExcel() {
    let csvContent = '\uFEFF'; // UTF-8 BOM
    csvContent += `Fluxo de Caixa - ${this.period} (${this.year})\r\n`;
    csvContent += `Conta: ${this.selectedAccountId ? this.contasBancarias.find(c => c.id === this.selectedAccountId)?.label : 'TODAS AS CONTAS'}\r\n`;
    csvContent += `Modo: ${this.mode}\r\n\r\n`;

    csvContent += 'Categorias de Lançamentos';
    for (const label of this.columnsLabels) {
      if (this.mode === 'PREVISTO E REALIZADO') {
        csvContent += `;${label} (Previsto);${label} (Realizado)`;
      } else if (this.mode === 'Somente previsto') {
        csvContent += `;${label} (Previsto)`;
      } else {
        csvContent += `;${label} (Realizado)`;
      }
    }
    csvContent += '\r\n';

    for (const cat of this.categories) {
      csvContent += cat;
      for (const colKey of this.columnsList) {
        const prevVal = this.value(cat, colKey, 'previsto');
        const realVal = this.value(cat, colKey, 'realizado');

        if (this.mode === 'PREVISTO E REALIZADO') {
          csvContent += `;"${this.formatBR(prevVal)}";"${this.formatBR(realVal)}"`;
        } else if (this.mode === 'Somente previsto') {
          csvContent += `;"${this.formatBR(prevVal)}"`;
        } else {
          csvContent += `;"${this.formatBR(realVal)}"`;
        }
      }
      csvContent += '\r\n';
    }

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const filename = `fluxo_caixa_${this.period.toLowerCase()}_${this.year}.csv`;
    link.href = URL.createObjectURL(blob);
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}