import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DataViewState, NeriTechIcon, PageHeader } from '@shared';
@Component({standalone:true,selector:'app-faturamento-fila',changeDetection:ChangeDetectionStrategy.OnPush,imports:[FormsModule,RouterLink,PageHeader,NeriTechIcon,DataViewState],templateUrl:'./faturamento-fila.html',styleUrl:'./faturamento-fila.scss'})
export class FaturamentoFila { readonly abas=['Liberadas para faturar','Cobranças preparadas','Aguardando pagamento','Fiscal pendente','Concluídas']; abaAtiva=this.abas[0]; busca=''; unidade=''; situacao=''; vencimento=''; responsavel=''; limpar():void{this.busca='';this.unidade='';this.situacao='';this.vencimento='';this.responsavel='';} }
