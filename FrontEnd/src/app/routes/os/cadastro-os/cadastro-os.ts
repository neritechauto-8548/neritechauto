import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';

// Services
import { OrdemServicoService } from '../ordem-servico.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { VeiculoService } from '../../veiculo/veiculo/veiculo.service';
import { StatusOSService } from '../status-os.service';
import { ItemOSService } from '../item-os.service';
import { ProdutoService } from '../../produtos-servicos/produto.service';
import { ServicoService } from '../../produtos-servicos/servicos/servico.service';
import { OrcamentoService } from '../../orcamento/orcamento.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { MessageService } from 'primeng/api';

// Models
import {
  OrdemServicoRequest, OrdemServicoResponse,
  TipoOS, NivelCombustivel,
  ItemOSProdutoResponse, ItemOSServicoResponse
} from '../models/os.models';
import { OrcamentoRequest, StatusOrcamento, TipoOrcamento } from '../../orcamento/models/orcamento.models';
import { NfeService } from '../../financeiro/nfe.service';
import { NfeRequest, TipoOperacaoNfe, StatusNfe, AmbienteNfe, TipoEmissaoNfe } from '../../financeiro/models/nfe.models';

// PrimeNG
import { PanelModule } from 'primeng/panel';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { CheckboxModule } from 'primeng/checkbox';
import { DatePickerModule } from 'primeng/datepicker';
import { InputMaskModule } from 'primeng/inputmask';
import { TextareaModule } from 'primeng/textarea';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { InputNumberModule } from 'primeng/inputnumber';
import { TabsModule } from 'primeng/tabs';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'cadastro-os',
  standalone: true,
  templateUrl: './cadastro-os.html',
  styleUrls: ['./cadastro-os.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    PanelModule,
    InputTextModule,
    ButtonModule,
    SelectModule,
    CheckboxModule,
    DatePickerModule,
    InputMaskModule,
    TextareaModule,
    AutoCompleteModule,
    InputNumberModule,
    TabsModule,
    TableModule,
    DialogModule,
    TooltipModule,
    TagModule,
    ConfirmDialogModule,
    ToastModule
  ],
  providers: [ConfirmationService, MessageService]
})
export class CadastroOS implements OnInit {
  private osService = inject(OrdemServicoService);
  private itemService = inject(ItemOSService);
  private clienteService = inject(ClientesService);
  private veiculoService = inject(VeiculoService);
  private statusService = inject(StatusOSService);
  private produtoService = inject(ProdutoService);
  private servicoService = inject(ServicoService);
  private orcamentoService = inject(OrcamentoService);
  private nfeService = inject(NfeService);
  private toast = inject(HotToastService);
  private messageService = inject(MessageService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private confirmationService = inject(ConfirmationService);
  private fb = inject(FormBuilder);

  // Estado
  loading = false;
  osId?: number;
  osData?: OrdemServicoResponse;
  activeIndex = 0; // Tab index
  lockTipoOS = false;

  // Form OS Header
  osForm: FormGroup;

  // Listas Auxiliares
  clientesFiltrados: any[] = [];
  veiculos: any[] = [];
  statusOptions: any[] = [];
  tipoOSOptions = [
    { label: 'Manutenção', value: TipoOS.MANUTENCAO },
    { label: 'Reparo', value: TipoOS.REPARO },
    { label: 'Revisão', value: TipoOS.REVISAO },
    { label: 'Diagnóstico', value: TipoOS.DIAGNOSTICO },
    { label: 'Orçamento', value: TipoOS.ORCAMENTO },
    { label: 'Garantia', value: TipoOS.GARANTIA }
  ];
  funcionarios = [{ label: 'ALEXANDRE ROMULO', value: 'ALEXANDRE ROMULO' }]; // Mock

  // Itens
  itensProduto: ItemOSProdutoResponse[] = [];
  itensServico: ItemOSServicoResponse[] = [];

  // Dialog Produto
  dialogProdutoVisible = false;
  produtoForm: FormGroup;
  produtosFiltrados: any[] = [];

  // Dialog Serviço
  dialogServicoVisible = false;
  servicoForm: FormGroup;
  servicosFiltrados: any[] = [];

  // Dialog Orçamento (Formalização)
  dialogOrcamentoVisible = false;
  orcamentoForm: FormGroup;

  constructor() {
    this.osForm = this.fb.group({
      numeroOS: [{ value: '', disabled: true }],
      cliente: [null, Validators.required],
      veiculoId: [null],
      statusId: [null],
      tipoOS: [null, Validators.required],
      funcionario: ['ALEXANDRE ROMULO'],
      km: [0],
      reservico: [false],
      reboque: [false],
      dataPrevisao: [new Date()],
      horaPrevisao: ['18:00'],
      dataEntrada: [new Date()],
      horaEntrada: ['08:00'],
      observacaoInterna: [''],
      descricaoCliente: ['Todos os nossos serviços e produtos possuem 3 meses de garantia.\n\nObrigado pela preferência!']
    });

    this.produtoForm = this.fb.group({
      produto: [null, Validators.required],
      quantidade: [1, [Validators.required, Validators.min(0.01)]],
      precoUnitario: [0, Validators.required],
      desconto: [0]
    });

    this.servicoForm = this.fb.group({
      servico: [null, Validators.required],
      quantidade: [1, [Validators.required, Validators.min(0.01)]],
      precoUnitario: [0, Validators.required],
      desconto: [0],
      mecanico: [null]
    });

    // Validar dias depois
    const validadePadrao = new Date();
    validadePadrao.setDate(validadePadrao.getDate() + 10);

    this.orcamentoForm = this.fb.group({
        prazoValidade: [10],
        dataVencimento: [validadePadrao],
        condicoesPagamento: ['À vista com 5% de desconto ou 3x no cartão sem juros.'],
        observacoes: ['Orçamento sujeito a análise de estoque na data da aprovação.']
    });
  }

  ngOnInit() {
    this.carregarStatus();
    this.checkRouteParams();
  }

  checkRouteParams() {
    // Verifica se veio ID na rota ou parametro
    const id = this.route.snapshot.params['id'] || this.route.snapshot.queryParams['id'];
    const tipo = this.route.snapshot.queryParams['tipo'];

    if (id) {
        this.osId = Number(id);
        this.loadOS(this.osId);
    } else {
        this.gerarNumeroOS();
        // Se vier tipo na URL (ex: ?tipo=ORCAMENTO), pré-seleciona
        if (tipo && tipo === 'ORCAMENTO') {
             this.osForm.patchValue({ tipoOS: TipoOS.ORCAMENTO });
             this.lockTipoOS = true;
        }
    }
  }

  loadOS(id: number) {
    this.loading = true;
    this.osService.getById(id).pipe(finalize(() => this.loading = false)).subscribe(os => {
      this.osData = os;

      // Parsear equipeExecucao (pode ser string JSON ou array)
      let funcionario = '';
      try {
        const eq = os.equipeExecucao;
        if (Array.isArray(eq) && eq.length > 0) funcionario = eq[0];
        else if (typeof eq === 'string') {
          const parsed = JSON.parse(eq);
          funcionario = Array.isArray(parsed) ? parsed[0] : parsed;
        }
      } catch { funcionario = os.equipeExecucao || ''; }

      this.osForm.patchValue({
        numeroOS: os.numeroOS,
        tipoOS: os.tipoOS,
        statusId: os.statusId,
        funcionario,
        km: os.quilometragemEntrada,
        veiculoId: os.veiculoId,
        observacaoInterna: os.observacoesInternas,
        descricaoCliente: os.observacoesCliente,
        dataEntrada: os.dataAbertura ? new Date(os.dataAbertura) : null,
        dataPrevisao: os.dataPromessa ? new Date(os.dataPromessa) : null,
      });

      // Carregar cliente para popular o p-autoComplete
      if (os.clienteId) {
        this.clienteService.getById(os.clienteId).subscribe(c => {
          const clienteObj = {
            ...c,
            displayName: c.nomeCompleto || c.nomeFantasia || c.razaoSocial || `Cliente #${c.id}`
          };
          this.osForm.patchValue({ cliente: clienteObj });
          // Carregar veículos do cliente
          this.carregarVeiculos(os.clienteId!);
        });
      }

      this.loadItens();
    });
  }

  carregarStatus() {
    this.statusService.list({ ativo: true }).subscribe({
      next: (page) => {
        this.statusOptions = page.content.map(s => ({ label: s.nome, value: s.id }));
        if (this.statusOptions.length > 0 && !this.osId) {
           this.osForm.patchValue({ statusId: this.statusOptions[0].value });
        }
      },
      error: () => this.toast.error('Erro ao carregar status')
    });
  }

  gerarNumeroOS() {
    if (!this.osId) {
      const num = 'OS-' + Math.floor(Math.random() * 100000);
      this.osForm.patchValue({ numeroOS: num });
    }
  }

  // === CLIENTE & VEICULO ===

  filtrarClientes(event: any) {
    this.clienteService.list({ nome: event.query, size: 10 }).subscribe(
      (page) => {
        this.clientesFiltrados = (page.content || []).map((c: any) => ({
          ...c,
          displayName: c.razaoSocial || c.nomeFantasia || c.nomeCompleto || (`Cliente #${c.id}`)
        }));
      }
    );
  }

  onClienteSelect(event: any) {
    const cliente = event.value;
    if (cliente?.id) {
       this.carregarVeiculos(cliente.id);
    } else {
       this.veiculos = [];
       this.osForm.patchValue({ veiculoId: null });
    }
  }

  carregarVeiculos(clienteId: number) {
    this.veiculoService.list(clienteId).subscribe(
      (lista) => {
        this.veiculos = lista.map((v: any) => ({
            label: `${v.modelo?.nome || 'Veículo'} - ${v.placa}`,
            value: v.id
        }));
      }
    );
  }

  // === SALVAR CABEÇALHO ===

  salvarHeader() {
    if (this.osForm.invalid) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha os campos obrigatórios.' });
      return;
    }

    this.loading = true;
    const formVal = this.osForm.getRawValue();

    const dtAbertura = this.combineDateAndTime(formVal.dataEntrada, formVal.horaEntrada);
    const dtPromessa = this.combineDateAndTime(formVal.dataPrevisao, formVal.horaPrevisao);

    const payload: OrdemServicoRequest = {
        empresaId: 1,
        numeroOS: formVal.numeroOS,
        clienteId: formVal.cliente?.id || this.osData?.clienteId, // Fallback se não mexeu
        veiculoId: formVal.veiculoId,
        statusId: formVal.statusId,
        tipoOS: formVal.tipoOS,
        valorTotal: this.getTotalOS() || 0,
        quilometragemEntrada: Number(formVal.km),
        dataAbertura: dtAbertura?.toISOString(),
        dataPromessa: dtPromessa?.toISOString(),
        observacoesInternas: formVal.observacaoInterna,
        observacoesCliente: formVal.descricaoCliente,
        equipeExecucao: JSON.stringify([formVal.funcionario])
    };

    const req$ = this.osId
        ? this.osService.update(this.osId, payload)
        : this.osService.create(payload);

    req$.pipe(finalize(() => this.loading = false))
        .subscribe({
            next: (res) => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: this.osId ? 'OS atualizada!' : 'OS criada! Adicione itens.' });
                this.osId = res.id;
                this.osData = res;
                // Ao criar, navegar para Visualizar OS com o ID
                if (!this.osData) this.activeIndex = 1;
                if (this.osId) {
                  this.router.navigate(['/os/visualizar-os', this.osId]);
                }
            },
            error: (err) => {
                console.error(err);
                this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao salvar OS.' });
            }
        });
  }

  // === ITENS: PRODUTOS ===

  loadItens() {
    if (!this.osId) return;
    this.itemService.listProdutos(this.osId).subscribe(itens => this.itensProduto = itens);
    this.itemService.listServicos(this.osId).subscribe(itens => this.itensServico = itens);
  }

  onTabChange(event: any) {
    if (event.index > 0 && this.osId) {
        this.loadItens();
    }
  }

  abrirDialogProduto() {
    this.produtoForm.reset({ quantidade: 1, desconto: 0 });
    this.dialogProdutoVisible = true;
  }

  filtrarProdutos(event: any) {
    this.produtoService.list({ nome: event.query, size: 10 }).subscribe(
      (res: any) => this.produtosFiltrados = res.content
    );
  }

  onProdutoSelect(event: any) {
    const p = event.value;
    if (p) {
        this.produtoForm.patchValue({ precoUnitario: p.precoVenda });
    }
  }

  adicionarProduto() {
    if (this.produtoForm.invalid || !this.osId) return;
    const val = this.produtoForm.value;
    const total = (val.precoUnitario * val.quantidade) - (val.desconto || 0);

    const item: any = {
        ordemServicoId: this.osId,
        produtoId: val.produto.id,
        descricao: val.produto.nome,
        quantidade: val.quantidade,
        valorUnitario: val.precoUnitario,
        valorTotal: val.precoUnitario * val.quantidade,
        descontoValor: val.desconto,
        valorFinal: total
    };

    this.itemService.addProduto(item).subscribe({
        next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto adicionado' });
            this.dialogProdutoVisible = false;
            this.loadItens();
        },
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao adicionar produto' })
    });
  }

  removerProduto(id: number) {
    this.itemService.deleteProduto(id).subscribe(() => {
        this.messageService.add({ severity: 'success', summary: 'Removido', detail: 'Produto removido' });
        this.loadItens();
    });
  }

  // === ITENS: SERVIÇOS ===

  abrirDialogServico() {
    this.servicoForm.reset({ quantidade: 1, desconto: 0 });
    this.dialogServicoVisible = true;
  }

  filtrarServicos(event: any) {
    this.servicoService.list({ search: event.query, size: 10 }).subscribe(
      (res: any) => this.servicosFiltrados = res.content
    );
  }

  onServicoSelect(event: any) {
    const s = event.value;
    if (s) {
        this.servicoForm.patchValue({ precoUnitario: s.precoBase });
    }
  }

  adicionarServico() {
    if (this.servicoForm.invalid || !this.osId) return;
    const val = this.servicoForm.value;
    const total = (val.precoUnitario * val.quantidade) - (val.desconto || 0);

    const item: any = {
        ordemServicoId: this.osId,
        servicoId: val.servico.id,
        descricao: val.servico.nome,
        quantidade: val.quantidade,
        valorUnitario: val.precoUnitario,
        valorTotal: val.precoUnitario * val.quantidade,
        descontoValor: val.desconto,
        valorFinal: total
    };

    this.itemService.addServico(item).subscribe({
        next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Serviço adicionado' });
            this.dialogServicoVisible = false;
            this.loadItens();
        },
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao adicionar serviço' })
    });
  }

  removerServico(id: number) {
    this.itemService.deleteServico(id).subscribe(() => {
        this.messageService.add({ severity: 'success', summary: 'Removido', detail: 'Serviço removido' });
        this.loadItens();
    });
  }

  getTotalOS() {
    const totalProd = this.itensProduto.reduce((acc, i) => acc + i.valorFinal, 0);
    const totalServ = this.itensServico.reduce((acc, i) => acc + i.valorFinal, 0);
    return totalProd + totalServ;
  }

  // === ORÇAMENTO / PROPOSTA / FORMALIZAÇÃO ===

  get isOrcamento(): boolean {
    return this.osForm.get('tipoOS')?.value === TipoOS.ORCAMENTO;
  }

  get isCriarOrcamento(): boolean {
    return !this.osId && this.isOrcamento;
  }

  abrirDialogOrcamento() {
     if (!this.osId) {
         this.toast.warning('Salve a OS antes de gerar o orçamento.');
         return;
     }
     this.dialogOrcamentoVisible = true;
  }

  gerarProposta() {
    if (!this.osId || this.orcamentoForm.invalid) return;

    const val = this.orcamentoForm.value;

    // Calcula totais baseados nos itens atuais
    const totalServ = this.itensServico.reduce((acc, i) => acc + i.valorFinal, 0);
    const totalProd = this.itensProduto.reduce((acc, i) => acc + i.valorFinal, 0);

    const req: OrcamentoRequest = {
        ordemServicoId: this.osId,
        tipoOrcamento: TipoOrcamento.FORMAL,
        status: StatusOrcamento.PENDENTE,
        valorTotal: totalServ + totalProd,
        valorServicos: totalServ,
        valorProdutos: totalProd,
        valorMaoObra: totalServ,
        valorDesconto: 0,
        valorAcrescimo: 0,
        prazoValidadeDias: val.prazoValidade,
        dataVencimento: val.dataVencimento,
        condicoesPagamento: val.condicoesPagamento,
        observacoesComerciais: val.observacoes
    };

    this.orcamentoService.create(req).subscribe({
        next: (res) => {
            this.messageService.add({ severity: 'success', summary: 'Proposta gerada!', detail: `Nº: ${res.numeroOrcamento}` });
            this.dialogOrcamentoVisible = false;
        },
        error: (err) => {
            console.error(err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao gerar proposta de orçamento.' });
        }
    });

  }


  // === IMPRESSÃO & FISCAL ===
  imprimirOS() {
    if (!this.osId) return;
    this.loading = true;
    this.osService.imprimir(this.osId)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          window.open(url);
        },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao gerar relatório.' })
      });
  }

  emitirNfe() {
      if (!this.osId || !this.osData) {
          this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Salve a OS antes de emitir nota.' });
          return;
      }

      this.loading = true;
      const totalProd = this.itensProduto.reduce((acc, i) => acc + i.valorFinal, 0);
      const totalServ = this.itensServico.reduce((acc, i) => acc + i.valorFinal, 0);

      const request: NfeRequest = {
          // faturaId: null, // Pode vincular a Fatura se houver
          tipoOperacao: TipoOperacaoNfe.SAIDA,
          status: StatusNfe.EM_DIGITACAO,
          ambiente: AmbienteNfe.HOMOLOGACAO,
          tipoEmissao: TipoEmissaoNfe.NORMAL,
          dataEmissao: new Date().toISOString(),
          valorTotalNota: totalProd + totalServ,
          valorTotalProdutos: totalProd,
          valorTotalServicos: totalServ,
          observacoes: `Referente a OS #${this.osData.numeroOS}`
      };

      this.nfeService.create(request).subscribe({
          next: (nfe) => {
              this.messageService.add({ severity: 'success', summary: 'NFe gerada!', detail: 'NFe gerada em rascunho.' });
              this.loading = false;
              this.router.navigate(['/financeiro/nfe']);
          },
          error: (err) => {
              console.error(err);
              this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao gerar NFe.' });
              this.loading = false;
          }
      });
  }

  private combineDateAndTime(date: Date | null, timeStr: string): Date | undefined {
    if (!date) return undefined;
    const result = new Date(date);
    if (timeStr) {
       const parts = timeStr.split(':');
       if (parts.length === 2) {
          result.setHours(Number(parts[0]), Number(parts[1]), 0, 0);
       }
    }
    return result;
  }

  excluirOS() {
    if (!this.osId) return;
    this.confirmationService.confirm({
      message: `Tem certeza que deseja excluir a OS #${this.osData?.numeroOS || this.osId}? Esta ação não pode ser desfeita.`,
      header: 'Excluir Ordem de Serviço',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim, excluir',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.loading = true;
        this.osService.delete(this.osId!).pipe(finalize(() => this.loading = false)).subscribe({
          next: () => {
            this.toast.success('OS excluída com sucesso!');
            this.router.navigate(['/os']);
          },
          error: () => this.toast.error('Erro ao excluir OS.')
        });
      }
    });
  }

  voltar() {
     this.router.navigate(['/os']);
  }
}
