import { CommonModule } from '@angular/common';
import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, firstValueFrom, finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  FornecedorService, ContatoFornecedor, CanalFornecedor, CreditoFornecedor,
  DevolucaoFornecedor, LinhaImportacaoFornecedor
} from '../fornecedor.service';
import { FornecedorRequest, FornecedorResponse } from '../models/fornecedor.models';

type Aba = 'geral' | 'contato' | 'devolvidos' | 'credito';
type Drawer = 'novo' | 'contato' | 'filtro' | 'importar' | null;
type TipoPessoa = 'FISICA' | 'JURIDICA';

@Component({
  selector: 'nt-fornecedor-referencia',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './fornecedor-referencia.html',
  styleUrls: ['./fornecedor-referencia.scss']
})
export class FornecedorReferenciaComponent implements OnInit {
  private readonly api = inject(FornecedorService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly searchInput = new Subject<string>();
  id: number | null = null;
  detalhe = false;
  aba: Aba = 'geral';
  drawer: Drawer = null;
  loading = false;
  saving = false;
  exporting = false;
  erro = '';
  aviso = '';
  busca = '';
  filtroSituacao: '' | 'true' | 'false' = '';
  filtroDocumento = '';
  filtroNome = '';
  itens: FornecedorResponse[] = [];
  total = 0;
  page = 0;
  size = 20;
  selecionado: FornecedorResponse | null = null;
  form: FornecedorRequest = this.formVazio();
  planos: Array<{ id: number; codigo: string; nome: string }> = [];
  contatos: ContatoFornecedor[] = [];
  contatoForm: ContatoFornecedor = this.contatoVazio();
  devolvidos: DevolucaoFornecedor[] = [];
  creditos: CreditoFornecedor[] = [];
  filtroCompra = '';
  ordem = 'desc';
  importarEtapa: 1 | 2 | 3 = 1;
  importarLinhas: LinhaImportacaoFornecedor[] = [];
  importarNome = '';
  importarErro = '';
  private ultimaBusca = 0;
  readonly estados = [
    'AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB',
    'PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'
  ];

  ngOnInit(): void {
    this.searchInput.pipe(debounceTime(260), distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)).subscribe(() => this.listar(true));
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id > 0 && Number.isSafeInteger(id)) this.abrirFornecedor(id);
    else if (this.route.snapshot.routeConfig?.path === 'novo') {
      this.listar(); this.novo();
    } else this.listar();
    this.api.planosContas().subscribe({ next: p => this.planos=p,
      error: e => this.erro=this.descreverErro(e) });
  }
  formVazio(): FornecedorRequest {
    return {
      empresaId: 0, tipoPessoa: 'FISICA', cpf: '', cnpj: '', nomeFantasia: '',
      razaoSocial: '', inscricaoEstadual: '', inscricaoMunicipal: '', emailPrincipal: '',
      telefonePrincipal: '', website: '', observacoes: '', cep: '', logradouro: '',
      numero: '', bairro: '', estado: '', cidade: '', complemento: '',
      classificacao: 'OUTROS', planoContasId: null, linhaPecas: '', ativo: true
    };
  }
  contatoVazio(): ContatoFornecedor {
    return { nome: '', sexo: 'MASCULINO', dataNascimento: null, contatoPrincipal: false, meios: [] };
  }
  get pessoaJuridica(): boolean { return this.form.tipoPessoa === 'JURIDICA'; }
  get titulo(): string {
    return this.selecionado ? 'Fornecedor - ' + (this.selecionado.razaoSocial || this.selecionado.nomeFantasia) : 'Fornecedores';
  }
  get nomeValido(): boolean { return !!this.form.razaoSocial?.trim(); }
  get podeSalvar(): boolean {
    const cpf=(this.form.cpf??'').replace(/\D/g,'');
    const cnpj=(this.form.cnpj??'').replace(/[^a-zA-Z0-9]/g,'');
    const email=this.form.emailPrincipal?.trim()??'';
    return this.nomeValido && !!this.form.classificacao &&
      (!cpf || this.cpfValido(cpf)) && (!cnpj || this.cnpjValido(cnpj)) &&
      (!email || /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) &&
      (!this.form.cep || /^\d{5}-?\d{3}$/.test(this.form.cep)) &&
      (!this.form.website || /^https?:\/\/\S+\.\S+/.test(this.form.website));
  }
  private cpfValido(d: string): boolean {
    if (d.length!==11 || /^(\d)\1+$/.test(d)) return false;
    for(let n=9;n<11;n++){
      let s=0;for(let i=0;i<n;i++)s+=Number(d[i])*(n+1-i);
      let r=(s*10)%11;if(r===10)r=0;if(r!==Number(d[n]))return false;
    }
    return true;
  }
  private cnpjValido(d: string): boolean {
    if(d.length!==14 || /^(\w)\1+$/.test(d))return false;
    for(const [n,initial] of [[12,5],[13,6]]){
      let sum=0,w=initial;for(let i=0;i<n;i++){sum+=Number(d[i])*w;w=w===2?9:w-1;}
      const rem=sum%11;if(Number(d[n])!==(rem<2?0:11-rem))return false;
    }
    return true;
  }
  formatarDocumento(value: string|null|undefined, pessoa: TipoPessoa): string {
    const d=(value??'').replace(/\D/g,'');
    if(pessoa==='FISICA')return d.replace(/^(\d{3})(\d)/,'$1.$2').replace(/^(\d{3})\.(\d{3})(\d)/,'$1.$2.$3').replace(/(\d{3})(\d{1,2})$/,'$1-$2').slice(0,14);
    return d.replace(/^(\d{2})(\d)/,'$1.$2').replace(/^(\d{2})\.(\d{3})(\d)/,'$1.$2.$3').replace(/\.(\d{3})(\d)/,'.$1/$2').replace(/(\d{4})(\d{1,2})$/,'$1-$2').slice(0,18);
  }
  formatarCep(v: string): string {
    const d=v.replace(/\D/g,'').slice(0,8);return d.length>5?d.slice(0,5)+'-'+d.slice(5):d;
  }
  documentoLinha(r: FornecedorResponse): string {
    return r.tipoPessoa==='FISICA' ? this.formatarDocumento(r.cpf,'FISICA') :
      this.formatarDocumento(r.cnpj,'JURIDICA');
  }
  labelLinha(r: FornecedorResponse): string {return r.razaoSocial||r.nomeFantasia||'—';}
  digitarBusca(): void {this.searchInput.next(this.busca);}
  listar(reset=false): void {
    if(reset)this.page=0;
    this.loading=true;this.erro='';
    const n=++this.ultimaBusca;
    this.api.list({page:this.page,size:this.size,sort:'id,desc',
      search:this.busca,documento:this.filtroDocumento,nome:this.filtroNome,
      ativo:this.filtroSituacao===''?undefined:this.filtroSituacao==='true'})
      .pipe(finalize(()=>{if(n===this.ultimaBusca)this.loading=false;}))
      .subscribe({
        next:r=>{if(n!==this.ultimaBusca)return;this.itens=r.content||[];this.total=r.totalElements||0;},
        error:e=>{if(n!==this.ultimaBusca)return;this.erro=this.descreverErro(e);this.itens=[];this.total=0;}
      });
  }
  navegarPagina(delta:number): void {
    const p=this.page+delta;if(p<0||p*this.size>=this.total||this.loading)return;
    this.page=p;this.listar();
  }
  novo(): void {
    this.id=null;this.selecionado=null;this.form=this.formVazio();this.drawer='novo';this.erro='';
  }
  selecionarTipo(tipo:TipoPessoa): void {
    this.form.tipoPessoa=tipo;
    if(tipo==='FISICA'){this.form.cnpj='';this.form.inscricaoEstadual='';this.form.inscricaoMunicipal='';}
    else this.form.cpf='';
  }
  abrirFornecedor(id:number): void {
    this.loading=true;this.erro='';
    this.api.get(id).pipe(finalize(()=>this.loading=false)).subscribe({
      next:r=>{this.id=id;this.selecionado=r;this.form={
        ...r, empresaId:r.empresaId, tipoPessoa:r.tipoPessoa||'FISICA',
        cpf:r.cpf??'',cnpj:r.cnpj??'',razaoSocial:r.razaoSocial??'',
        nomeFantasia:r.nomeFantasia??'', emailPrincipal:r.emailPrincipal??'',
        website:r.website??'',classificacao:r.classificacao??'OUTROS'
      };this.detalhe=true;this.drawer=null;this.aba='geral';},
      error:e=>this.erro=this.descreverErro(e)
    });
  }
  abrirAba(aba:Aba): void {
    this.aba=aba;this.filtroCompra='';
    if(!this.id)return;
    if(aba==='contato')this.carregarContatos();
    if(aba==='devolvidos')this.carregarDevolvidos();
    if(aba==='credito')this.carregarCreditos();
  }
  voltar(): void {
    this.detalhe=false;this.id=null;this.selecionado=null;this.drawer=null;
    this.router.navigate(['/fornecedor/lista']);this.listar();
  }
  salvar():void {
    if(this.saving||!this.podeSalvar){this.erro='Revise nome, documento, e-mail, CEP e classificação.';return;}
    this.saving=true;this.erro='';
    const payload={...this.form,nomeFantasia:this.form.nomeFantasia?.trim()||this.form.razaoSocial?.trim(),
      cpf:this.pessoaJuridica?null:this.form.cpf,cnpj:this.pessoaJuridica?this.form.cnpj:null,
      emailPrincipal:this.form.emailPrincipal?.trim()||'',razaoSocial:this.form.razaoSocial?.trim(),
      enderecoCompleto:[this.form.logradouro,this.form.numero,this.form.bairro,this.form.complemento]
        .filter(Boolean).join(', ')};
    const obs=this.selecionado
      ?this.api.update(this.selecionado.id,{...payload,versao:this.selecionado.versao})
      :this.api.create(payload);
    obs.pipe(finalize(()=>this.saving=false)).subscribe({
      next:r=>{this.aviso='Fornecedor salvo com sucesso.';this.drawer=null;this.abrirFornecedor(r.id);this.listar();},
      error:e=>this.erro=this.descreverErro(e)
    });
  }
  alterarSituacao(ativo:boolean):void {
    if(!this.selecionado||this.saving)return;
    this.form.ativo=ativo;this.salvar();
  }
  removerLista(item:FornecedorResponse):void {
    if(this.saving||!item.ativo||!window.confirm('Desativar '+this.labelLinha(item)+'? Pedidos e documentos serão preservados.'))return;
    this.saving=true;
    this.api.delete(item.id).pipe(finalize(()=>this.saving=false)).subscribe({
      next:()=>{this.aviso='Fornecedor desativado.';this.listar();},
      error:e=>this.erro=this.descreverErro(e)
    });
  }
  excluir():void {
    if(!this.selecionado||this.saving)return;
    if(!window.confirm('Desativar fornecedor? Compras, pedidos e vínculos históricos serão preservados.'))return;
    this.saving=true;
    this.api.delete(this.selecionado.id).pipe(finalize(()=>this.saving=false)).subscribe({
      next:()=>{this.aviso='Fornecedor desativado.';this.voltar();},
      error:e=>this.erro=this.descreverErro(e)
    });
  }
  filtrar():void{this.drawer=null;this.listar(true);}
  limparFiltro():void{this.filtroNome='';this.filtroDocumento='';this.filtroSituacao='';this.drawer=null;this.listar(true);}
  carregarContatos():void{
    if(!this.id)return;this.loading=true;this.erro='';
    this.api.contatos(this.id).pipe(finalize(()=>this.loading=false)).subscribe({
      next:r=>this.contatos=r,error:e=>this.erro=this.descreverErro(e)
    });
  }
  novoContato():void{this.contatoForm=this.contatoVazio();this.drawer='contato';this.erro='';}
  editarContato(item:ContatoFornecedor):void{
    this.contatoForm={...item,meios:item.meios.map(x=>({...x}))};this.drawer='contato';this.erro='';
  }
  adicionarCanal():void{
    this.contatoForm.meios.push({tipo:'CELULAR',valor:'',whatsapp:false});
  }
  retirarCanal(index:number):void{this.contatoForm.meios.splice(index,1);}
  salvarContato():void{
    if(!this.id||!this.contatoForm.nome.trim()) {this.erro='Nome obrigatório.';return;}
    if(this.contatoForm.meios.some(x=>!x.valor.trim())){this.erro='Preencha todos os contatos adicionados.';return;}
    this.saving=true;this.erro='';
    const obs=this.contatoForm.id
      ?this.api.editarContato(this.id,this.contatoForm.id,this.contatoForm)
      :this.api.criarContato(this.id,this.contatoForm);
    obs.pipe(finalize(()=>this.saving=false)).subscribe({
      next:()=>{this.drawer=null;this.aviso='Contato salvo.';this.carregarContatos();},
      error:e=>this.erro=this.descreverErro(e)
    });
  }
  excluirContato(item:ContatoFornecedor):void{
    if(!this.id||!item.id||!window.confirm('Remover contato?'))return;
    this.api.excluirContato(this.id,item.id).subscribe({
      next:()=>{this.aviso='Contato desativado.';this.carregarContatos();},
      error:e=>this.erro=this.descreverErro(e)
    });
  }
  contatoValor(item:ContatoFornecedor,tipo:CanalFornecedor['tipo']):string{
    return item.meios.filter(m=>m.tipo===tipo).map(m=>m.valor).join(' / ')||'—';
  }
  contatoWhatsapp(item:ContatoFornecedor):string{
    return item.meios.filter(m=>m.tipo==='CELULAR'&&m.whatsapp).map(m=>m.valor).join(' / ')||'—';
  }
  carregarDevolvidos():void{
    if(!this.id)return;this.loading=true;this.erro='';
    this.api.devolucoes(this.id,this.filtroCompra,this.ordem)
      .pipe(finalize(()=>this.loading=false)).subscribe({
        next:x=>this.devolvidos=x,error:e=>this.erro=this.descreverErro(e)
      });
  }
  carregarCreditos():void{
    if(!this.id)return;this.loading=true;this.erro='';
    this.api.creditos(this.id,this.filtroCompra,this.ordem)
      .pipe(finalize(()=>this.loading=false)).subscribe({
        next:x=>this.creditos=x,error:e=>this.erro=this.descreverErro(e)
      });
  }
  moeda(n:number|null|undefined):string {
    return new Intl.NumberFormat('pt-BR',{style:'currency',currency:'BRL'}).format(n??0);
  }
  private csvLine(data:unknown[]):string{
    return data.map(v=>'"'+String(v??'').replace(/^[=+@\-\t\r]/,"'$&").replace(/"/g,'""')+'"').join(';');
  }
  private exportarArquivo(nome:string,conteudo:string):void{
    const url=URL.createObjectURL(new Blob(['\uFEFF'+conteudo],{type:'text/csv;charset=utf-8'}));
    const a=document.createElement('a');a.href=url;a.download=nome;a.click();
    setTimeout(()=>URL.revokeObjectURL(url),1000);
  }
  async relatorio():Promise<void>{
    if(this.exporting)return;this.exporting=true;
    try {
      const lista:FornecedorResponse[]=[];let pag=0;
      while(true){
        const r=await firstValueFrom(this.api.list({page:pag,size:100,sort:'id,desc',
          search:this.busca,documento:this.filtroDocumento,nome:this.filtroNome,
          ativo:this.filtroSituacao===''?undefined:this.filtroSituacao==='true'}));
        lista.push(...(r.content||[]));pag++;
        if(pag>=r.totalPages)break;
      }
      this.exportarArquivo('fornecedores-neritech.csv',[
        this.csvLine(['Fornecedor','CPF/CNPJ','E-mail','Telefone']),
        ...lista.map(f=>this.csvLine([this.labelLinha(f),this.documentoLinha(f),
          f.emailPrincipal,f.telefonePrincipal]))
      ].join('\r\n'));
    } catch(e){this.erro=this.descreverErro(e);}
    finally{this.exporting=false;}
  }
  abrirImportar():void{
    this.drawer='importar';this.importarEtapa=1;this.importarLinhas=[];this.importarErro='';
  }
  baixarModelo():void{
    this.exportarArquivo('modelo-fornecedores.csv',
      'Tipo de pessoa;CPF;CNPJ;Nome/Razão social;Nome fantasia;Email;Classificação\nJURIDICA;;11.222.333/0001-81;Exemplo Ltda;Exemplo;contato@exemplo.com;OUTROS\n');
  }
  async selecionarCsv(event:Event):Promise<void>{
    const file=(event.target as HTMLInputElement).files?.[0];if(!file)return;
    this.importarNome=file.name;this.importarErro='';
    try{
      if(file.size>2_000_000)throw Error('Arquivo deve ter até 2 MB.');
      const text=(await file.text()).replace(/^\uFEFF/,'');
      const lines=text.split(/\r?\n/).filter(x=>x.trim());
      if(lines.length<2)throw Error('Arquivo sem registros.');
      const parse=(line:string):string[]=>{
        const vals:string[]=[];let current='',quoted=false;
        for(let i=0;i<line.length;i++){
          const c=line[i];if(c==='"'){if(quoted&&line[i+1]==='"'){current+='"';i++;}else quoted=!quoted;}
          else if(c===';'&&!quoted){vals.push(current.trim());current='';}
          else current+=c;
        } if(quoted)throw Error('CSV com aspas inválidas.');
        vals.push(current.trim());return vals;
      };
      const names=parse(lines[0]).map(x=>x.toLocaleLowerCase('pt-BR').normalize('NFD').replace(/[\u0300-\u036f]/g,''));
      const expected=['tipo de pessoa','cpf','cnpj','nome/razao social','nome fantasia','email','classificacao'];
      if(expected.some(n=>!names.includes(n)))throw Error('Cabeçalhos inválidos; baixe o modelo CSV.');
      const rows=lines.slice(1).map((line,index)=>{
        const d=parse(line);const at=(n:string)=>d[names.indexOf(n)]||'';
        const pessoa=at('tipo de pessoa').toUpperCase();
        if(pessoa!=='FISICA'&&pessoa!=='JURIDICA')throw Error('Tipo inválido na linha '+(index+2));
        if(!at('nome/razao social'))throw Error('Nome obrigatório na linha '+(index+2));
        const classe=at('classificacao').toUpperCase()||'OUTROS';
        if(!['OUTROS','A','B','C','D','E'].includes(classe))throw Error('Classificação inválida na linha '+(index+2));
        return {tipoPessoa:pessoa as TipoPessoa,cpf:at('cpf'),cnpj:at('cnpj'),
          razaoSocial:at('nome/razao social'),nomeFantasia:at('nome fantasia'),
          email:at('email'),classificacao:classe};
      });
      if(rows.length>500)throw Error('Limite de 500 fornecedores por arquivo.');
      this.importarLinhas=rows;this.importarEtapa=2;
    }catch(e){this.importarErro=(e as Error).message;}
  }
  confirmarImportacao():void{
    if(!this.importarLinhas.length||this.saving)return;
    this.saving=true;
    this.api.importar(this.importarLinhas).pipe(finalize(()=>this.saving=false)).subscribe({
      next:r=>{this.importarEtapa=3;this.aviso=r.importados+' fornecedores importados.';this.listar(true);},
      error:e=>this.importarErro=this.descreverErro(e)
    });
  }
  erroDesenho():void{this.erro='';}
  private descreverErro(e:any):string {
    if(e?.status===403)return 'Acesso negado: empresa ou permissão insuficiente.';
    if(e?.status===409)return 'Cadastro duplicado ou registro alterado por outra sessão (409).';
    if(e?.status===422||e?.status===400)return e.error?.message||'Dados inválidos. Revise os campos.';
    if(e?.status===0)return 'Sem conexão com o servidor.';
    return 'Não foi possível concluir a operação.';
  }
}
