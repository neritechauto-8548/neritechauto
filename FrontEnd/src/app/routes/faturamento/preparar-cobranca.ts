import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NeriTechIcon, PageHeader } from '@shared';
@Component({standalone:true,selector:'app-preparar-cobranca',changeDetection:ChangeDetectionStrategy.OnPush,imports:[FormsModule,RouterLink,PageHeader,NeriTechIcon],templateUrl:'./preparar-cobranca.html',styleUrl:'./preparar-cobranca.scss'})
export class PrepararCobranca { origem='OS'; vinculo=''; unidade=''; cliente=''; vencimento=''; canal='PIX'; responsavel=''; observacoes=''; tentou=false; revisar():void{this.tentou=true} }
