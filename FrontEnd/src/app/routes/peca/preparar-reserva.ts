import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NeriTechIcon, PageHeader } from '@shared';

@Component({ standalone:true, selector:'app-preparar-reserva', changeDetection:ChangeDetectionStrategy.OnPush, imports:[FormsModule,RouterLink,PageHeader,NeriTechIcon], templateUrl:'./preparar-reserva.html', styleUrl:'./preparar-reserva.scss' })
export class PrepararReserva {
  origem='OS'; vinculo=''; unidade=''; peca=''; local=''; quantidade:number|null=null; politica='PADRAO'; validade=''; observacoes=''; tentou=false;
  revisar():void{this.tentou=true}
}
