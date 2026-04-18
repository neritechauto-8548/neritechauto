import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { switchMap, map } from 'rxjs/operators';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { Select } from 'primeng/select';
import { MessageService } from 'primeng/api';
import { MatIconModule } from '@angular/material/icon';

import { OrdemServicoService } from '../../os/ordem-servico.service';
import { TipoOS } from '../../os/models/os.models';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { ProdutoService } from '../../produtos-servicos/produto.service';

interface ItemCarrinho {
  produtoId: number;
  codigo: string;
  descricao: string;
  qtd: number;
  valorUnitario: number;
  descontoPercent: number;
  total: number;
}

@Component({
  selector: 'app-pdv-venda-balcao',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    ToastModule,
    AutoCompleteModule,
    Select,
    MatIconModule
  ],
  providers: [MessageService, DatePipe],
  templateUrl: './venda-balcao.html',
})
export class VendaBalcaoPDV implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly osService = inject(OrdemServicoService);
  private readonly clienteService = inject(ClientesService);
  private readonly produtoService = inject(ProdutoService);
  private readonly messageService = inject(MessageService);
  private readonly datePipe = inject(DatePipe);

  // Estado Caixa
  dataHoje = new Date().toLocaleDateString('pt-BR');
  caixaLivre = true;
  salvando = false;
  idVendaParam: number | null = null;
  readOnlyMode = false;
  numeroOSRastreado = '';

  // Cliente e Vendedor
  clienteFiltrados: any[] = [];
  clienteSelecionado: any = null;

  vendedores = [
      { id: 1, nome: '1 - ALEXANDRE ROMULO ALBUQUERQUE' },
      { id: 2, nome: '2 - BALCÃO PADRÃO' }
  ];
  vendedorSelecionado: any = this.vendedores[0];

  formasPagamento = [
      { id: 1, nome: 'Dinheiro' },
      { id: 2, nome: 'PIX' },
      { id: 3, nome: 'Cartão de Crédito' },
      { id: 4, nome: 'Cartão de Débito' }
  ];
  formaPagamentoSelecionada: any = this.formasPagamento[0];

  // Produto Form
  produtoFiltrados: any[] = [];
  produtoSelecionado: any = null;
  qtd = 1;
  valorUnit = 0;
  descPercent = 0;

  // Carrinho
  itens: ItemCarrinho[] = [];
  totalGeralReadOnlyFallback = 0;
  get totalGeral() {
    if (this.readOnlyMode && this.itens.length === 0) return this.totalGeralReadOnlyFallback;
    return this.itens.reduce((acc, i) => acc + i.total, 0);
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
        const idParam = params.get('id');
        if (idParam) {
            this.idVendaParam = Number(idParam);
            this.readOnlyMode = true;
            this.carregarVendaCadastrada(this.idVendaParam);
        }
    });
  }

  carregarVendaCadastrada(id: number) {
      this.osService.getById(id).subscribe({
          next: (os) => {
              this.numeroOSRastreado = os.numeroOS || String(os.id);
              if (os.clienteId) {
                  // Stub para exibir apenas um nome (a NeriTech nao envia o cliente string via getById sempre)
                  this.clienteSelecionado = { label: `Cliente ID #${os.clienteId}` };
                  this.clienteService.getById(os.clienteId).subscribe(cli => {
                      this.clienteSelecionado = { label: cli.nomeCompleto || cli.razaoSocial || 'Cliente S/ Nome' };
                  });
              }
              this.totalGeralReadOnlyFallback = os.valorTotal;

              // Buscar os itens
              this.osService.getProdutos(id).subscribe(itensBanco => {
                   if (itensBanco && itensBanco.length > 0) {
                       this.caixaLivre = false;
                       this.itens = itensBanco.map((i: any) => ({
                           produtoId: i.produtoId,
                           codigo: i.codigoProduto || i.produtoId,
                           descricao: i.nomeProduto || i.descricao || 'Produto Recuperado',
                           qtd: i.quantidade,
                           valorUnitario: i.valorUnitario,
                           descontoPercent: i.descontoPercentual || 0,
                           total: i.valorFinal || (i.quantidade * i.valorUnitario)
                       }));
                   }
              })
          },
          error: () => this.messageService.add({severity: 'error', summary: 'Ops', detail: 'Erro ao extrair venda do banco local.'})
      });
  }

  // --- AutoComplete Cliente ---
  pesquisarCliente(event: any) {
    const query = event.query;
    const isNum = /^[\d\.\-\/]+$/.test(query);
    const params = isNum ? { cpf: query, size: 10 } : { nomeCompleto: query, size: 10 };

    this.clienteService.list(params).subscribe({
      next: (res) => {
        this.clienteFiltrados = (res.content || []).map((c: any) => ({
             ...c,
             label: c.nomeCompleto || c.nomeFantasia || c.razaoSocial || 'Cliente S/ Nome'
        }));
      },
      error: () => {
         this.clienteFiltrados = [];
      }
    });
  }

  // --- AutoComplete Produto ---
  pesquisarProduto(event: any) {
    const query = event.query;
    this.produtoService.list({ search: query, size: 20 }).subscribe({
      next: (res) => {
        this.produtoFiltrados = (res.content || []).map((p: any) => ({
             ...p,
             label: p.nome
        }));
      },
      error: () => {
         this.produtoFiltrados = [];
      }
    });
  }

  onProdutoSelect(event: any) {
    // Auto-preenche o valor do produto quando selecionado
    if (this.produtoSelecionado && this.produtoSelecionado.precoVenda) {
        this.valorUnit = this.produtoSelecionado.precoVenda;
        this.qtd = 1;
    }
  }

  // --- Carrinho de Compras ---
  incluirItem() {
    if (!this.produtoSelecionado || typeof this.produtoSelecionado === 'string') {
        this.messageService.add({ severity: 'warn', summary: 'Cuidado', detail: 'Selecione um produto válido do catálogo.' });
        return;
    }

    const qtd = Number(this.qtd) || 1;
    const unit = Number(this.valorUnit) || 0;
    const desc = Number(this.descPercent) || 0;
    const unitComDesc = unit * (1 - desc / 100);
    const total = Number((qtd * unitComDesc).toFixed(2));

    this.itens.push({
        produtoId: this.produtoSelecionado.id,
        codigo: this.produtoSelecionado.codigoInterno || String(this.produtoSelecionado.id),
        descricao: this.produtoSelecionado.nome,
        qtd,
        valorUnitario: unit,
        descontoPercent: desc,
        total
    });

    // reset parcials
    this.caixaLivre = false;
    this.qtd = 1;
    this.valorUnit = 0;
    this.descPercent = 0;
    this.produtoSelecionado = null;
  }

  removerItem(idx: number) {
    this.itens.splice(idx, 1);
    if(this.itens.length === 0) {
        this.caixaLivre = true;
    }
  }

  // --- Checkout ---
  guardarVenda() {
    if (this.itens.length === 0) {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Adicione pelo menos um produto na venda.' });
        return;
    }

    this.salvando = true;
    const dtoReq = {
        clienteId: this.clienteSelecionado?.id || null,
        consultorResponsavelId: this.vendedorSelecionado?.id || null,
        formaPagamentoId: this.formaPagamentoSelecionada?.id || null,
        tipoOS: 'VENDA_PRODUTO',
        dataAbertura: new Date().toISOString().substring(0, 19),
        numeroOS: 'BV-' + Math.floor(Math.random() * 1000000),
        valorTotal: this.totalGeral,
        valorProdutos: this.totalGeral,
        valorServicos: 0,
        observacoesInternas: 'Venda Rápida de Balcão - PDV',
        aprovadoCliente: true,
        metodoAprovacao: 'PRESENCIAL'
    };

    // Submete a Ordem de Serviço, e após sucesso, submete os itens do carrinho atrelados ao novo ID
    this.osService.create(dtoReq as any).pipe(
        switchMap((osResult: any) => {
            const requests = this.itens.map(it => {
                const valorTotalBase = it.qtd * it.valorUnitario;
                const descontoReais = valorTotalBase - it.total;

                return this.osService.addProduto({
                    ordemServicoId: osResult.id,
                    produtoId: it.produtoId,
                    quantidade: it.qtd,
                    valorUnitario: it.valorUnitario,
                    valorTotal: valorTotalBase,
                    descontoPercentual: it.descontoPercent,
                    descontoValor: descontoReais,
                    valorFinal: it.total,
                    descricao: it.descricao,
                    observacoes: it.descricao
                });
            });

            return requests.length ? forkJoin(requests).pipe(map(() => osResult)) : of(osResult);
        })
    ).subscribe({
        next: (res) => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: `Venda #${res.id} computada!` });
            setTimeout(() => {
                this.router.navigate(['/pdv/listar-vendas']);
            }, 1200);
        },
        error: (err) => {
            console.error(err);
            this.messageService.add({ severity: 'error', summary: 'Ops', detail: 'Erro ao gerar cupom fiscal/venda.' });
            this.salvando = false;
        }
    });

  }

  fechar() {
      this.router.navigate(['/pdv/listar-vendas']);
  }
}
