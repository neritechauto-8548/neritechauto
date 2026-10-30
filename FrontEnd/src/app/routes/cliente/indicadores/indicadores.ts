import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ClientesService } from '../cliente/cliente.service';
import { ClienteResponse } from '../models/cliente.models';
import { ClientesNav } from '../clientes-nav';
import { environment } from '@env/environment';

type PeriodGroup = 'dia' | 'semana' | 'mes' | 'ano';

@Component({
  selector: 'nt-clientes-indicadores',
  standalone: true,
  imports: [CommonModule, FormsModule, ClientesNav],
  templateUrl: './indicadores.html',
  styleUrl: './indicadores.scss',
})
export class ClientesIndicadores implements OnInit {
  private readonly clientService = inject(ClientesService);
  readonly isUxPreview = environment.uxPreview;
  readonly groupOptions = [
    {value:'dia', label:'Dia — máx. 30 dias'},
    {value:'semana', label:'Semana — máx. 90 dias'},
    {value:'mes', label:'Mês — máx. 12 meses'},
    {value:'ano', label:'Ano — máx. 5 anos'},
  ];
  grouping: PeriodGroup = 'mes';
  from = this.dateToInput(new Date(new Date().getFullYear()-1,new Date().getMonth(),new Date().getDate()));
  to = this.dateToInput(new Date());
  loading = false;
  error = '';
  total: number | null = null;
  male = 0; female = 0; unknown = 0;
  avgMale: number | null = null; avgFemale: number | null = null;
  avgUnknown: number | null = null; avgOverall: number | null = null;
  bars: Array<{label:string;count:number;height:number}> = [];
  points: Array<{label:string;total:number;male:number;female:number;unknown:number;x:number;maleY:number;femaleY:number;unknownY:number;totalY:number}> = [];
  series: {male:string;female:string;unknown:string;total:string} = {male:'',female:'',unknown:'',total:''};
  percent(kind:'male'|'female'|'unknown'):number {
    const total=this.male+this.female+this.unknown;
    return total ? (this[kind]/total)*100 : 0;
  }
  get pieGradient():string {
    const male=this.percent('male'), female=this.percent('female');
    return `conic-gradient(#3496ee 0 ${male}%,#fb6096 ${male}% ${male+female}%,#a9b1ba ${male+female}% 100%)`;
  }
  get periodDescription():string {
    return {dia:'dia',semana:'semana',mes:'mês',ano:'ano'}[this.grouping];
  }
  get totalEligible(): number { return Math.max(1, this.male + this.female + this.unknown); }
  private fetchVersion = 0;

  ngOnInit(): void { this.refresh(); }

  private dateToInput(date: Date): string {
    return [date.getFullYear(), String(date.getMonth()+1).padStart(2,'0'), String(date.getDate()).padStart(2,'0')].join('-');
  }
  private validDates(): boolean {
    if (!this.from || !this.to || this.from > this.to) return false;
    const duration = (new Date(this.to+'T00:00:00').getTime() - new Date(this.from+'T00:00:00').getTime()) / 86400000;
    const limits: Record<PeriodGroup,number> = {dia:30,semana:90,mes:366,ano:1830};
    return Number.isFinite(duration) && duration <= limits[this.grouping];
  }
  refresh(): void {
    if (!this.validDates()) {
      this.error = 'Selecione datas válidas dentro do limite do agrupamento.';
      return;
    }
    const current = ++this.fetchVersion;
    this.loading = true; this.error = ''; this.total = null; this.bars = []; this.points=[];
    const filters = { dataInicio:this.from, dataFim:this.to, page:0, size:100, sort:'dataCadastro,asc' };
    this.clientService.list(filters).subscribe({
      next: first => {
        if (this.fetchVersion !== current) return;
        this.total = first.totalElements;
        const pages = first.totalPages;
        if (pages > 10) {
          this.error = 'A base possui mais de 1.000 registros no período. Para indicadores exatos, é necessária uma API de agregação no servidor.';
          this.loading = false;
          return;
        }
        if (pages <= 1) {
          this.calculate(first.content || []);
          this.loading = false;
          return;
        }
        const rest = Array.from({length:pages-1},(_,index) =>
          this.clientService.list({...filters,page:index+1}));
        forkJoin(rest).subscribe({
          next: results => {
            if(this.fetchVersion !== current) return;
            this.calculate([...(first.content||[]),...results.flatMap(page => page.content || [])]);
            this.loading = false;
          },
          error:() => {
            if(this.fetchVersion !== current) return;
            this.loading = false; this.total = null;
            this.error = 'Não foi possível consultar todas as páginas. Nenhum indicador parcial será apresentado.';
          },
        });
      },
      error: () => { if(this.fetchVersion !== current) return; this.loading = false; this.error = 'Não foi possível consultar os indicadores desta empresa.'; },
    });
  }
  private age(birth?:string):number|null {
    if(!birth) return null;
    const date = new Date(birth+'T00:00:00');
    const now = new Date();
    if(Number.isNaN(date.getTime()) || date > now) return null;
    let age = now.getFullYear()-date.getFullYear();
    if(now.getMonth()<date.getMonth() || now.getMonth()===date.getMonth() && now.getDate()<date.getDate()) age--;
    return age >= 0 && age <= 120 ? age : null;
  }
  private average(items: ClienteResponse[]):number|null {
    const ages = items.map(item => this.age(item.dataNascimento)).filter((age):age is number=>age!==null);
    return ages.length ? Math.round(ages.reduce((sum,value)=>sum+value,0)/ages.length) : null;
  }
  private periodKey(dateStr:string): string {
    const date = new Date(dateStr.slice(0,10)+'T00:00:00');
    if (Number.isNaN(date.getTime())) return '';
    if (this.grouping === 'ano') return String(date.getFullYear());
    if (this.grouping === 'mes') return dateStr.slice(0,7);
    if (this.grouping === 'dia') return dateStr.slice(0,10);
    const weekStart = new Date(date);
    weekStart.setDate(weekStart.getDate() - ((weekStart.getDay()+6)%7));
    return this.dateToInput(weekStart);
  }
  private calculate(rows: ClienteResponse[]):void {
    // Both totals and cards use only clients with birth date, per reference note.
    const eligible = rows.filter(row=>this.age(row.dataNascimento)!==null);
    const maleRows = eligible.filter(row=>row.sexo==='M');
    const femaleRows = eligible.filter(row=>row.sexo==='F');
    const unknownRows = eligible.filter(row=>row.sexo!=='M'&&row.sexo!=='F');
    this.male=maleRows.length;this.female=femaleRows.length;this.unknown=unknownRows.length;
    this.avgMale=this.average(maleRows);this.avgFemale=this.average(femaleRows);
    this.avgUnknown=this.average(unknownRows);this.avgOverall=this.average(eligible);
    const grouped=new Map<string,{male:number;female:number;unknown:number;total:number}>();
    for (const item of eligible) {
      const key=item.dataCadastro?this.periodKey(item.dataCadastro):'';
      if(!key)continue;
      const counts=grouped.get(key)||{male:0,female:0,unknown:0,total:0};
      if(item.sexo==='M')counts.male++;
      else if(item.sexo==='F')counts.female++;
      else counts.unknown++;
      counts.total++;
      grouped.set(key,counts);
    }
    const ordered=[...grouped].sort(([a],[b])=>a.localeCompare(b));
    const max=Math.max(1,...ordered.map(([,value])=>value.total));
    this.bars=ordered.map(([label,counts])=>({label,count:counts.total,height:Math.round(counts.total/max*100)}));
    this.points=ordered.map(([label,counts],i)=>{
      const x=ordered.length===1?500:45+(i*910/(ordered.length-1));
      const y=(count:number)=>235-Math.round(count/max*200);
      return {label,...counts,x,maleY:y(counts.male),femaleY:y(counts.female),unknownY:y(counts.unknown),totalY:y(counts.total)};
    });
    const line=(kind:'maleY'|'femaleY'|'unknownY'|'totalY')=>this.points.map(p=>p.x+','+p[kind]).join(' ');
    this.series={male:line('maleY'),female:line('femaleY'),unknown:line('unknownY'),total:line('totalY')};
  }
}
