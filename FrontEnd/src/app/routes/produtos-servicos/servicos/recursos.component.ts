import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import {
  RecursosApi, AreaRecurso, SubareaRecurso, ServicoRecurso,
  AcessorioRecurso, OpcaoRecurso, AplicacaoRecurso
} from './recursos-api.service';

type AbaRecurso = 'areas' | 'servicos' | 'acessorios';
type DrawerRecurso = null | 'area' | 'acessorio' | 'filtro-servicos' | 'filtro-acessorios';
type ModalRecurso = null | 'colunas' | 'precos' | 'importacao';
type ChaveColuna = 'descricao' | 'codigoTempario' | 'area' | 'preco' | 'checklist' | 'padrao' | 'ativo';

@Component({
  selector: 'app-recursos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './recursos.component.html',
  styleUrls: ['./recursos.component.scss']
})
export class RecursosComponent implements OnInit {
  private readonly api = inject(RecursosApi);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly route = inject(ActivatedRoute);
  aba: AbaRecurso = 'servicos';
  drawer: DrawerRecurso = null;
  modal: ModalRecurso = null;
  carregando = false;
  salvando = false;
  erro = '';
  mensagem = '';
  pesquisa = '';
  filtroSituacao: '' | 'true' | 'false' = '';
  filtroCodigo = '';
  filtroDescricao = '';
  filtroAreaId: number | null = null;
  filtroChecklist = false;
  areas: AreaRecurso[] = [];
  servicos: ServicoRecurso[] = [];
  acessorios: AcessorioRecurso[] = [];
  areaDraft: AreaRecurso = this.novaArea();
  acessorioDraft: AcessorioRecurso = this.novoAcessorio();
  servicoDraft: Partial<ServicoRecurso> = this.novoServico();
  servicoDetalhe = false;
  servicoAba: 'geral' | 'aplicacoes' = 'geral';
  aplicacoes: AplicacaoRecurso[] = [];
  marcas: OpcaoRecurso[] = [];
  modelos: OpcaoRecurso[] = [];
  marcaId: number | null = null;
  modeloId: number | null = null;
  tempoAplicacao: number | null = null;
  precoVisualizado: number | null = null;
  valorLote: number | null = null;
  operacaoLote: 'SOMAR_VALOR' | 'SUBSTITUIR_VALOR' | 'SOMAR_PERCENTUAL' = 'SOMAR_VALOR';
  empresaAtualSelecionada = true;
  csvEtapa: 1 | 2 | 3 = 1;
  csvLinhas: Array<{ codigo: string; descricao: string; areaId: number; unidade: string; tempo: number; preco: number }> = [];
  csvNome = '';
  csvErro = '';
  colunas: ChaveColuna[] = ['descricao','codigoTempario','area','preco'];
  readonly sugeridos = ['Estepe','Macaco','Chave de roda','Triângulo','Extintor','Tapetes','Calotas','Documentos','Ar-condicionado','Rádio'];
  readonly unidades = [{ valor:'Hr',nome:'Horas' },{ valor:'Mi',nome:'Minutos' },{ valor:'D',nome:'Dias' },{ valor:'Sm',nome:'Semanas' },{ valor:'Me',nome:'Meses' }];
  readonly rotulos: Record<ChaveColuna, string> = {
    descricao:'Descrição', codigoTempario:'Cód. Tempário', area:'Área', preco:'Preços',
    checklist:'Checklist', padrao:'Padrão', ativo:'Ativo'
  };
  readonly colunasServicos: ChaveColuna[] = ['descricao','codigoTempario','area','preco'];
  readonly colunasAcessorios: ChaveColuna[] = ['descricao','checklist','padrao','ativo'];

  ngOnInit() {
    // Cadastros abre Áreas; a URL legada de Serviços mantém sua aba inicial.
    this.aba = this.route.snapshot.routeConfig?.path === 'recursos' ? 'areas' : 'servicos';
    this.recuperarColunas();
    this.carregarAreas();
    this.carregar();
  }
  novaArea(): AreaRecurso { return { id: 0, descricao: '', ativo: true, versao: 0, subareas: [] }; }
  novoAcessorio(): AcessorioRecurso {
    return { id: 0, descricao: '', checklist: false, padrao: false, ativo: true, versao: 0 };
  }
  novoServico(): Partial<ServicoRecurso> {
    return { descricao:'',codigoTempario:'',areaId:undefined, unidade:'Hr',tempo:undefined,
      vidaUtilDias:null,kmManutencao:null,nbs:null,observacao:'',preco:0,custoFixo:0,comissao:0,ativo:true };
  }
  get colunasDisponiveis(): ChaveColuna[] {
    return this.aba === 'acessorios' ? this.colunasAcessorios : this.colunasServicos;
  }
  get duplicado(): boolean {
    const nome = this.acessorioDraft.descricao.trim().toLocaleLowerCase('pt-BR');
    return !!nome && this.acessorios.some(a => a.descricao.trim().toLocaleLowerCase('pt-BR') === nome
      && a.id !== this.acessorioDraft.id && a.ativo);
  }
  get servicoAtualId(): number { return this.servicoDraft.id ?? 0; }
  get tituloServico(): string {
    return this.servicoDraft.id ? 'Serviço - ' + this.servicoDraft.descricao : 'Criação do serviço';
  }
  trocarAba(aba: AbaRecurso) {
    this.aba = aba; this.servicoDetalhe = false; this.drawer = null; this.modal = null;
    this.pesquisa = ''; this.filtroSituacao = ''; this.filtroCodigo = ''; this.filtroDescricao = '';
    this.filtroAreaId = null; this.filtroChecklist = false; this.precoVisualizado = null;
    this.recuperarColunas(); this.carregar();
  }
  private tratarErro(err: unknown, fallback: string): string {
    const http = err as HttpErrorResponse;
    if (http?.status === 403) return 'Você não possui permissão para essa operação.';
    if (http?.status === 409) return http.error?.detail ?? 'Registro duplicado ou alterado por outra pessoa.';
    if (http?.status === 422 || http?.status === 400) return http.error?.detail ?? http.error?.message ?? 'Revise os dados informados.';
    return fallback;
  }
  carregarAreas() {
    this.api.areas().subscribe({
      next: rows => { this.areas = rows; this.cdr.detectChanges(); },
      error: err => { this.erro = this.tratarErro(err,'Não foi possível consultar áreas.'); this.cdr.detectChanges(); }
    });
  }
  carregar() {
    this.erro = ''; this.carregando = true;
    const ativo = this.filtroSituacao || null;
    if (this.aba === 'areas') {
      this.api.areas({ busca: this.pesquisa, ativo }).subscribe({
        next: rows => { this.areas = rows; this.carregando=false; this.cdr.detectChanges(); },
        error: err => this.falha(err)
      });
    } else if (this.aba === 'servicos') {
      this.api.servicos({ busca: this.pesquisa || this.filtroDescricao,
        codigo:this.filtroCodigo,areaId:this.filtroAreaId,ativo }).subscribe({
        next: rows => { this.servicos = rows; this.carregando=false; this.cdr.detectChanges(); },
        error: err => this.falha(err)
      });
    } else {
      this.api.acessorios({ busca: this.pesquisa, ativo, checklist: this.filtroChecklist ? true : null }).subscribe({
        next: rows => { this.acessorios = rows; this.carregando=false; this.cdr.detectChanges(); },
        error: err => this.falha(err)
      });
    }
  }
  private falha(err: unknown) {
    this.carregando = false; this.erro = this.tratarErro(err,'Falha ao carregar dados. Tente novamente.');
    this.cdr.detectChanges();
  }
  private sucesso(message: string) {
    this.salvando = false; this.drawer = null; this.modal = null;
    this.mensagem = message; this.erro = ''; this.carregar(); this.carregarAreas();
  }
  private erroSalvar(err: unknown) {
    this.salvando = false; this.erro = this.tratarErro(err,'Não foi possível salvar.'); this.cdr.detectChanges();
  }

  abrirNovaArea() { this.areaDraft = this.novaArea(); this.drawer = 'area'; this.erro = ''; }
  editarArea(area: AreaRecurso) {
    this.api.area(area.id).subscribe({
      next: dado => { this.areaDraft = structuredClone(dado); this.drawer = 'area'; this.cdr.detectChanges(); },
      error: err => this.erroSalvar(err)
    });
  }
  adicionarSubarea() {
    this.areaDraft.subareas = [...(this.areaDraft.subareas ?? []),{descricao:'',ativo:true}];
  }
  removerSubarea(indice: number) {
    this.areaDraft.subareas?.splice(indice,1);
  }
  salvarArea() {
    if (!this.areaDraft.descricao.trim() || this.areaDraft.subareas?.some(s => !s.descricao.trim())) {
      this.erro = 'Preencha a descrição da área e de todas as subáreas.'; return;
    }
    this.salvando = true;
    const obs = this.areaDraft.id
      ? this.api.atualizarArea(this.areaDraft.id,this.areaDraft)
      : this.api.criarArea(this.areaDraft);
    obs.subscribe({ next: () => this.sucesso('Área salva com sucesso.'),error: err => this.erroSalvar(err) });
  }
  excluirArea(area: AreaRecurso) {
    if (!window.confirm('Desativar esta área? Os vínculos históricos serão preservados.')) return;
    this.api.excluirArea(area.id).subscribe({
      next: () => this.sucesso('Área desativada.'), error: err => this.erroSalvar(err)
    });
  }

  abrirNovoAcessorio() { this.acessorioDraft = this.novoAcessorio(); this.drawer = 'acessorio'; this.erro = ''; }
  editarAcessorio(item: AcessorioRecurso) {
    this.acessorioDraft = { ...item }; this.drawer = 'acessorio'; this.erro = '';
  }
  escolherSugestao(nome: string) { this.acessorioDraft.descricao = nome; }
  salvarAcessorio() {
    if (!this.acessorioDraft.descricao.trim()) { this.erro='Descrição obrigatória.';return; }
    if (this.duplicado) { this.erro='Descrição já cadastrada. Não é possível duplicar o item.';return; }
    this.salvando = true;
    const obs = this.acessorioDraft.id
      ? this.api.atualizarAcessorio(this.acessorioDraft.id,this.acessorioDraft)
      : this.api.criarAcessorio(this.acessorioDraft);
    obs.subscribe({
      next: () => this.sucesso('Acessório salvo com sucesso.'),
      error: err => this.erroSalvar(err)
    });
  }
  excluirAcessorio(item: AcessorioRecurso) {
    if (!window.confirm('Desativar o acessório?')) return;
    this.api.excluirAcessorio(item.id).subscribe({
      next: () => this.sucesso('Acessório desativado.'),
      error: err => this.erroSalvar(err)
    });
  }

  criarServico() {
    this.servicoDraft = this.novoServico(); this.servicoDetalhe = true; this.servicoAba = 'geral';
    this.erro = '';
  }
  editarServico(servico: ServicoRecurso) {
    this.api.servico(servico.id).subscribe({
      next: dado => {
        this.servicoDraft = { ...dado }; this.servicoDetalhe = true; this.servicoAba = 'geral';
        this.api.marcas().subscribe({next: rows => {this.marcas = rows;this.cdr.detectChanges();}});
        this.carregarAplicacoes(); this.cdr.detectChanges();
      },
      error: err => this.erroSalvar(err)
    });
  }
  voltarServicos() { this.servicoDetalhe = false; this.servicoAba = 'geral'; this.carregar(); }
  salvarServico() {
    const f = this.servicoDraft;
    if (!f.descricao?.trim() || !f.areaId || !f.unidade || f.tempo == null || f.tempo <= 0) {
      this.erro = 'Descrição, área, unidade e tempo maior que zero são obrigatórios.'; return;
    }
    if ((f.preco ?? 0) < 0 || (f.custoFixo ?? 0) < 0 || (f.comissao ?? 0) < 0 ||
      (f.comissao ?? 0) > 100) { this.erro = 'Revise os valores e a comissão (0 a 100%).'; return; }
    this.salvando = true;
    const obs = f.id ? this.api.atualizarServico(f.id,f) : this.api.criarServico(f);
    obs.subscribe({
      next: dado => {
        this.servicoDraft = { ...dado }; this.salvando = false;
        this.mensagem = 'Serviço salvo com sucesso.'; this.erro = '';
        this.carregar(); this.carregarAplicacoes(); this.cdr.detectChanges();
      },
      error: err => this.erroSalvar(err)
    });
  }
  excluirServico(servico: ServicoRecurso) {
    if (!window.confirm('Desativar este serviço? O histórico será preservado.')) return;
    this.api.excluirServico(servico.id).subscribe({
      next: () => this.sucesso('Serviço desativado.'),
      error: err => this.erroSalvar(err)
    });
  }
  selecionarAbaServico(aba: 'geral' | 'aplicacoes') {
    if (aba === 'aplicacoes' && !this.servicoDraft.id) {
      this.erro = 'Salve o serviço antes de cadastrar aplicações.'; return;
    }
    this.servicoAba = aba; this.erro = '';
    if (aba === 'aplicacoes') { this.carregarAplicacoes(); this.api.marcas().subscribe({
      next: result => { this.marcas=result;this.cdr.detectChanges(); }, error: err => this.erroSalvar(err)
    }); }
  }
  carregarAplicacoes() {
    if (!this.servicoAtualId) return;
    this.api.aplicacoes(this.servicoAtualId).subscribe({
      next: result => { this.aplicacoes=result;this.cdr.detectChanges(); },error: err => this.erroSalvar(err)
    });
  }
  escolherMarca() {
    this.modeloId=null;this.modelos=[];
    if (!this.marcaId) return;
    this.api.modelos(this.marcaId).subscribe({
      next: result => { this.modelos=result;this.cdr.detectChanges(); },error: err => this.erroSalvar(err)
    });
  }
  incluirAplicacao() {
    if (!this.marcaId || !this.modeloId || !this.tempoAplicacao || this.tempoAplicacao<=0 || !this.servicoAtualId) {
      this.erro='Informe montadora, modelo e tempo válido.';return;
    }
    this.api.adicionarAplicacao(this.servicoAtualId,{marcaId:this.marcaId,modeloId:this.modeloId,tempo:this.tempoAplicacao}).subscribe({
      next: () => {this.carregarAplicacoes();this.mensagem='Aplicação vinculada.';this.modeloId=null;this.tempoAplicacao=null;},
      error: err => this.erroSalvar(err)
    });
  }
  salvarTempoAplicacao(item: AplicacaoRecurso) {
    if (!item.tempo || item.tempo<=0) {this.erro='Tempo inválido.';return;}
    this.api.alterarAplicacao(this.servicoAtualId,item.id,{tempo:item.tempo}).subscribe({
      next: () => {this.mensagem='Tempo atualizado.';this.carregarAplicacoes();},
      error: err => this.erroSalvar(err)
    });
  }
  removerAplicacao(item: AplicacaoRecurso) {
    if (!window.confirm('Remover aplicação deste modelo?')) return;
    this.api.removerAplicacao(this.servicoAtualId,item.id).subscribe({
      next: () => {this.mensagem='Aplicação removida.';this.carregarAplicacoes();},
      error: err => this.erroSalvar(err)
    });
  }

  abrirColunas() { this.modal = 'colunas'; this.erro=''; }
  possuiColuna(col: ChaveColuna) { return this.colunas.includes(col); }
  alternarColuna(col: ChaveColuna) {
    if (col==='descricao') return;
    this.colunas = this.possuiColuna(col) ? this.colunas.filter(c=>c!==col) :
      [...this.colunas,col];
  }
  moverColuna(col: ChaveColuna, step: number) {
    const i = this.colunas.indexOf(col), j=i+step;
    if (col==='descricao' || j<=0 || j>=this.colunas.length) return;
    const clone=[...this.colunas];[clone[i],clone[j]]=[clone[j],clone[i]];this.colunas=clone;
  }
  restaurarColunas() { this.colunas = [...this.colunasDisponiveis]; }
  salvarColunas() {
    window.localStorage.setItem('neritech-recursos-colunas-'+this.aba,JSON.stringify(this.colunas));
    this.modal=null;
  }
  recuperarColunas() {
    const defaultCols = this.aba==='acessorios'?this.colunasAcessorios:this.colunasServicos;
    try {
      const saved = JSON.parse(window.localStorage.getItem('neritech-recursos-colunas-'+this.aba) ?? 'null');
      this.colunas = Array.isArray(saved) ? [...new Set<ChaveColuna>(['descricao',...saved.filter((c:ChaveColuna)=>defaultCols.includes(c))])] : [...defaultCols];
    } catch { this.colunas=[...defaultCols]; }
  }
  abrirFiltro() {this.drawer=this.aba==='servicos'?'filtro-servicos':'filtro-acessorios';}
  filtrar() {this.drawer=null;this.carregar();}
  resetarFiltros() {
    this.filtroCodigo='';this.filtroDescricao='';this.filtroAreaId=null;this.filtroSituacao='';this.filtroChecklist=false;
    this.carregar();
  }
  abrirPrecos() {this.modal='precos';this.valorLote=null;this.operacaoLote='SOMAR_VALOR';this.filtroAreaId=null;}
  aplicarPrecos() {
    if (this.valorLote == null || this.valorLote<0) {this.erro='Informe o valor.';return;}
    if (!window.confirm('Alterar preços dos serviços ativos selecionados?')) return;
    this.salvando=true;
    this.api.alterarPrecos({operacao:this.operacaoLote,valor:this.valorLote,areaId:this.filtroAreaId}).subscribe({
      next: resp => this.sucesso(resp.alterados+' preços atualizados.'),
      error: err => this.erroSalvar(err)
    });
  }
  abrirImportacao() {this.modal='importacao';this.csvEtapa=1;this.csvLinhas=[];this.csvErro='';this.csvNome='';}
  baixarModelo() {
    const modelo='Código do Serviço;Descrição;Área;Unidade;Tempo;Preço\nSRV001;Revisão geral;OFICINA;Hr;1,50;150,00\n';
    this.baixarArquivo('modelo-servicos.csv',modelo);
  }
  private baixarArquivo(filename: string,content: string) {
    const url=URL.createObjectURL(new Blob(['\uFEFF',content],{type:'text/csv;charset=utf-8;'}));
    const a=document.createElement('a');a.href=url;a.download=filename;a.click();URL.revokeObjectURL(url);
  }
  exportarRelatorio() {
    const headers=this.colunas.map(c=>this.rotulos[c]);
    const rows=this.aba==='areas'
      ? this.areas.map(a=>[a.descricao,a.ativo?'Sim':'Não'])
      : this.aba==='servicos'
        ? this.servicos.map(s=>this.colunas.map(c=>String(s[c as keyof ServicoRecurso]??'')))
        : this.acessorios.map(a=>this.colunas.map(c=>String(a[c as keyof AcessorioRecurso]??'')));
    const linhas=[this.aba==='areas'?['Descrição','Ativo']:headers,...rows];
    this.baixarArquivo('neritech-'+this.aba+'.csv',linhas.map(row=>row.map(cell=>'"'+String(cell).replace(/"/g,'""')+'"').join(';')).join('\r\n'));
  }
  async arquivoCsv(event: Event) {
    this.csvErro='';const file=(event.target as HTMLInputElement).files?.[0];if(!file)return;
    this.csvNome=file.name;
    try {
      const content=await file.text();
      const linhas=content.replace(/^\uFEFF/,'').split(/\r?\n/).filter(l=>l.trim());
      if(linhas.length<2){this.csvErro='Arquivo sem dados.';return;}
      const delim=linhas[0].includes(';')?';':',';
      const parse=(line:string):string[] => {
        const parts:string[]=[];let cur='';let quoted=false;
        for(let i=0;i<line.length;i++){
          if(line[i]==='"'){if(quoted && line[i+1]==='"'){cur+='"';i++;}else quoted=!quoted;}
          else if(line[i]===delim&&!quoted){parts.push(cur.trim());cur='';}
          else cur+=line[i];
        } parts.push(cur.trim());return parts;
      };
      const header=parse(linhas[0]).map(s=>s.toLocaleLowerCase('pt-BR').normalize('NFD').replace(/[\u0300-\u036f]/g,''));
      const col=(nome:string)=>header.findIndex(x=>x===nome);
      const ci=col('codigo do servico'), di=col('descricao'), ai=col('area'), ui=col('unidade'), ti=col('tempo'), pi=col('preco');
      if([ci,di,ai,ui].some(i=>i<0)){this.csvErro='Cabeçalhos necessários: Código do Serviço, Descrição, Área, Unidade.';return;}
      const registros=linhas.slice(1).map((line,i)=>{
        const val=parse(line);const areaNome=val[ai]??'';
        const area=this.areas.find(a=>a.descricao.toLocaleLowerCase('pt-BR')===areaNome.toLocaleLowerCase('pt-BR'));
        if(!area)throw new Error('Linha '+(i+2)+': área não cadastrada: '+areaNome);
        const number=(raw:string|undefined)=>{ const s=(raw??'0').trim(); return Number(s.includes(',') ? s.replace(/\./g,'').replace(',','.') : s); };
        return {codigo:val[ci],descricao:val[di],areaId:area.id,unidade:val[ui],
          tempo:ti>=0&&val[ti]?number(val[ti]):1,preco:pi>=0?number(val[pi]):0};
      });
      if(registros.length>1000)throw new Error('Máximo de 1000 linhas por importação.');
      const codes=new Set<string>();
      for(const [i,r] of registros.entries()){
        if(!r.codigo||!r.descricao||!r.unidade||r.unidade.length>5||r.tempo<=0||r.preco<0
          ||codes.has(r.codigo.toLowerCase()))throw new Error('Dados inválidos ou código repetido na linha '+(i+2));
        codes.add(r.codigo.toLowerCase());
      }
      this.csvLinhas=registros;this.csvEtapa=2;this.cdr.detectChanges();
    } catch(err){this.csvErro=(err as Error).message;this.cdr.detectChanges();}
  }
  confirmarImportacao() {
    if(!this.csvLinhas.length)return;
    this.salvando=true;this.csvErro='';
    this.api.importarServicos(this.csvLinhas).subscribe({
      next: result=>{this.csvEtapa=3;this.mensagem=result.importados+' serviços importados.';this.salvando=false;this.carregar();this.cdr.detectChanges();},
      error: err=>{this.csvErro=this.tratarErro(err,'Arquivo recusado pelo servidor. Nenhum dado foi confirmado.');this.salvando=false;this.cdr.detectChanges();}
    });
  }
  moeda(valor: number|null|undefined): string {
    return new Intl.NumberFormat('pt-BR',{style:'currency',currency:'BRL'}).format(valor??0);
  }
}
