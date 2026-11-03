import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { forkJoin, take } from 'rxjs';
import { AuthService } from '@core';
import { environment } from '@env/environment';
import { UsuarioResponse } from '../../configuracoes/permissoes/usuario.models';
import { MinhaContaService } from '../minha-conta.service';
import { Chave, Politica, PreferenciasService } from '../../configuracoes/preferencias/preferencias.service';
import { AlteracaoItemConfiguracao, GrupoItemConfiguracao, ItemConfiguracao, ItensConfiguracaoService } from '../../configuracoes/preferencias/itens-configuracao.service';
import { CertificadoProtegido, ConfiguracaoSmtp, ConfiguracoesSegurasService } from './configuracoes-seguras.service';

@Component({ selector: 'app-profile-overview', templateUrl: './overview.html', styleUrl: './overview.scss', imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink] })
export class ProfileOverview implements OnInit {
  @Input() modal = false;
  @Input() abaInicial: 'perfil' | 'matriz' = 'perfil';
  @Output() fecharConta = new EventEmitter<void>();
  private readonly construtor = inject(FormBuilder);
  private readonly servico = inject(MinhaContaService);
  private readonly roteador = inject(Router);
  private readonly autenticacao = inject(AuthService);
  private readonly preferencias = inject(PreferenciasService);
  private readonly itensConfiguracao = inject(ItensConfiguracaoService);
  private readonly configuracoesSeguras = inject(ConfiguracoesSegurasService);
  readonly preVisualizacao = environment.uxPreview;
  usuario?: UsuarioResponse;
  secao: 'geral' | 'email' | 'vinculos' = 'geral';
  aba: 'perfil' | 'matriz' = 'perfil';
  secaoMatriz = 'configGeral';
  grupoMatriz = 'geral';
  politicasMatriz: Partial<Record<Chave, Politica>> = {};
  valoresMatriz: Partial<Record<Chave, string>> = {};
  carregandoMatriz = false;
  salvandoMatriz = false;
  erroMatriz = '';
  sucessoMatriz = '';
  itensMatriz: ItemConfiguracao[] = [];
  buscaItensMatriz = '';
  carregandoItensMatriz = false;
  modalItemMatriz = false;
  salvandoItemMatriz = false;
  descricaoItemMatriz = '';
  corItemMatriz = '#899198';
  itemEditandoMatriz?: ItemConfiguracao;
  erroItemMatriz = '';
  readonly formularioSmtp = this.construtor.nonNullable.group({ email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]], senha: [''], servidorSmtp: ['', [Validators.required, Validators.maxLength(255)]], portaSmtp: [587, [Validators.required, Validators.min(1), Validators.max(65535)]], usarSsl: [false], usarTls: [false], assinatura: [''] });
  escopoSmtp: 'MATRIZ' | 'CRM' = 'MATRIZ';
  temSenhaSmtp = false;
  carregandoSmtp = false;
  salvandoSmtp = false;
  verificandoSmtp = false;
  erroSmtp = '';
  sucessoSmtp = '';
  certificadosProtegidos: CertificadoProtegido[] = [];
  carregandoCertificados = false;
  modalCertificado = false;
  nomeCertificado = '';
  emailCertificado = '';
  senhaCertificado = '';
  arquivoCertificado?: File;
  erroCertificado = '';
  salvandoCertificado = false;
  readonly coresItensMatriz = ['#2D2D2D','#899198','#FF4D55','#E83E7A','#BA42D2','#7544E8','#4367E8','#2585DA','#16A7B9','#12B886','#3BB956','#7EC51D','#F3A900','#FF7A00','#FFFFFF00'];
  readonly camposCompraMatriz = [
    { chave: 'alterarPrecoCompra' as Chave, rotulo: 'Alterar o preço do produto ao autorizar uma compra', tipo: 'opcao' },
    { chave: 'enviarCustoCompraOs' as Chave, rotulo: 'Enviar o preço de custo dos itens da OS ao vincular na compra', tipo: 'booleano' },
    { chave: 'atualizarFabricanteCompra' as Chave, rotulo: 'Atualizar o fabricante do item da ordem de serviço conforme o item da compra vinculada', tipo: 'booleano' },
    { chave: 'atualizarOrigemCompra' as Chave, rotulo: 'Atualizar automaticamente o campo origem no cadastro da peça ao autorizar compra', tipo: 'booleano' },
    { chave: 'atualizarNcmImportacao' as Chave, rotulo: 'Atualiza NCM na importação de XML de compra', tipo: 'booleano' },
  ];
  readonly camposFinanceiroMatriz = [
    { chave: 'liquidarBoletoNoVencimento' as Chave, rotulo: 'Parcelas serão liquidadas apenas se os boletos forem pagos antes da data de vencimento.', tipo: 'booleano' },
    { chave: 'liquidacaoAutomaticaBoleto' as Chave, rotulo: 'Habilitar liquidação automática de parcelas após pagamento do boleto', tipo: 'booleano' },
    { chave: 'orientacaoImpressaoBoleto' as Chave, rotulo: 'Orientação da impressão', tipo: 'opcao' },
  ];
  readonly camposVendaMatriz = [{ chave: 'estoqueTodasEmpresasVenda' as Chave, rotulo: 'Visualizar o estoque de todas as empresas na seleção de peça', tipo: 'booleano' }];
  readonly camposCrmMatriz = [
    { chave: 'calculoRevisaoCrm' as Chave, rotulo: 'Cálculo para envio de próximas revisões será feito por', tipo: 'opcao' },
    { chave: 'nomeRemetenteCrm' as Chave, rotulo: 'Nome do remetente', tipo: 'texto' },
    { chave: 'emailRespostaCrm' as Chave, rotulo: 'E-mail de resposta', tipo: 'texto' },
    { chave: 'crmAniversario' as Chave, rotulo: 'Aniversário', tipo: 'booleano' },
    { chave: 'crmServicosFuturos' as Chave, rotulo: 'Serviços futuros', tipo: 'booleano' },
    { chave: 'crmProximasRevisoes' as Chave, rotulo: 'Próximas revisões', tipo: 'booleano' },
    { chave: 'crmClientesSemMovimento' as Chave, rotulo: 'Clientes sem movimento', tipo: 'booleano' },
    { chave: 'crmPesquisaSatisfacao' as Chave, rotulo: 'Pesquisa de satisfação', tipo: 'booleano' },
    { chave: 'crmCobrancaDividas' as Chave, rotulo: 'Cobrança de dívidas', tipo: 'booleano' },
    { chave: 'crmViaSmtp' as Chave, rotulo: 'Enviar e-mails de CRM via SMTP', tipo: 'booleano' },
  ];
  readonly camposRelatorioMatriz = [
    { chave: 'relatorioQuadroAnotacoes' as Chave, rotulo: 'Exibir quadro de anotações em relatório de oficina', tipo: 'booleano' },
    { chave: 'relatorioSepararAreas' as Chave, rotulo: 'Separar itens da listagem de serviços e peças do relatório administrativo por área', tipo: 'booleano' },
    { chave: 'relatorioDadosCliente' as Chave, rotulo: 'Exibir informações do cliente em relatório de oficina', tipo: 'booleano' },
    { chave: 'ordenacaoRelatorio' as Chave, rotulo: 'Ordenar relatórios administrativos por', tipo: 'opcao' },
    { chave: 'baseCalculoComissao' as Chave, rotulo: 'Selecione o valor a ser utilizado no cálculo da comissão', tipo: 'opcao' },
  ];
  readonly camposFiscalMatriz = [{ chave: 'notaFiscalOutraEmpresa' as Chave, rotulo: 'Permitir gerar nota fiscal para empresa diferente', tipo: 'booleano' }];
  readonly chavesCompraMatriz: Chave[] = this.camposCompraMatriz.map(campo => campo.chave);
  readonly chavesCrmMatriz: Chave[] = this.camposCrmMatriz.map(campo => campo.chave);
  readonly chavesRelatorioMatriz: Chave[] = this.camposRelatorioMatriz.map(campo => campo.chave);
  telaCheia = false;
  carregando = false;
  erro = '';
  sucesso = '';
  readonly formularioEmail = this.construtor.nonNullable.group({ email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]] });
  readonly formularioSenha = this.construtor.nonNullable.group({ senhaAtual: ['', [Validators.required, Validators.minLength(12)]], novaSenha: ['', [Validators.required, Validators.minLength(12), Validators.maxLength(128)]], confirmarSenha: ['', Validators.required] });
  visibilidadeSenhas = { atual: false, nova: false, confirmacao: false };
  readonly opcoesCategoriaMatriz: { chave: Chave; rotulo: string }[] = [
    { chave: 'categoriaEmContasReceber', rotulo: 'Exibir categoria do cliente em contas a receber?' },
    { chave: 'categoriaEmVendaDireta', rotulo: 'Exibir categoria do cliente na venda direta?' },
    { chave: 'categoriaEmNotaFiscal', rotulo: 'Exibir categoria do cliente na nota fiscal?' },
    { chave: 'categoriaEmOrdemServico', rotulo: 'Exibir categoria do cliente na ordem de serviço?' },
    { chave: 'categoriaEmListagem', rotulo: 'Exibir categoria do cliente na listagem de cliente?' },
  ];

  ngOnInit(): void { this.aba = this.abaInicial; this.carregar(); if (this.aba === 'matriz') this.carregarPoliticasMatriz(); }
  selecionarAba(aba: 'perfil' | 'matriz'): void { this.aba = aba; if (aba === 'matriz' && !Object.keys(this.politicasMatriz).length) this.carregarPoliticasMatriz(); }
  carregar(): void {
    this.carregando = true; this.erro = '';
    this.servico.obterMinhaConta().pipe(take(1)).subscribe({
      next: usuario => { this.usuario = usuario; this.formularioEmail.patchValue({ email: usuario.email }); this.carregando = false; },
      error: falha => { console.error('Falha ao carregar a própria conta.', { status: falha?.status }); this.erro = 'Não foi possível carregar sua conta. Atualize a tela e tente novamente.'; this.carregando = false; },
    });
  }
  salvarEmail(): void {
    this.sucesso = ''; this.erro = '';
    if (this.formularioEmail.invalid) { this.formularioEmail.markAllAsTouched(); return; }
    const dados = this.formularioEmail.getRawValue();
    const senhaAtual = this.formularioSenha.controls.senhaAtual.value;
    if (!senhaAtual) { this.erro = 'Informe a senha atual em Credenciais de acesso para confirmar a alteração do e-mail.'; this.formularioSenha.controls.senhaAtual.markAsTouched(); return; }
    this.carregando = true;
    this.servico.atualizarEmail(dados.email, senhaAtual).pipe(take(1)).subscribe({
      next: usuario => { this.usuario = usuario; this.carregando = false; this.sucesso = 'E-mail atualizado. Entre novamente usando o novo endereço.'; this.autenticacao.logout().pipe(take(1)).subscribe({ next: () => void this.roteador.navigateByUrl('/auth/login'), error: () => void this.roteador.navigateByUrl('/auth/login') }); },
      error: falha => { console.error('Falha ao salvar o e-mail da própria conta.', { status: falha?.status }); this.carregando = false; this.erro = falha?.status === 409 ? 'Este e-mail já está cadastrado nesta empresa.' : 'Não foi possível atualizar o e-mail. Confira sua senha atual.'; },
    });
  }
  alternarVisibilidadeSenha(campo: 'atual' | 'nova' | 'confirmacao'): void { this.visibilidadeSenhas[campo] = !this.visibilidadeSenhas[campo]; }
  selecionarSecaoMatriz(secao: string, grupo: string): void { this.secaoMatriz = secao; this.grupoMatriz = grupo; this.sucessoMatriz = ''; if (!Object.keys(this.politicasMatriz).length) this.carregarPoliticasMatriz(); if (['categorias','etiquetasFinanceiro','etiquetasVenda'].includes(secao)) this.carregarItensMatriz(); if (secao === 'configEmail' || secao === 'crmEmail') this.carregarSmtp(secao === 'crmEmail' ? 'CRM' : 'MATRIZ'); if (secao === 'certificados') this.carregarCertificados(); }
  carregarSmtp(escopo: 'MATRIZ'|'CRM'): void { this.escopoSmtp=escopo; this.carregandoSmtp=true; this.erroSmtp=''; this.configuracoesSeguras.obterSmtp(escopo).pipe(take(1)).subscribe({next:dados=>{this.aplicarSmtp(dados);this.carregandoSmtp=false;},error:falha=>{console.error('Falha ao carregar configuração SMTP.',{status:falha?.status});this.erroSmtp=falha?.status===403?'Você não tem permissão para consultar a configuração de e-mail.':'Não foi possível carregar a configuração de e-mail desta empresa.';this.carregandoSmtp=false;}}); }
  private aplicarSmtp(dados: ConfiguracaoSmtp): void { this.temSenhaSmtp=dados.temSenha; this.formularioSmtp.patchValue({email:dados.email,servidorSmtp:dados.servidorSmtp,portaSmtp:dados.portaSmtp,usarSsl:dados.usarSsl,usarTls:dados.usarTls,assinatura:dados.assinatura,senha:''}); }
  salvarSmtp(): void { this.erroSmtp='';this.sucessoSmtp='';if(this.formularioSmtp.invalid){this.formularioSmtp.markAllAsTouched();return;}const dados=this.formularioSmtp.getRawValue();if(dados.usarSsl&&dados.usarTls){this.erroSmtp='Escolha SSL ou TLS, não ambos.';return;}if(!this.temSenhaSmtp&&!dados.senha){this.erroSmtp='Informe a senha do servidor SMTP.';return;}this.salvandoSmtp=true;this.configuracoesSeguras.salvarSmtp(this.escopoSmtp,dados).pipe(take(1)).subscribe({next:resposta=>{this.aplicarSmtp(resposta);this.sucessoSmtp='Configuração de e-mail salva.';this.salvandoSmtp=false;},error:falha=>{console.error('Falha ao salvar configuração SMTP.',{status:falha?.status});this.erroSmtp=falha?.status===403?'Você não tem permissão para alterar esta configuração.':'Não foi possível salvar. Confira os campos e tente novamente.';this.salvandoSmtp=false;}}); }
  verificarSmtp(): void { this.erroSmtp='';this.sucessoSmtp='';const dados=this.formularioSmtp.getRawValue();if(!dados.email||!dados.servidorSmtp||!dados.portaSmtp||(!this.temSenhaSmtp&&!dados.senha)){this.erroSmtp='Preencha e-mail, senha, SMTP e porta para verificar.';this.formularioSmtp.markAllAsTouched();return;}if(dados.usarSsl&&dados.usarTls){this.erroSmtp='Escolha SSL ou TLS, não ambos.';return;}this.verificandoSmtp=true;this.configuracoesSeguras.verificarSmtp(this.escopoSmtp,dados).pipe(take(1)).subscribe({next:()=>{this.sucessoSmtp='E-mail de verificação enviado. Confira a caixa de entrada.';this.verificandoSmtp=false;},error:falha=>{console.error('Falha na verificação SMTP.',{status:falha?.status});this.erroSmtp=falha?.status===502?'Não foi possível autenticar no servidor SMTP. Revise servidor, porta e credenciais.':'Salve a configuração antes de verificar o e-mail.';this.verificandoSmtp=false;}}); }
  formatarAssinatura(formato: 'b'|'i'|'u'): void { const campo=document.querySelector<HTMLTextAreaElement>('.editor-assinatura textarea');if(!campo)return;const inicio=campo.selectionStart,fim=campo.selectionEnd,valor=campo.value,selecionado=valor.slice(inicio,fim)||'texto';const novo=`${valor.slice(0,inicio)}<${formato}>${selecionado}</${formato}>${valor.slice(fim)}`;this.formularioSmtp.controls.assinatura.setValue(novo);campo.focus();campo.setSelectionRange(inicio+formato.length+2,inicio+formato.length+2+selecionado.length);}
  carregarCertificados(): void {this.carregandoCertificados=true;this.erroCertificado='';this.configuracoesSeguras.listarCertificados().pipe(take(1)).subscribe({next:lista=>{this.certificadosProtegidos=lista;this.carregandoCertificados=false;},error:falha=>{console.error('Falha ao listar certificados digitais.',{status:falha?.status});this.erroCertificado=falha?.status===403?'Você não tem permissão para consultar certificados.':'Não foi possível carregar os certificados desta empresa.';this.carregandoCertificados=false;}});}
  abrirCertificado(): void {this.nomeCertificado='';this.emailCertificado='';this.senhaCertificado='';this.arquivoCertificado=undefined;this.erroCertificado='';this.modalCertificado=true;}
  selecionarArquivoCertificado(evento: Event): void {const entrada=evento.target as HTMLInputElement;this.arquivoCertificado=entrada.files?.[0];if(this.arquivoCertificado){this.nomeCertificado=this.arquivoCertificado.name;if(!/\.(pfx|p12)$/i.test(this.arquivoCertificado.name))this.erroCertificado='Selecione um arquivo .pfx ou .p12.';else this.erroCertificado='';}}
  salvarCertificado(): void {if(!this.nomeCertificado.trim()||!this.senhaCertificado||!this.arquivoCertificado){this.erroCertificado='Informe o arquivo A1, a senha e o nome do certificado.';return;}if(this.arquivoCertificado.size>5242880){this.erroCertificado='O arquivo deve ter até 5 MB.';return;}const dados=new FormData();dados.append('nomeArquivo',this.nomeCertificado.trim());dados.append('email',this.emailCertificado.trim());dados.append('senha',this.senhaCertificado);dados.append('arquivo',this.arquivoCertificado);this.salvandoCertificado=true;this.erroCertificado='';this.configuracoesSeguras.criarCertificado(dados).pipe(take(1)).subscribe({next:()=>{this.modalCertificado=false;this.salvandoCertificado=false;this.carregarCertificados();},error:falha=>{console.error('Falha ao cadastrar certificado A1.',{status:falha?.status});this.erroCertificado=falha?.status===422?'Arquivo ou senha inválidos. Selecione um certificado A1 válido.':'Não foi possível cadastrar o certificado.';this.salvandoCertificado=false;}});}
  carregarPoliticasMatriz(): void {
    this.carregandoMatriz = true; this.erroMatriz = '';
    this.preferencias.listar('', '', undefined, 0, 100).pipe(take(1)).subscribe({
      next: resposta => { this.politicasMatriz = Object.fromEntries(resposta.content.map(p => [p.chave, p])) as Partial<Record<Chave, Politica>>; this.valoresMatriz = Object.fromEntries(resposta.content.map(p => [p.chave, p.valorAplicado])) as Partial<Record<Chave, string>>; this.carregandoMatriz = false; },
      error: falha => { console.error('Falha ao carregar configurações da matriz.', { status: falha?.status }); this.erroMatriz = 'Não foi possível carregar as configurações autorizadas desta empresa.'; this.carregandoMatriz = false; },
    });
  }
  salvarPoliticasMatriz(chaves: Chave[]): void {
    const alteradas = chaves.filter(chave => this.valoresMatriz[chave] !== undefined && this.valoresMatriz[chave] !== (this.politicasMatriz[chave]?.valorAplicado ?? ''));
    if (!alteradas.length || this.salvandoMatriz) return;
    this.salvandoMatriz = true; this.erroMatriz = ''; this.sucessoMatriz = '';
    const requisicoes = alteradas.map(chave => {
      const valor = this.valoresMatriz[chave]!;
      const politica = this.politicasMatriz[chave];
      return politica?.versao == null ? this.preferencias.criar(chave, valor) : this.preferencias.editar(chave, valor, politica.versao);
    });
    forkJoin(requisicoes).pipe(take(1)).subscribe({
      next: atualizadas => { for (const politica of atualizadas) { this.politicasMatriz[politica.chave] = politica; this.valoresMatriz[politica.chave] = politica.valorAplicado; } this.sucessoMatriz = 'Configurações salvas.'; this.salvandoMatriz = false; },
      error: falha => { console.error('Falha ao salvar configurações da matriz.', { status: falha?.status }); this.erroMatriz = falha?.status === 403 ? 'Você não tem permissão para alterar as configurações da empresa.' : falha?.status === 409 ? 'As configurações mudaram em outra sessão. Recarregue e tente novamente.' : 'Não foi possível salvar as configurações.'; this.salvandoMatriz = false; },
    });
  }
  atualizarPolitica(chave: Chave, valor: string): void { this.valoresMatriz[chave] = valor; this.sucessoMatriz = ''; }
  valorCategoriaMatriz(chave: string): string { return this.valoresMatriz[chave as Chave] ?? ''; }
  atualizarCategoriaMatriz(chave: string, valor: string): void { this.atualizarPolitica(chave as Chave, valor); }
  algumaPoliticaAlterada(chaves: Chave[]): boolean { return chaves.some(chave => this.valoresMatriz[chave] !== undefined && this.valoresMatriz[chave] !== (this.politicasMatriz[chave]?.valorAplicado ?? '')); }
  cancelarPoliticasMatriz(chaves: Chave[]): void { for (const chave of chaves) this.valoresMatriz[chave] = this.politicasMatriz[chave]?.valorAplicado ?? this.valorPadraoPolitica(chave); this.erroMatriz = ''; this.sucessoMatriz = ''; }
  private valorPadraoPolitica(chave: Chave): string { return ({ codigoPadraoPecas: 'INTERNO', kmObrigatorio: 'false', atualizarKmAutomaticamente: 'true', ordemSelecaoAvaria: 'LOCAL_AVARIA', periodoExpiracaoSenhas: 'NAO_EXPIRA', categoriaEmContasReceber: 'false', categoriaEmVendaDireta: 'false', categoriaEmNotaFiscal: 'false', categoriaEmOrdemServico: 'false', categoriaEmListagem: 'false' } as Partial<Record<Chave, string>>)[chave] ?? ''; }
  valorPolitica(chave: Chave): string { return this.valoresMatriz[chave] ?? this.politicasMatriz[chave]?.valorAplicado ?? this.valorPadraoPolitica(chave); }
  carregarItensMatriz(): void {
    const grupo = this.grupoItemAtual(); if (!grupo) return;
    this.carregandoItensMatriz = true; this.erroItemMatriz = '';
    this.itensConfiguracao.listar(grupo, this.buscaItensMatriz).pipe(take(1)).subscribe({ next: itens => { this.itensMatriz = itens; this.carregandoItensMatriz = false; }, error: falha => { this.itensMatriz = []; this.erroItemMatriz = falha?.status === 403 ? 'Você não tem permissão para consultar esta configuração.' : 'Não foi possível carregar os itens desta empresa.'; this.carregandoItensMatriz = false; } });
  }
  grupoItemAtual(): GrupoItemConfiguracao | null { return this.secaoMatriz === 'categorias' ? 'CLIENTE_CATEGORIA' : this.secaoMatriz === 'etiquetasFinanceiro' ? 'FINANCEIRO_ETIQUETA' : this.secaoMatriz === 'etiquetasVenda' ? 'VENDA_DIRETA_ETIQUETA' : null; }
  abrirItemMatriz(item?: ItemConfiguracao): void { this.itemEditandoMatriz = item; this.descricaoItemMatriz = item?.descricao ?? ''; this.corItemMatriz = item?.cor ?? '#899198'; this.erroItemMatriz = ''; this.modalItemMatriz = true; }
  fecharItemMatriz(): void { if (!this.salvandoItemMatriz) this.modalItemMatriz = false; }
  salvarItemMatriz(): void {
    const grupo = this.grupoItemAtual(); const descricao = this.descricaoItemMatriz.trim();
    if (!grupo || descricao.length < 2 || descricao.length > 100 || this.salvandoItemMatriz) { this.erroItemMatriz = 'Informe uma descrição de 2 a 100 caracteres.'; return; }
    const alteracao: AlteracaoItemConfiguracao = { grupo, descricao, cor: this.corItemMatriz };
    this.salvandoItemMatriz = true; this.erroItemMatriz = '';
    const requisicao = this.itemEditandoMatriz ? this.itensConfiguracao.editar(this.itemEditandoMatriz.id, alteracao) : this.itensConfiguracao.criar(alteracao);
    requisicao.pipe(take(1)).subscribe({ next: () => { this.salvandoItemMatriz = false; this.modalItemMatriz = false; this.carregarItensMatriz(); }, error: falha => { this.salvandoItemMatriz = false; this.erroItemMatriz = falha?.status === 409 ? 'Já existe um item com essa descrição.' : falha?.status === 403 ? 'Você não tem permissão para salvar este item.' : 'Não foi possível salvar o item.'; } });
  }
  excluirItemMatriz(item: ItemConfiguracao): void {
    this.itensConfiguracao.excluir(item.id).pipe(take(1)).subscribe({ next: () => this.carregarItensMatriz(), error: falha => { this.erroItemMatriz = falha?.status === 403 ? 'Você não tem permissão para excluir este item.' : 'Não foi possível excluir o item.'; } });
  }
  camposAtualizados(campos: {chave: Chave}[]): void { this.salvarPoliticasMatriz(campos.map(campo => campo.chave)); }
  atualizarBuscaItensMatriz(valor: string): void { this.buscaItensMatriz = valor; this.carregarItensMatriz(); }
  salvarSenha(): void {
    this.sucesso = ''; this.erro = '';
    if (this.formularioSenha.invalid) { this.formularioSenha.markAllAsTouched(); return; }
    const dados = this.formularioSenha.getRawValue();
    if (dados.novaSenha !== dados.confirmarSenha) { this.erro = 'A confirmação da senha não corresponde.'; return; }
    this.carregando = true;
    this.servico.alterarSenha(dados.senhaAtual, dados.novaSenha).pipe(take(1)).subscribe({
      next: () => { this.formularioSenha.reset(); this.carregando = false; this.sucesso = 'Senha atualizada com sucesso.'; },
      error: falha => { console.error('Falha ao atualizar a senha da própria conta.', { status: falha?.status }); this.carregando = false; this.erro = falha?.status === 422 ? 'Use uma frase-senha de 12 a 128 caracteres, diferente da senha atual.' : 'Não foi possível atualizar a senha. Confira a senha atual.'; },
    });
  }
  fechar(): void { if (this.modal) this.fecharConta.emit(); else void this.roteador.navigateByUrl('/home'); }
  alternarTelaCheia(): void { this.telaCheia = !this.telaCheia; }
}
