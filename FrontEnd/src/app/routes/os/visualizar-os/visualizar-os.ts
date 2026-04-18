import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

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
// PrimeNG v20 tabs (standalone components)
import { Tabs, TabList, Tab, TabPanels, TabPanel } from 'primeng/tabs';
import { MenuModule } from 'primeng/menu';
import { ToolbarModule } from 'primeng/toolbar';
import { CardModule } from 'primeng/card';
import { AvatarModule } from 'primeng/avatar';

// Shared
import { OrdemServicoService } from '../ordem-servico.service';
import { StatusOSService } from '../status-os.service';
import { OrdemServicoResponse, OrdemServicoRequest, ItemOSProdutoRequest, ItemOSServicoRequest, DiagnosticoRequest, TipoOS, StatusOSResponse } from '../models/os.models';

interface ViewItem {
  id: number;
  descricao: string;
  qtd: number;
  preco: number;
  total: number;
  tipo: 'servico' | 'produto';
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
    // Tabs API
    Tabs,
    TabList,
    Tab,
    TabPanels,
    TabPanel,
    DialogModule,
    MenuModule,
    FileUploadModule,
  ],
  providers: [MessageService, ConfirmationService]
})
export class VisualizarOS implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly osService = inject(OrdemServicoService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly statusService = inject(StatusOSService);

  orcamentoNumero = 0;
  currentOS?: OrdemServicoResponse;
  isOrcamento = false;

  menuPS: MenuItem[] = [];
  menuSolic: MenuItem[] = [];
  menuChecklist: MenuItem[] = [];

  constructor() {}

  ngOnInit() {
    this.menuPS = [{ label: 'Incluir Item', icon: 'pi pi-plus', command: () => this.abrirIncluirDialog() }];
    this.menuSolic = [{ label: 'Nova Solicitação', icon: 'pi pi-plus', command: () => this.abrirSolicitacaoDialog() }];
    this.menuChecklist = [
      { label: 'Selecionar Checklist', icon: 'pi pi-list-check', command: () => this.abrirChecklistDialog() },
      { label: 'Ver itens do modelo', icon: 'pi pi-eye', command: () => this.checklistSelecionado ? this.verItensChecklist(this.checklistSelecionado) : null }
    ];

    const id = this.route.snapshot.paramMap.get('id') || this.route.snapshot.paramMap.get('numero');
    const n = id ? Number(id) : undefined;
    if (n && !Number.isNaN(n)) {
      this.orcamentoNumero = n;
      this.carregarOS(n);
    }

    // Carrega lista de status para mapear nome -> id
    this.statusService.list({ ativo: true }).subscribe({
      next: (page) => this.statusLista = page?.content || [],
      error: () => this.statusLista = []
    });
  }

  carregarOS(id: number) {
    this.osService.getById(id).subscribe({
      next: (os) => {
        this.applyOS(os);
        this.carregarItens(id);
        this.carregarSolicitacoes(id);
        this.carregarChecklistsOS(id);
        this.carregarPagamentosOS(id);
        this.carregarFotosOS(id);
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar a OS' });
      },
    });
  }

  carregarItens(osId: number) {
    this.itens = [];
    this.totalServicos = 0;
    this.totalProdutos = 0;
    this.total = 0;

    // Carregar Produtos
    this.osService.getProdutos(osId).subscribe(produtos => {
      const mappedProds = produtos.map(p => ({
        id: p.id,
        descricao: p.descricao || p.nomeProduto || 'Produto',
        qtd: p.quantidade,
        preco: p.valorUnitario,
        total: p.valorTotal,
        tipo: 'produto' as const,
        original: p
      }));
      this.itens.push(...mappedProds);
      this.calculateTotals();
    });

    // Carregar Serviços
    this.osService.getServicos(osId).subscribe(servicos => {
      const mappedServs = servicos.map(s => ({
        id: s.id,
        descricao: s.descricao || s.nomeServico || 'Serviço',
        qtd: s.quantidade || 1,
        preco: s.valorUnitario,
        total: s.valorTotal,
        tipo: 'servico' as const,
        original: s
      }));
      this.itens.push(...mappedServs);
      this.calculateTotals();
    });
  }

  calculateTotals() {
    this.totalProdutos = this.itens.filter(i => i.tipo === 'produto').reduce((acc, cur) => acc + cur.total, 0);
    this.totalServicos = this.itens.filter(i => i.tipo === 'servico').reduce((acc, cur) => acc + cur.total, 0);
    this.total = this.totalProdutos + this.totalServicos;
  }

  carregarSolicitacoes(osId: number) {
    this.osService.getDiagnosticos(osId).subscribe(diags => {
      this.solicitacoes = diags.map(d => ({
        id: d.id,
        quantidade: 1,
        descricao: d.problemaIdentificado || d.observacoes || 'Sem descrição',
        codigoOriginal: '',
        codigo: d.codigoErro || '',
        idSolicitacao: d.id
      }));
    });
  }

  // Cabeçalho
  placa = '';
  veiculo = '';
  cliente = '';
  dataEntrada = { data: '', hora: '' };
  status = 'Aguardando';
  statusId?: number;
  statusLista: StatusOSResponse[] = [];
  quilometragem = '';
  previsaoSaidaDate = '';
  previsaoSaidaHora = '';
  editing = false;

  // Modal de Status
  statusDialogVisible = false;
  statusSeverity: Record<string, 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast' | null | undefined> = {
    Aguardando: 'warn',
    Aprovado: 'success',
    Negado: 'danger',
    Entregue: 'success',
  };
  statusIcon: Record<string, string> = {
    Aguardando: 'pi pi-clock',
    Aprovado: 'pi pi-check',
    Negado: 'pi pi-times',
    Entregue: 'pi pi-send',
  };
  abrirStatusDialog() { this.statusDialogVisible = true; }
  fecharStatusDialog() { this.statusDialogVisible = false; }
  setStatus(s: string) {
    this.status = s;
    const key = (s || '').toLowerCase();
    let found = this.statusLista.find(st => (st.nome || '').toLowerCase() === key);
    if (!found) {
      const codeMap: Record<string, string> = {
        aguardando: 'AGUARDANDO',
        aprovado: 'APROVADO',
        negar: 'NEGADO',
        negado: 'NEGADO',
        entregue: 'ENTREGUE',
        aberta: 'ABERTA'
      };
      const codigo = codeMap[key];
      if (codigo) {
        found = this.statusLista.find(st => (st.codigo || '').toUpperCase() === codigo);
      }
    }
    this.statusId = found?.id ?? this.statusId;
    this.fecharStatusDialog();
  }

  situacao = 'ALINHAMENTO';
  localizacao = 'ALINHAMENTO';
  responsavel = '';

  // Produtos e serviços
  prodServBusca = '';
  itens: ViewItem[] = [];
  totalServicos = 0.0;
  totalProdutos = 0.0;
  total = 0.0;

  // Solicitações
  solicitacoes: { quantidade: number; descricao: string; codigoOriginal: string; codigo: string; idSolicitacao?: number; id?: number }[] = [];
  solicitacaoDialogVisible = false;
  solicitacao = { descricao: '', quantidade: 1, codigoOriginal: '', codigo: '', duvida: false };

  // Pagamentos
  faturaOS?: any;
  pagamentosExistentes: any[] = [];
  osFotos: any[] = [];
  showFotoDialog = false;
  showFotoViewDialog = false;
  selectedFotoUrl = '';
  selectedFotoDesc = '';
  fotoDescricao = '';
  fotoFile: File | null = null;
  carregarPagamentosOS(osId: number) {
    this.faturaOS = undefined;
    this.pagamentosExistentes = [];
    this.osService.getFaturaPorOS(osId).subscribe({
      next: (fat) => {
        this.faturaOS = fat;
        if (fat?.id) {
          this.osService.listPagamentosPorFatura(fat.id).subscribe({
            next: (page) => this.pagamentosExistentes = page?.content || [],
            error: () => this.pagamentosExistentes = []
          });
        }
      },
      error: () => {
        this.faturaOS = undefined;
        this.pagamentosExistentes = [];
      }
    });
  }
  editandoSolicitacaoId: number | null = null;

  abrirSolicitacaoDialog() {
    this.solicitacaoDialogVisible = true;
    this.solicitacao = { descricao: '', quantidade: 1, codigoOriginal: '', codigo: '', duvida: false };
    this.editandoSolicitacaoId = null;
  }
  fecharSolicitacaoDialog() { this.solicitacaoDialogVisible = false; }

  incluirSolicitacao() {
    if (!this.orcamentoNumero) return;
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
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: this.editandoSolicitacaoId ? 'Solicitação atualizada' : 'Solicitação adicionada' });
        this.fecharSolicitacaoDialog();
        this.carregarSolicitacoes(this.orcamentoNumero);
        this.editandoSolicitacaoId = null;
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar a solicitação' })
    });
  }

  editarSolicitacao(s: { quantidade: number; descricao: string; codigoOriginal: string; codigo: string; idSolicitacao?: number; id?: number }) {
    this.solicitacaoDialogVisible = true;
    this.solicitacao = {
      descricao: s.descricao,
      quantidade: s.quantidade || 1,
      codigoOriginal: s.codigoOriginal || '',
      codigo: s.codigo || '',
      duvida: false
    };
    this.editandoSolicitacaoId = s.idSolicitacao || s.id || null;
  }

  excluirSolicitacao(s: { idSolicitacao?: number; id?: number; descricao: string }) {
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
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao excluir solicitação' })
        });
      }
    });
  }

  // Checklist
  checklistMsg = 'Nenhum checklist cadastrada.';
  checklistDialogVisible = false;
  checklistLista: any[] = [];
  checklistSelecionado: any | null = null;
  checklistsOS: any[] = [];
  checklistPreview: any[] = [];
  checklistPreviewNome = '';

  abrirChecklistDialog() {
    this.checklistDialogVisible = true;
    this.checklistSelecionado = null;
    this.buscarChecklists('');
  }
  fecharChecklistDialog() { this.checklistDialogVisible = false; }

  buscarChecklists(texto: string) {
    this.osService.listChecklists(texto).subscribe({
      next: (page) => this.checklistLista = page?.content || [],
      error: () => this.checklistLista = [],
    });
  }

  selecionarChecklist(c: any) {
    this.checklistSelecionado = c;
  }

  verItensChecklist(c: any) {
    this.checklistPreview = [];
    this.checklistPreviewNome = c?.dsChecklist || c?.nome || ('Checklist #' + c?.id);
    if (!c?.id) return;
    this.osService.getChecklistModeloItens(c.id).subscribe({
      next: (list) => this.checklistPreview = list || [],
      error: () => this.checklistPreview = []
    });
  }

  adicionarChecklist() {
    if (!this.orcamentoNumero || !this.checklistSelecionado?.id) return;
    this.osService.addOSChecklist(this.orcamentoNumero, this.checklistSelecionado.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Checklist adicionada à OS' });
        this.fecharChecklistDialog();
        this.carregarChecklistsOS(this.orcamentoNumero);
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao adicionar checklist' }),
    });
  }

  carregarChecklistsOS(osId: number) {
    this.osService.getOSChecklists(osId).subscribe({
      next: (list) => {
        this.checklistsOS = list || [];
        this.checklistMsg = this.checklistsOS.length ? '' : 'Nenhum checklist cadastrada.';
      },
      error: () => {
        this.checklistsOS = [];
        this.checklistMsg = 'Nenhum checklist cadastrada.';
      }
    });
  }

  toggleChecklistItem(item: any) {
    if (!item?.id) return;
    const original = !!item.feito;
    this.osService.updateOSChecklistItem(item.id, { feito: !!item.feito }).subscribe({
      next: () => {},
      error: () => {
        item.feito = original;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao atualizar item' });
      }
    });
  }

  // Observações
  observacaoInterna = '';
  descricaoCliente = '';

  // Comentários
  comentarios: any[] = [];
  novoComentario = '';
  adicionarComentario() {
    if (!this.novoComentario.trim()) return;
    this.comentarios.push({ autor: 'Você', data: new Date().toLocaleDateString('pt-BR'), texto: this.novoComentario });
    this.novoComentario = '';
  }

  // Popup "Incluir" (Produtos e Serviços)
  incluirDialogVisible = false;
  incluirTabIndex = 0;
  funcionarioOptions = [{ label: '1 - ALEXANDRE ROM', value: '1 - ALEXANDRE ROM' }];
  setorOptions = [{ label: 'Sem escolher', value: 'NONE' }];

  incluir = {
    funcionario: '1 - ALEXANDRE ROM',
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
  servicosCadastrados: any[] = []; // Mantido para compatibilidade se algo usar
  estoqueLista: any[] = []; // Mantido para compatibilidade se algo usar

  abrirIncluirDialog() {
    this.incluirDialogVisible = true;
    this.incluirTabIndex = 0;
    this.incluir.buscaTexto = '';
    this.listaProdutos = [];
    this.listaServicos = [];
    this.incluir.descricao = '';
    this.incluir.quantidade = 1;
    this.incluir.valor = 0;
    this.incluir.total = 0;
    this.incluir.produtoId = undefined;
    this.incluir.servicoId = undefined;
    this.editandoItem = null;
  }
  fecharIncluirDialog() { this.incluirDialogVisible = false; }

  calcularTotal() {
    const q = Number(this.incluir.quantidade) || 0;
    const v = Number(this.incluir.valor) || 0;
    this.incluir.total = q * v;
  }

  onSearchInput(event: any) {
    this.incluir.buscaTexto = event.target?.value || '';
  }

  buscarProdutos(texto?: string) {
    const query = texto || this.incluir.buscaTexto;
    this.osService.searchProdutos(query).subscribe({
      next: (page) => this.listaProdutos = page.content || [],
      error: () => this.listaProdutos = []
    });
  }

  buscarServicos(texto?: string) {
    const query = texto || this.incluir.buscaTexto;
    this.osService.searchServicos(query).subscribe({
      next: (page) => this.listaServicos = page.content || [],
      error: () => this.listaServicos = []
    });
  }

  selecionarProduto(p: any) {
    this.incluir.descricao = p.nome;
    this.incluir.valor = p.precoVenda || 0;
    this.incluir.quantidade = 1;
    this.incluir.produtoId = p.id;
    this.calcularTotal();
    this.incluirTabIndex = 2; // Aba Produto
  }

  selecionarServico(s: any) {
    this.incluir.descricao = s.nome;
    this.incluir.valor = s.valorVenda || 0;
    this.incluir.quantidade = 1;
    this.incluir.servicoId = s.id;
    this.calcularTotal();
    this.incluirTabIndex = 0; // Aba Serviço
  }

  editandoItem: { id: number; tipo: 'produto' | 'servico' } | null = null;

  incluirItem() {
    if (!this.orcamentoNumero) return;

    if (this.incluirTabIndex === 0 || this.incluirTabIndex === 1) {
      // Serviço
      const req: ItemOSServicoRequest = {
        ordemServicoId: this.orcamentoNumero,
        servicoId: this.incluir.servicoId,
        descricao: this.incluir.descricao,
        quantidade: Number(this.incluir.quantidade),
        valorUnitario: Number(this.incluir.valor),
        valorTotal: Number(this.incluir.total),
        valorFinal: Number(this.incluir.total)
      };
      if (this.editandoItem?.tipo === 'servico') {
        this.osService.updateServico(this.editandoItem.id, req).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Serviço atualizado' });
            this.carregarItens(this.orcamentoNumero);
            this.fecharIncluirDialog();
            this.editandoItem = null;
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao atualizar serviço' }),
        });
      } else {
        this.osService.addServico(req).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Serviço adicionado' });
            this.carregarItens(this.orcamentoNumero);
            this.fecharIncluirDialog();
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao adicionar serviço' }),
        });
      }
    } else {
      // Produto
      const req: ItemOSProdutoRequest = {
        ordemServicoId: this.orcamentoNumero,
        produtoId: this.incluir.produtoId,
        descricao: this.incluir.descricao,
        quantidade: Number(this.incluir.quantidade),
        valorUnitario: Number(this.incluir.valor),
        valorTotal: Number(this.incluir.total),
        valorFinal: Number(this.incluir.total)
      };
      if (this.editandoItem?.tipo === 'produto') {
        this.osService.updateProduto(this.editandoItem.id, req).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto atualizado' });
            this.carregarItens(this.orcamentoNumero);
            this.fecharIncluirDialog();
            this.editandoItem = null;
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao atualizar produto' }),
        });
      } else {
        this.osService.addProduto(req).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto adicionado' });
            this.carregarItens(this.orcamentoNumero);
            this.fecharIncluirDialog();
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao adicionar produto' }),
        });
      }
    }
  }

  removerItem(item: ViewItem) {
    this.confirmationService.confirm({
      message: `Deseja remover o item "${item.descricao}"?`,
      header: 'Confirmação',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (item.tipo === 'produto') {
          this.osService.deleteProduto(item.id).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto removido' });
              this.carregarItens(this.orcamentoNumero);
            },
            error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao remover produto' }),
          });
        } else {
          this.osService.deleteServico(item.id).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Serviço removido' });
              this.carregarItens(this.orcamentoNumero);
            },
            error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao remover serviço' }),
          });
        }
      }
    });
  }

  editarItem(item: ViewItem) {
    this.editandoItem = { id: item.id, tipo: item.tipo };
    this.incluir.descricao = item.descricao;
    this.incluir.quantidade = Number(item.qtd || 1);
    this.incluir.valor = Number(item.preco || 0);
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

  // Pagamento / Negociação
  pagamentoDialogVisible = false;
  reciboDialogVisible = false;
  pagamento = {
    desconto: 0,
    valorPagar: 0,
    juros: 0,
    forma: 'CARTAO',
    parcelas: 1,
    contaDestino: 'BANCO',
    parcelasList: [] as any[],
    totalNegociado: 0,
  };

  parcelasOptions = Array.from({ length: 12 }, (_, i) => ({ label: `${i + 1} Parcela${i + 1 > 1 ? 's' : ''}`, value: i + 1 }));
  hoje = new Date();
  formaPagamentoOptions: { label: string; value: number }[] = [];
  contaDestinoOptions: { label: string; value: number }[] = [];

  abrirPagamentoDialog() {
    this.pagamentoDialogVisible = true;
    this.pagamento.desconto = 0;
    this.pagamento.juros = 0;
    this.pagamento.forma = 'CARTAO';
    this.pagamento.parcelas = 1;
    this.pagamento.parcelasList = [];
    this.pagamento.totalNegociado = 0;
    this.pagamento.valorPagar = Number((this.total || 0).toFixed(2));

    this.osService.listFormasPagamento().subscribe({
      next: (page) => this.formaPagamentoOptions = (page?.content || []).map((f: any) => ({ label: f.nome, value: f.id })),
      error: () => this.formaPagamentoOptions = []
    });
    this.osService.listContasBancarias().subscribe({
      next: (page) => this.contaDestinoOptions = (page?.content || []).map((c: any) => ({ label: `${c.bancoNome} • ${c.agencia}/${c.conta}`, value: c.id })),
      error: () => this.contaDestinoOptions = []
    });

    // Verificar fatura e pagamentos existentes para esta OS
    if (this.orcamentoNumero) {
      this.osService.getFaturaPorOS(this.orcamentoNumero).subscribe({
        next: (fat) => {
          if (fat?.id) {
            this.osService.listPagamentosPorFatura(fat.id).subscribe({
              next: (page) => {
                const pagos = page?.content || [];
                if (pagos.length) {
                  this.messageService.add({ severity: 'info', summary: 'Pagamento', detail: 'Já existe pagamento registrado para esta OS' });
                }
              }
            });
          }
        }
      });
    }
  }

  carregarFotosOS(osId: number) {
    this.osService.listOsFotos(osId).subscribe({
      next: (list) => this.osFotos = list || [],
      error: () => this.osFotos = []
    });
  }

  abrirDialogFoto() {
    this.fotoDescricao = '';
    this.fotoFile = null;
    this.showFotoDialog = true;
  }

  onFotoSelected(event: any) {
    if (event.files && event.files.length > 0) this.fotoFile = event.files[0];
  }

  salvarFotoOS() {
    if (!this.orcamentoNumero || !this.fotoFile) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione uma imagem' });
      return;
    }
    this.osService.uploadOsFoto(this.orcamentoNumero, this.fotoFile, this.fotoDescricao).subscribe({
      next: (res) => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Foto adicionada' });
        this.showFotoDialog = false;
        this.carregarFotosOS(this.orcamentoNumero);
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao enviar foto' })
    });
  }

  private scrollToFotos() {
    try {
      const el = document.getElementById('os-fotos-panel');
      if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' });
    } catch {}
  }

  visualizarFotoOS(f: any) {
    this.selectedFotoUrl = f?.arquivoUrl || '';
    this.selectedFotoDesc = f?.descricao || '';
    this.showFotoViewDialog = true;
  }

  excluirFotoOS(f: any) {
    if (!f?.id) return;
    this.osService.deleteOsFoto(f.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Foto excluída' });
        this.carregarFotosOS(this.orcamentoNumero);
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao excluir foto' })
    });
  }
  fecharPagamentoDialog() { this.pagamentoDialogVisible = false; }

  private localDate(d: Date = new Date()): string {
    return new Date(d.getTime() - d.getTimezoneOffset() * 60000).toISOString().slice(0, 10);
  }

  isPagamentoValido(): boolean {
    if (!this.pagamento.parcelasList || this.pagamento.parcelasList.length === 0) return false;
    const p = Number(this.pagamento.valorPagar || 0).toFixed(2);
    const n = Number(this.pagamento.totalNegociado || 0).toFixed(2);
    return p === n && Number(p) > 0;
  }

  calcularPagamento() {
    const desc = Number(this.pagamento.desconto) || 0;
    const juros = Number(this.pagamento.juros) || 0;
    const base = Number(this.total) || 0;
    const valorComDesconto = base * (1 - desc / 100);
    const valorFinal = valorComDesconto * (1 + juros / 100);
    this.pagamento.valorPagar = Number(valorFinal.toFixed(2));

    const parcelas = Math.max(1, Number(this.pagamento.parcelas) || 1);
    const valorParcela = Number((valorFinal / parcelas).toFixed(2));
    const lista: any[] = [];
    const hoje = new Date();
    for (let i = 0; i < parcelas; i++) {
      const venc = new Date(hoje.getTime());
      venc.setDate(hoje.getDate() + i * 30);
      lista.push({
        parcela: i + 1,
        contaDestino: this.pagamento.contaDestino,
        formaPagamento: this.pagamento.forma,
        vencimento: this.localDate(venc),
        pagamento: '',
        valor: valorParcela,
        situacao: 'Pendente',
        pago: false,
      });
    }
    this.pagamento.parcelasList = lista;
    this.pagamento.totalNegociado = lista.reduce((s, p) => s + Number(p.valor || 0), 0);
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
    this.pagamento.totalNegociado = this.pagamento.parcelasList.reduce((s, p) => s + Number(p.valor || 0), 0);
  }

  salvarPagamento() {
    if (!this.isPagamentoValido()) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Valores negociados divergem do valor a pagar!' });
      return;
    }
    const formaId = typeof this.pagamento.forma === 'number' ? this.pagamento.forma : this.formaPagamentoOptions[0]?.value;
    const contaId = this.contaDestinoOptions[0]?.value;
    const total = Number(this.total || 0);
    const req: any = {
      clienteId: this.currentOS?.clienteId,
      formaPagamentoId: formaId,
      contaBancariaId: contaId,
      dataPagamento: this.localDate(),
      valorOriginal: total,
      valorDesconto: Number(this.pagamento.desconto) || 0,
      valorJuros: Number(this.pagamento.juros) || 0,
      valorMulta: 0,
      valorTotal: Number(this.pagamento.valorPagar) || total,
      status: 'PENDENTE',
      observacoes: '',
      parcelas: (this.pagamento.parcelasList || []).map((p: any) => ({
        numeroParcela: Number(p.parcela),
        dataVencimento: String(p.vencimento || this.localDate()),
        valorParcela: Number(p.valor || 0),
        valorJuros: 0,
        valorMulta: 0,
        valorDesconto: 0,
        valorPago: p.pago ? Number(p.valor || 0) : 0,
        dataPagamento: p.pago ? this.localDate() : null,
        status: p.pago ? 'CONFIRMADO' : 'PENDENTE',
        observacoes: ''
      }))
    };

    this.osService.createPagamento(req).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Pagamento registrado' });
        this.pagamentoDialogVisible = false;
        this.reciboDialogVisible = true;
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao registrar pagamento' })
    });
  }
  abrirRecibo() { this.reciboDialogVisible = true; }

  onAction(key: string) {
    switch (key) {
      case 'imprimir':
        if (this.orcamentoNumero) {
           this.osService.imprimir(this.orcamentoNumero).subscribe(blob => {
             const url = window.URL.createObjectURL(blob);
             window.open(url);
           });
        }
        break;
      case 'editar': this.editing = !this.editing; break;
      case 'salvar': this.salvarCabecalho(); break;
      case 'prodserv': this.abrirIncluirDialog(); break;
      case 'pagamento': this.abrirPagamentoDialog(); break;
      case 'solicitacao': this.abrirSolicitacaoDialog(); break;
      case 'checklist': this.abrirChecklistDialog(); break;
      case 'fotos':
        if (this.orcamentoNumero) {
          this.carregarFotosOS(this.orcamentoNumero);
        }
        this.scrollToFotos();
        break;
      case 'emitir-nfe':
        if (!this.orcamentoNumero) return;
        if (this.isOrcamento) {
          this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Converta o orçamento para OS antes de emitir NFe' });
          return;
        }
        this.osService.emitirNFePdf(this.orcamentoNumero).subscribe({
          next: (blob) => {
            const url = window.URL.createObjectURL(blob);
            window.open(url);
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Nota Fiscal gerada' });
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao emitir Nota Fiscal' })
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
    this.status = os?.statusNome || this.status;
    this.statusId = os?.statusId ?? this.statusId;
    this.quilometragem = os?.quilometragemEntrada ? String(os.quilometragemEntrada) : this.quilometragem;
    
    // Configura o dropdown responsável
    if (os?.consultorResponsavelId === 1) {
      this.responsavel = '1 - ALEXANDRE ROM';
    }

    const promessa = os?.dataPromessa || '';
    this.previsaoSaidaDate = promessa ? promessa.slice(0, 10) : this.previsaoSaidaDate;
    this.previsaoSaidaHora = promessa ? promessa.slice(11, 16) : this.previsaoSaidaHora;
  }

  private findStatusIdAberta(): number | undefined {
    const byNome = this.statusLista.find(st => (st.nome || '').toLowerCase() === 'aberta');
    if (byNome?.id) return byNome.id;
    const byCodigo = this.statusLista.find(st => (st.codigo || '').toUpperCase() === 'ABERTA');
    return byCodigo?.id || undefined;
  }

  private converterOrcamento() {
    if (!this.isOrcamento || !this.orcamentoNumero) return;
    const dto: Omit<OrdemServicoRequest, 'empresaId'> = {
      numeroOS: this.currentOS?.numeroOS || String(this.orcamentoNumero),
      tipoOS: TipoOS.MANUTENCAO,
      valorTotal: this.currentOS?.valorTotal ?? this.total ?? 0,
      statusId: this.findStatusIdAberta(),
    };
    this.osService.update(this.orcamentoNumero, dto).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Orçamento convertido em OS' });
        this.carregarOS(this.orcamentoNumero);
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao converter orçamento' }),
    });
  }

  private parseResponsavelId(): number | undefined {
    // formato "1 - NOME" => extrai 1
    const match = String(this.responsavel || '').trim().match(/^\s*(\d+)/);
    return match ? Number(match[1]) : undefined;
  }

  salvarCabecalho() {
    if (!this.orcamentoNumero) return;
    
    // Sempre re-resolve o statusId baseado no status selecionado (evitar usar o cached se tiver mudado)
    let idParaSalvar = this.statusId;
    const std = this.status;
    if (std) {
       const found = this.statusLista.find(st => st.nome?.toLowerCase() === std.toLowerCase() || st.codigo?.toUpperCase() === std.toUpperCase());
       if (found) idParaSalvar = found.id;
    }

    const tipo = this.currentOS?.tipoOS || TipoOS.MANUTENCAO;
    const dto: Omit<OrdemServicoRequest, 'empresaId'> = {
      numeroOS: this.currentOS?.numeroOS || String(this.orcamentoNumero),
      tipoOS: tipo,
      valorTotal: this.currentOS?.valorTotal ?? this.total ?? 0,
      quilometragemEntrada: Number(this.quilometragem) || this.currentOS?.quilometragemEntrada,
      consultorResponsavelId: this.parseResponsavelId() || this.currentOS?.consultorResponsavelId,
      observacoesInternas: this.observacaoInterna,
      observacoesCliente: this.descricaoCliente,
      dataPromessa: this.composeDateTime(this.previsaoSaidaDate, this.previsaoSaidaHora),
      statusId: idParaSalvar,
    };
    
    console.log('--- PAYLOAD SALVAR CABECALHO ---', dto);
    
    this.osService.update(this.orcamentoNumero, dto).subscribe({
      next: () => {
         this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Alterações salvas' });
         this.editing = false;
         this.carregarOS(this.orcamentoNumero); // Recarregar para fixar campos
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar' }),
    });
  }

  private composeDateTime(date: string, time: string): string | undefined {
    const d = (date || '').trim();
    const t = (time || '').trim();
    if (!d || !t) return undefined;
    // Expect YYYY-MM-DD and HH:mm
    return `${d}T${t}:00`;
  }
}
