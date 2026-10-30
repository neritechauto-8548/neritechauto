import { CommonModule } from '@angular/common';
import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { A11yModule } from '@angular/cdk/a11y';
import { DEFAULT_LAYOUT, WIDGET_GROUPS, WIDGET_ITEMS, WIDGET_PRESETS, WidgetLayout, WidgetType } from './dashboard-widgets';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import { DashboardUiIcon } from './dashboard-ui-icon';

@Component({
  selector:'nt-dashboard-component-picker',
  standalone:true,
  imports:[CommonModule,FormsModule,A11yModule,NeriTechIcon, DashboardUiIcon],
  templateUrl:'./dashboard-component-picker.html',
  styleUrl:'./dashboard-component-picker.scss'
})
export class DashboardComponentPicker {
  @Input({required:true}) componentes:WidgetLayout[]=[];
  @Input() saving=false;
  @Input() error='';
  @Output() closePicker=new EventEmitter<void>();
  @Output() saveLayout=new EventEmitter<WidgetLayout[]>();

  readonly groups=WIDGET_GROUPS;
  readonly presets=WIDGET_PRESETS;
  readonly items=WIDGET_ITEMS;
  expanded=new Set<string>(['os']);
  modal:'nenhum'|'modelos'|'confirmacao'='nenhum';
  selectedPreset=(typeof WIDGET_PRESETS)[number] | null=null;
  configuring:string | null=null;
  tipo:WidgetType='indicador';
  colunas=1;

  @HostListener('document:keydown.escape',['$event'])
  escape(e:KeyboardEvent):void {
    e.preventDefault();
    if(this.modal!=='nenhum')this.modal='nenhum';
    else if(this.configuring)this.configuring=null;
    else this.closePicker.emit();
  }
  item(id:string){return this.items.find(x=>x.id===id);}
  isSelected(id:string){return this.componentes.some(x=>x.id===id);}
  toggle(group:string):void {
    if(this.expanded.has(group))this.expanded.delete(group);
    else this.expanded.add(group);
  }
  configure(id:string):void{
    const found=this.componentes.find(x=>x.id===id);
    this.configuring=id;
    this.tipo=found?.tipo || (id==='financeiro-os'?'listagem':'indicador');
    this.colunas=found?.colunas || 1;
  }
  saveSelected():void {
    if(!this.configuring || this.saving)return;
    const isExisting=this.isSelected(this.configuring);
    const next=this.componentes.map(x=>({...x}));
    const candidate:WidgetLayout={id:this.configuring,tipo:this.tipo,colunas:this.colunas};
    if(isExisting){const index=next.findIndex(x=>x.id===candidate.id);next[index]=candidate;}
    else next.push(candidate);
    this.saveLayout.emit(next);
    this.configuring=null;
  }
  openPresets():void{this.configuring=null;this.modal='modelos';}
  choosePreset(id:string):void{
    const preset=this.presets.find(x=>x.id===id);
    if(!preset)return;
    this.selectedPreset=preset;
    this.modal=this.componentes.length?'confirmacao':'modelos';
    if(!this.componentes.length)this.confirmPreset();
  }
  confirmPreset():void {
    if(!this.selectedPreset || this.saving)return;
    const next:WidgetLayout[]=this.selectedPreset.ids.map(id=>({
      id,tipo:id==='financeiro-os'?'listagem':'indicador',colunas:1
    }));
    this.saveLayout.emit(next);
    this.modal='nenhum';
    this.selectedPreset=null;
  }
  restoreDefault():void{this.saveLayout.emit(DEFAULT_LAYOUT.map(w=>({...w})));}
}
