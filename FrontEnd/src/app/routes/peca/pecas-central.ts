import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DataViewState, NeriTechIcon, PageHeader } from '@shared';

@Component({ standalone:true, selector:'app-pecas-central', changeDetection:ChangeDetectionStrategy.OnPush, imports:[FormsModule,RouterLink,PageHeader,NeriTechIcon,DataViewState], templateUrl:'./pecas-central.html', styleUrl:'./pecas-central.scss' })
export class PecasCentral {
  readonly abas=['Necessidades','Reservas','Separação','Consumo','Histórico'];
  abaAtiva='Necessidades'; busca=''; unidade=''; origem=''; situacao=''; local='';
  limpar():void { this.busca=''; this.unidade=''; this.origem=''; this.situacao=''; this.local=''; }
}
