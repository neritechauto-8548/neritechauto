import { Component, inject, OnInit, ChangeDetectorRef, DestroyRef } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { environment } from '../../../../environments/environment';

import { MenuItem, MessageService } from 'primeng/api';
import { ViewChild } from '@angular/core';
// PrimeNG modules for compatibility
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';
import { SkeletonModule } from 'primeng/skeleton';
import { MenuModule } from 'primeng/menu';
import { DialogModule } from 'primeng/dialog';
import { ClientesService, ClienteResponseDTO, Page } from './cliente.service';
import { CadastroCliente } from '../cadastro-cliente/cadastro-cliente';
import { ClientesNav } from '../clientes-nav';
import { ContatoClienteResponse, TipoContato } from '../models/cliente.models';
import { forkJoin, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, map } from 'rxjs/operators';
import { NgxPermissionsService } from 'ngx-permissions';
import { VeiculoService } from '../../veiculo/veiculo/veiculo.service';
import { EmpresaService } from '../../configuracoes/empresa/services/empresa.service';
import { Empresa, EnderecoEmpresa } from '../../configuracoes/empresa/models/empresa.models';
import { LocalStorageService } from '@shared/services/storage.service';
import {
  TipoCliente,
  StatusCliente,
  getTipoClienteOptions,
  getStatusClienteOptions,
  StatusClienteLabels,
  TipoClienteLabels
} from '../models/cliente.models';

@Component({
  selector: 'cliente',
  standalone: true,
  templateUrl: './cliente.html',
  styleUrls: ['./cliente.scss'],
  imports: [
    CommonModule,
    FormsModule,
    // PrimeNG
    SelectModule,
    InputTextModule,
    ButtonModule,
    TagModule,
    TooltipModule,
    ToastModule,
    SkeletonModule,
    MenuModule,
    DialogModule,
    CadastroCliente,
    ClientesNav,
    RouterModule,
  ],
})
export class Cliente implements OnInit {
  readonly isUxPreview = environment.uxPreview;
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly clientesService = inject(ClientesService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly messageService = inject(MessageService);
  private readonly permissionsService = inject(NgxPermissionsService);
  private readonly veiculosService = inject(VeiculoService);
  private readonly empresaService = inject(EmpresaService);
  private readonly storageService = inject(LocalStorageService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly searchChanges = new Subject<string>();

  // Expose enums to template
  readonly TipoCliente = TipoCliente;
  readonly StatusCliente = StatusCliente;

  // Filtros
  searchTerm = '';
  isLoading = false;
  loadError = false;
  loadErrorMessage = 'Tente novamente em instantes.';
  private requestVersion = 0;

  // Menu popup
  activeRow: any = null;
  activeMenuItems: MenuItem[] = [];

  toggleMenu(row: any, event: Event, menu: any) {
    this.activeRow = row;
    this.activeMenuItems = this.menuItemsFor(row);
    menu.toggle(event);
  }
  // Advanced filters use tenant-scoped server queries.
  advancedOpen = false;
  advancedDocument = '';
  advancedName = '';
  advancedContact = '';
  advancedGender: 'M'|'F'|'O'|'' = '';
  advancedPlate = '';
  advancedFrom = '';
  advancedTo = '';
  advancedError = '';

  // Browser-only column preferences. Never alter server-side permissions.
  columnsOpen = false;
  readonly columnOptions = [
    { key: 'cliente', label: 'Cliente' }, { key: 'cpf', label: 'CPF/CNPJ' },
    { key: 'categoria', label: 'Categoria' }, { key: 'tipo', label: 'Tipo de pessoa' },
    { key: 'criacao', label: 'Criação' }, { key: 'contato', label: 'Contato' },
    { key: 'veiculos', label: 'Veículos' }, { key: 'situacao', label: 'Situação' },
  ];
  selectedColumns: string[] = ['cliente','cpf','categoria','tipo','criacao'];
  draftColumns: string[] = [];
  columnsSearch = '';
  selectedColumnsSearch = '';
  labelsOpen = false;
  labelsColumns = 3;
  labelsFontSize = 9;
  labelsMargin = 10;
  labelsMarginTop=10;
  labelsMarginBottom=10;
  labelsMarginLeft=10;
  labelsMarginRight=10;
  labelsZoom=100;
  labelsShowDocument = true;
  labelsWithBorder = true;
  labelsConfigOpen = false;
  labelsOpenSection: 'filters'|'page'|'margins'|'content' = 'filters';
  labelsType: TipoCliente | '' = '';
  labelsStatus: StatusCliente | '' = StatusCliente.ATIVO;
  labelsGender: 'M'|'F'|'N'|'' = '';
  labelsDateFrom = '';
  labelsDateTo = '';
  labelsSheet: 'A4'|'CARTA' = 'A4';
  labelsOrientation: 'portrait'|'landscape' = 'portrait';
  labelsGap = 4;
  labelsAlign: 'left'|'center'|'right' = 'left';
  labelsNameChars = 40;
  labelsAddressColumns = 2;
  labelsAutomaticHeight = true;
  labelsHeight = 30;
  labelsShowDate = true;
  labelsCompany: Empresa | null = null;
  labelsCompanyAddress = '';
  labelsCompanyLoading = false;
  labelsCompanyError = '';
  labelsShowLogo = true;
  labelsShowCompanyName = true;
  labelsShowCompanyCnpj = true;
  labelsShowCompanyAddress = false;
  labelsShowCompanyContacts = false;
  labelsShowIssuedBy = false;
  labelsLogoUrl = '';
  get hasLabelsCompany():boolean { return !!this.labelsCompany; }
  loadLabelCompany():void {
    if(this.isUxPreview){this.labelsCompanyError='Dados reais da oficina indisponíveis na demonstração.';return;}
    const tenant = Number(this.storageService.get('tenantId') || this.storageService.get('empresaId'));
    if(!Number.isSafeInteger(tenant)||tenant<=0){this.labelsCompanyError='Empresa da sessão não identificada.';return;}
    this.labelsCompanyLoading=true;this.labelsCompanyError='';
    this.empresaService.getEmpresa(tenant).subscribe({
      next: empresa => {
        this.labelsCompany=empresa;
        this.labelsCompanyLoading=false;
        this.labelsLogoUrl='';
        if(empresa.logoPath){
          this.empresaService.getLogo(tenant).subscribe({
            next:blob => {
              if(!blob.type.startsWith('image/'))return;
              const reader=new FileReader();
              reader.onload=()=>{if(typeof reader.result==='string' && reader.result.startsWith('data:image/'))this.labelsLogoUrl=reader.result;};
              reader.readAsDataURL(blob);
            },
            error:()=>{this.labelsLogoUrl='';}
          });
        }
        if(this.labelsShowCompanyAddress){
          this.loadLabelAddress(tenant);
        }
      },
      error: () => {this.labelsCompany=null;this.labelsCompanyLoading=false;this.labelsCompanyError='Dados da empresa indisponíveis para o relatório.';}
    });
  }
  loadLabelAddress(tenant?:number):void {
    const id=tenant || Number(this.storageService.get('tenantId'));
    if(!Number.isSafeInteger(id)||id<=0)return;
    this.empresaService.getEnderecos(id).subscribe({
      next: addresses => {
        const a: EnderecoEmpresa|undefined=addresses.find(a=>a.principal)||addresses[0];
        this.labelsCompanyAddress=a?[a.logradouro,a.numero,a.bairro,a.cidade,a.estado,a.cep].filter(Boolean).join(', '):'';
      },
      error:()=>{this.labelsCompanyAddress='';}
    });
  }
  get labelCompanyPreview():string[] {
    if(!this.labelsCompany)return ['Dados reais da empresa indisponíveis'];
    const company=this.labelsCompany;
    return [
      this.labelsShowCompanyName ? company.razaoSocial : '',
      this.labelsShowCompanyCnpj ? 'CNPJ: '+company.cnpj : '',
      this.labelsShowCompanyAddress ? this.labelsCompanyAddress : '',
      this.labelsShowCompanyContacts ? [company.telefone,company.email].filter(Boolean).join(' • ') : ''
    ].filter(Boolean);
  }
  labelsHeaderDivider = true;
  labelsLayoutType: 'dividido'|'centralizado' = 'dividido';
  labelsHeaderSize: 'P'|'M'|'G' = 'M';
  labelsHighlight = '#18328a';
  readonly labelPalette=['#343a40','#9099a2','#f34f57','#e53f73','#b83ee0','#7e56fb','#4265ed','#2888dc','#12a4bd','#10b98d','#30bf52','#7fca29','#f0ab0b','#ff7c1c','#18328a'];
  get labelRowsCount():number { return this.labelRows.length; }
  get labelRowsFiltered() {
    return this.pagedData.filter(row =>
      (!this.labelsType || row.tipo === this.labelsType) &&
      (!this.labelsStatus || row.status === this.labelsStatus) &&
      (!this.labelsGender || (this.labelsGender === 'N' ? !row.sexo : row.sexo === this.labelsGender)) &&
      (!this.labelsDateFrom || (row.dataCadastro || '').slice(0,10) >= this.labelsDateFrom) &&
      (!this.labelsDateTo || (row.dataCadastro || '').slice(0,10) <= this.labelsDateTo));
  }
  resetLabelsFilters():void {
    this.labelsType='';this.labelsStatus=StatusCliente.ATIVO;this.labelsGender='';this.labelsDateFrom='';this.labelsDateTo='';
  }
  restoreLabels():void {
    this.labelsShowLogo=true;this.labelsShowCompanyName=true;this.labelsShowCompanyCnpj=true;
    this.labelsShowCompanyAddress=false;this.labelsShowCompanyContacts=false;
    this.labelsShowIssuedBy=false;this.labelsShowDate=true;this.labelsHeaderDivider=true;
    this.labelsColumns=3;this.labelsMargin=10;this.labelsMarginTop=10;this.labelsMarginBottom=10;this.labelsMarginLeft=10;this.labelsMarginRight=10;this.labelsZoom=100;this.labelsFontSize=9;this.labelsWithBorder=true;this.labelsShowDocument=true;
    this.labelsSheet='A4';this.labelsOrientation='portrait';this.labelsGap=4;this.labelsAlign='left';
    this.labelsNameChars=40;this.labelsAddressColumns=2;this.labelsAutomaticHeight=true;this.labelsHeight=30;
  }
  saveLabelSettings():void {
    const data = {columns:this.labelsColumns,margin:this.labelsMargin,top:this.labelsMarginTop,bottom:this.labelsMarginBottom,left:this.labelsMarginLeft,right:this.labelsMarginRight,font:this.labelsFontSize,border:this.labelsWithBorder,
      document:this.labelsShowDocument,sheet:this.labelsSheet,orientation:this.labelsOrientation,gap:this.labelsGap,
      align:this.labelsAlign,nameChars:this.labelsNameChars,addressColumns:this.labelsAddressColumns,
      automaticHeight:this.labelsAutomaticHeight,height:this.labelsHeight,showDate:this.labelsShowDate,
      headerDivider:this.labelsHeaderDivider,layoutType:this.labelsLayoutType,headerSize:this.labelsHeaderSize,
      highlight:this.labelsHighlight,showLogo:this.labelsShowLogo,showName:this.labelsShowCompanyName,
      showCnpj:this.labelsShowCompanyCnpj,showAddress:this.labelsShowCompanyAddress,
      showContacts:this.labelsShowCompanyContacts,showBy:this.labelsShowIssuedBy};
    try { localStorage.setItem('neritech-client-label-layout-v1',JSON.stringify(data)); }catch{}
    this.labelsConfigOpen=false;
  }
  loadLabelSettings():void {
    try {
      const text=localStorage.getItem('neritech-client-label-layout-v1');
      if(!text)return;
      const raw=JSON.parse(text);
      if(Number.isInteger(raw.columns)&&raw.columns>=1&&raw.columns<=4)this.labelsColumns=raw.columns;
      if(Number.isFinite(raw.margin)&&raw.margin>=0&&raw.margin<=30)this.labelsMargin=raw.margin;
      for(const [name,target] of [['top','labelsMarginTop'],['bottom','labelsMarginBottom'],['left','labelsMarginLeft'],['right','labelsMarginRight']] as const){
        if(Number.isFinite(raw[name])&&raw[name]>=0&&raw[name]<=30)this[target]=raw[name];
      }
      if(Number.isFinite(raw.font)&&raw.font>=7&&raw.font<=18)this.labelsFontSize=raw.font;
      if(typeof raw.border==='boolean')this.labelsWithBorder=raw.border;
      if(typeof raw.document==='boolean')this.labelsShowDocument=raw.document;
      if(['A4','CARTA'].includes(raw.sheet))this.labelsSheet=raw.sheet;
      if(['portrait','landscape'].includes(raw.orientation))this.labelsOrientation=raw.orientation;
      if(Number.isFinite(raw.gap)&&raw.gap>=0&&raw.gap<=30)this.labelsGap=raw.gap;
      if(['left','center','right'].includes(raw.align))this.labelsAlign=raw.align;
      if(Number.isInteger(raw.nameChars)&&raw.nameChars>=10&&raw.nameChars<=255)this.labelsNameChars=raw.nameChars;
      if(Number.isInteger(raw.addressColumns)&&raw.addressColumns>=1&&raw.addressColumns<=3)this.labelsAddressColumns=raw.addressColumns;
      if(typeof raw.automaticHeight==='boolean')this.labelsAutomaticHeight=raw.automaticHeight;
      if(Number.isFinite(raw.height)&&raw.height>=10&&raw.height<=200)this.labelsHeight=raw.height;
      if(typeof raw.showDate==='boolean')this.labelsShowDate=raw.showDate;
      if(typeof raw.showLogo==='boolean')this.labelsShowLogo=raw.showLogo;
      if(typeof raw.showName==='boolean')this.labelsShowCompanyName=raw.showName;
      if(typeof raw.showCnpj==='boolean')this.labelsShowCompanyCnpj=raw.showCnpj;
      if(typeof raw.showAddress==='boolean')this.labelsShowCompanyAddress=raw.showAddress;
      if(typeof raw.showContacts==='boolean')this.labelsShowCompanyContacts=raw.showContacts;
      if(typeof raw.showBy==='boolean')this.labelsShowIssuedBy=raw.showBy;
      if(typeof raw.headerDivider==='boolean')this.labelsHeaderDivider=raw.headerDivider;
      if(['dividido','centralizado'].includes(raw.layoutType))this.labelsLayoutType=raw.layoutType;
      if(['P','M','G'].includes(raw.headerSize))this.labelsHeaderSize=raw.headerSize;
      if(typeof raw.highlight==='string'&&/^#[0-9a-f]{6}$/i.test(raw.highlight))this.labelsHighlight=raw.highlight;
    }catch{}
  }


  openColumns(): void { this.draftColumns = [...this.selectedColumns]; this.columnsSearch = ''; this.selectedColumnsSearch = ''; this.columnsOpen = true; }
  toggleColumn(key: string): void {
    if (key === 'cliente') return;
    this.draftColumns = this.draftColumns.includes(key)
      ? this.draftColumns.filter(value => value !== key) : [...this.draftColumns, key];
  }
  shiftColumn(key: string, delta: number): void {
    const index = this.draftColumns.indexOf(key);
    const next = index + delta;
    if (key === 'cliente' || next < 1 || next >= this.draftColumns.length) return;
    [this.draftColumns[index], this.draftColumns[next]] = [this.draftColumns[next], this.draftColumns[index]];
  }
  restoreColumns(): void { this.draftColumns = ['cliente','cpf','categoria','tipo','criacao']; }
  saveColumns(): void {
    this.selectedColumns = [...this.draftColumns];
    try { window.localStorage.setItem('neritech-clients-column-order', JSON.stringify(this.selectedColumns)); } catch {}
    this.columnsOpen = false;
  }
  get filteredColumnOptions() {
    const term = this.columnsSearch.toLocaleLowerCase('pt-BR').trim();
    return this.columnOptions.filter(col => col.label.toLocaleLowerCase('pt-BR').includes(term));
  }
  get visibleDraftColumns(): string[] {
    const q = this.selectedColumnsSearch.trim().toLocaleLowerCase('pt-BR');
    return q ? this.draftColumns.filter(k => this.columnLabel(k).toLocaleLowerCase('pt-BR').includes(q)) : this.draftColumns;
  }
  openCategory(row: { uuid: number | string }): void {
    this.navigateToEdit(row);
  }
  columnLabel(key: string): string { return this.columnOptions.find(c => c.key === key)?.label || key; }
  get labelRows() { return this.labelRowsFiltered; }
  openLabels(): void { this.loadLabelSettings(); this.labelsOpen = true; this.loadLabelCompany(); }
  printLabels(): void {
    if (!this.labelRows.length) return;
    // Isolated print document; escape all customer-supplied text to avoid HTML injection.
    const escape = (value: string) => String(value).replace(/[&<>"']/g, char =>
      ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'} as Record<string,string>)[char]);
    const columns = Math.min(4, Math.max(1, Number(this.labelsColumns) || 3));
    const font = Math.min(18, Math.max(7, Number(this.labelsFontSize) || 9));
    const top = Math.min(30, Math.max(0, Number(this.labelsMarginTop) || 0));
    const right = Math.min(30, Math.max(0, Number(this.labelsMarginRight) || 0));
    const bottom = Math.min(30, Math.max(0, Number(this.labelsMarginBottom) || 0));
    const left = Math.min(30, Math.max(0, Number(this.labelsMarginLeft) || 0));
    const gap = Math.min(30, Math.max(0, Number(this.labelsGap) || 0));
    const headerLines=this.labelCompanyPreview.filter(line=>this.hasLabelsCompany&&line);
    const logo = this.labelsShowLogo && this.labelsCompany ?
      (this.labelsLogoUrl.startsWith('data:image/') ?
        '<img src="'+this.labelsLogoUrl+'" alt="Logo da empresa" style="max-height:20mm;max-width:40mm;object-fit:contain;margin-right:4mm">' :
        '<span style="display:inline-grid;place-items:center;background:'+this.labelsHighlight+';color:white;font-size:14pt;padding:4mm;border-radius:2mm;margin-right:4mm">'+escape(this.labelsCompany.razaoSocial.slice(0,1).toUpperCase())+'</span>') : '';
    const companyHeader=headerLines.length?'<div style="display:flex;flex-direction:'+(this.labelsLayoutType==='centralizado'?'column':'row')+';align-items:'+(this.labelsLayoutType==='centralizado'?'center':'start')+';gap:2mm;font-size:'+({P:8,M:9,G:11}[this.labelsHeaderSize])+'pt;text-align:'+this.labelsAlign+';margin:0 0 3mm">'+logo+
      '<div>'+headerLines.map(line=>'<div>'+escape(line)+'</div>').join('')+'</div></div>':'';
    const cells = this.labelRows.map(item =>
      '<article><strong>'+escape(item.nome.slice(0,this.labelsNameChars))+'</strong>' +
      (this.labelsShowDocument && item.cpfCnpj ? '<span>'+escape(this.formatCpfCnpj(item.cpfCnpj))+'</span>' : '') +
      '</article>').join('');
    const markup = '<!doctype html><html lang="pt-BR"><head><meta charset="utf-8"><title>Etiquetas NERITECH AUTO</title>' +
      '<style>@page{size:'+this.labelsSheet+' '+this.labelsOrientation+';margin:'+top+'mm '+right+'mm '+bottom+'mm '+left+'mm}body{margin:0;font-family:Arial,sans-serif}' +
      'main{display:grid;grid-template-columns:repeat('+columns+',minmax(0,1fr));gap:'+gap+'mm}' +
      'article{box-sizing:border-box;min-height:'+(this.labelsAutomaticHeight ? 'auto' : Math.min(200,Math.max(10,Number(this.labelsHeight)))+'mm')+';padding:3mm;text-align:'+this.labelsAlign+';border:'+
        (this.labelsWithBorder ? '1px solid #b9c0c9' : '1px solid transparent')+
        ';font-size:'+font+'pt;break-inside:avoid;overflow-wrap:anywhere}' +
      'strong,span{display:block}span{margin-top:2mm;font-size:.9em}</style></head><body>' +
      (this.labelsShowDate ? '<header style="font-size:9pt;text-align:'+this.labelsAlign+';color:'+this.labelsHighlight+
        ';border-bottom:'+(this.labelsHeaderDivider?'1px solid '+this.labelsHighlight:'none')+
        ';padding:0 0 2mm;margin:0 0 3mm">Emitido em '+escape(new Date().toLocaleString('pt-BR'))+'</header>' : '') +
      companyHeader + '<main>'+cells+'</main></body></html>';
    const iframe = document.createElement('iframe');
    iframe.setAttribute('title', 'Impressão de etiquetas');
    iframe.style.position = 'fixed'; iframe.style.width = '0'; iframe.style.height = '0';
    iframe.style.border = '0'; iframe.style.opacity = '0';
    iframe.onload = () => {
      const preview = iframe.contentWindow;
      if (!preview) return;
      // Print only when the isolated document has loaded, then release resources.
      preview.addEventListener('afterprint', () => iframe.remove(), { once: true });
      preview.focus();
      preview.print();
    };
    iframe.srcdoc = markup;
    document.body.appendChild(iframe);
  }
  formatLabelMm(value:number):string {
    return Number(value || 0).toLocaleString('pt-BR',{minimumFractionDigits:2,maximumFractionDigits:2});
  }
  changeLabelMm(field:'all'|'top'|'bottom'|'left'|'right',value:string):void {
    const parsed=Number(value.replace(/\s/g,'').replace(/\./g,'').replace(',','.'));
    if(!Number.isFinite(parsed)||parsed<0||parsed>30)return;
    if(field==='all'){this.labelsMargin=parsed;this.setUniformLabelMargin(parsed);}
    else {
      const map={top:'labelsMarginTop',bottom:'labelsMarginBottom',left:'labelsMarginLeft',right:'labelsMarginRight'} as const;
      this[map[field]]=parsed;
    }
  }
  labelZoomIn():void { this.labelsZoom=Math.min(150,this.labelsZoom+10); }
  labelZoomOut():void { this.labelsZoom=Math.max(70,this.labelsZoom-10); }
  setUniformLabelMargin(value: number):void {
    const n=Math.min(30,Math.max(0,Number(value)||0));
    this.labelsMarginTop=n;this.labelsMarginBottom=n;this.labelsMarginLeft=n;this.labelsMarginRight=n;
  }
  downloadCsvTemplate(): void {
    const header = 'tipoCliente;nomeCompleto;razaoSocial;nomeFantasia;cpf;cnpj;email;status\r\n';
    const blob = new Blob(['\uFEFF' + header], { type: 'text/csv;charset=utf-8' });
    const objectUrl = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = objectUrl; link.download = 'neritech-modelo-clientes.csv'; link.click();
    setTimeout(() => URL.revokeObjectURL(objectUrl), 0);
  }
  applyAdvancedFilters(): void {
    if (this.advancedFrom && this.advancedTo && this.advancedFrom > this.advancedTo) {
      this.advancedError = 'A data inicial deve ser anterior ou igual à data final.';
      return;
    }
    this.advancedError = '';
    this.advancedOpen = false;
    this.first = 0;
    this.fetchPage();
  }
  resetAdvancedFilters(): void {
    this.advancedDocument = ''; this.advancedName = '';
    this.advancedFrom = ''; this.advancedTo = ''; this.advancedError = '';
    this.advancedContact='';this.advancedGender='';this.advancedPlate='';
    this.selectedTipo = null; this.selectedStatus = null;
    this.first = 0; this.fetchPage();
  }

  selectedTipo: TipoCliente | null = null;
  selectedStatus: StatusCliente | null = null;
  sort = 'nomeCompleto,asc';
  readonly pageSizes = [10, 25, 50];
  readonly sortOptions = [
    { label: 'Nome: A–Z', value: 'nomeCompleto,asc' },
    { label: 'Nome: Z–A', value: 'nomeCompleto,desc' },
    { label: 'Cadastro mais recente', value: 'dataCadastro,desc' },
    { label: 'Status', value: 'status,asc' },
  ];

  // Opções para dropdowns
  tipoOptions = [{ label: 'Todos', value: null }, ...getTipoClienteOptions()];
  get tipoOptionsFiltered() {
    return getTipoClienteOptions();
  }

  statusOptions = [{ label: 'Todos', value: null }, ...getStatusClienteOptions()];

  // Labels
  StatusClienteLabels = StatusClienteLabels;
  TipoClienteLabels = TipoClienteLabels;

  // Dados carregados do backend
  clients: {
    uuid: string | number;
    id?: number;
    nome: string;
    cpfCnpj: string;
    tipo: TipoCliente;
    status: StatusCliente;
    contato: string;
    dataCadastro?: string;
    quantidadeVeiculos: number | null;
    email: string;
    categoria: string;
    sexo?: string;
  }[] = [];
  backendPage: Page<ClienteResponseDTO> | null = null;

  // Paginação
  rows = 10;
  first = 0;

  get totalRecords() {
    return this.backendPage?.totalElements ?? this.filtered.length;
  }
  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }
  jumpPageInput = '';
  isQuickFormOpen = false;

  // Filtrados (aplicado no frontend para dados já carregados)
  get filtered() {
    return this.clients;
  }
  // Removed incorrect pagedData getter for server-side pagination

  // Client-side pagination implementation
  get pagedData() {
    return this.filtered; // Agora a filtered sempre tem apenas a página atual
  }

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }
  get rangeEnd() {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  onSearchTermChange(value: string): void {
    this.searchChanges.next(value);
  }

  onSearch(): void {
    this.first = 0;
    this.fetchPage();
  }

  atualizar(): void {
    this.fetchPage();
  }

  get canCreateClient(): boolean {
    return this.isUxPreview || !!this.permissionsService.getPermission('CLIENTE_CRIAR');
  }

  get canOpenClient(): boolean {
    return this.isUxPreview || !!this.permissionsService.getPermission('CLIENTE_EDITAR') || !!this.permissionsService.getPermission('GERAL_USUARIO');
  }

  get canOpenVehicles(): boolean {
    return this.isUxPreview || !!this.permissionsService.getPermission('GERAL_USUARIO');
  }

  abrirVeiculos(row: { id?: number; uuid: number | string }): void {
    if (!this.canOpenVehicles) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito', detail: 'Você não tem permissão para consultar veículos.' });
      return;
    }
    const clienteId = row.id ?? row.uuid;
    this.router.navigate(['/veiculo'], { queryParams: { clienteId } });
  }

  onFilterChange(): void {
    this.first = 0;
    this.fetchPage();
  }

  onPageSizeChange(): void {
    this.first = 0;
    this.fetchPage();
  }

  onSortChange(): void {
    this.first = 0;
    this.fetchPage();
  }

  limparFiltros(): void {
    this.searchTerm = '';
    this.selectedTipo = null;
    this.selectedStatus = null;
    this.advancedDocument = '';
    this.advancedName = '';
    this.advancedContact='';this.advancedGender='';this.advancedPlate='';
    this.advancedFrom = '';
    this.advancedTo = '';
    this.first = 0;
    this.fetchPage();
  }

  get hasFilters(): boolean {
    return !!this.searchTerm.trim() || !!this.selectedTipo || !!this.selectedStatus ||
      !!this.advancedDocument.trim() || !!this.advancedName.trim() || !!this.advancedContact.trim() ||
      !!this.advancedGender || !!this.advancedPlate.trim() || !!this.advancedFrom || !!this.advancedTo;
  }

  goPrev() {
    this.first = Math.max(0, this.first - this.rows);
    this.fetchPage();
  }

  goNext() {
    if (this.first + this.rows < this.totalRecords) {
      this.first = this.first + this.rows;
      this.fetchPage();
    }
  }

  jumpToPage() {
    const page = Number(this.jumpPageInput);
    if (!isNaN(page) && page >= 1) {
      const maxPage = Math.max(1, Math.ceil(this.totalRecords / this.rows));
      const clamped = Math.min(page, maxPage);
      this.first = (clamped - 1) * this.rows;
      this.fetchPage();
    }
  }

  pendingRoute: any[] | string | null = null;
  onMenuNavigate(item: MenuItem) {
    if (item && item.routerLink) {
      this.pendingRoute = item.routerLink as any;
    }
  }
  onMenuClosed() {
    if (this.pendingRoute) {
      const route = this.pendingRoute;
      this.pendingRoute = null;
      if (Array.isArray(route)) {
        this.router.navigate(route);
      } else if (typeof route === 'string') {
        this.router.navigateByUrl(route);
      }
    }
  }

  menuItemsFor(row: any): MenuItem[] {
    const items: MenuItem[] = [];
    const clienteId = row.id ?? row.uuid;
    if (this.canOpenClient) {
      items.push({ label: this.permissionsService.getPermission('CLIENTE_EDITAR') || this.isUxPreview ? 'Consultar / editar cliente' : 'Consultar cliente',
        routerLink: ['/cliente/editar', row.uuid], queryParams: this.listQueryParams() });
    }
    if (this.isUxPreview || this.permissionsService.getPermission('GERAL_USUARIO')) {
      items.push({ label: 'Visualizar / Editar Veículos', routerLink: ['/veiculo'], queryParams: { clienteId } });
    }
    if (this.isUxPreview || this.permissionsService.getPermission('VEICULO_CRIAR')) {
      items.push({ label: 'Adicionar veículo', routerLink: ['/veiculo/cadastro'], queryParams: { clienteId } });
    }
    if (this.isUxPreview || this.permissionsService.getPermission('AGENDAMENTO_CRIAR')) {
      items.push({ label: 'Cadastrar Agendamento / Alerta', routerLink: ['/agendamento/cadastro'], queryParams: { clienteId } });
    }
    if (this.isUxPreview || this.permissionsService.getPermission('OS_INCLUIR')) {
      items.push({ label: 'Cadastrar OS', routerLink: ['/os/cadastro'], queryParams: { clienteId } });
    }
    return items;
  }

  cadastrarCliente() {
    if (!this.isUxPreview && !this.permissionsService.getPermission('CLIENTE_CRIAR')) {
      this.messageService.add({ 
        severity: 'warn', 
        summary: 'Atenção', 
        detail: 'Seu perfil não possui permissão para realizar esta operação.' 
      });
      return;
    }
    this.router.navigate(['/cliente/cadastro'], { queryParams: this.listQueryParams() });
  }

  abrirCadastroRapido() {
    if (!this.isUxPreview && !this.permissionsService.getPermission('CLIENTE_CRIAR')) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Seu perfil não possui permissão para cadastrar clientes.' });
      return;
    }
    this.isQuickFormOpen = true;
  }

  onQuickClientSaved() {
    this.isQuickFormOpen = false;
    this.messageService.add({ severity: 'success', summary: 'Cliente salvo', detail: 'O cadastro e os dados associados foram salvos.' });
    this.fetchPage();
  }

  onQuickClientCanceled() {
    this.isQuickFormOpen = false;
  }

  navigateToEdit(row: { uuid?: string | number }) {
    if (!this.canOpenClient) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito', detail: 'Seu perfil não possui permissão para consultar este cadastro.' });
      return;
    }
    const id = row?.uuid;
    if (id) {
      this.router.navigate(['/cliente/editar', id], { queryParams: this.listQueryParams() });
    }
  }

  navigateToAddVeiculo(row: { id?: string | number; uuid?: string | number; nome?: string }) {
    if (!this.isUxPreview && !this.permissionsService.getPermission('VEICULO_CRIAR')) {
      this.messageService.add({ 
        severity: 'warn', 
        summary: 'Atenção', 
        detail: 'Seu perfil não possui permissão para realizar esta operação.' 
      });
      return;
    }
    const clienteId = row?.id || row?.uuid;
    if (clienteId) {
      this.router.navigate(['/veiculo/cadastro'], { queryParams: { clienteId } });
    }
  }




  ngOnInit() {
    this.searchChanges.pipe(
      map(value => value.trim()),
      debounceTime(350),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef),
    ).subscribe(() => this.onSearch());

    const qp = this.route.snapshot.queryParamMap;
    const statusParam = qp.get('status') as StatusCliente | null;
    const tipoParam = qp.get('tipo') as TipoCliente | null;
    this.searchTerm = qp.get('busca') || '';
    this.advancedDocument = qp.get('doc') || '';
    this.advancedName = qp.get('nome') || '';
    this.advancedFrom = qp.get('de') || '';
    this.advancedTo = qp.get('ate') || '';
    const page = Number(qp.get('page'));
    const size = Number(qp.get('size'));
    this.first = Number.isInteger(page) && page >= 0 ? page * (this.pageSizes.includes(size) ? size : this.rows) : 0;
    if (this.pageSizes.includes(size)) this.rows = size;
    const sortParam = qp.get('sort');
    if (this.sortOptions.some(option => option.value === sortParam)) this.sort = sortParam!;
    if (statusParam && Object.values(StatusCliente).includes(statusParam)) {
      this.selectedStatus = statusParam;
    }
    if (tipoParam && Object.values(TipoCliente).includes(tipoParam)) {
      this.selectedTipo = tipoParam;
    }
    try {
      const stored = JSON.parse(window.localStorage.getItem('neritech-clients-column-order') || 'null');
      const valid = new Set(this.columnOptions.map(c => c.key));
      if (Array.isArray(stored) && stored[0] === 'cliente' && stored.every(key => typeof key === 'string' && valid.has(key)) && new Set(stored).size === stored.length) this.selectedColumns = stored;
    } catch { /* Local preferences are optional. */ }
    this.fetchPage();
  }

  private mapToRow(dto: ClienteResponseDTO) {
    const nome = dto.nomeCompleto || dto.nomeFantasia || dto.razaoSocial || '';
    const cpfCnpj = dto.cpf || dto.cnpj || '';
    const tipo = (dto.tipoCliente as TipoCliente) || TipoCliente.PESSOA_FISICA;
    const status = (dto.status as StatusCliente) || StatusCliente.ATIVO;

    return {
      uuid: (dto as any).id ?? (dto as any).uuid,
      id: (dto as any).id,
      nome,
      cpfCnpj,
      tipo,
      status,
      email: dto.email || '',
      categoria: dto.categoria || '',
      sexo: dto.sexo || '',
      contato: '', // Placeholder
      dataCadastro: dto.dataCadastro,
      quantidadeVeiculos: null,
    };
  }

  private fetchPage() {
    const requestId = ++this.requestVersion;
    this.loadError = false;
    // Spring Data Pageable usa paginação 0-indexed: a primeira página é page=0.
    const pageIndex = Math.floor(this.first / this.rows);
    this.isLoading = true;


    const filters: any = { page: pageIndex, size: this.rows, sort: this.sort };
    this.router.navigate([], { relativeTo: this.route, queryParams: this.listQueryParams(), replaceUrl: true });

    // Filtro de busca por nome ou documento
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.trim();
      // A busca é tratada no backend como OR entre nome/razão/nome fantasia/CPF/CNPJ.
      // Isso evita exigir que o mesmo termo exista simultaneamente em nome e documento.
      filters.busca = term;
    }

    // Filtro por tipo de cliente
    if (this.selectedTipo) {
      filters.tipoCliente = this.selectedTipo;
    }

    // Filtro por status
    if (this.selectedStatus) {
      filters.status = this.selectedStatus;
    }
    if (this.advancedDocument.trim()) filters.cpf = this.advancedDocument.trim();
    if (this.advancedName.trim()) filters.nomeFantasia = this.advancedName.trim();
    if (this.advancedContact.trim()) filters.contato = this.advancedContact.trim();
    if (this.advancedGender) filters.sexo = this.advancedGender;
    if (this.advancedPlate.trim()) filters.placa = this.advancedPlate.trim().toUpperCase();
    if (this.advancedFrom) filters.dataInicio = this.advancedFrom;
    if (this.advancedTo) filters.dataFim = this.advancedTo;

    this.clientesService.list(filters).subscribe({
      next: (res: Page<ClienteResponseDTO>) => {
        if (requestId !== this.requestVersion) return;
        this.backendPage = res;
        this.clients = (res.content || []).map((d: ClienteResponseDTO) => this.mapToRow(d));

        this.isLoading = false;
        this.cdr.detectChanges(); // Forçar atualização da view

        // Enriquecer a página com os contatos e veículos disponíveis nos serviços existentes.
        this.loadContatosForRows(requestId);
      },
      error: (err: any) => {
        if (requestId !== this.requestVersion) return;
        this.isLoading = false;
        this.loadError = true;
        this.loadErrorMessage = err.status === 403 ? 'Seu perfil não tem permissão para consultar esta lista.' : 'Verifique sua conexão e tente novamente.';
        this.backendPage = null;
        this.clients = [];
        // A UI apresenta a falha sem registrar dados de clientes no console.
        
        if (err.status !== 403) {
          this.messageService.add({ 
            severity: 'error', 
            summary: 'Erro', 
            detail: 'Ocorreu um erro ao carregar os dados dos clientes.' 
          });
        }
      },
    });
  }

  private listQueryParams() {
    return {
      busca: this.searchTerm.trim() || null,
      tipo: this.selectedTipo,
      status: this.selectedStatus,
      page: this.first ? Math.floor(this.first / this.rows) : null,
      size: this.rows === 10 ? null : this.rows,
      sort: this.sort === 'nomeCompleto,asc' ? null : this.sort,
      doc: this.advancedDocument || null,
      nome: this.advancedName || null,
      contato: this.advancedContact || null,
      genero: this.advancedGender || null,
      placa: this.advancedPlate || null,
      de: this.advancedFrom || null,
      ate: this.advancedTo || null,
    };
  }

  formatDataCadastro(value?: string): string {
    if (!value) return '—';
    const [datePart] = value.split('T');
    const [year, month, day] = datePart.split('-').map(Number);
    const date = new Date(year, month - 1, day);
    return Number.isNaN(date.getTime()) ? '—' : new Intl.DateTimeFormat('pt-BR').format(date);
  }

  private loadContatosForRows(requestId: number) {
    const currentPageRows = this.clients;
    if (!currentPageRows || !currentPageRows.length) return;

    const requests = currentPageRows.map(row => {
      const id = row.id ?? row.uuid;
      return forkJoin({
        contatos: this.clientesService.listarContatos(id).pipe(
          map((res: Page<ContatoClienteResponse>) => res.content || []),
          catchError(() => of([] as ContatoClienteResponse[]))
        ),
        veiculos: this.veiculosService.list(Number(id)).pipe(catchError(() => of(null)))
      }).pipe(map(result => ({ id, ...result })));
    });

    forkJoin(requests).subscribe(results => {
      if (requestId !== this.requestVersion) return;
      const byId = new Map(results.map(r => [r.id, r]));
      this.clients = this.clients.map(c => {
        const key = (c as any).id ?? c.uuid;
        const result = byId.get(key);
        return {
          ...c,
          contato: this.formatContatoLabel((c as any).email, result?.contatos || []),
          quantidadeVeiculos: result?.veiculos?.length ?? null,
        };
      });
    });
  }

  private formatContatoLabel(emailFallback: string, contatos: ContatoClienteResponse[]): string {
    if (!contatos || !contatos.length) return emailFallback;
    // Preferência: CELULAR -> WHATSAPP -> TELEFONE_FIXO -> OUTROS
    const ordered = [
          ...contatos.filter(c => c.tipoContato === TipoContato.CELULAR),
          ...contatos.filter(c => c.tipoContato === TipoContato.WHATSAPP),
          ...contatos.filter(c => c.tipoContato === TipoContato.TELEFONE_FIXO),
          ...contatos.filter(c => c.tipoContato === TipoContato.OUTROS),
        ];

    const chosen = ordered[0] || contatos[0];
    const value = (chosen as any)?.valor ?? (chosen as any)?.contato ?? '';
    
    if (this.isPhoneType(chosen.tipoContato)) {
      return this.applyPhoneMask(value, chosen.tipoContato) || emailFallback;
    }

    return value || emailFallback;
  }

  private isPhoneType(tipo: any): boolean {
    return [TipoContato.TELEFONE_FIXO, TipoContato.CELULAR, TipoContato.WHATSAPP, TipoContato.TELEGRAM].includes(tipo);
  }

  private applyPhoneMask(digits: string, tipo: any): string {
    const d = this.stripNonDigits(digits);
    if (tipo === TipoContato.TELEFONE_FIXO && d.length >= 10) {
       return `(${d.slice(0, 2)}) ${d.slice(2, 6)}-${d.slice(6, 10)}`;
    }
    if (d.length >= 11) {
       return `(${d.slice(0, 2)}) ${d.slice(2, 7)}-${d.slice(7, 11)}`;
    }
    return d; // Retorna apenas os dígitos se não bater com tamanho padrão
  }

  private stripNonDigits(v: string) {
    return (v || '').replace(/\D+/g, '');
  }

  // Helper para obter classe de severidade do badge de status
  getStatusSeverity(status: StatusCliente): 'success' | 'secondary' | 'danger' {
    switch (status) {
      case StatusCliente.ATIVO:
        return 'success';
      case StatusCliente.INATIVO:
        return 'secondary';
      case StatusCliente.BLOQUEADO:
        return 'danger';
      default:
        return 'secondary';
    }
  }

  // Helper para obter ícone do tipo de cliente
  getTipoIcon(tipo: TipoCliente): string {
    return tipo === TipoCliente.PESSOA_FISICA ? 'person' : 'business';
  }

  // Helper para obter label do tipo de cliente
  getTipoClienteLabel(tipo: TipoCliente): string {
    return TipoClienteLabels[tipo];
  }

  // Helper para obter label do status
  getStatusLabel(status: StatusCliente): string {
    return StatusClienteLabels[status];
  }

  // Helper para formatar CPF/CNPJ
  formatCpfCnpj(value: string): string {
    if (!value) return '';
    const clean = value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    if (clean.length === 11) {
      return clean.replace(/(.{3})(.{3})(.{3})(.{2})/, '$1.$2.$3-$4');
    } else if (clean.length === 14) {
      return clean.replace(/(.{2})(.{3})(.{3})(.{4})(.{2})/, '$1.$2.$3/$4-$5');
    }
    return value;
  }
}
