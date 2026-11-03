import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { finalize, take } from 'rxjs';
import { ConfiguracaoComplementarEmpresaService } from '../services/configuracao-complementar-empresa.service';
import { ConfiguracoesSegurasService, CertificadoProtegido } from '../../../profile/overview/configuracoes-seguras.service';
import { UtilService } from '../../../../shared/services/util.service';

type CampoFiscal = { chave: string; titulo: string; tipo?: 'texto'|'numero'|'selecao'|'data'|'area'; opcoes?: string[]; largura?: 'total'; };
type SerieFiscal = { serie: string; numeroInicial: number; principal: boolean };
type ContatoFiscal = { tipo: string; valor: string };
type AutorizacaoFiscal = { documento: string; email: string };

const PADROES_FISCAIS: Record<string, Record<string, any>> = {
  FISCAL_GERAL: {
    ambienteEmissao: 'PRODUCAO', regimeTributarioEspecial: '', optanteSimplesNacional: false,
    habilitarNfe: false, habilitarNfce: false, habilitarNfse: false, emitePropriasNotas: true,
    mostrarValorTotalNotas: false, recuperarPesoPeca: false, incentivoCultural: false, incentivoFiscal: false,
    deduzirIcmsPisCofins: false, codigoPecaDanfe: 'CODIGO_INTERNO', contatos: [], autorizacoes: [],
    inscricaoEstadual: '', inscricaoMunicipal: '', regimeTributario: '', cnae: '', cep: '', estado: '', cidade: '', bairro: '', logradouro: '', numero: '', complemento: ''
  },
  FISCAL_NFE: {
    cfopPadrao: '', naturezaOperacao: '', unidade: '', codigoBeneficioFiscal: '', calcularIbptAutomaticamente: false,
    cstIcms: '', aliquotaIcms: '', cstIpi: '', aliquotaIpi: '', codigoEnquadramentoIpi: '', cstPis: '', aliquotaPis: '',
    cstCofins: '', aliquotaCofins: '', aliquotaCsll: '', aliquotaIrrf: '', cstIbsCbs: '', classificacaoTributaria: '',
    aliquotaIbsUf: '', aliquotaIbsMunicipal: '', aliquotaCbs: '', diferimentoIbsUf: '', diferimentoIbsMunicipal: '', diferimentoCbs: '',
    devolvidoIbsUf: '', devolvidoIbsMunicipal: '', devolvidoCbs: '', tipoEmissao: '', csrtEstado: '', impressaoFcp: false,
    partilhaImpressao: false, consultarDfe: false, enviarEmail: false, informacaoInteresseFisco: '', informacaoInteresseContribuinte: '',
    informacaoDevolucao: '', series: []
  },
  FISCAL_NFCE: {
    cfopPadrao: '', naturezaOperacao: '', unidade: '', codigoBeneficioFiscal: '', calcularIbptAutomaticamente: false,
    cstIcms: '', aliquotaIcms: '', cstIpi: '', aliquotaIpi: '', codigoEnquadramentoIpi: '', cstPis: '', aliquotaPis: '', cstCofins: '',
    aliquotaCofins: '', aliquotaCsll: '', aliquotaIrrf: '', cstIbsCbs: '', classificacaoTributaria: '', aliquotaIbsUf: '',
    aliquotaIbsMunicipal: '', aliquotaCbs: '', diferimentoIbsUf: '', diferimentoIbsMunicipal: '', diferimentoCbs: '', devolvidoIbsUf: '',
    devolvidoIbsMunicipal: '', devolvidoCbs: '', csrtEstado: '', utilizarQrCodeVersao3: false, enviarEmailEntrega: false,
    informacaoInteresseFisco: '', informacaoInteresseContribuinte: '', series: []
  },
  FISCAL_NFSE: {
    codigoServicoIssqn: '', codigoNbsPadrao: '', tipoOperacao: '', naturezaTributacao: '', codigoIndicadorOperacao: '', unidade: '',
    agruparServicosPorArea: false, regimeApuracao: '', leiAnexoTabela: '', dataInicioAtividade: '', receitaBruta: '',
    aliquotaIbptSimples: '', aliquotaIbptFederal: '', aliquotaIbptEstadual: '', aliquotaIbptMunicipal: '', aliquotaAproximadaTributos: '',
    ocultarIbptDescricao: false, calcularIbptAutomaticamente: false, emitirNfsNacional: false, loginPrefeitura: '', senhaPrefeitura: '',
    numeracaoAutomaticaRps: true, loteRps: '', quebrarLinhaPadrao: true, enviarEmail: false, cstIbsCbs: '', classificacaoTributaria: '',
    aliquotaIbsUf: '', aliquotaIbsMunicipal: '', aliquotaCbs: '', diferimentoIbsUf: '', diferimentoIbsMunicipal: '', diferimentoCbs: '',
    devolvidoIbsUf: '', devolvidoIbsMunicipal: '', devolvidoCbs: '', informacaoInteresseFisco: '', series: []
  },
  FISCAL_FCP: {},
  FISCAL_SINTEGRA: { codigoEstrutura: '', codigoNaturezaOperacoes: '', nomeResponsavel: '', telefoneResponsavel: '', gerarRegistro51: false, gerarRegistro53: false },
  FISCAL_SPED: { nomeContador: '', cpfContador: '', crcContador: '', telefoneContador: '', emailContador: '', regimePisCofins: '' }
};

const ESTADOS = [
  ['AC','Acre'],['AL','Alagoas'],['AM','Amazonas'],['AP','Amapá'],['BA','Bahia'],['CE','Ceará'],['DF','Distrito Federal'],
  ['ES','Espírito Santo'],['GO','Goiás'],['MA','Maranhão'],['MG','Minas Gerais'],['MS','Mato Grosso do Sul'],['MT','Mato Grosso'],
  ['PA','Pará'],['PB','Paraíba'],['PE','Pernambuco'],['PI','Piauí'],['PR','Paraná'],['RJ','Rio de Janeiro'],['RN','Rio Grande do Norte'],
  ['RO','Rondônia'],['RR','Roraima'],['RS','Rio Grande do Sul'],['SC','Santa Catarina'],['SE','Sergipe'],['SP','São Paulo'],['TO','Tocantins']
];

@Component({
  selector: 'nt-configuracoes-fiscais-empresa',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './configuracoes-fiscais-empresa.html',
  styleUrl: './configuracoes-fiscais-empresa.scss'
})
export class ConfiguracoesFiscaisEmpresaComponent implements OnChanges {
  @Input({ required: true }) secao = 'FISCAL_GERAL';
  @Input() cnpjEmpresa = '';
  private readonly api = inject(ConfiguracaoComplementarEmpresaService);
  private readonly segura = inject(ConfiguracoesSegurasService);
  private readonly mensagens = inject(MessageService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly util = inject(UtilService);
  dados: Record<string, any> = {};
  original = '';
  versao: number | null = null;
  carregando = true;
  salvando = false;
  erro = '';
  abaGeral: 'EMISSAO'|'EMITENTE' = 'EMISSAO';
  abaTributo: Record<string, string> = { FISCAL_NFE: 'Geral', FISCAL_NFCE: 'Geral', FISCAL_NFSE: 'Geral' };
  readonly estados = ESTADOS;
  certificados: CertificadoProtegido[] = [];
  carregandoCertificados = false;
  modalCertificado = false;
  arquivoCertificado?: File;
  senhaCertificado = '';
  emailCertificado = '';
  erroCertificado = '';
  salvandoCertificado = false;
  contatos: ContatoFiscal[] = [];
  autorizacoes: AutorizacaoFiscal[] = [];
  series: SerieFiscal[] = [];
  autorizacaoNova: AutorizacaoFiscal = { documento: '', email: '' };
  serieNova: SerieFiscal = { serie: '', numeroInicial: 1, principal: false };
  dialogoAutorizacao = false;
  senhaPrefeitura = '';
  prefeituraTemSenha = false;
  preenchendoCnpj = false;
  cscNfce = '';
  tokenNfce = '';
  nfceTemCsc = false;
  nfceTemToken = false;

  readonly abasTributos = ['Geral','ICMS','IPI','PIS','COFINS','Retenção','IBS/CBS'];
  readonly camposTributo: Record<string, CampoFiscal[]> = {
    Geral: [{chave:'codigoBeneficioFiscal',titulo:'Código de benefício fiscal padrão'}],
    ICMS: [{chave:'cstIcms',titulo:'CST ICMS',tipo:'selecao',opcoes:['00','10','20','30','40','41','50','51','60','70','90']},{chave:'aliquotaIcms',titulo:'Alíquota ICMS (%)',tipo:'numero'}],
    IPI: [{chave:'cstIpi',titulo:'CST IPI',tipo:'selecao',opcoes:['00','01','02','03','04','05','49','50','51','52','53','54','55','99']},{chave:'aliquotaIpi',titulo:'Alíquota IPI (%)',tipo:'numero'},{chave:'codigoEnquadramentoIpi',titulo:'Cód. enquadramento'}],
    PIS: [{chave:'cstPis',titulo:'CST PIS',tipo:'selecao',opcoes:['01','02','03','04','05','06','07','08','09','49','50','99']},{chave:'aliquotaPis',titulo:'Alíquota PIS (%)',tipo:'numero'}],
    COFINS: [{chave:'cstCofins',titulo:'CST COFINS',tipo:'selecao',opcoes:['01','02','03','04','05','06','07','08','09','49','50','99']},{chave:'aliquotaCofins',titulo:'Alíquota COFINS (%)',tipo:'numero'}],
    Retenção: [{chave:'aliquotaCsll',titulo:'Alíquota do CSLL (%)',tipo:'numero'},{chave:'aliquotaIrrf',titulo:'Alíquota do IRRF (%)',tipo:'numero'}],
    'IBS/CBS': [{chave:'cstIbsCbs',titulo:'CST IBS/CBS',tipo:'selecao',opcoes:['000','010','011','200','210','220','400','410','510','550','800','810','820','830','900']},{chave:'classificacaoTributaria',titulo:'Classificação tributária'},{chave:'aliquotaIbsUf',titulo:'Alíquota IBS UF (%)',tipo:'numero'},{chave:'aliquotaIbsMunicipal',titulo:'Alíquota IBS municipal (%)',tipo:'numero'},{chave:'aliquotaCbs',titulo:'Alíquota CBS (%)',tipo:'numero'},{chave:'diferimentoIbsUf',titulo:'Percentual do Diferimento UF IBS (%)',tipo:'numero'},{chave:'diferimentoIbsMunicipal',titulo:'Percentual do Diferimento Municipal IBS (%)',tipo:'numero'},{chave:'diferimentoCbs',titulo:'Percentual do Diferimento CBS (%)',tipo:'numero'},{chave:'devolvidoIbsUf',titulo:'Percentual Devolvido UF IBS (%)',tipo:'numero'},{chave:'devolvidoIbsMunicipal',titulo:'Percentual Devolvido Municipal IBS (%)',tipo:'numero'},{chave:'devolvidoCbs',titulo:'Percentual Devolvido CBS (%)',tipo:'numero'}]
  };

  ngOnChanges(mudancas: SimpleChanges): void {
    if (mudancas['secao']) {
      this.carregar();
      if (this.secao === 'FISCAL_GERAL') this.carregarCertificados();
      if (this.secao === 'FISCAL_NFSE') this.segura.obterSenhaPrefeituraNfse().pipe(take(1)).subscribe({next: resposta => this.prefeituraTemSenha = resposta.temSenha, error: () => this.prefeituraTemSenha = false});
      if (this.secao === 'FISCAL_NFCE') this.segura.obterCredenciaisNfce().pipe(take(1)).subscribe({next: resposta => {this.nfceTemCsc=resposta.temCsc;this.nfceTemToken=resposta.temToken;},error:()=>{this.nfceTemCsc=false;this.nfceTemToken=false;}});
    }
  }
  get alterado(): boolean { return this.serializar(this.dados) !== this.original || !!this.senhaPrefeitura.trim() || !!this.cscNfce.trim() || !!this.tokenNfce.trim(); }
  get titulo(): string { return ({FISCAL_GERAL:'Geral',FISCAL_NFE:'NF-e',FISCAL_NFCE:'NFC-e',FISCAL_NFSE:'NFS-e',FISCAL_FCP:'Alíquota FCP',FISCAL_SINTEGRA:'Sintegra',FISCAL_SPED:'SPED'} as Record<string,string>)[this.secao] || 'Fiscal'; }
  get descricao(): string { return ({FISCAL_GERAL:'Ambiente, regime tributário, tipos de nota habilitados, certificado digital e dados do emitente.',FISCAL_NFE:'Configuração, tributos padrão e série da Nota Fiscal Eletrônica (modelo 55).',FISCAL_NFCE:'Configuração, tributos padrão e série da Nota Fiscal de Consumidor Eletrônica (modelo 65).',FISCAL_NFSE:'Configuração, tributos padrão e RPS da Nota Fiscal de Serviço Eletrônica.',FISCAL_FCP:'Percentual do Fundo de Combate à Pobreza por estado de destino.',FISCAL_SINTEGRA:'Dados da empresa usados na geração do arquivo Sintegra (Convênio ICMS 57/95).',FISCAL_SPED:'Contador responsável e regime de PIS/COFINS usados na geração dos arquivos do SPED.'} as Record<string,string>)[this.secao] || ''; }
  private carregar(): void {
    this.carregando = true;
    this.api.listar().pipe(take(1), finalize(() => { this.carregando = false; this.cdr.markForCheck(); })).subscribe({
      next: lista => {
        const salvo = lista.find(item => item.secao === this.secao);
        this.dados = { ...structuredClone(PADROES_FISCAIS[this.secao] || {}), ...(salvo?.dados || {}) };
        this.versao = salvo?.versao ?? null;
        this.original = this.serializar(this.dados);
        this.contatos = this.dados['contatos'] || [];
        this.autorizacoes = this.dados['autorizacoes'] || [];
        this.series = this.dados['series'] || [];
      },
      error: falha => { this.erro = falha?.error?.message || falha?.message || 'Não foi possível carregar a configuração fiscal.'; }
    });
  }
  salvar(): void {
    if (!this.alterado || this.salvando) return;
    if (this.secao === 'FISCAL_NFCE' && ((!this.nfceTemCsc && !this.cscNfce.trim()) || (!this.nfceTemToken && !this.tokenNfce.trim()) || (!!this.cscNfce.trim() !== !!this.tokenNfce.trim()))) {
      this.mensagens.add({severity:'error',summary:'Credenciais NFC-e obrigatórias',detail:'Informe CSC e token CLD juntos para salvar a configuração.'}); return;
    }
    if (this.secao === 'FISCAL_GERAL') { this.dados['contatos'] = this.contatos; this.dados['autorizacoes'] = this.autorizacoes; }
    if (['FISCAL_NFE','FISCAL_NFCE','FISCAL_NFSE'].includes(this.secao)) this.dados['series'] = this.series;
    const dadosSalvar = { ...this.dados };
    delete dadosSalvar['codigoSegurancaCsc']; delete dadosSalvar['tokenCld']; delete dadosSalvar['senhaPrefeitura'];
    this.salvando = true;
    this.api.salvar(this.secao, dadosSalvar, this.versao).pipe(take(1)).subscribe({
      next: salvo => {
        this.dados = { ...salvo.dados }; this.versao = salvo.versao; this.original = this.serializar(this.dados);
        if (this.secao === 'FISCAL_NFSE' && this.senhaPrefeitura.trim()) {
          this.segura.salvarSenhaPrefeituraNfse(this.senhaPrefeitura).pipe(take(1), finalize(() => this.salvando = false)).subscribe({
            next: () => { this.prefeituraTemSenha = true; this.senhaPrefeitura = ''; this.mensagens.add({severity:'success',summary:'Alteração de configuração',detail:'Configuração alterada com sucesso!',life:5000}); },
            error: falha => this.mensagens.add({severity:'error',summary:'Erro ao proteger a senha da prefeitura',detail:falha?.error?.message || falha?.message || 'Não foi possível proteger a senha.',life:9000})
          });
        } else if (this.secao === 'FISCAL_NFCE' && (this.cscNfce.trim() || this.tokenNfce.trim())) {
          this.segura.salvarCredenciaisNfce(this.cscNfce,this.tokenNfce).pipe(take(1),finalize(()=>this.salvando=false)).subscribe({next:()=>{this.nfceTemCsc=true;this.nfceTemToken=true;this.cscNfce='';this.tokenNfce='';this.mensagens.add({severity:'success',summary:'Alteração de configuração',detail:'Configuração alterada com sucesso!',life:5000});},error:falha=>this.mensagens.add({severity:'error',summary:'Erro ao proteger credenciais NFC-e',detail:falha?.error?.message||falha?.message||'Não foi possível proteger as credenciais.',life:9000})});
        } else { this.salvando = false; this.mensagens.add({severity:'success',summary:'Alteração de configuração',detail:'Configuração alterada com sucesso!',life:5000}); }
      },
      error: falha => { this.salvando = false; this.mensagens.add({severity:'error',summary:'Erro ao salvar configuração fiscal',detail:falha?.error?.message || falha?.message || 'Não foi possível salvar.',life:9000}); }
    });
  }
  cancelar(): void { this.dados = JSON.parse(this.original || '{}'); this.contatos = this.dados['contatos'] || []; this.autorizacoes = this.dados['autorizacoes'] || []; this.series = this.dados['series'] || []; this.senhaPrefeitura = ''; this.cscNfce='';this.tokenNfce=''; }
  campo(chave: string, titulo: string, tipo: CampoFiscal['tipo']='texto', opcoes?: string[]): CampoFiscal { return {chave,titulo,tipo,opcoes}; }
  valor(chave: string): any { return this.dados[chave]; }
  alternar(chave: string): void { this.dados[chave] = !this.dados[chave]; }
  camposDaAba(): CampoFiscal[] { return this.camposTributo[this.abaTributo[this.secao] || 'Geral'] || []; }
  abrirAutorizacao(): void { this.autorizacaoNova = { documento:'',email:'' }; this.dialogoAutorizacao = true; }
  adicionarAutorizacao(): void { if (!this.autorizacaoNova.documento.trim() || !this.autorizacaoNova.email.trim()) return; this.autorizacoes = [...this.autorizacoes, {...this.autorizacaoNova}]; this.dados['autorizacoes'] = this.autorizacoes; this.dialogoAutorizacao = false; }
  removerAutorizacao(indice: number): void { this.autorizacoes.splice(indice,1); this.dados['autorizacoes'] = this.autorizacoes; }
  adicionarSerie(): void { if (!this.serieNova.serie.trim()) return; const nova = {...this.serieNova}; if (!this.series.length) nova.principal = true; if (nova.principal) this.series = this.series.map(item => ({...item,principal:false})); this.series = [...this.series,nova]; this.dados['series'] = this.series; this.serieNova = {serie:'',numeroInicial:1,principal:false}; }
  principalSerie(indice: number): void { this.series = this.series.map((item,posicao) => ({...item,principal:posicao===indice})); this.dados['series'] = this.series; }
  removerSerie(indice: number): void { this.series.splice(indice,1); if (this.series.length && !this.series.some(item=>item.principal)) this.series[0].principal=true; this.dados['series'] = this.series; }
  adicionarContato(): void { this.contatos = [...this.contatos,{tipo:'E-mail',valor:''}]; this.dados['contatos'] = this.contatos; }
  removerContato(indice:number):void { this.contatos.splice(indice,1); this.dados['contatos']=this.contatos; }
  abrirCertificado(): void { this.modalCertificado=true; this.arquivoCertificado=undefined; this.senhaCertificado=''; this.emailCertificado=''; this.erroCertificado=''; }
  selecionarCertificado(evento:Event):void { const input=evento.target as HTMLInputElement; this.arquivoCertificado=input.files?.[0]; if(this.arquivoCertificado && !/\.(pfx|p12)$/i.test(this.arquivoCertificado.name)) this.erroCertificado='Selecione um arquivo .pfx ou .p12.'; else this.erroCertificado=''; }
  salvarCertificado():void {
    if(!this.arquivoCertificado||!this.senhaCertificado){this.erroCertificado='Selecione o certificado A1 e informe a senha.';return;}
    if(this.arquivoCertificado.size>5242880){this.erroCertificado='O arquivo deve ter até 5 MB.';return;}
    const dados=new FormData();dados.append('nomeArquivo',this.arquivoCertificado.name);dados.append('email',this.emailCertificado.trim());dados.append('senha',this.senhaCertificado);dados.append('arquivo',this.arquivoCertificado);
    this.salvandoCertificado=true;this.segura.criarCertificado(dados).pipe(take(1),finalize(()=>this.salvandoCertificado=false)).subscribe({next:()=>{this.modalCertificado=false;this.carregarCertificados();this.mensagens.add({severity:'success',summary:'Certificado enviado',detail:'Certificado salvo com proteção de dados.'});},error:falha=>{this.erroCertificado=falha?.error?.message||falha?.message||'Não foi possível salvar o certificado.';}});
  }
  carregarCertificados():void { this.carregandoCertificados=true; this.segura.listarCertificados().pipe(take(1),finalize(()=>this.carregandoCertificados=false)).subscribe({next:lista=>this.certificados=lista||[],error:()=>this.certificados=[]}); }
  preencherDadosCnpj(): void {
    const cnpj = (this.cnpjEmpresa || '').replace(/\D/g, '');
    if (cnpj.length !== 14) { this.mensagens.add({severity:'warn',summary:'CNPJ inválido',detail:'Cadastre um CNPJ válido nos dados da empresa antes de preencher.'}); return; }
    this.preenchendoCnpj = true;
    this.util.buscarCnpj(cnpj).pipe(take(1),finalize(()=>this.preenchendoCnpj=false)).subscribe({
      next: (resposta: any) => {
        this.dados['inscricaoEstadual'] ||= resposta.inscricoes_estaduais?.[0]?.inscricao_estadual || '';
        this.dados['cnae'] ||= resposta.cnae_fiscal ? String(resposta.cnae_fiscal) : '';
        this.dados['cep'] ||= resposta.cep || '';
        this.dados['estado'] ||= resposta.uf || '';
        this.dados['cidade'] ||= resposta.municipio || '';
        this.dados['bairro'] ||= resposta.bairro || '';
        this.dados['logradouro'] ||= resposta.logradouro || '';
        this.dados['numero'] ||= resposta.numero || '';
        this.dados['complemento'] ||= resposta.complemento || '';
        this.mensagens.add({severity:'success',summary:'CNPJ localizado',detail:'Dados fiscais e endereço foram preenchidos nos campos vazios.'});
      },
      error: falha => this.mensagens.add({severity:'error',summary:'Erro ao consultar CNPJ',detail:falha?.error?.message || falha?.message || 'Não foi possível consultar os dados do CNPJ.',life:9000})
    });
  }
  solicitarUpgrade(): void { this.mensagens.add({severity:'info',summary:'Upgrade de plano',detail:'O SPED está disponível no plano Ultra. Consulte as opções de plano com a equipe comercial.'}); }
  mensagemSeries(): string { return this.secao==='FISCAL_NFSE'?'O RPS (Recibo Provisório de Serviços) é numerado por série. Cadastre ao menos uma série liberada pela prefeitura para emitir NFS-e.':`Cadastre a série e o número inicial para emissão de ${this.secao==='FISCAL_NFE'?'NF-e':'NFC-e'}.`; }
  nomeSerie(): string { return this.secao==='FISCAL_NFE'?'Séries NF-e':this.secao==='FISCAL_NFCE'?'Séries NFC-e':'Séries NFS-e (RPS)'; }
  private serializar(valor: unknown): string {
    const ordenar = (item: any): any => Array.isArray(item) ? item.map(ordenar) : item && typeof item === 'object' ? Object.keys(item).sort().reduce((saida: Record<string,unknown>,chave) => { saida[chave]=ordenar(item[chave]); return saida; },{}) : item;
    return JSON.stringify(ordenar(valor));
  }
}
