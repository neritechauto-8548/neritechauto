import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgxPermissionsService } from 'ngx-permissions';

// PrimeNG
import { ButtonModule } from 'primeng/button';
import { SplitButtonModule } from 'primeng/splitbutton';
import { PanelModule } from 'primeng/panel';
import { TagModule } from 'primeng/tag';
import { CheckboxModule } from 'primeng/checkbox';
import { RadioButtonModule } from 'primeng/radiobutton';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { TableModule } from 'primeng/table';
import { BadgeModule } from 'primeng/badge';
import { TooltipModule } from 'primeng/tooltip';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { FileUploadModule } from 'primeng/fileupload';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { Tabs, TabList, Tab, TabPanels, TabPanel } from 'primeng/tabs';
import { MenuModule } from 'primeng/menu';
import { ToolbarModule } from 'primeng/toolbar';
import { CardModule } from 'primeng/card';
import { AvatarModule } from 'primeng/avatar';
import { InputNumberModule } from 'primeng/inputnumber';

// Shared
import { AuthService } from '@core';
import { OrdemServicoService } from '../ordem-servico.service';
import { StatusOSService } from '../status-os.service';
import { SetorService } from '../../configuracoes/setores/setor.service';
import { FuncionarioService } from '../../configuracoes/colaboradores/funcionario.service';
import { SituacaoService } from '../../configuracoes/situacao/situacao.service';
import { LocalizacaoService } from '../../configuracoes/localizacao/localizacao.service';
import {
  OrdemServicoResponse,
  OrdemServicoRequest,
  ItemOSProdutoRequest,
  ItemOSServicoRequest,
  DiagnosticoRequest,
  TipoOS,
  StatusOSResponse,
} from '../models/os.models';
import {
  authoritativeOsTotal,
  CockpitItemState,
  CockpitLoadState,
  resolveCockpitLoadError,
  resolveItemState,
} from './os-cockpit-state';

interface ViewItem {
  id: number;
  descricao: string;
  qtd: number;
  preco: number;
  total: number;
  tipo: 'servico' | 'produto';
  status: CockpitItemState;
  original?: any;
}

@Component({
  selector: 'visualizar-os',
  standalone: true,
  templateUrl: './visualizar-os.html',
  styleUrls: ['./visualizar-os.scss'],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ButtonModule,
    SplitButtonModule,
    PanelModule,
    TagModule,
    CheckboxModule,
    RadioButtonModule,
    SelectModule,
    DatePickerModule,
    InputTextModule,
    TextareaModule,
    TableModule,
    BadgeModule,
    TooltipModule,
    ToastModule,
    ConfirmDialogModule,
    ToolbarModule,
    CardModule,
    AvatarModule,
    Tabs,
    TabList,
    Tab,
    TabPanels,
    TabPanel,
    DialogModule,
    MenuModule,
    FileUploadModule,
    InputNumberModule,
  ],
  providers: [MessageService, ConfirmationService],
})
export class VisualizarOS implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);
  private readonly permissions = inject(NgxPermissionsService);
  private readonly osService = inject(OrdemServicoService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly statusService = inject(StatusOSService);
  private readonly setorService = inject(SetorService);
  private readonly funcionarioService = inject(FuncionarioService);
  private readonly situacaoOficinaService = inject(SituacaoService);
  private readonly localizacaoPatioService = inject(LocalizacaoService);

  orcamentoNumero = 0;
  currentOS?: OrdemServicoResponse;
  isOrcamento = false;
  isGeneratingPdf = false;
  isSendingEmail = false;

  cockpitState: CockpitLoadState = 'idle';
  cockpitMessage = '';
  partialSourceWarnings: string[] = [];

  get hasPartialSources(): boolean {
    return this.partialSourceWarnings.length > 0;
  }

  get canEditOS(): boolean {
    return Boolean(this.permissions.getPermission('OS_EDITAR'));
  }

  get canIncludeItems(): boolean {
    return Boolean(this.permissions.getPermission('OS_INC_ITENS'));
  }

  get canPrintOS(): boolean {
    return Boolean(
      this.permissions.getPermission('OS_IMP_CLIENTE') ||
      this.permissions.getPermission('OS_IMP_INTERNO')
    );
  }

  get canNegotiatePayment(): boolean {
    return Boolean(this.permissions.getPermission('OS_NEG_PAGAMENTO'));
  }

  get canUploadPhotos(): boolean {
    return Boolean(this.permissions.getPermission('OS_ENV_FOTOS'));
  }

  get canViewChecklist(): boolean {
    return Boolean(this.permissions.getPermission('OS_VIS_CHECKLIST'));
  }

  get canAddChecklist(): boolean {
    return Boolean(this.permissions.getPermission('OS_ADC_CHECKLIST'));
  }

  get canEditChecklist(): boolean {
    return Boolean(this.permissions.getPermission('OS_EDIT_CHECKLIST'));
  }

  get canComment(): boolean {
    return Boolean(
      this.permissions.getPermission('OS_COMENTARIOS') ||
      this.permissions.getPermission('OS_COMENTARIOS_OUTROS')
    );
  }

  get authenticatedActorName(): string {
    const user = this.auth.snapshot();
    return user.nomeCompleto?.trim() || user.name?.trim() || user.email?.trim() || 'Usuário autenticado';
  }

  // Modal: Enviar por WhatsApp / E-mail
  enviarDialogVisible = false;
  enviarCanal: 'whatsapp' | 'email' = 'whatsapp';
  enviarEmail = '';

  abrirEnviarDialog() {
    this.enviarCanal = 'whatsapp';
    this.enviarEmail = '';
    this.enviarDialogVisible = true;
  }

  confirmarEnvio() {
    if (this.enviarCanal === 'whatsapp') {
      const tipo = this.isOrcamento ? 'Orçamento' : 'OS';
      const num = this.orcamentoNumero;
      const cliente = this.cliente;
      const total = this.total;
      const veiculo = this.veiculo;
      const placa = this.placa;
      const msg = encodeURIComponent(
        `Olá ${cliente}! Segue o detalhamento do seu ${tipo} #${num}:\n` +
          `Veículo: ${veiculo} (${placa})\n` +
          `Total: R$ ${total.toFixed(2).replace('.', ',')}\n` +
          `Para aprovar ou tirar dúvidas, entre em contato conosco.`
      );
      window.open(`https://wa.me/?text=${msg}`, '_blank');
      this.enviarDialogVisible = false;
      return;
    }

    if (!this.orcamentoNumero) return;
    this.isSendingEmail = true;
    this.osService.enviarPorEmail(this.orcamentoNumero, this.enviarEmail || undefined).subscribe({
      next: () => {
        this.isSendingEmail = false;
        this.enviarDialogVisible = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Enviado!',
          detail: 'E-mail enviado com sucesso para o cliente.',
        });
      },
      error: (err) => {
        this.isSendingEmail = false;
        const detail = err?.error?.message || 'Não foi possível enviar o e-mail. Verifique o e-mail do cliente ou o SMTP.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail });
      },
    });
  }

  menuPS: MenuItem[] = [];
  menuSolic: MenuItem[] = [];
  menuChecklist: MenuItem[] = [];

  constructor() {}

  ngOnInit() {
    this.menuPS = [{ label: 'Incluir Item', icon: 'pi pi-plus', command: () => this.abrirIncluirDialog() }];
    this.menuSolic = [{ label: 'Nova Solicitação', icon: 'pi pi-plus', command: () => this.abrirSolicitacaoDialog() }];
    this.menuChecklist = [
      { label: 'Selecionar Checklist', icon: 'pi pi-list-check', command: () => this.abrirChecklistDialog() },
      {
        label: 'Ver itens do modelo',
        icon: 'pi pi-eye',
        command: () => (this.checklistSelecionado ? this.verItensChecklist(this.checklistSelecionado) : null),
      },
    ];

    const id = this.route.snapshot.paramMap.get('id') || this.route.snapshot.paramMap.get('numero');
    const n = id ? Number(id) : undefined;
    if (n && !Number.isNaN(n)) {
      this.orcamentoNumero = n;
      this.carregarOS(n);
    } else {
      this.cockpitState = 'not-found';
      this.cockpitMessage = 'Não foi possível identificar a ordem de serviço solicitada.';
    }

    this.statusService.list({ ativo: true }).subscribe({
      next: (page) => (this.statusLista = page?.content || []),
      error: () => {
        this.statusLista = [];
        this.markPartialSource('Status da OS');
      },
    });

    this.setorService.list({ size: 1000, sort: 'nome,asc' }).subscribe({
      next: (resp) => {
        const list = resp?.content || [];
        this.setorOptions = [
          { label: 'Sem escolher', value: 'NONE' },
          ...list.map((s: any) => ({ label: s.nome, value: s.nome })),
        ];
      },
      error: () => {
        this.setorOptions = [{ label: 'Sem escolher', value: 'NONE' }];
        this.markPartialSource('Setores');
      },
    });

    this.funcionarioService.list({ size: 1000 }).subscribe({
      next: (resp) => {
        const list = resp?.content || [];
        this.funcionarioOptions = list
          .filter((f: any) => f?.id && (f?.nomeCompleto || f?.nome))
          .map((f: any) => ({
            label: f.nomeCompleto || f.nome,
            value: `${f.id} - ${f.nomeCompleto || f.nome}`,
          }));

        this.responsavelOptions = [...this.funcionarioOptions];

        if (this.currentOS?.consultorResponsavelId) {
          this.atualizarResponsavelNome(this.currentOS.consultorResponsavelId);
        }
      },
      error: () => {
        this.funcionarioOptions = [];
        this.responsavelOptions = [];
        this.responsavel = '';
        this.incluir.funcionario = '';
        this.markPartialSource('Equipe');
      },
    });

    this.situacaoOficinaService.list({ size: 1000 }).subscribe({
      next: (resp) => {
        const list = resp?.content || [];
        this.situacaoOptions = list
          .filter((s: any) => s?.nmSituacao)
          .map((s: any) => ({ label: s.nmSituacao, value: s.nmSituacao }));
      },
      error: () => {
        this.situacaoOptions = [];
        this.situacao = '';
        this.markPartialSource('Situações da oficina');
      },
    });

    this.localizacaoPatioService.list({ size: 1000 }).subscribe({
      next: (resp) => {
        const list = resp?.content || [];
        this.localizacaoOptions = list
          .filter((l: any) => l?.descricao)
          .map((l: any) => ({ label: l.descricao, value: l.descricao }));
      },
      error: () => {
        this.localizacaoOptions = [];
        this.localizacao = '';
        this.markPartialSource('Localizações do pátio');
      },
    });
  }

  private markPartialSource(source: string) {
    if (!this.partialSourceWarnings.includes(source)) {
      this.partialSourceWarnings = [...this.partialSourceWarnings, source];
    }
  }

  private requireCapability(allowed: boolean, detail: string): boolean {
    if (allowed) return true;
    this.messageService.add({ severity: 'warn', summary: 'Sem permissão', detail });
    return false;
  }

  carregarOS(id: number) {
    this.cockpitState = 'loading';
    this.cockpitMessage = '';

    this.osService.getById(id).subscribe({
      next: (os) => {
        this.applyOS(os);
        this.cockpitState = 'ready';
        this.carregarItens(id);
        this.carregarSolicitacoes(id);
        this.carregarChecklistsOS(id);
        this.carregarPagamentosOS(id);
        this.carregarFotosOS(id);
      },
      error: (err) => {
        const resolved = resolveCockpitLoadError(err?.status);
        this.cockpitState = resolved.state;
        this.cockpitMessage = resolved.message;
        this.messageService.add({ severity: 'error', summary: 'Ordem de Serviço', detail: resolved.message });
      },
    });
  }

  carregarItens(osId: number) {
    this.itens = [];
    this.totalServicos = 0;
    this.totalProdutos = 0;
    this.totalAprovado = 0;
    this.totalAguardando = 0;
    this.totalNegado = 0;
    this.total = authoritativeOsTotal(this.currentOS);

    this.osService.getProdutos(osId).subscribe({
      next: (produtos) => {
        const mappedProds: ViewItem[] = produtos.map((p) => ({
          id: p.id,
          descricao: p.descricao || p.nomeProduto || 'Produto sem descrição',
          qtd: p.quantidade,
          preco: p.valorUnitario,
          total: p.valorFinal ?? p.valorTotal,
          tipo: 'produto',
          status: resolveItemState(p.aprovadoCliente),
          original: p,
        }));
        this.itens.push(...mappedProds);
        this.calculateTotals();
      },
      error: () => {
        this.markPartialSource('Produtos da OS');
        this.calculateTotals();
      },
    });

    this.osService.getServicos(osId).subscribe({
      next: (servicos) => {
        const mappedServs: ViewItem[] = servicos.map((s) => ({
          id: s.id,
          descricao: s.descricao || s.nomeServico || 'Serviço sem descrição',
          qtd: s.quantidade || 1,
          preco: s.valorUnitario,
          total: s.valorFinal ?? s.valorTotal,
          tipo: 'servico',
          status: resolveItemState(s.aprovadoCliente),
          original: s,
        }));
        this.itens.push(...mappedServs);
        this.calculateTotals();
      },
      error: () => {
        this.markPartialSource('Serviços da OS');
        this.calculateTotals();
      },
    });
  }

  calculateTotals() {
    this.totalProdutos = this.itens.filter((i) => i.tipo === 'produto').reduce((acc, cur) => acc + Number(cur.total || 0), 0);
    this.totalServicos = this.itens.filter((i) => i.tipo === 'servico').reduce((acc, cur) => acc + Number(cur.total || 0), 0);
    this.totalAprovado = this.itens.filter((i) => i.status === 'aprovado').reduce((acc, cur) => acc + Number(cur.total || 0), 0);
    this.totalAguardando = this.itens.filter((i) => i.status === 'aguardando').reduce((acc, cur) => acc + Number(cur.total || 0), 0);
    this.totalNegado = 0;
    this.total = authoritativeOsTotal(this.currentOS);
  }

  carregarSolicitacoes(osId: number) {
    this.osService.getDiagnosticos(osId).subscribe({
      next: (diags) => {
        this.solicitacoes = diags.map((d) => ({
          id: d.id,
          quantidade: 1,
          descricao: d.problemaIdentificado || d.observacoes || 'Sem descrição',
          codigoOriginal: '',
          codigo: d.codigoErro || '',
          idSolicitacao: d.id,
        }));
      },
      error: () => {
        this.solicitacoes = [];
        this.markPartialSource('Diagnósticos e solicitações');
      },
    });
  }

  // Cabeçalho
  placa = '';
  veiculo = '';
  cliente = '';
  dataEntrada = { data: '', hora: '' };
  status = '';
  statusId?: number;
  statusLista: StatusOSResponse[] = [];
  quilometragem = '';
  previsaoSaidaDate = '';
  previsaoSaidaHora = '';
  editing = true;
  statusDialogVisible = false;

  isOSFinalizada(): boolean {
    if (!this.statusId || this.statusLista.length === 0) return false;
    return Boolean(this.statusLista.find((st) => st.id === this.statusId)?.finalizaOS);
  }

  getStatusSeverity(s: string) {
    const key = (s || '').toLowerCase();
    if (key.includes('entreg') || key.includes('finaliz') || key.includes('aprov')) return 'success';
    if (key.includes('cancel') || key.includes('negad') || key.includes('recus')) return 'danger';
    if (key.includes('aguard') || key.includes('pend')) return 'warn';
    return 'info';
  }

  getStatusIcon(s: string) {
    const key = (s || '').toLowerCase();
    if (key.includes('entreg') || key.includes('finaliz') || key.includes('aprov')) return 'pi pi-check';
    if (key.includes('cancel') || key.includes('negad') || key.includes('recus')) return 'pi pi-times';
    if (key.includes('aguard') || key.includes('pend')) return 'pi pi-clock';
    return 'pi pi-info-circle';
  }

  abrirStatusDialog() {
    if (!this.requireCapability(this.canEditOS, 'Você não possui permissão para alterar a OS.')) return;
    if (this.statusLista.length === 0) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Status indisponíveis',
        detail: 'A lista de status não está disponível. Atualize a página antes de alterar o andamento.',
      });
      return;
    }
    this.statusDialogVisible = true;
  }

  fecharStatusDialog() {
    this.statusDialogVisible = false;
  }

  setStatus(status: StatusOSResponse | string) {
    const found = typeof status === 'string'
      ? this.statusLista.find(
          (st) =>
            (st.nome || '').toLowerCase() === status.toLowerCase() ||
            (st.codigo || '').toLowerCase() === status.toLowerCase()
        )
      : status;

    if (!found?.id) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Status não disponível',
        detail: 'Esse status não faz parte do catálogo retornado pela API.',
      });
      return;
    }

    this.statusId = found.id;
    this.status = found.nome || found.codigo;
    this.fecharStatusDialog();
  }

  situacao = '';
  localizacao = '';
  responsavel = '';
  situacaoOptions: any[] = [];
  localizacaoOptions: any[] = [];
  responsavelOptions: any[] = [];

  // Produtos e serviços
  prodServBusca = '';
  itens: ViewItem[] = [];

  get filteredItens() {
    if (!this.prodServBusca?.trim()) return this.itens;
    const query = this.prodServBusca.toLowerCase().trim();
    return this.itens.filter(
      (i) => i.descricao?.toLowerCase().includes(query) || i.tipo?.toLowerCase().includes(query)
    );
  }

  alternarStatusItem(_: ViewItem) {
    this.messageService.add({
      severity: 'info',
      summary: 'Status somente leitura',
      detail: 'O estado de autorização do item vem do backend e não pode ser alterado somente no navegador.',
    });
  }

  totalServicos = 0;
  totalProdutos = 0;
  totalAprovado = 0;
  totalAguardando = 0;
  totalNegado = 0;
  total = 0;

  // Solicitações
  solicitacoes: {
    quantidade: number;
    descricao: string;
    codigoOriginal: string;
    codigo: string;
    idSolicitacao?: number;
    id?: number;
  }[] = [];
  solicitacaoDialogVisible = false;
  solicitacao = { descricao: '', quantidade: 1, codigoOriginal: '', codigo: '', duvida: false };

  // Pagamentos
  faturaOS?: any;
  pagamentosExistentes: any[] = [];
  osFotos: any[] = [];
  showFotoDialog = false;
  showFotoViewDialog = false;
  selectedFotoUrl = '';

  get dataPagamentoRealizado(): string {
    if (this.pagamentosExistentes.length > 0) {
      const p = this.pagamentosExistentes[0];
      if (p.dataPagamento) return new Date(p.dataPagamento).toLocaleDateString('pt-BR');
    }
    return '';
  }

  isPagamentoPago(): boolean {
    if (!this.pagamentosExistentes.length) return false;
    return this.pagamentosExistentes.some((p) =>
      ['CONFIRMADO', 'PAGO', 'PAGA', 'QUITADO'].includes(String(p.status || '').toUpperCase())
    );
  }

  selectedFotoDesc = '';
  fotoDescricao = '';
  fotoFile: File | null = null;

  carregarPagamentosOS(osId: number) {
    this.faturaOS = undefined;
    this.pagamentosExistentes = [];

    this.osService.getFaturaPorOS(osId).subscribe({
      next: (fat) => (this.faturaOS = fat),
      error: () => {
        this.faturaOS = undefined;
        this.markPartialSource('Fatura da OS');
      },
    });

    this.osService.listPagamentosPorOS(osId).subscribe({
      next: (page) => (this.pagamentosExistentes = page?.content || []),
      error: () => {
        this.pagamentosExistentes = [];
        this.markPartialSource('Pagamentos da OS');
      },
    });
  }

  editandoSolicitacaoId: number | null = null;

  abrirSolicitacaoDialog() {
    if (!this.requireCapability(this.canEditOS, 'Você não possui permissão para alterar solicitações da OS.')) return;
    this.solicitacaoDialogVisible = true;
    this.solicitacao = { descricao: '', quantidade: 1, codigoOriginal: '', codigo: '', duvida: false };
    this.editandoSolicitacaoId = null;
  }

  fecharSolicitacaoDialog() {
    this.solicitacaoDialogVisible = false;
  }

  incluirSolicitacao() {
    if (!this.orcamentoNumero || !this.canEditOS) return;
    const req: DiagnosticoRequest = {
      ordemServicoId: this.orcamentoNumero,
      problemaIdentificado: this.solicitacao.descricao,
      codigoErro: this.solicitacao.codigo,
      observacoes: this.solicitacao.codigoOriginal || '',
    };
    const op$ = this.editandoSolicitacaoId
      ? this.osService.updateDiagnostico(this.editandoSolicitacaoId, req)
      : this.osService.addDiagnostico(req);
    op$.subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: this.editandoSolicitacaoId ? 'Solicitação atualizada' : 'Solicitação adicionada',
        });
        this.fecharSolicitacaoDialog();
        this.carregarSolicitacoes(this.orcamentoNumero);
        this.editandoSolicitacaoId = null;
      },
      error: () =>
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar a solicitação' }),
    });
  }

  editarSolicitacao(s: {
    quantidade: number;
    descricao: string;
    codigoOriginal: string;
    codigo: string;
    idSolicitacao?: number;
    id?: number;
  }) {
    if (!this.requireCapability(this.canEditOS, 'Você não possui permissão para editar solicitações da OS.')) return;
    this.solicitacaoDialogVisible = true;
    this.solicitacao = {
      descricao: s.descricao,
      quantidade: s.quantidade || 1,
      codigoOriginal: s.codigoOriginal || '',
      codigo: s.codigo || '',
      duvida: false,
    };
    this.editandoSolicitacaoId = s.idSolicitacao || s.id || null;
  }

  excluirSolicitacao(s: { idSolicitacao?: number; id?: number; descricao: string }) {
    if (!this.requireCapability(this.canEditOS, 'Você não possui permissão para excluir solicitações da OS.')) return;
    const id = s.idSolicitacao || s.id;
    if (!id) return;
    this.confirmationService.confirm({
      message: `Deseja excluir a solicitação "${s.descricao}"?`,
      header: 'Confirmação',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.osService.deleteDiagnostico(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Solicitação excluída' });
            this.carregarSolicitacoes(this.orcamentoNumero);
          },
          error: () =>
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao excluir solicitação' }),
        });
      },
    });
  }

  // Checklist
  checklistMsg = 'Nenhum checklist cadastrado.';
  checklistDialogVisible = false;
  checklistLista: any[] = [];
  checklistSelecionado: any | null = null;
  checklistsOS: any[] = [];
  checklistPreview: any[] = [];
  checklistPreviewNome = '';

  abrirChecklistDialog() {
    if (!this.requireCapability(this.canAddChecklist, 'Você não possui permissão para adicionar checklist à OS.')) return;
    this.checklistDialogVisible = true;
    this.checklistSelecionado = null;
    this.buscarChecklists('');
  }

  fecharChecklistDialog() {
    this.checklistDialogVisible = false;
  }

  buscarChecklists(texto: string) {
    if (!this.canViewChecklist && !this.canAddChecklist) return;
    this.osService.listChecklists(texto).subscribe({
      next: (page) => (this.checklistLista = page?.content || []),
      error: () => {
        this.checklistLista = [];
        this.markPartialSource('Catálogo de checklists');
      },
    });
  }

  selecionarChecklist(c: any) {
    this.checklistSelecionado = c;
  }

  verItensChecklist(c: any) {
    this.checklistPreview = [];
    this.checklistPreviewNome = c?.dsChecklist || c?.nome || `Checklist #${c?.id}`;
    if (!c?.id) return;
    this.osService.getChecklistModeloItens(c.id).subscribe({
      next: (list) => (this.checklistPreview = list || []),
      error: () => {
        this.checklistPreview = [];
        this.markPartialSource('Itens do modelo de checklist');
      },
    });
  }

  adicionarChecklist() {
    if (!this.orcamentoNumero || !this.checklistSelecionado?.id || !this.canAddChecklist) return;
    this.osService.addOSChecklist(this.orcamentoNumero, this.checklistSelecionado.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Checklist adicionado à OS' });
        this.fecharChecklistDialog();
        this.carregarChecklistsOS(this.orcamentoNumero);
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao adicionar checklist' }),
    });
  }

  carregarChecklistsOS(osId: number) {
    if (!this.canViewChecklist && !this.canAddChecklist && !this.canEditChecklist) {
      this.checklistsOS = [];
      this.checklistMsg = 'Você não possui permissão para visualizar checklists desta OS.';
      return;
    }

    this.osService.getOSChecklists(osId).subscribe({
      next: (list) => {
        this.checklistsOS = list || [];
        this.checklistMsg = this.checklistsOS.length ? '' : 'Nenhum checklist cadastrado.';
      },
      error: () => {
        this.checklistsOS = [];
        this.checklistMsg = 'Não foi possível carregar os checklists desta OS.';
        this.markPartialSource('Checklists da OS');
      },
    });
  }

  toggleChecklistItem(item: any) {
    if (!item?.id) return;
    if (!this.requireCapability(this.canEditChecklist, 'Você não possui permissão para atualizar checklist.')) return;
    const desired = !!item.feito;
    this.osService.updateOSChecklistItem(item.id, { feito: desired }).subscribe({
      next: () => {},
      error: () => {
        item.feito = !desired;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao atualizar item' });
      },
    });
  }

  // Observações
  observacaoInterna = '';
  descricaoCliente = '';

  // Comentários
  comentarios: any[] = [];
  novoComentario = '';

  adicionarComentario() {
    if (!this.requireCapability(this.canComment, 'Você não possui permissão para comentar nesta OS.')) return;
    if (!this.novoComentario.trim()) return;
    const dataHora =
      new Date().toLocaleDateString('pt-BR') +
      ' ' +
      new Date().toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    this.comentarios.push({ autor: this.authenticatedActorName, data: dataHora, texto: this.novoComentario.trim() });
    this.novoComentario = '';
    this.salvarComentarios();
  }

  salvarComentarios() {
    if (!this.orcamentoNumero || !this.canComment) return;

    const dto: Omit<OrdemServicoRequest, 'empresaId'> = {
      numeroOS: this.currentOS?.numeroOS || String(this.orcamentoNumero),
      tipoOS: this.currentOS?.tipoOS || TipoOS.MANUTENCAO,
      valorTotal: authoritativeOsTotal(this.currentOS),
      quilometragemEntrada: Number(this.quilometragem) || this.currentOS?.quilometragemEntrada,
      consultorResponsavelId: this.parseResponsavelId() || this.currentOS?.consultorResponsavelId,
      observacoesInternas: this.observacaoInterna,
      observacoesCliente: this.descricaoCliente,
      comentarios: JSON.stringify(this.comentarios),
      dataPromessa: this.composeDateTime(this.previsaoSaidaDate, this.previsaoSaidaHora),
      statusId: this.statusId,
    };

    this.osService.update(this.orcamentoNumero, dto).subscribe({
      next: () => this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Diário atualizado' }),
      error: (err) => this.handleMutationError(err, 'Não foi possível salvar no diário'),
    });
  }

  // Popup "Incluir" (Produtos e Serviços)
  incluirDialogVisible = false;
  incluirTabIndex = 0;
  funcionarioOptions: Array<{ label: string; value: string }> = [];
  setorOptions = [{ label: 'Sem escolher', value: 'NONE' }];

  incluir = {
    funcionario: '',
    setor: 'NONE',
    descricao: '',
    quantidade: 1,
    valor: 0,
    valorCusto: 0,
    total: 0,
    fornecedor: '',
    buscaTipo: 'BUSCA GERAL',
    buscaTexto: '',
    produtoId: undefined as number | undefined,
    servicoId: undefined as number | undefined,
  };

  listaProdutos: any[] = [];
  listaServicos: any[] = [];
  servicosCadastrados: any[] = [];
  estoqueLista: any[] = [];

  abrirIncluirDialog() {
    if (!this.requireCapability(this.canIncludeItems, 'Você não possui permissão para incluir itens na OS.')) return;
    this.incluirDialogVisible = true;
    this.incluirTabIndex = 0;
    this.incluir.buscaTexto = '';
    this.listaProdutos = [];
    this.listaServicos = [];
    this.incluir.descricao = '';
    this.incluir.quantidade = 1;
    this.incluir.valor = 0;
    this.incluir.valorCusto = 0;
    this.incluir.total = 0;
    this.incluir.produtoId = undefined;
    this.incluir.servicoId = undefined;
    this.editandoItem = null;
    this.buscarProdutos('');
    this.buscarServicos('');
  }

  fecharIncluirDialog() {
    this.incluirDialogVisible = false;
  }

  calcularTotal() {
    const q = Number(this.incluir.quantidade) || 0;
    const v = Number(this.incluir.valor) || 0;
    this.incluir.total = q * v;
  }

  onSearchInput(event: any) {
    this.incluir.buscaTexto = event.target?.value || '';
  }

  buscarProdutos(texto?: string) {
    if (!this.canIncludeItems) return;
    const query = texto || this.incluir.buscaTexto;
    this.osService.searchProdutos(query).subscribe({
      next: (page) => (this.listaProdutos = page.content || []),
      error: () => {
        this.listaProdutos = [];
        this.markPartialSource('Catálogo de produtos');
      },
    });
  }

  buscarServicos(texto?: string) {
    if (!this.canIncludeItems) return;
    const query = texto || this.incluir.buscaTexto;
    this.osService.searchServicos(query).subscribe({
      next: (page) => (this.listaServicos = page.content || []),
      error: () => {
        this.listaServicos = [];
        this.markPartialSource('Catálogo de serviços');
      },
    });
  }

  selecionarProduto(p: any) {
    this.incluir.descricao = p.nome;
    this.incluir.valor = p.precoVenda || 0;
    this.incluir.valorCusto = p.precoCusto || 0;
    this.incluir.quantidade = 1;
    this.incluir.produtoId = p.id;
    this.calcularTotal();
    this.incluirTabIndex = 2;
  }

  selecionarServico(s: any) {
    this.incluir.descricao = s.nome;
    this.incluir.valor = s.precoBase || 0;
    this.incluir.valorCusto = s.custo || 0;
    this.incluir.quantidade = 1;
    this.incluir.servicoId = s.id;
    this.calcularTotal();
    this.incluirTabIndex = 0;
  }

  editandoItem: { id: number; tipo: 'produto' | 'servico' } | null = null;

  incluirItem() {
    if (!this.orcamentoNumero || !this.canIncludeItems) return;

    if (this.incluirTabIndex === 0 || this.incluirTabIndex === 1) {
      const req: ItemOSServicoRequest = {
        ordemServicoId: this.orcamentoNumero,
        servicoId: this.incluir.servicoId,
        descricao: this.incluir.descricao,
        quantidade: Number(this.incluir.quantidade),
        valorUnitario: Number(this.incluir.valor),
        valorTotal: Number(this.incluir.total),
        valorFinal: Number(this.incluir.total),
      };
      if (this.editandoItem?.tipo === 'servico') {
        this.osService.updateServico(this.editandoItem.id, req).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Serviço atualizado' });
            this.carregarItens(this.orcamentoNumero);
            this.fecharIncluirDialog();
            this.editandoItem = null;
          },
          error: (err) => this.handleMutationError(err, 'Falha ao atualizar serviço'),
        });
      } else {
        this.osService.addServico(req).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Serviço adicionado' });
            this.carregarItens(this.orcamentoNumero);
            this.fecharIncluirDialog();
          },
          error: (err) => this.handleMutationError(err, 'Falha ao adicionar serviço'),
        });
      }
      return;
    }

    const req: ItemOSProdutoRequest = {
      ordemServicoId: this.orcamentoNumero,
      produtoId: this.incluir.produtoId,
      descricao: this.incluir.descricao,
      quantidade: Number(this.incluir.quantidade),
      valorUnitario: Number(this.incluir.valor),
      valorTotal: Number(this.incluir.total),
      valorFinal: Number(this.incluir.total),
      precoCusto: Number(this.incluir.valorCusto),
    };
    if (this.editandoItem?.tipo === 'produto') {
      this.osService.updateProduto(this.editandoItem.id, req).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto atualizado' });
          this.carregarItens(this.orcamentoNumero);
          this.fecharIncluirDialog();
          this.editandoItem = null;
        },
        error: (err) => this.handleMutationError(err, 'Falha ao atualizar produto'),
      });
    } else {
      this.osService.addProduto(req).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto adicionado' });
          this.carregarItens(this.orcamentoNumero);
          this.fecharIncluirDialog();
        },
        error: (err) => this.handleMutationError(err, 'Falha ao adicionar produto'),
      });
    }
  }

  removerItem(item: ViewItem) {
    if (!this.requireCapability(this.canEditOS, 'Você não possui permissão para remover itens da OS.')) return;
    this.confirmationService.confirm({
      message: `Deseja remover o item "${item.descricao}"?`,
      header: 'Confirmação',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        const request$ = item.tipo === 'produto'
          ? this.osService.deleteProduto(item.id)
          : this.osService.deleteServico(item.id);
        request$.subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Item removido' });
            this.carregarItens(this.orcamentoNumero);
          },
          error: (err) => this.handleMutationError(err, 'Falha ao remover item'),
        });
      },
    });
  }

  editarItem(item: ViewItem) {
    if (!this.requireCapability(this.canEditOS, 'Você não possui permissão para editar itens da OS.')) return;
    this.editandoItem = { id: item.id, tipo: item.tipo };
    this.incluir.descricao = item.descricao;
    this.incluir.quantidade = Number(item.qtd || 1);
    this.incluir.valor = Number(item.preco || 0);
    this.incluir.valorCusto = Number(item.original?.precoCusto || item.original?.custo || 0);
    this.calcularTotal();
    if (item.tipo === 'produto') {
      this.incluirTabIndex = 2;
      this.incluir.produtoId = item.original?.produtoId || item.original?.id || undefined;
    } else {
      this.incluirTabIndex = 0;
      this.incluir.servicoId = item.original?.servicoId || item.original?.id || undefined;
    }
    this.incluirDialogVisible = true;
  }

  // Pagamento / Negociação legado. O cockpit deve encaminhar para o owner Financeiro.
  pagamentoDialogVisible = false;
  reciboDialogVisible = false;
  ultimoPagamentoId?: number;
  pagamento = {
    desconto: 0,
    valorPagar: 0,
    juros: 0,
    forma: 1,
    parcelas: 1,
    contaDestino: 1,
    parcelasList: [] as any[],
    totalNegociado: 0,
  };

  parcelasOptions = Array.from({ length: 12 }, (_, i) => ({
    label: `${i + 1} Parcela${i + 1 > 1 ? 's' : ''}`,
    value: i + 1,
  }));
  hoje = new Date();
  formaPagamentoOptions: { label: string; value: number }[] = [];
  contaDestinoOptions: { label: string; value: number }[] = [];

  abrirPagamentoDialog() {
    if (!this.requireCapability(this.canNegotiatePayment, 'Você não possui permissão para negociar pagamento desta OS.')) return;
    this.router.navigate(['/financeiro/receber'], {
      queryParams: { ordemServicoId: this.orcamentoNumero },
    });
  }

  carregarFotosOS(osId: number) {
    this.osService.listOsFotos(osId).subscribe({
      next: (list) => (this.osFotos = list || []),
      error: () => {
        this.osFotos = [];
        this.markPartialSource('Evidências e fotos');
      },
    });
  }

  abrirDialogFoto() {
    if (!this.requireCapability(this.canUploadPhotos, 'Você não possui permissão para enviar fotos desta OS.')) return;
    this.fotoDescricao = '';
    this.fotoFile = null;
    this.showFotoDialog = true;
  }

  onFotoSelected(event: any) {
    if (event.files?.length > 0) this.fotoFile = event.files[0];
  }

  salvarFotoOS() {
    if (!this.orcamentoNumero || !this.fotoFile || !this.canUploadPhotos) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione uma imagem válida' });
      return;
    }
    this.osService.uploadOsFoto(this.orcamentoNumero, this.fotoFile, this.fotoDescricao).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Foto adicionada' });
        this.showFotoDialog = false;
        this.carregarFotosOS(this.orcamentoNumero);
      },
      error: (err) => this.handleMutationError(err, 'Falha ao enviar foto'),
    });
  }

  private scrollToFotos() {
    const el = document.getElementById('os-fotos-panel');
    if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  visualizarFotoOS(f: any) {
    this.selectedFotoUrl = f?.arquivoUrl || '';
    this.selectedFotoDesc = f?.descricao || '';
    this.showFotoViewDialog = true;
  }

  excluirFotoOS(f: any) {
    if (!f?.id || !this.canUploadPhotos) return;
    this.osService.deleteOsFoto(f.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Foto excluída' });
        this.carregarFotosOS(this.orcamentoNumero);
      },
      error: (err) => this.handleMutationError(err, 'Falha ao excluir foto'),
    });
  }

  fecharPagamentoDialog() {
    this.pagamentoDialogVisible = false;
  }

  private localDate(d: Date = new Date()): string {
    return new Date(d.getTime() - d.getTimezoneOffset() * 60000).toISOString().slice(0, 10);
  }

  isPagamentoValido(): boolean {
    if (!this.pagamento.parcelasList?.length) return false;
    const p = Number(this.pagamento.valorPagar || 0);
    const n = Number(this.pagamento.totalNegociado || 0);
    return Math.abs(p - n) < 0.05 && p > 0;
  }

  calcularPagamento() {
    const desc = Number(this.pagamento.desconto) || 0;
    const juros = Number(this.pagamento.juros) || 0;
    const base = authoritativeOsTotal(this.currentOS);
    const valorComDesconto = base * (1 - desc / 100);
    const valorFinal = valorComDesconto * (1 + juros / 100);
    this.pagamento.valorPagar = Number(valorFinal.toFixed(2));

    const parcelas = Math.max(1, Number(this.pagamento.parcelas) || 1);
    const valorParcelaBase = Math.floor((valorFinal / parcelas) * 100) / 100;
    let somaParcelas = 0;
    const lista: any[] = [];
    const hoje = new Date();
    for (let i = 0; i < parcelas; i++) {
      const venc = new Date(hoje.getTime());
      venc.setDate(hoje.getDate() + i * 30);
      let valorDaParcela = valorParcelaBase;
      if (i === parcelas - 1) {
        valorDaParcela = Number((this.pagamento.valorPagar - somaParcelas).toFixed(2));
      }
      somaParcelas += valorDaParcela;
      lista.push({
        parcela: i + 1,
        contaDestino: this.pagamento.contaDestino,
        formaPagamento: this.pagamento.forma,
        vencimento: this.localDate(venc),
        pagamento: '',
        valor: valorDaParcela,
        situacao: 'Pendente',
        pago: false,
      });
    }
    this.pagamento.parcelasList = lista;
    this.pagamento.totalNegociado = Number(lista.reduce((s, p) => s + Number(p.valor || 0), 0).toFixed(2));
  }

  marcarParcelaQuitada(idx: number) {
    const p = this.pagamento.parcelasList[idx];
    if (!p) return;
    p.pago = !p.pago;
    p.situacao = p.pago ? 'Quitado' : 'Pendente';
    p.pagamento = p.pago ? this.localDate() : '';
  }

  removerParcela(idx: number) {
    this.pagamento.parcelasList.splice(idx, 1);
    this.pagamento.totalNegociado = Number(
      this.pagamento.parcelasList.reduce((s, p) => s + Number(p.valor || 0), 0).toFixed(2)
    );
  }

  salvarPagamento() {
    this.messageService.add({
      severity: 'info',
      summary: 'Fluxo financeiro separado',
      detail: 'Registre ou negocie o recebimento no módulo Financeiro, que é o owner deste processo.',
    });
    this.abrirPagamentoDialog();
  }

  abrirRecibo() {
    this.reciboDialogVisible = true;
  }

  mostrarRecibo() {
    if (this.pagamentosExistentes.length > 0) {
      const p =
        this.pagamentosExistentes.find((x) => ['CONFIRMADO', 'PAGO'].includes(String(x.status || '').toUpperCase())) ||
        this.pagamentosExistentes[0];
      this.ultimoPagamentoId = p.id;
    }
    this.reciboDialogVisible = true;
  }

  imprimirComprovante(pagamentoId?: number) {
    const id = pagamentoId || this.ultimoPagamentoId;
    if (!id) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Nenhum pagamento identificado para impressão' });
      return;
    }
    this.isGeneratingPdf = true;
    this.osService.imprimirComprovante(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        window.open(url);
        this.isGeneratingPdf = false;
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Comprovante gerado com sucesso!' });
      },
      error: () => {
        this.isGeneratingPdf = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível gerar o comprovante' });
      },
    });
  }

  onAction(key: string) {
    switch (key) {
      case 'imprimir':
        if (!this.requireCapability(this.canPrintOS, 'Você não possui permissão para imprimir esta OS.')) return;
        if (this.orcamentoNumero) {
          this.isGeneratingPdf = true;
          const print$ = this.isOrcamento
            ? this.osService.imprimirOrcamento(this.orcamentoNumero)
            : this.osService.imprimir(this.orcamentoNumero);
          print$.subscribe({
            next: (blob) => {
              const url = window.URL.createObjectURL(blob);
              window.open(url);
              this.isGeneratingPdf = false;
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'PDF gerado com sucesso!' });
            },
            error: () => {
              this.isGeneratingPdf = false;
              this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível gerar o PDF' });
            },
          });
        }
        break;
      case 'editar':
        if (this.requireCapability(this.canEditOS, 'Você não possui permissão para editar esta OS.')) this.editing = !this.editing;
        break;
      case 'salvar':
        this.salvarCabecalho();
        break;
      case 'prodserv':
        this.abrirIncluirDialog();
        break;
      case 'pagamento':
        this.abrirPagamentoDialog();
        break;
      case 'solicitacao':
        this.abrirSolicitacaoDialog();
        break;
      case 'checklist':
        this.abrirChecklistDialog();
        break;
      case 'fotos':
        if (this.orcamentoNumero) this.carregarFotosOS(this.orcamentoNumero);
        this.scrollToFotos();
        break;
      case 'emitir-nfe':
        if (this.isOrcamento) {
          this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Converta o orçamento para OS antes de acessar o Fiscal.' });
          return;
        }
        this.router.navigate(['/fiscal/nfe/nfe-lista'], {
          queryParams: { ordemServicoId: this.orcamentoNumero },
        });
        break;
      case 'converter-orcamento':
        this.converterOrcamento();
        break;
    }
  }

  private applyOS(os: OrdemServicoResponse) {
    this.currentOS = os;
    this.isOrcamento = os?.tipoOS === TipoOS.ORCAMENTO;
    this.placa = os?.placaVeiculo || '';
    this.veiculo = os?.nomeVeiculo || '';
    this.cliente = os?.nomeCliente || '';
    const abertura = os?.dataAbertura || '';
    this.dataEntrada = {
      data: abertura ? abertura.slice(0, 10) : '',
      hora: abertura ? abertura.slice(11, 16) : '',
    };
    this.status = os?.statusNome || '';
    this.statusId = os?.statusId;
    this.quilometragem = os?.quilometragemEntrada != null ? String(os.quilometragemEntrada) : '';
    this.total = authoritativeOsTotal(os);

    if (os?.consultorResponsavelId) {
      this.atualizarResponsavelNome(os.consultorResponsavelId);
    } else {
      this.responsavel = '';
    }

    const promessa = os?.dataPromessa || '';
    this.previsaoSaidaDate = promessa ? promessa.slice(0, 10) : '';
    this.previsaoSaidaHora = promessa ? promessa.slice(11, 16) : '';
    this.observacaoInterna = os?.observacoesInternas || '';
    this.descricaoCliente = os?.observacoesCliente || '';

    if (os?.comentarios) {
      try {
        this.comentarios = JSON.parse(os.comentarios);
      } catch {
        this.comentarios = [];
        this.markPartialSource('Diário da OS');
      }
    } else {
      this.comentarios = [];
    }
  }

  atualizarResponsavelNome(id: number) {
    if (!id) return;
    const found = this.responsavelOptions.find((o) => {
      const match = String(o.value || '').match(/^\s*(\d+)/);
      return match && Number(match[1]) === id;
    });
    this.responsavel = found?.value || '';
  }

  private findStatusIdAberta(): number | undefined {
    const byCodigo = this.statusLista.find((st) => (st.codigo || '').toUpperCase() === 'ABERTA');
    if (byCodigo?.id) return byCodigo.id;
    const byNome = this.statusLista.find((st) => (st.nome || '').toLowerCase() === 'aberta');
    return byNome?.id;
  }

  private converterOrcamento() {
    if (!this.isOrcamento || !this.orcamentoNumero) return;
    if (!this.requireCapability(this.canEditOS, 'Você não possui permissão para converter este registro.')) return;

    const statusAbertaId = this.findStatusIdAberta();
    if (!statusAbertaId) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Status indisponível',
        detail: 'Não foi possível identificar o status ABERTA no catálogo. A conversão foi bloqueada para evitar estado inválido.',
      });
      return;
    }

    const dto: Omit<OrdemServicoRequest, 'empresaId'> = {
      numeroOS: this.currentOS?.numeroOS || String(this.orcamentoNumero),
      tipoOS: TipoOS.MANUTENCAO,
      valorTotal: authoritativeOsTotal(this.currentOS),
      statusId: statusAbertaId,
    };
    this.osService.update(this.orcamentoNumero, dto).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Orçamento convertido em OS' });
        this.router.navigate(['/os/visualizar-os', this.orcamentoNumero]);
      },
      error: (err) => this.handleMutationError(err, 'Falha ao converter orçamento'),
    });
  }

  private parseResponsavelId(): number | undefined {
    const match = String(this.responsavel || '').trim().match(/^\s*(\d+)/);
    return match ? Number(match[1]) : undefined;
  }

  salvarCabecalho() {
    if (!this.orcamentoNumero) return;
    if (!this.requireCapability(this.canEditOS, 'Você não possui permissão para salvar alterações nesta OS.')) return;

    const dto: Omit<OrdemServicoRequest, 'empresaId'> = {
      numeroOS: this.currentOS?.numeroOS || String(this.orcamentoNumero),
      tipoOS: this.currentOS?.tipoOS || TipoOS.MANUTENCAO,
      valorTotal: authoritativeOsTotal(this.currentOS),
      quilometragemEntrada: Number(this.quilometragem) || this.currentOS?.quilometragemEntrada,
      consultorResponsavelId: this.parseResponsavelId() || this.currentOS?.consultorResponsavelId,
      observacoesInternas: this.observacaoInterna,
      observacoesCliente: this.descricaoCliente,
      comentarios: JSON.stringify(this.comentarios),
      dataPromessa: this.composeDateTime(this.previsaoSaidaDate, this.previsaoSaidaHora),
      statusId: this.statusId,
    };

    this.osService.update(this.orcamentoNumero, dto).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Alterações salvas' });
        this.carregarOS(this.orcamentoNumero);
      },
      error: (err) => this.handleMutationError(err, 'Não foi possível salvar a OS'),
    });
  }

  private handleMutationError(err: any, fallback: string) {
    if (err?.status === 409) {
      this.cockpitState = 'conflict';
      this.cockpitMessage = 'A OS foi alterada em outro contexto. Recarregue antes de repetir a operação.';
      this.messageService.add({ severity: 'warn', summary: 'Conflito de versão', detail: this.cockpitMessage });
      return;
    }

    if (err?.status === 403) {
      this.messageService.add({ severity: 'warn', summary: 'Sem permissão', detail: 'A operação foi recusada pelo servidor.' });
      return;
    }

    this.messageService.add({ severity: 'error', summary: 'Erro', detail: err?.error?.message || fallback });
  }

  private composeDateTime(date: string, time: string): string | undefined {
    const d = (date || '').trim();
    const t = (time || '').trim();
    if (!d || !t) return undefined;
    return `${d}T${t}:00`;
  }
}
