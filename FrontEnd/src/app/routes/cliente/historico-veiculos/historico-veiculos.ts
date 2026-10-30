import { CommonModule } from '@angular/common';
import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Subject, finalize, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { HistoricoVeiculosService, HistoricoVeiculo, HistoricoRelatorioOpcoes } from './historico-veiculos.service';
import { LocalStorageService } from '@shared/services/storage.service';

type ColunaHistorico = 'cliente' | 'placa';
@Component({
  selector: 'nt-historico-veiculos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './historico-veiculos.html',
  styleUrl: './historico-veiculos.scss'
})
export class HistoricoVeiculos implements OnInit {
  private readonly api = inject(HistoricoVeiculosService);
  private readonly storage = inject(LocalStorageService);
  private readonly destroy = inject(DestroyRef);
  private readonly search$ = new Subject<string>();
  private readonly until$ = new Subject<void>();
  private requestId = 0;
  loading = false;
  error = '';
  busca = '';
  ordem = 'AZ';
  page = 0;
  pageSize = 10;
  rows: HistoricoVeiculo[] = [];
  total = 0;
  columnsOpen = false;
  filterOpen = false;
  reportOpen = false;
  columnSearch = '';
  selectedColumnSearch = '';
  readonly options: {key:ColunaHistorico,label:string}[] = [
    {key:'cliente',label:'Cliente'}, {key:'placa',label:'Placa'}
  ];
  selectedColumns: ColunaHistorico[] = ['cliente','placa'];
  draftColumns: ColunaHistorico[] = ['cliente','placa'];
  dragging: ColunaHistorico | null = null;
  reportCategory = 'Clientes';
  reportName = 'Histórico de veículos';
  reportSearch = '';
  reportAuthorized = false;
  reportCommissioned = false;
  reportValues = false;
  reportLoading = false;
  reportError = '';
  private readonly columnsKey = 'neritech-historico-colunas:';

  constructor() {
    this.search$.pipe(debounceTime(320), distinctUntilChanged(), takeUntil(this.until$))
      .subscribe(() => this.refresh());
    this.destroy.onDestroy(() => { this.until$.next(); this.until$.complete(); });
  }

  ngOnInit(): void {
    this.restoreColumns(); this.load();
  }
  get hasMore():boolean { return this.rows.length < this.total; }
  get filteredColumnOptions() {
    const q=this.columnSearch.trim().toLocaleLowerCase('pt-BR');
    return this.options.filter(o => o.label.toLocaleLowerCase('pt-BR').includes(q));
  }
  get filteredSelectedColumns() {
    const q=this.selectedColumnSearch.trim().toLocaleLowerCase('pt-BR');
    return this.draftColumns.filter(c => this.label(c).toLocaleLowerCase('pt-BR').includes(q));
  }
  label(key: ColunaHistorico):string { return this.options.find(o=>o.key===key)?.label || key; }
  inputSearch(value:string):void {this.busca=value;this.search$.next(value);}
  refresh():void {this.page=0;this.rows=[];this.load();}
  load():void {
    this.loading=true;this.error='';
    const at=this.page, request=++this.requestId;
    this.api.listar(this.busca.trim(),this.ordem,at,this.pageSize).pipe(
      finalize(()=>{if(request===this.requestId)this.loading=false;})
    ).subscribe({
      next: data => {
        if(request!==this.requestId)return;
        if(at===0)this.rows=data.content||[];
        else this.rows=[...this.rows,...(data.content||[])];
        this.total=data.totalElements||0;
      },
      error: err => {
        if(request!==this.requestId)return;
        this.error=err.status===403?'Seu perfil não possui permissão para consultar o histórico.':
          'Não foi possível consultar os veículos. Verifique sua conexão.';
      }
    });
  }
  loadMore():void {if(this.loading || !this.hasMore)return;this.page++;this.load();}
  clearFilter():void {this.busca='';this.ordem='AZ';this.filterOpen=false;this.refresh();}
  openColumns():void {this.draftColumns=[...this.selectedColumns];this.columnSearch='';this.selectedColumnSearch='';this.columnsOpen=true;}
  toggleColumn(key:ColunaHistorico):void {
    if(key==='cliente')return;
    this.draftColumns=this.draftColumns.includes(key)
      ? this.draftColumns.filter(k=>k!==key) : [...this.draftColumns,key];
  }
  moveColumn(key:ColunaHistorico,step:number):void {
    if(key==='cliente')return;
    const i=this.draftColumns.indexOf(key),j=i+step;
    if(i<1 || j<1 || j>=this.draftColumns.length)return;
    const next=[...this.draftColumns];[next[i],next[j]]=[next[j],next[i]];this.draftColumns=next;
  }
  dropColumn(target:ColunaHistorico):void {
    if(!this.dragging||this.dragging==='cliente'||target==='cliente'||target===this.dragging)return;
    const next=this.draftColumns.filter(k=>k!==this.dragging);
    const index=next.indexOf(target);
    next.splice(index,0,this.dragging);this.draftColumns=next;this.dragging=null;
  }
  restoreDefault():void {this.draftColumns=['cliente','placa'];}
  private scopedColumnsKey():string {
    const value=this.storage.get('tenantId') || this.storage.get('empresaId');
    const id=typeof value==='number' || typeof value==='string' ? String(value) : 'local';
    return this.columnsKey+id;
  }
  restoreColumns():void {
    try {
      const raw=JSON.parse(localStorage.getItem(this.scopedColumnsKey())||'null');
      if(Array.isArray(raw) && raw.includes('cliente') && raw.every(x=>x==='cliente'||x==='placa'))
        this.selectedColumns=['cliente',...(raw.filter(x=>x==='placa') as ColunaHistorico[])];
    } catch {}
  }
  saveColumns():void {
    this.selectedColumns=[...this.draftColumns];
    try {localStorage.setItem(this.scopedColumnsKey(),JSON.stringify(this.selectedColumns));}catch{}
    this.columnsOpen=false;
  }
  openReport():void {
    this.reportSearch='';this.reportAuthorized=false;this.reportCommissioned=false;
    this.reportValues=false;this.reportError='';this.reportOpen=true;
  }
  readonly reportGroups = [
    {title:'Orçamentos & Serv.',items:[] as string[]},
    {title:'Venda direta',items:[] as string[]},
    {title:'Clientes',items:['Clientes','Clientes sem movimentação','Veículos',
      'Histórico de veículos','Veículos sem movimentação']},
    {title:'Gestão de pessoas',items:[] as string[]},
    {title:'Gestão de recursos',items:[] as string[]},
    {title:'Peças & Estoque',items:[] as string[]},
    {title:'Financeiro',items:[] as string[]},
    {title:'Fiscal',items:[] as string[]}
  ];
  get reportVisible():boolean {
    return !this.reportSearch.trim() || this.reportName.toLocaleLowerCase('pt-BR')
      .includes(this.reportSearch.toLocaleLowerCase('pt-BR'));
  }
  reportReset():void {this.reportAuthorized=this.reportCommissioned=this.reportValues=false;this.reportError='';}
  reportAction(mode:'visualizar'|'baixar'):void {
    if(this.reportLoading)return;
    this.reportLoading=true;this.reportError='';
    // Abrir a aba no clique (e não na resposta assíncrona) evita bloqueio de pop-up.
    const preview = mode==='visualizar' ? window.open('about:blank','_blank') : null;
    if(mode==='visualizar' && !preview){
      this.reportLoading=false;
      this.reportError='Permita a abertura de abas para visualizar o relatório.';
      return;
    }
    if(preview){preview.document.title='Gerando relatório · NERITECH AUTO';
      preview.document.body.textContent='Gerando PDF…';}
    const options:HistoricoRelatorioOpcoes={busca:this.busca,soAutorizadas:this.reportAuthorized,
      mostrarComissionado:this.reportCommissioned,mostrarValores:this.reportValues};
    this.api.baixarRelatorio(options).pipe(finalize(()=>this.reportLoading=false)).subscribe({
      next: blob => {
        if(blob.type && !blob.type.includes('pdf')) {
          preview?.close();this.reportError='O servidor não retornou um PDF válido.';return;
        }
        const url=URL.createObjectURL(blob);
        if(mode==='visualizar'){
          if(preview)preview.location.href=url;
          setTimeout(()=>URL.revokeObjectURL(url),60000);
        } else {
          const a=document.createElement('a');a.href=url;a.download='historico-veiculos.pdf';
          a.click();setTimeout(()=>URL.revokeObjectURL(url),1000);
        }
      },
      error: err => {
        preview?.close();
        this.reportError=err.status===422?'Relatório muito extenso. Refine a busca por veículos e tente novamente.':
          err.status===403?'Sem permissão para emitir este relatório.':
          'Não foi possível gerar o PDF. Tente novamente.';
      }
    });
  }
}
