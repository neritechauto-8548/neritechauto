import { CommonModule } from '@angular/common';
import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, finalize, takeUntil } from 'rxjs';
import { HistoricoOS, HistoricoPage, HistoricoVeiculo, HistoricoVeiculosService } from './historico-veiculos.service';

@Component({
  selector: 'nt-historico-veiculo-detalhe',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './historico-veiculo-detalhe.html',
  styleUrl: './historico-veiculos.scss'
})
export class HistoricoVeiculoDetalhe implements OnInit {
  private readonly api = inject(HistoricoVeiculosService);
  private readonly route = inject(ActivatedRoute);
  private readonly destroyRef = inject(DestroyRef);
  private readonly until$ = new Subject<void>();
  private readonly search$ = new Subject<string>();
  private requestId=0;
  veiculo: HistoricoVeiculo | null = null;
  id = 0;
  carregandoVeiculo = false;
  carregandoOS = false;
  erroVeiculo = '';
  erroOS = '';
  busca = '';
  ordenar = 'OS';
  incluirAutorizados = false;
  rows: HistoricoOS[] = [];
  total = 0;
  page = 0;
  pageSize = 10;
  imprimindo = false;
  erroImpressao = '';

  constructor() {
    this.search$.pipe(debounceTime(320),distinctUntilChanged(),takeUntil(this.until$))
      .subscribe(()=>this.reload());
    this.destroyRef.onDestroy(()=>{this.until$.next();this.until$.complete();});
  }
  ngOnInit():void {
    this.id=Number(this.route.snapshot.paramMap.get('id'));
    if(!Number.isSafeInteger(this.id)||this.id<=0){this.erroVeiculo='Veículo inválido.';return;}
    this.carregandoVeiculo=true;
    this.api.veiculo(this.id).pipe(finalize(()=>this.carregandoVeiculo=false)).subscribe({
      next: v=>{this.veiculo=v;this.reload();},
      error: err=>this.erroVeiculo=err.status===404?'Veículo não encontrado nesta empresa.':
        'Não foi possível consultar o veículo. Verifique sua conexão.'
    });
  }
  get initials():string {
    return (this.veiculo?.cliente || '').trim().split(/\s+/).filter(Boolean).slice(0,2).map(w=>w[0]).join('').toUpperCase() || 'CL';
  }
  get hasMore():boolean {return this.rows.length<this.total;}
  buscaAlterada(value:string):void {this.busca=value;this.search$.next(value);}
  reload():void {this.rows=[];this.page=0;this.load();}
  load():void {
    if(!this.id || !this.veiculo)return;
    this.carregandoOS=true;this.erroOS='';
    const current=this.page, request=++this.requestId;
    this.api.ordens(this.id,this.busca.trim(),this.ordenar,this.incluirAutorizados,current,this.pageSize)
      .pipe(finalize(()=>{if(request===this.requestId)this.carregandoOS=false;})).subscribe({
        next: (res:HistoricoPage<HistoricoOS>)=>{
          if(request!==this.requestId)return;
          if(current===0)this.rows=res.content||[];
          else this.rows=[...this.rows,...(res.content||[])];
          this.total=res.totalElements||0;
        },
        error:err=>{if(request!==this.requestId)return;this.erroOS=err.status===403?'Acesso não autorizado para consultar ordens.':
          'Não foi possível carregar o histórico de OS.';}
      });
  }
  loadMore():void{if(this.carregandoOS||!this.hasMore)return;this.page++;this.load();}
  formatDate(value?:string):string {
    if(!value)return '—';
    const d=value.substring(0,10).split('-');
    return d.length===3?d.reverse().join('/'):'—';
  }
  currency(value?:number):string {return new Intl.NumberFormat('pt-BR',{style:'currency',currency:'BRL'}).format(value||0);}
  imprimir():void {
    if(!this.veiculo||!this.total||this.imprimindo)return;
    this.imprimindo=true;this.erroImpressao='';
    const preview=window.open('about:blank','_blank');
    if(!preview){this.imprimindo=false;this.erroImpressao='Permita pop-ups para imprimir o histórico.';return;}
    preview.document.title='Gerando histórico · NERITECH AUTO';
    preview.document.body.textContent='Gerando PDF…';
    this.api.baixarRelatorio({veiculoId:this.id,soAutorizadas:false,
      mostrarComissionado:false,mostrarValores:false}).pipe(finalize(()=>this.imprimindo=false))
    .subscribe({
      next:blob=>{
        const url=URL.createObjectURL(blob);
        preview.location.href=url;
        setTimeout(()=>URL.revokeObjectURL(url),60000);
      },
      error:()=>{preview.close();this.erroImpressao='Não foi possível emitir o histórico deste veículo.';}
    });
  }
}
