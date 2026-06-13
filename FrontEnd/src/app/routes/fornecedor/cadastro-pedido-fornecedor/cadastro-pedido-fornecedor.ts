import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { MatIconModule } from '@angular/material/icon';
import { DialogModule } from 'primeng/dialog';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TableModule } from 'primeng/table';
import { CadastroFornecedor } from '../cadastro-fornecedor/cadastro-fornecedor';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FornecedorService } from '../fornecedor.service';
import { PedidoFornecedorService } from '../pedido-fornecedor.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { PedidoFornecedorRequest } from '../models/compra.models';
import { ProdutoService } from '../../produtos-servicos/produto.service';


@Component({
  selector: 'cadastro-pedido-fornecedor',
  standalone: true,
  templateUrl: './cadastro-pedido-fornecedor.html',
  imports: [CommonModule, FormsModule, RouterModule, ButtonModule, InputTextModule, TextareaModule, SelectModule, DatePickerModule, MatIconModule, DialogModule, CadastroFornecedor, ToastModule, AutoCompleteModule, ConfirmDialogModule, TableModule],
  providers: [MessageService, ConfirmationService]
})
export class CadastroPedidoFornecedor implements OnInit {
  private readonly location = inject(Location);
  private readonly route = inject(ActivatedRoute);
  private readonly fornecedorService = inject(FornecedorService);
  private readonly pedidoService = inject(PedidoFornecedorService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly storage = inject(LocalStorageService);
  private readonly produtoService = inject(ProdutoService);




  // Modo: 'novo' | 'editar' | 'visualizar'
  modo: 'novo' | 'editar' | 'visualizar' = 'novo';
  pedidoId: number | null = null;
  carregando = false;
  salvando = false;
  excluindo = false;

  // Modelo do formulário
  form = {
    fornecedor: null as any,
    funcionario: 'ALEXANDRE ROMULO',
    previsao: null as Date | null,
    numeroNf: '',
    observacao: '',
    descricaoInterna: '',
    status: 'PENDENTE',
    itens: [] as any[]
  };


  funcionarios = [
    { label: 'ALEXANDRE ROMULO', value: 'ALEXANDRE ROMULO' },
    { label: 'JOSILENE MARIA', value: 'JOSILENE MARIA' },
  ];

  showFornecedorDialog = false;
  fornecedoresFiltrados: any[] = [];

  // Getter/setter que resolve o [object Object] do PrimeNG AutoComplete:
  // quando o PrimeNG escreve o objeto selecionado, o setter extrai nomeFantasia
  // e guarda o objeto em form.fornecedor
  private _fornecedorNome = '';
  get fornecedorNome(): any { return this._fornecedorNome; }
  set fornecedorNome(val: any) {
    if (val && typeof val === 'object') {
      this._fornecedorNome = val.nomeFantasia || val.razaoSocial || '';
      this.form.fornecedor = val;
    } else {
      this._fornecedorNome = val || '';
      if (!val) this.form.fornecedor = null;
    }
  }

  get somenteLeitura(): boolean {
    return this.modo === 'visualizar';
  }

  get modoEdicao(): boolean {
    return this.modo === 'editar';
  }

  get tituloPagina(): string {
    if (this.modo === 'editar') return 'Editar Pedido a Fornecedor';
    if (this.modo === 'visualizar') return 'Visualizar Pedido a Fornecedor';
    return 'Novo Pedido a Fornecedor';
  }

  get subtitulo(): string {
    if (this.modo === 'visualizar') return 'Dados do pedido de compra';
    return 'Preencha os dados do pedido de compra';
  }

  ngOnInit(): void {
    const url = this.route.snapshot.url.map(s => s.path).join('/');
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.pedidoId = +id;
      this.modo = url.includes('visualizar') ? 'visualizar' : 'editar';
      this.carregarPedido(this.pedidoId);
    }
  }

  private carregarPedido(id: number): void {
    this.carregando = true;
    this.pedidoService.get(id).subscribe({
      next: (pedido) => {
        this.form.funcionario = pedido.responsavel;
        this.form.numeroNf = pedido.numeroNf || '';
        this.form.observacao = pedido.observacao || '';
        this.form.descricaoInterna = pedido.descricaoInterna || '';
        this.form.status = pedido.status || 'PENDENTE';
        
        if (pedido.itens) {
            this.form.itens = pedido.itens.map(item => ({
                produto: { id: item.produtoId, nome: item.nomeProduto },
                quantidade: item.quantidade,
                precoUnitario: item.precoUnitario,
                subtotal: item.subtotal || (item.quantidade * item.precoUnitario)
            }));
        }

        // Converter ISO string para Date para o DatePicker
        if (pedido.dataPrevisao) {
          const [year, month, day] = pedido.dataPrevisao.split('-');
          this.form.previsao = new Date(+year, +month - 1, +day);
        }

        if (pedido.fornecedorId) {
          // Force angular recompile to ensure old http request is removed
          this.form.fornecedor = { id: pedido.fornecedorId, nomeFantasia: pedido.nomeFornecedor };
          this.fornecedorNome = this.form.fornecedor;  // setter extrai nomeFantasia automaticamente
          this.carregando = false;
        } else {
          this.carregando = false;
        }
      },
      error: () => {
        this.carregando = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Pedido não encontrado.' });
        setTimeout(() => this.location.back(), 1500);
      }
    });
  }

  filtrarFornecedores(event: any) {
    this.fornecedorService.list({ nome: event.query, nomeFantasia: event.query, page: 0, size: 10 }).subscribe(
      page => this.fornecedoresFiltrados = page.content
    );
  }

  onFornecedorSelect(event: any) {
    // O setter de fornecedorNome já extrai nomeFantasia e guarda em form.fornecedor
    this.fornecedorNome = event.value ?? event;
  }

  onFornecedorClear() {
    this.fornecedorNome = '';
  }

  buscarFornecedor() {
    this.showFornecedorDialog = true;
  }

  onFornecedorSaved(model: any) {
    this.form.fornecedor = model;
    this.showFornecedorDialog = false;
  }

  onFornecedorCanceled() {
    this.showFornecedorDialog = false;
  }

  cancelar() {
    this.location.back();
  }

  excluir() {
    if (!this.pedidoId) return;
    this.confirmationService.confirm({
      message: 'Tem certeza que deseja excluir este pedido? Esta ação não pode ser desfeita.',
      header: 'Confirmar Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim, excluir',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.excluindo = true;
        this.pedidoService.delete(this.pedidoId!).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Excluído', detail: 'Pedido excluído com sucesso.' });
            setTimeout(() => this.location.back(), 800);
          },
          error: (err) => {
            this.excluindo = false;
            const msg = err?.error?.message || 'Erro ao excluir pedido.';
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
          }
        });
      }
    });
  }

  produtosFiltrados: any[] = [];
  
  // Variáveis para o Dialog de Busca
  showProdutoDialog = false;
  showStatusDialog = false;
  linhaAtivaProduto = -1;
  produtosDialog: any[] = [];
  totalRecordsDialog = 0;
  firstDialog = 0;
  rowsDialog = 10;
  loadingProdutosDialog = false;
  termoBuscaProduto = '';

  abrirDialogProdutos(index: number) {
      this.linhaAtivaProduto = index;
      this.showProdutoDialog = true;
      this.buscarProdutosDialog();
  }

  abrirDialogStatus() {
    if (this.somenteLeitura) return;
    this.showStatusDialog = true;
  }

  alterarStatus(novoStatus: string) {
    this.showStatusDialog = false;
    if (!this.pedidoId) {
      this.form.status = novoStatus;
      return;
    }

    let msg = '';
    let icon = '';
    let btnClass = '';
    
    if (novoStatus === 'RECEBIDO') {
        msg = `Tem certeza que deseja marcar o pedido como entregue? Os produtos serão adicionados ao estoque.`;
        icon = 'pi pi-check-circle';
        btnClass = 'p-button-success';
    } else if (novoStatus === 'CANCELADO') {
        msg = `Tem certeza que deseja cancelar este pedido?`;
        icon = 'pi pi-exclamation-triangle';
        btnClass = 'p-button-danger';
    } else {
        msg = `Deseja alterar o status do pedido para ${novoStatus === 'PENDENTE' ? 'Aguardando' : 'Enviado'}?`;
        icon = 'pi pi-info-circle';
        btnClass = 'p-button-info';
    }

    this.confirmationService.confirm({
      message: msg,
      header: 'Confirmar Ação',
      icon: icon,
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      acceptButtonStyleClass: btnClass,
      accept: () => {
        this.salvando = true;
        this.pedidoService.updateStatus(this.pedidoId!, novoStatus).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Status atualizado com sucesso.' });
            this.form.status = novoStatus;
            this.salvando = false;
          },
          error: (err) => {
            this.salvando = false;
            const errorMsg = err?.error?.message || 'Erro ao atualizar status.';
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMsg });
          }
        });
      }
    });
  }

  buscarProdutosDialog() {
      this.firstDialog = 0;
      this.carregarProdutosDialog();
  }

  onPageProdutoDialog(event: any) {
      this.firstDialog = event.first;
      this.rowsDialog = event.rows;
      this.carregarProdutosDialog();
  }

  carregarProdutosDialog() {
      this.loadingProdutosDialog = true;
      const params: any = {
          page: Math.floor(this.firstDialog / this.rowsDialog),
          size: this.rowsDialog
      };
      if (this.termoBuscaProduto) {
          params.search = this.termoBuscaProduto;
      }
      this.produtoService.list(params).subscribe({
          next: (page) => {
              this.produtosDialog = page.content;
              this.totalRecordsDialog = page.totalElements;
              this.loadingProdutosDialog = false;
          },
          error: () => {
              this.produtosDialog = [];
              this.loadingProdutosDialog = false;
          }
      });
  }

  selecionarProdutoDialog(produto: any) {
      if (this.linhaAtivaProduto >= 0 && this.linhaAtivaProduto < this.form.itens.length) {
          this.form.itens[this.linhaAtivaProduto].produto = produto;
          // Set default price or values if available
          if (produto.precoVenda) {
              this.form.itens[this.linhaAtivaProduto].precoUnitario = produto.precoVenda;
              this.calcularSubtotal(this.form.itens[this.linhaAtivaProduto]);
          }
      }
      this.showProdutoDialog = false;
  }

  filtrarProdutos(event: any) {
    this.produtoService.list({ search: event.query, page: 0, size: 10 }).subscribe({
      next: (page) => this.produtosFiltrados = page.content,
      error: () => this.produtosFiltrados = []
    });
  }

  adicionarItem() {
    this.form.itens.push({
      produto: null,
      quantidade: 1,
      precoUnitario: 0,
      subtotal: 0
    });
  }

  removerItem(index: number) {
    this.form.itens.splice(index, 1);
  }

  calcularSubtotal(item: any) {
    if (item.quantidade && item.precoUnitario) {
      item.subtotal = item.quantidade * item.precoUnitario;
    } else {
      item.subtotal = 0;
    }
  }

  get totalPedido(): number {
    return this.form.itens.reduce((acc, item) => acc + (item.subtotal || 0), 0);
  }

  private getEmpresaId(): number {
    let tenantId = this.storage.has('tenantId') ? this.storage.get('tenantId') : '1';
    if (tenantId && typeof tenantId === 'object') {
      tenantId = (tenantId as any).id || (tenantId as any).tenantId || (tenantId as any).empresaId || '1';
    }
    const val = String(tenantId);
    return parseInt((val.includes('[object') || val === 'undefined' || val === 'null') ? '1' : val, 10);
  }

  salvar() {
    if (this.somenteLeitura) return;
    if (!this.form.fornecedor || !this.form.funcionario) {
      this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Preencha os campos obrigatórios (*)' });
      return;
    }

    // Validate itens
    for (let i = 0; i < this.form.itens.length; i++) {
        const item = this.form.itens[i];
        if (!item.produto || !item.produto.id) {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: `Selecione um produto para o item ${i + 1}` });
            return;
        }
        if (!item.quantidade || item.quantidade <= 0) {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: `Quantidade inválida no item ${i + 1}` });
            return;
        }
        if (item.precoUnitario === null || item.precoUnitario === undefined || item.precoUnitario < 0) {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: `Preço unitário inválido no item ${i + 1}` });
            return;
        }
    }

    this.salvando = true;

    // Converter Date para ISO YYYY-MM-DD
    let dataPrevisao: string | null = null;
    if (this.form.previsao instanceof Date) {
      const d = this.form.previsao;
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      dataPrevisao = `${d.getFullYear()}-${month}-${day}`;
    }

    const dto: PedidoFornecedorRequest = {
      empresaId: this.getEmpresaId(),
      fornecedorId: this.form.fornecedor.id,
      responsavel: this.form.funcionario,
      dataPrevisao,
      numeroNf: this.form.numeroNf || undefined,
      observacao: this.form.observacao || undefined,
      descricaoInterna: this.form.descricaoInterna || undefined,
      itens: this.form.itens.map(item => ({
        produtoId: item.produto.id,
        quantidade: item.quantidade,
        precoUnitario: item.precoUnitario
      }))
    };

    const operacao$ = this.pedidoId
      ? this.pedidoService.update(this.pedidoId, dto)
      : this.pedidoService.create(dto);


    operacao$.subscribe({
      next: () => {
        const msg = this.pedidoId ? 'Pedido atualizado com sucesso' : 'Pedido registrado com sucesso';
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: msg });
        setTimeout(() => this.location.back(), 800);
      },
      error: (err) => {
        this.salvando = false;
        const msg = err?.error?.message || 'Erro ao salvar pedido. Tente novamente.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
      }
    });
  }
}
