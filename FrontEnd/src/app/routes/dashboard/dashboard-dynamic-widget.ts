import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgApexchartsModule } from 'ng-apexcharts';
import { DashboardUiIcon } from './dashboard-ui-icon';
import { DashboardDTO } from './dashboard.service';
import { WIDGET_ITEMS, WidgetLayout } from './dashboard-widgets';

@Component({
  selector:'nt-dashboard-dynamic-widget',
  standalone:true,
  imports:[CommonModule,NgApexchartsModule,DashboardUiIcon],
  templateUrl:'./dashboard-dynamic-widget.html',
  styleUrl:'./dashboard-dynamic-widget.scss'
})
export class DashboardDynamicWidget {
  @Input({required:true}) widget!:WidgetLayout;
  @Input() data:DashboardDTO|null=null;
  @Input() rows:{nome:string,valor:string}[]=[];
  @Input() chartOptions:any=null;
  @Input() index=0;
  @Input() total=0;
  @Input() updatedAt:Date|null=null;
  @Input() saving=false;
  @Output() remove=new EventEmitter<string>();
  @Output() move=new EventEmitter<{id:string,step:number}>();
  @Output() edit=new EventEmitter<string>();
  @Output() reload=new EventEmitter<void>();

  menu=false;
  info=false;
  get meta(){return WIDGET_ITEMS.find(x=>x.id===this.widget.id);}
  get chartSupported():boolean {
    return ['faturamento-geral','despesas-receitas','faturamento-despesas'].includes(this.widget.id);
  }
  action(type:'edit'|'remove'|'before'|'after'):void{
    this.menu=false;
    if(type==='edit')this.edit.emit(this.widget.id);
    if(type==='remove')this.remove.emit(this.widget.id);
    if(type==='before')this.move.emit({id:this.widget.id,step:-1});
    if(type==='after')this.move.emit({id:this.widget.id,step:1});
  }
}
