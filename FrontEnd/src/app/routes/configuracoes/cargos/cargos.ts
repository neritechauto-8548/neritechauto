import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { A11yModule } from '@angular/cdk/a11y';
import { Subscription, Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { CargoRequest, CargoResponse, CargoService } from './cargo.service';

@Component({
  selector: 'app-cargos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, A11yModule],
  template: `
    <main class="min-h-screen space-y-4 bg-[#f7f8fa] px-4 py-5 sm:px-6 lg:px-8">
      <nav aria-label="Caminho de navegação" class="text-[11px] text-[#8c98a8]">NERITECH AUTO <span class="mx-2">›</span> Gestão de pessoas <span class="mx-2">›</span> Cargos</nav>
      <nav aria-label="Gestão de pessoas" class="flex flex-wrap gap-1 rounded-lg border border-[#e1e5eb] bg-white px-2 py-1 shadow-sm">
        <a routerLink="/configuracoes/colaboradores" class="rounded-md px-3 py-2 text-[13px] text-slate-700 hover:bg-slate-100">♙ &nbsp;Funcionários</a>
        <span aria-current="page" class="rounded-md bg-[#d9e7ff] px-3 py-2 text-[13px] font-medium text-[#18328A]">▣ &nbsp;Cargos</span>
        <a routerLink="/configuracoes/epis" class="rounded-md px-3 py-2 text-[13px] text-slate-700 hover:bg-slate-100">♢ &nbsp;EPIs</a>
        <a routerLink="/financeiro/comissoes" class="rounded-md px-3 py-2 text-[13px] text-slate-700 hover:bg-slate-100">♧ &nbsp;Comissões</a>
      </nav>
      <header class="flex items-center justify-between gap-3">
        <h1 class="text-[28px] font-semibold tracking-tight text-[#101828]">Cargos <span aria-label="Ajuda" title="Cargos da empresa autenticada" class="align-middle text-[15px] text-[#18328A]">⬡</span></h1>
        <button type="button" (click)="abrirNovo()" class="rounded-lg bg-[#83ef47] px-5 py-2.5 text-[13px] font-medium text-[#102000] hover:bg-[#74e635]">＋ &nbsp;Novo cargo</button>
      </header>
      <label class="relative block">
        <span class="sr-only">Pesquisar cargos</span>
        <span class="pointer-events-none absolute left-3 top-2.5 text-[#94a3b8]">⌕</span>
        <input type="search" [ngModel]="busca" (ngModelChange)="alterarBusca($event)" placeholder="Pesquisar cargos"
          class="h-10 w-full rounded-lg border border-[#ccd4df] bg-white pl-10 pr-3 text-[13px] outline-none focus:border-[#18328A]"/>
      </label>
      <p *ngIf="aviso" role="status" class="rounded-lg bg-blue-50 p-3 text-[13px] text-[#18328A]">{{ aviso }}</p>
      <section aria-label="Listagem de cargos" class="overflow-hidden rounded-lg border border-[#e1e5eb] bg-white shadow-sm">
        <div *ngIf="carregando" role="status" class="p-10 text-center text-[13px] text-slate-500">Carregando cargos...</div>
        <p *ngIf="erro && !carregando" role="alert" class="p-8 text-center text-red-700">{{ erro }} <button type="button" (click)="carregar()" class="underline">Tentar novamente</button></p>
        <div *ngIf="!carregando && !erro" class="overflow-x-auto">
          <table class="w-full min-w-[520px] text-left text-[13px]">
            <thead class="border-b border-[#e1e5eb] text-[#8190a3]"><tr>
              <th scope="col" class="w-[38%] px-4 py-4 font-medium">Descrição</th>
              <th scope="col" class="w-[30%] px-4 py-4 font-medium">Código</th>
              <th scope="col" class="px-4 py-4 font-medium">Tipo</th>
            </tr></thead>
            <tbody class="divide-y divide-[#e1e5eb]">
              <tr *ngFor="let cargo of cargos" class="hover:bg-[#f7f9fb]">
                <td class="px-4 py-4"><button type="button" (click)="abrirEdicao(cargo)" class="text-[#18328A] hover:underline">{{ cargo.nome }}</button></td>
                <td class="px-4 py-4">{{ cargo.codigoCbo || '—' }}</td>
                <td class="px-4 py-4">{{ cargo.tipo || '—' }}</td>
              </tr>
              <tr *ngIf="cargos.length===0"><td colspan="3" class="h-[122px] text-center text-[#8c98a8]">▧ &nbsp;Não há dados</td></tr>
            </tbody>
          </table>
        </div>
      </section>
      <nav *ngIf="paginas>1" aria-label="Paginação de cargos" class="flex justify-end gap-3 text-[13px]">
        <button type="button" (click)="pagina=pagina-1; carregar()" [disabled]="pagina===0||carregando" class="rounded-lg border border-slate-300 bg-white px-3 py-2 disabled:opacity-40">Anterior</button>
        <span class="self-center">{{ pagina+1 }} / {{ paginas }}</span>
        <button type="button" (click)="pagina=pagina+1; carregar()" [disabled]="pagina+1>=paginas||carregando" class="rounded-lg border border-slate-300 bg-white px-3 py-2 disabled:opacity-40">Próxima</button>
      </nav>
      <div *ngIf="modal" class="fixed inset-0 z-[80] flex items-center justify-center p-4">
        <button class="absolute inset-0 bg-black/80" type="button" (click)="fechar()" aria-label="Fechar modal"></button>
        <section role="dialog" aria-modal="true" aria-labelledby="cargo-titulo" cdkTrapFocus [cdkTrapFocusAutoCapture]="true"
          (keydown.escape)="fechar()" class="relative w-full max-w-[780px] rounded-lg bg-white shadow-xl">
          <form (ngSubmit)="salvar()" #f="ngForm">
            <header class="px-4 pb-3 pt-5">
              <h2 id="cargo-titulo" class="text-[15px] font-semibold text-[#8591a1]">{{ idAtual ? 'Edição de cargo' : 'Cadastro de cargo' }}</h2>
              <p class="mt-1 text-[13px] text-[#8591a1]">Preencha as informações para {{ idAtual ? 'atualizar' : 'registrar' }} um cargo de trabalho</p>
            </header>
            <div class="mx-4 rounded-lg border border-[#d6dee9] p-4">
              <h3 class="mb-4 text-[14px] font-semibold text-[#8591a1]">Dados gerais</h3>
              <div class="grid grid-cols-1 gap-4 sm:grid-cols-[2fr_1fr]">
                <label class="flex flex-col gap-2 text-[13px] font-medium">Descrição do cargo <span class="text-red-500">*</span>
                  <input name="nome" [(ngModel)]="form.nome" required minlength="2" maxlength="100" placeholder="Informe uma descrição do cargo"
                    class="h-11 w-full rounded-lg border border-[#cbd4df] px-3 font-normal focus:border-[#18328A]"/>
                </label>
                <label class="flex flex-col gap-2 text-[13px] font-medium">Código CBO <span class="text-red-500">*</span>
                  <input name="codigoCbo" [(ngModel)]="form.codigoCbo" required maxlength="10" placeholder="Informe um código CBO"
                    class="h-11 w-full rounded-lg border border-[#cbd4df] px-3 font-normal focus:border-[#18328A]"/>
                </label>
              </div>
            </div>
            <p *ngIf="erroFormulario" role="alert" class="mx-4 mt-3 text-[13px] text-red-700">{{ erroFormulario }}</p>
            <footer class="flex justify-end gap-3 px-4 pb-5 pt-5">
              <button type="button" (click)="fechar()" [disabled]="salvando" class="rounded-lg border border-[#cbd4df] px-5 py-2.5 text-[13px]">Cancelar</button>
              <button type="submit" [disabled]="f.invalid||salvando" class="rounded-lg bg-[#83ef47] px-5 py-2.5 text-[13px] font-medium text-[#102000] disabled:opacity-40">＋ &nbsp;{{ salvando ? 'Salvando...' : idAtual ? 'Salvar cargo' : 'Cadastrar cargo' }}</button>
            </footer>
          </form>
        </section>
      </div>
    </main>
  `
})
export class CargosComponent implements OnInit, OnDestroy {
  private readonly api=inject(CargoService);
  private readonly busca$=new Subject<string>();
  private readonly assinatura=this.busca$.pipe(debounceTime(260), distinctUntilChanged()).subscribe(()=>{this.pagina=0;this.carregar();});
  cargos: CargoResponse[]=[];
  busca='';
  pagina=0;
  paginas=1;
  carregando=false;
  salvando=false;
  erro='';
  aviso='';
  modal=false;
  idAtual:number|null=null;
  form:CargoRequest={nome:'',codigoCbo:'',temComissao:false,ativo:true};
  erroFormulario='';
  ngOnInit():void{this.carregar();}
  ngOnDestroy():void{this.assinatura.unsubscribe();this.busca$.complete();}
  alterarBusca(v:string):void{this.busca=v;this.busca$.next(v);}
  carregar():void{
    this.carregando=true;this.erro='';
    this.api.list({page:this.pagina,size:20,q:this.busca.trim()||undefined}).subscribe({
      next:p=>{this.cargos=p.content||[];this.paginas=Math.max(1,p.totalPages||1);this.carregando=false;},
      error:e=>{this.carregando=false;this.erro=e.status===403?'Você não tem permissão para consultar cargos.':'Não foi possível carregar os cargos.';}
    });
  }
  abrirNovo():void{this.idAtual=null;this.form={nome:'',codigoCbo:'',temComissao:false,ativo:true};this.erroFormulario='';this.modal=true;}
  abrirEdicao(c:CargoResponse):void{
    this.idAtual=c.id;this.form={nome:c.nome,codigoCbo:c.codigoCbo||'',temComissao:!!c.temComissao,ativo:c.ativo,versao:c.versao};
    this.erroFormulario='';this.modal=true;
  }
  fechar():void{if(!this.salvando)this.modal=false;}
  salvar():void{
    if(this.salvando)return;
    const nome=(this.form.nome||'').trim(),codigo=(this.form.codigoCbo||'').trim();
    if(nome.length<2||!codigo){this.erroFormulario='Informe descrição e código CBO.';return;}
    this.salvando=true;this.erroFormulario='';
    const payload={...this.form,nome,codigoCbo:codigo};
    (this.idAtual?this.api.atualizar(this.idAtual,payload):this.api.criar(payload)).subscribe({
      next:()=>{this.salvando=false;this.modal=false;this.aviso='Cargo salvo com sucesso.';this.carregar();},
      error:e=>{this.salvando=false;this.erroFormulario=e.status===403?'Sem permissão.':e.status===409?'Cargo duplicado ou alterado por outro usuário.':'Não foi possível salvar.';}
    });
  }
}
