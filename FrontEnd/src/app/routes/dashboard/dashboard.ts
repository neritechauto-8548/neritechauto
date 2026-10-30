import { Component, OnInit, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { AuthService } from '@core/authentication';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgApexchartsModule } from 'ng-apexcharts';
import { DashboardService, DashboardDTO } from './dashboard.service';
import { environment } from '@env/environment';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import { DashboardComponentPicker } from './dashboard-component-picker';
import { DashboardDynamicWidget } from './dashboard-dynamic-widget';
import { DashboardPreferencesService } from './dashboard-preferences.service';
import { DEFAULT_LAYOUT, WIDGET_ITEMS, WidgetLayout } from './dashboard-widgets';

type DashboardView = 'padrao' | 'gerencial' | 'financeiro' | 'orcamento';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  imports: [CommonModule, FormsModule, RouterModule, NgApexchartsModule, NeriTechIcon, DashboardComponentPicker, DashboardDynamicWidget],
})
export class Dashboard implements OnInit {
  private dashboardService = inject(DashboardService);
  private router = inject(Router);
  private auth = inject(AuthService);
  private preferences = inject(DashboardPreferencesService);
  readonly usuario = toSignal(this.auth.user(), { initialValue: {} });
  get nomeSaudacao(): string {
    const profile = this.usuario() as { name?: string; nomeCompleto?: string };
    return (profile.name || profile.nomeCompleto || 'equipe').trim().split(/\s+/)[0];
  }
  showAddComponent = false;
  showViewOptions = false;
  widgets: WidgetLayout[] = DEFAULT_LAYOUT.map(x => ({...x}));
  layoutSaving = false;
  layoutError = '';
  layoutNotice = '';
  activeInfo = '';
  activeWidgetMenu = '';

  get widgetFinanceiro(): boolean { return this.widgets.some(w=>w.id==='financeiro-os'); }
  set widgetFinanceiro(value:boolean) { this.toggleWidget('financeiro-os',value,'listagem'); }
  get widgetOrdens(): boolean { return this.widgets.some(w=>w.id==='ordens'); }
  set widgetOrdens(value:boolean) { this.toggleWidget('ordens',value,'indicador'); }
  get widgetClientes(): boolean { return this.widgets.some(w=>w.id==='clientes-sem-movimentacao'); }
  set widgetClientes(value:boolean) { this.toggleWidget('clientes-sem-movimentacao',value,'indicador'); }
  lastUpdatedAt: Date | null = null;

  readonly isUxPreview = environment.uxPreview;
  loading = true;
  error = false;
  errorMessage = 'Os dados da oficina não puderam ser carregados agora.';
  data: DashboardDTO | null = null;
  selectedView: DashboardView = 'padrao';
  selectedPeriod = 'mes';
  chartOptions: any = null;

  readonly viewOptions: { value: DashboardView; label: string }[] = [
    { value: 'padrao', label: 'Padrão' },
    { value: 'gerencial', label: 'Gerencial' },
    { value: 'financeiro', label: 'Financeiro' },
    { value: 'orcamento', label: 'Orçamento' },
  ];
  readonly periodOptions = [
    { value: 'mes', label: 'Este mês' },
    { value: 'trimestre', label: 'Últimos 3 meses' },
    { value: 'semestre', label: 'Últimos 6 meses' },
    { value: 'ano', label: 'Este ano' },
  ];

  ngOnInit(): void {
    this.carregarDados();
    this.carregarLayoutPessoal();
  }

  private carregarLayoutPessoal(): void {
    this.preferences.consultar().subscribe({
      next: response => {
        this.widgets = this.normalizarComponentes(response.componentes);
        this.layoutError = '';
      },
      error: err => {
        this.layoutError = err.status===403
          ? 'Sem permissão para carregar sua configuração pessoal.'
          : 'Não foi possível carregar seu layout. A disposição padrão é temporária.';
      }
    });
  }

  private normalizarComponentes(items:WidgetLayout[] | null | undefined):WidgetLayout[] {
    if(!Array.isArray(items))return DEFAULT_LAYOUT.map(x=>({...x}));
    const seen = new Set<string>();
    return items.filter(x=>{
      if(!x || !WIDGET_ITEMS.some(y=>y.id===x.id) || seen.has(x.id)
        || !['indicador','grafico','listagem'].includes(x.tipo) || ![1,2].includes(x.colunas))
        return false;
      seen.add(x.id);return true;
    }).map(x=>({...x}));
  }

  salvarLayoutPessoal(next:WidgetLayout[]):void {
    if(this.layoutSaving)return;
    this.layoutSaving=true;
    this.layoutError='';
    this.layoutNotice='';
    this.preferences.salvar(this.normalizarComponentes(next)).subscribe({
      next:response=>{
        this.widgets=this.normalizarComponentes(response.componentes);
        this.selectedView='padrao';
        this.layoutNotice='Sua visão geral foi atualizada apenas para sua conta.';
        this.layoutSaving=false;
      },
      error:err=>{
        this.layoutError=err.status===403
          ? 'Seu usuário não tem permissão para editar esta visão geral.'
          : 'Não foi possível salvar suas preferências. Nenhuma alteração foi aplicada.';
        this.layoutSaving=false;
      }
    });
  }
  private toggleWidget(id:string,enabled:boolean,tipo:'indicador'|'listagem'):void {
    const old=this.widgets.some(x=>x.id===id);
    if(old===enabled)return;
    const next=enabled ? [...this.widgets,{id,tipo,colunas:1}] : this.widgets.filter(w=>w.id!==id);
    this.salvarLayoutPessoal(next);
  }
  removerComponente(id:string):void {this.salvarLayoutPessoal(this.widgets.filter(x=>x.id!==id));}
  moverComponente(id:string,step:number):void {
    const index=this.widgets.findIndex(w=>w.id===id),target=index+step;
    if(index<0||target<0||target>=this.widgets.length)return;
    const copy=this.widgets.map(x=>({...x}));
    [copy[index],copy[target]]=[copy[target],copy[index]];
    this.salvarLayoutPessoal(copy);
    this.activeWidgetMenu='';
  }
  editarComponente(id:string):void {
    this.showAddComponent=true;
    this.activeWidgetMenu='';
  }
  widgetCatalog(id:string){return WIDGET_ITEMS.find(x=>x.id===id);}
  canShowChart(id:string):boolean {
    return ['faturamento-geral','despesas-receitas','faturamento-despesas'].includes(id);
  }
  widgetRows(id:string):{nome:string,valor:string}[]{
    const d=this.data; if(!d)return [];
    const money=(x:number|null|undefined)=>this.formatCurrency(x);
    const num=(x:number|null|undefined)=>String(x??0);
    const rows:{
      [key:string]:{nome:string,valor:string}[]
    }={
      'financeiro-os':[
        {nome:'A receber (pendente)',valor:money(d.contasReceber)},
        {nome:'A pagar (pendente)',valor:money(d.contasPagar)},
        {nome:'Recebíveis vencidos',valor:money(d.valoresVencidos)},
        {nome:'Receita do período',valor:money(d.faturamentoMes)},
        {nome:'Despesas do período',valor:money(d.despesasMes)}
      ],
      'ordens':[
        {nome:'Ordens em andamento',valor:num(d.osEmAndamento)},
        {nome:'Ordens concluídas',valor:num(d.osConcluidas)},
        {nome:'Veículos em atraso',valor:num(d.veiculosEmAtraso)},
        {nome:'Ticket médio global',valor:money(d.ticketMedio)}
      ],
      'gerencial':[{nome:'Resultado do período',valor:money(d.lucroMes)},{nome:'OS em andamento',valor:num(d.osEmAndamento)},{nome:'Clientes ativos',valor:num(d.totalClientes)}],
      'padrao':[{nome:'Ordens abertas',valor:num(d.osAbertas)},{nome:'Ordens concluídas',valor:num(d.osConcluidas)}],
      'faturamento-geral':[{nome:'Receitas do período',valor:money(d.faturamentoMes)},{nome:'Período anterior',valor:money(d.faturamentoPeriodoAnterior)}],
      'ticket-medio':[{nome:'Ticket médio global da OS',valor:money(d.ticketMedio)}],
      'faturamento-passagem':[{nome:'Faturamento',valor:money(d.faturamentoMes)},{nome:'Entradas de veículos',valor:num(d.entradasVeiculosMes)},{nome:'Ticket médio global',valor:money(d.ticketMedio)}],
      'despesas-receitas':[{nome:'Receitas',valor:money(d.faturamentoMes)},{nome:'Despesas',valor:money(d.despesasMes)}],
      'faturamento-despesas':[{nome:'Faturamento',valor:money(d.faturamentoMes)},{nome:'Despesas',valor:money(d.despesasMes)},{nome:'Resultado',valor:money(d.lucroMes)}],
      'passagem-veiculos':[{nome:'Entradas de veículos',valor:num(d.entradasVeiculosMes)},{nome:'Saídas de veículos',valor:num(d.saidasVeiculosMes)}]
    };
    return rows[id]??[];
  }
  widgetMainValue(id:string):string{
    const rows=this.widgetRows(id);
    return rows.length?rows[0].valor:'—';
  }

  get saudacao(): string {
    const h = new Date().getHours();
    return h < 12 ? 'Bom dia' : h < 18 ? 'Boa tarde' : 'Boa noite';
  }

  get variacaoFaturamentoPercentual(): number | null {
    const anterior = Number(this.data?.faturamentoPeriodoAnterior);
    const atual = Number(this.data?.faturamentoMes);
    if (!Number.isFinite(anterior) || anterior <= 0) return null;
    return Math.round(((atual - anterior) / anterior) * 1000) / 10;
  }

  get variacaoFaturamentoLabel(): string {
    return this.variacaoFaturamentoPercentual === null ? '' :
      Math.abs(this.variacaoFaturamentoPercentual).toLocaleString('pt-BR', { maximumFractionDigits: 1 }) + '%';
  }

  get periodoSelecionadoLabel(): string {
    return this.periodOptions.find(option => option.value === this.selectedPeriod)?.label ?? 'Período';
  }

  get selectedViewIndex(): number {
    return this.viewOptions.findIndex(option => option.value === this.selectedView);
  }

  get margemPercentual(): number {
    const faturamento = Number(this.data?.faturamentoMes ?? 0);
    return faturamento > 0 ? Math.round((Number(this.data?.lucroMes ?? 0) / faturamento) * 100) : 0;
  }

  get hasData(): boolean {
    if (!this.data) return false;
    return [
      this.data.totalClientes,
      this.data.osAbertas,
      this.data.osConcluidas,
      this.data.faturamentoMes,
      this.data.contasReceber,
      this.data.abertosTotal,
      this.data.autorizadosTotal,
      this.data.fechadosTotal,
    ].some(value => Number(value) > 0);
  }

  carregarDados(): void {
    this.loading = true;
    this.error = false;
    const { inicio, fim } = this.getPeriodRange();

    this.dashboardService.getDashboardData(inicio, fim).subscribe({
      next: data => {
        this.data = data;
        this.chartOptions = this.buildChart(data);
        this.lastUpdatedAt = new Date();
        this.loading = false;
      },
      error: err => {
        this.error = true;
        this.errorMessage = err.status === 403
          ? 'Seu perfil não tem permissão para visualizar estes indicadores.'
          : 'Os dados da oficina não puderam ser carregados agora. Verifique a conexão e tente novamente.';
        this.loading = false;
      },
    });
  }

  private getPeriodRange(): { inicio: string; fim: string } {
    const today = new Date();
    let firstMonth = today.getMonth();
    let firstYear = today.getFullYear();
    if (this.selectedPeriod === 'trimestre') {
      const start = new Date(today.getFullYear(), today.getMonth() - 2, 1);
      firstMonth = start.getMonth();
      firstYear = start.getFullYear();
    } else if (this.selectedPeriod === 'semestre') {
      const start = new Date(today.getFullYear(), today.getMonth() - 5, 1);
      firstMonth = start.getMonth();
      firstYear = start.getFullYear();
    } else if (this.selectedPeriod === 'ano') {
      firstMonth = 0;
    }
    const inicio = new Date(firstYear, firstMonth, 1);
    const format = (date: Date) => `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    return { inicio: format(inicio), fim: format(today) };
  }

  private buildChart(data: DashboardDTO): any {
    return {
      series: [
        { name: 'Faturamento', data: data.historicoFaturamento ?? [] },
        { name: 'Despesas', data: data.historicoDespesas ?? [] },
      ],
      chart: {
        type: 'area',
        height: 320,
        toolbar: { show: false },
        fontFamily: 'Inter, ui-sans-serif, system-ui, sans-serif',
        zoom: { enabled: false },
      },
      dataLabels: { enabled: false },
      stroke: { curve: 'smooth', width: 2.5 },
      colors: ['#2563eb', '#f59e0b'],
      fill: {
        type: 'gradient',
        gradient: { opacityFrom: 0.18, opacityTo: 0.02, stops: [0, 100] },
      },
      grid: { borderColor: '#e5e7eb', strokeDashArray: 4 },
      xaxis: {
        categories: data.historicoMeses ?? [],
        axisBorder: { show: false },
        axisTicks: { show: false },
      },
      yaxis: {
        labels: { formatter: (value: number) => this.formatCurrency(value) },
      },
      tooltip: {
        y: { formatter: (value: number) => this.formatCurrency(value) },
      },
      legend: { position: 'top', horizontalAlign: 'left' },
    };
  }

  barHeight(value: number | null | undefined): number {
    const numericValue = Number(value ?? 0);
    return Math.max(18, Math.min(100, Math.round((numericValue / 140000) * 100)));
  }

  formatCurrency(value: number | null | undefined): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      maximumFractionDigits: 2,
      minimumFractionDigits: 2,
    }).format(Number(value ?? 0));
  }

  selecionarVisao(view: DashboardView): void {
    this.selectedView = view;
  }

  onViewTabKeydown(event: KeyboardEvent, index: number): void {
    const direction = event.key === 'ArrowRight' ? 1 : event.key === 'ArrowLeft' ? -1 : 0;
    const target = event.key === 'Home' ? 0 : event.key === 'End' ? this.viewOptions.length - 1 :
      direction ? (index + direction + this.viewOptions.length) % this.viewOptions.length : -1;
    if (target < 0) return;
    event.preventDefault();
    this.selecionarVisao(this.viewOptions[target].value);
    document.getElementById(`dashboard-view-${target}`)?.focus();
  }

  comparacaoPercentual(atual: number | null | undefined, anterior: number | null | undefined): string {
    if (anterior == null || !Number.isFinite(Number(anterior)) || Number(anterior) <= 0) return 'Sem base comparativa';
    const change = ((Number(atual ?? 0) - Number(anterior)) / Number(anterior)) * 100;
    return `${Math.abs(change).toLocaleString('pt-BR', { maximumFractionDigits: 1 })}% ${change >= 0 ? 'acima' : 'abaixo'}`;
  }

  navegarPara(route: string): void {
    this.router.navigateByUrl(route);
  }
}
