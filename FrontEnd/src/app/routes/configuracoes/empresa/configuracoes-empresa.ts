import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs';
import { ItemConfiguracao, ItensConfiguracaoService } from '../preferencias/itens-configuracao.service';
import { ConfiguracaoComplementarEmpresaService } from './services/configuracao-complementar-empresa.service';
import { AreaRecurso, RecursosApi } from '../../produtos-servicos/servicos/recursos-api.service';
import { ConfiguracoesFiscaisEmpresaComponent } from './fiscal/configuracoes-fiscais-empresa';

type SecaoConfig = 'ATENDIMENTO' | 'COMERCIAL' | 'ESTOQUE_FINANCEIRO' | 'INTEGRACOES' | 'GESTAO_PATIO'
  | 'TAGS_ITENS' | 'FERIADOS_LOCAIS' | 'PRODUTO_COMPOSICAO' | 'PRODUTO_CUSTO_LUCRO' | 'COMPRAS'
  | 'AGENDAMENTO' | 'FINANCEIRO' | 'TERMOS' | 'FISCAL_GERAL' | 'FISCAL_NFE' | 'FISCAL_NFCE'
  | 'FISCAL_NFSE' | 'FISCAL_FCP' | 'FISCAL_SINTEGRA' | 'FISCAL_SPED';
interface NavegacaoConfig { secao: SecaoConfig; titulo: string; grupo: string; icone: string; }
interface PeriodoIndisponivel { descricao: string; data: string; recursos: number; todosRecursos: boolean; inicio: string; fim: string; }

const PADROES: Record<string, Record<string, any>> = {
  ATENDIMENTO: { campoKmObrigatorio: false, campoPrismaObrigatorio: false, campoNivelTanqueObrigatorio: false, exibirCamposIndicacaoCorretor: false, vincularObservacoesOrdensAtividades: false, preencherFolhaMecanicoAutomaticamente: false },
  COMERCIAL: { margemReferencia: 0, descontoMaximoPeca: 0, descontoMaximoServico: 0, aplicarAcrescimoOs: false },
  ESTOQUE_FINANCEIRO: { baixaAutomaticaEstoque: true, notificarEstoqueMinimo: false, modoCondicaoPagamento: 'CONDICOES_PAGAMENTO' },
  INTEGRACOES: { valorHoraPintura: 0, areaPintura: '', valorHoraTroca: 0, areaTroca: '', valorHoraReparacao: 0, areaReparacao: '', utilizarChecklistBosch: false, enviarOsMyBcsAutomaticamente: false, receberEventosMyBcs: false },
  GESTAO_PATIO: { criarAtividadeAoGerarOs: false, moverCartaoAoFecharOs: false },
  FERIADOS_LOCAIS: { feriados: [] },
  PRODUTO_COMPOSICAO: { percentualIpi: false, frete: false, custoFixo: false, despesasOperacionais: false, substituicaoTributaria: false, outrasDespesas: false, icmsDesoneracao: false, calcularMargemLucro: false },
  PRODUTO_CUSTO_LUCRO: { percentualCustoFixo: 0, percentualLucroFixo: 0 },
  COMPRAS: { bloquearCfopSaida: false, permitirExcluirItensImportados: false, permitirPrecoVendaNaCompra: false, exigirMotivoCancelamento: false },
  AGENDAMENTO: { indisponibilidades: [] },
  FINANCEIRO: { parcelasLivresVendaDireta: false, dataVendaReferenciaVencimento: false, permitirLiquidoNegativoPagar: false },
  TERMOS: { termoLgpd: '', termoGarantia: '', termoQuitacao: '', termoPecasFornecidas: '', termoResponsabilidade: '', declaracaoPermanencia: '', termoCorteSeloPlaca: '', declaracaoAssinaturaOs: '' }
};

@Component({
  selector: 'nt-configuracoes-empresa',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, ToastModule, ConfiguracoesFiscaisEmpresaComponent],
  templateUrl: './configuracoes-empresa.html',
  styleUrl: './configuracoes-empresa.scss'
})
export class ConfiguracoesEmpresaComponent implements OnInit {
  private readonly api = inject(ConfiguracaoComplementarEmpresaService);
  private readonly itensApi = inject(ItensConfiguracaoService);
  private readonly recursosApi = inject(RecursosApi);
  private readonly mensagens = inject(MessageService);
  private readonly cdr = inject(ChangeDetectorRef);
  @Input() nomeEmpresa = '';
  @Input() cnpjEmpresa = '';
  @Output() fechar = new EventEmitter<void>();

  readonly secoes: NavegacaoConfig[] = [
    { secao: 'ATENDIMENTO', titulo: 'Atendimento', grupo: 'Ordem de serviço', icone: 'pi pi-check-square' },
    { secao: 'COMERCIAL', titulo: 'Comercial', grupo: 'Ordem de serviço', icone: 'pi pi-dollar' },
    { secao: 'ESTOQUE_FINANCEIRO', titulo: 'Estoque e financeiro', grupo: 'Ordem de serviço', icone: 'pi pi-box' },
    { secao: 'INTEGRACOES', titulo: 'Integrações', grupo: 'Ordem de serviço', icone: 'pi pi-link' },
    { secao: 'GESTAO_PATIO', titulo: 'Gestão de pátio', grupo: 'Ordem de serviço', icone: 'pi pi-wrench' },
    { secao: 'TAGS_ITENS', titulo: 'Tags de itens', grupo: 'Ordem de serviço', icone: 'pi pi-tag' },
    { secao: 'FERIADOS_LOCAIS', titulo: 'Feriados locais (GNV)', grupo: 'Ordem de serviço', icone: 'pi pi-calendar-times' },
    { secao: 'PRODUTO_COMPOSICAO', titulo: 'Composição de preço', grupo: 'Produto', icone: 'pi pi-book' },
    { secao: 'PRODUTO_CUSTO_LUCRO', titulo: 'Custo e lucro', grupo: 'Produto', icone: 'pi pi-money-bill' },
    { secao: 'COMPRAS', titulo: 'Config. geral', grupo: 'Compras', icone: 'pi pi-cog' },
    { secao: 'AGENDAMENTO', titulo: 'Configuração', grupo: 'Agendamento', icone: 'pi pi-calendar-clock' },
    { secao: 'FINANCEIRO', titulo: 'Config. geral', grupo: 'Financeiro', icone: 'pi pi-cog' },
    { secao: 'TERMOS', titulo: 'Termos personalizados', grupo: 'Termos', icone: 'pi pi-file' },
    { secao: 'FISCAL_GERAL', titulo: 'Geral', grupo: 'Fiscal', icone: 'pi pi-cog' },
    { secao: 'FISCAL_NFE', titulo: 'NF-e', grupo: 'Fiscal', icone: 'pi pi-file' },
    { secao: 'FISCAL_NFCE', titulo: 'NFC-e', grupo: 'Fiscal', icone: 'pi pi-receipt' },
    { secao: 'FISCAL_NFSE', titulo: 'NFS-e', grupo: 'Fiscal', icone: 'pi pi-wrench' },
    { secao: 'FISCAL_FCP', titulo: 'Alíquota FCP', grupo: 'Fiscal', icone: 'pi pi-map-marker' },
    { secao: 'FISCAL_SINTEGRA', titulo: 'Sintegra', grupo: 'Fiscal', icone: 'pi pi-sync' },
    { secao: 'FISCAL_SPED', titulo: 'SPED', grupo: 'Fiscal', icone: 'pi pi-chart-bar' }
  ];
  readonly cores = ['#2D2D2D', '#899198', '#FF4D55', '#E83E7A', '#BA42D2', '#7544E8', '#4367E8', '#2585DA', '#16A7B9', '#12B886', '#3BB956', '#7EC51D', '#F3A900', '#FF7A00'];
  secaoAtual: SecaoConfig = 'ATENDIMENTO';
  busca = '';
  maximizada = false;
  loading = true;
  saving = false;
  erro = '';
  dados: Record<string, Record<string, any>> = {};
  originais: Record<string, string> = {};
  versoes: Record<string, number | null> = {};
  aberto: Record<string, boolean> = { 'Ordem de serviço': true };
  itens: ItemConfiguracao[] = [];
  areasRecursos: AreaRecurso[] = [];
  buscaTag = '';
  modalTag = false;
  descricaoTag = '';
  corTag = '#899198';
  feriadoEntrada = '';
  modalIndisponibilidade = false;
  novoPeriodo: PeriodoIndisponivel = this.periodoVazio();
  readonly termos: Array<{ chave: string; titulo: string }> = [
    { chave: 'termoLgpd', titulo: 'Termo LGPD' }, { chave: 'termoGarantia', titulo: 'Termo de Garantia' },
    { chave: 'termoQuitacao', titulo: 'Termo de Quitação' }, { chave: 'termoPecasFornecidas', titulo: 'Termo de Peças Fornecidas' },
    { chave: 'termoResponsabilidade', titulo: 'Termo de Responsabilidade' }, { chave: 'declaracaoPermanencia', titulo: 'Declaração de Permanência' },
    { chave: 'termoCorteSeloPlaca', titulo: 'Termo de corte de selo de placa' }, { chave: 'declaracaoAssinaturaOs', titulo: 'Declaração de assinatura da OS' }
  ];

  get secaoSelecionada(): NavegacaoConfig { return this.secoes.find(item => item.secao === this.secaoAtual)!; }
  descricaoSecao(): string {
    const textos: Partial<Record<SecaoConfig, string>> = {
      ATENDIMENTO: 'Campos obrigatórios e rotina de atendimento dos orçamentos e O.S.',
      COMERCIAL: 'Margens, descontos e acréscimos aplicados aos itens da O.S.',
      ESTOQUE_FINANCEIRO: 'Baixa automática de estoque e geração financeira da O.S.',
      INTEGRACOES: 'Valores, áreas e regras usadas nas integrações da ordem de serviço.',
      GESTAO_PATIO: 'Criação e conclusão automática de atividades vinculadas à O.S.',
      TAGS_ITENS: 'Tags usadas para classificar itens da ordem de serviço.',
      FERIADOS_LOCAIS: 'Feriados estaduais e municipais descontados da validade do Atestado de GNV.',
      PRODUTO_COMPOSICAO: 'Campos que participam da formação do preço de venda.',
      PRODUTO_CUSTO_LUCRO: 'Percentuais fixos usados na composição de custo e lucro.',
      COMPRAS: 'Regras de importação, itens e cancelamento de compras.',
      AGENDAMENTO: 'Dias habilitados e períodos indisponíveis para agendamento.',
      FINANCEIRO: 'Regras financeiras específicas da empresa.',
      TERMOS: 'Textos personalizados usados em documentos e ordens de serviço.',
      FISCAL_GERAL: 'Ambiente, regime tributário, documentos habilitados, certificado digital e dados do emitente.',
      FISCAL_NFE: 'Configuração, tributos padrão e série da Nota Fiscal Eletrônica (modelo 55).',
      FISCAL_NFCE: 'Configuração, tributos padrão e série da Nota Fiscal de Consumidor Eletrônica (modelo 65).',
      FISCAL_NFSE: 'Configuração, tributos padrão e RPS da Nota Fiscal de Serviço Eletrônica.',
      FISCAL_FCP: 'Percentual do Fundo de Combate à Pobreza por estado de destino.',
      FISCAL_SINTEGRA: 'Dados da empresa usados na geração do arquivo Sintegra (Convênio ICMS 57/95).',
      FISCAL_SPED: 'Contador responsável e regime de PIS/COFINS usados na geração dos arquivos do SPED.'
    };
    return textos[this.secaoAtual] || '';
  }
  get secoesVisiveis(): NavegacaoConfig[] {
    const termo = this.busca.trim().toLocaleLowerCase('pt-BR');
    return this.secoes.filter(item => !termo || `${item.grupo} ${item.titulo}`.toLocaleLowerCase('pt-BR').includes(termo));
  }
  get gruposVisiveis(): string[] { return [...new Set(this.secoesVisiveis.map(item => item.grupo))]; }
  get alterado(): boolean { return this.secaoAtual !== 'TAGS_ITENS' && !this.secaoAtual.startsWith('FISCAL_') && this.serializar(this.dados[this.secaoAtual] || {}) !== (this.originais[this.secaoAtual] || this.serializar(PADROES[this.secaoAtual] || {})); }

  ngOnInit(): void {
    this.recursosApi.areas({ ativo: true }).subscribe({ next: areas => this.areasRecursos = areas || [], error: () => this.areasRecursos = [] });
    this.api.listar().pipe(finalize(() => { this.loading = false; this.cdr.markForCheck(); })).subscribe({
      next: configuracoes => {
        for (const secao of Object.keys(PADROES)) this.dados[secao] = structuredClone(PADROES[secao]);
        for (const configuracao of configuracoes || []) {
          this.dados[configuracao.secao] = { ...(this.dados[configuracao.secao] || {}), ...configuracao.dados };
          this.versoes[configuracao.secao] = configuracao.versao;
        }
        for (const secao of Object.keys(PADROES)) this.originais[secao] = this.serializar(this.dados[secao]);
        this.carregarTags();
      },
      error: error => { this.erro = error?.error?.message || error?.message || 'Não foi possível carregar as configurações da empresa.'; }
    });
  }

  selecionar(secao: SecaoConfig): void {
    this.secaoAtual = secao;
    if (secao === 'TAGS_ITENS') this.carregarTags();
  }
  alternarGrupo(grupo: string): void { this.aberto[grupo] = !this.aberto[grupo]; }
  fecharPainel(): void { this.fechar.emit(); }
  alternarTamanho(): void { this.maximizada = !this.maximizada; }
  salvar(): void {
    if (!this.alterado || this.saving) return;
    const secao = this.secaoAtual;
    this.saving = true;
    this.api.salvar(secao, this.dados[secao], this.versoes[secao] ?? null).pipe(finalize(() => this.saving = false)).subscribe({
      next: salvo => {
        this.dados[secao] = { ...salvo.dados };
        this.versoes[secao] = salvo.versao;
        this.originais[secao] = this.serializar(this.dados[secao]);
        this.mensagens.add({ severity: 'success', summary: 'Alteração de configuração', detail: 'Configuração alterada com sucesso!' });
      },
      error: error => this.mensagens.add({ severity: 'error', summary: 'Erro ao salvar configuração', detail: error?.error?.message || error?.message || 'Falha ao salvar.' })
    });
  }
  cancelar(): void {
    const secao = this.secaoAtual;
    this.dados[secao] = JSON.parse(this.originais[secao] || this.serializar(PADROES[secao] || {}));
  }
  isBooleano(chave: string): boolean { return typeof this.dados[this.secaoAtual]?.[chave] === 'boolean'; }
  alternar(chave: string): void { this.dados[this.secaoAtual][chave] = !this.dados[this.secaoAtual][chave]; }
  rotulo(chave: string): string {
    const titulos: Record<string, string> = {
      campoKmObrigatorio: 'Campo KM obrigatório', campoPrismaObrigatorio: 'Campo Prisma obrigatório', campoNivelTanqueObrigatorio: 'Campo nível de tanque obrigatório',
      exibirCamposIndicacaoCorretor: 'Exibir campos de indicação de corretor', vincularObservacoesOrdensAtividades: 'Vincular observações de ordens de serviço e atividades', preencherFolhaMecanicoAutomaticamente: 'Preencher folha do mecânico automaticamente',
      aplicarAcrescimoOs: 'Aplicar acréscimo na O.S', baixaAutomaticaEstoque: 'Dar baixa automática no estoque', notificarEstoqueMinimo: 'Notificação de estoque mínimo',
      utilizarChecklistBosch: 'Utilizar checklist Bosch', enviarOsMyBcsAutomaticamente: 'Enviar O.S automaticamente para MyBCS', receberEventosMyBcs: 'Receber eventos MyBCS', criarAtividadeAoGerarOs: 'Criar atividade ao gerar O.S', moverCartaoAoFecharOs: 'Mover card para concluído ao fechar/cancelar O.S',
      percentualIpi: 'Percentual do IPI', frete: 'Frete', custoFixo: 'Custo fixo', despesasOperacionais: 'Despesas operacionais', substituicaoTributaria: 'Substituição tributária', outrasDespesas: 'Outras despesas', icmsDesoneracao: 'ICMS desoneração', calcularMargemLucro: 'Calcular margem de lucro',
      bloquearCfopSaida: 'Bloquear CFOP de saída na importação da compra', permitirExcluirItensImportados: 'Permitir exclusão de itens na importação de compras', permitirPrecoVendaNaCompra: 'Permitir informar preço de venda no item da compra', exigirMotivoCancelamento: 'Exigir motivo ao cancelar compra',
      parcelasLivresVendaDireta: 'Permitir parcelas livres na venda direta', dataVendaReferenciaVencimento: 'Usar a data da venda como referência do vencimento', permitirLiquidoNegativoPagar: 'Permitir valor líquido negativo em contas a pagar'
    };
    return titulos[chave] || chave;
  }
  descricao(chave: string): string {
    const textos: Record<string, string> = {
      campoKmObrigatorio: 'Exige o preenchimento do KM no orçamento e na ordem de serviço.', campoPrismaObrigatorio: 'Exige o preenchimento do prisma na ordem de serviço.', campoNivelTanqueObrigatorio: 'Exige o preenchimento do nível do tanque.',
      exibirCamposIndicacaoCorretor: 'Mostra os campos de indicação na ordem de serviço.', vincularObservacoesOrdensAtividades: 'Compartilha observações entre a ordem de serviço e as atividades da gestão de pátio.', preencherFolhaMecanicoAutomaticamente: 'Preenche a folha do mecânico com base nos dados da ordem de serviço.',
      baixaAutomaticaEstoque: 'Movimenta o estoque automaticamente pelos itens da ordem de serviço.', notificarEstoqueMinimo: 'Ativa alertas quando itens atingirem o estoque mínimo.', criarAtividadeAoGerarOs: 'Cria automaticamente uma atividade da gestão de pátio ao abrir uma O.S.', moverCartaoAoFecharOs: 'Move automaticamente o card quando a ordem de serviço for fechada ou cancelada.',
      bloquearCfopSaida: 'Exige que o CFOP seja informado para cada item importado da compra.', permitirExcluirItensImportados: 'Permite remover itens antes de confirmar uma compra importada.', permitirPrecoVendaNaCompra: 'Libera a edição do preço de venda ao lançar ou revisar os itens da compra.', exigirMotivoCancelamento: 'Solicita uma justificativa antes de cancelar uma compra.',
      parcelasLivresVendaDireta: 'Permite definir manualmente quantidade, vencimento e intervalo das parcelas.', dataVendaReferenciaVencimento: 'Define a primeira data de vencimento com base na data de criação da venda.', permitirLiquidoNegativoPagar: 'Permite que descontos, acréscimos ou crédito deixem o valor líquido negativo.'
    };
    return textos[chave] || '';
  }
  itensFiltrados(): ItemConfiguracao[] { const q = this.buscaTag.trim().toLocaleLowerCase('pt-BR'); return this.itens.filter(item => item.descricao.toLocaleLowerCase('pt-BR').includes(q)); }
  abrirCriacaoTag(): void { this.descricaoTag = ''; this.corTag = '#899198'; this.modalTag = true; }
  carregarTags(): void { this.itensApi.listar('ORDEM_SERVICO_TAG_ITEM', this.buscaTag).subscribe({ next: itens => this.itens = itens || [], error: () => this.itens = [] }); }
  salvarTag(): void {
    const descricao = this.descricaoTag.trim();
    if (descricao.length < 2 || this.saving) return;
    this.saving = true;
    this.itensApi.criar({ grupo: 'ORDEM_SERVICO_TAG_ITEM', descricao, cor: this.corTag }).pipe(finalize(() => this.saving = false)).subscribe({
      next: () => { this.modalTag = false; this.carregarTags(); this.mensagens.add({ severity: 'success', summary: 'Tag criada', detail: 'Tag adicionada com sucesso.' }); },
      error: erro => this.mensagens.add({ severity: 'error', summary: 'Erro ao salvar tag', detail: erro?.error?.message || 'Não foi possível salvar.' })
    });
  }
  excluirTag(item: ItemConfiguracao): void {
    this.itensApi.excluir(item.id, item.grupo).subscribe({ next: () => { this.itens = this.itens.filter(atual => atual.id !== item.id); this.mensagens.add({ severity: 'success', summary: 'Tag removida', detail: 'Tag removida com sucesso.' }); }, error: erro => this.mensagens.add({ severity: 'error', summary: 'Erro ao remover tag', detail: erro?.error?.message || 'Não foi possível remover.' }) });
  }
  adicionarFeriado(): void {
    const valor = this.feriadoEntrada.trim();
    if (!/^(\d{2}\/\d{2})(\/\d{4})?$/.test(valor)) return;
    const lista: string[] = this.dados['FERIADOS_LOCAIS']['feriados'];
    if (!lista.includes(valor)) this.dados['FERIADOS_LOCAIS']['feriados'] = [...lista, valor];
    this.feriadoEntrada = '';
  }
  removerFeriado(index: number): void { this.dados['FERIADOS_LOCAIS']['feriados'].splice(index, 1); }
  periodoVazio(): PeriodoIndisponivel { return { descricao: '', data: '', recursos: 1, todosRecursos: false, inicio: '', fim: '' }; }
  adicionarIndisponibilidade(): void {
    const periodo = this.novoPeriodo;
    if (!periodo.descricao.trim() || !periodo.data || !periodo.inicio || !periodo.fim || periodo.fim <= periodo.inicio) return;
    this.dados['AGENDAMENTO']['indisponibilidades'] = [...this.dados['AGENDAMENTO']['indisponibilidades'], { ...periodo, descricao: periodo.descricao.trim() }];
    this.modalIndisponibilidade = false; this.novoPeriodo = this.periodoVazio();
  }
  removerIndisponibilidade(index: number): void { this.dados['AGENDAMENTO']['indisponibilidades'].splice(index, 1); }
  private serializar(valor: unknown): string { return JSON.stringify(valor); }
}
