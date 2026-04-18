import { Component, EventEmitter, Input, Output, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { ProdutoService } from '../produto.service';
import { ProdutoRequest, OrigemProduto, CategoriaProdutoResponse, UnidadeMedidaResponse } from '../models/produto.models';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { TooltipModule } from 'primeng/tooltip';
import { TabsModule } from 'primeng/tabs'; // Adjusted import if needed, assuming simple usage or provided standalone
import { DatePickerModule } from 'primeng/datepicker';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-cadastro-produto',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputTextModule,
    TextareaModule,
    ButtonModule,
    SelectModule,
    TooltipModule,
    DatePickerModule,
    DialogModule,
    ConfirmDialogModule,
    AutoCompleteModule,
    CheckboxModule,
    ToastModule,
    InputNumberModule,
    MatIconModule
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './cadastro-produto.html',
  styleUrls: ['./cadastro-produto.scss'],
})
export class CadastroProduto implements OnInit {
  @Input() modalMode = false;
  @Output() saved = new EventEmitter<any>();
  @Output() canceled = new EventEmitter<void>();

  activeIndex = 0;
  form: FormGroup;
  editingId?: number;
  loading = false;
  quantidadeEstoqueOriginal = 0;

  // Dropdown Options
  categoriaOptions: any[] = [];
  unidadeOptions: any[] = [];

  origemOptions = [
    { label: '0 - Nacional', value: OrigemProduto.NACIONAL },
    { label: '1 - Estrangeira (Imp. Direta)', value: OrigemProduto.ESTRANGEIRA_IMPORTACAO_DIRETA },
    { label: '2 - Estrangeira (Merc. Interno)', value: OrigemProduto.ESTRANGEIRA_ADQUIRIDA_NO_MERCADO_INTERNO }
  ];

  /*
     O Backend suporta NCM, CEST e Origem.
     Nao suporta detalhamento de ICMS/IPI/PIS/COFINS por produto neste momento (DTO simples).
  */

  private readonly produtoService = inject(ProdutoService);
  private readonly route = inject(ActivatedRoute);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);

  constructor(private router: Router, private fb: FormBuilder) {
    this.form = this.fb.group({
      // Identificacao
      codigoInterno: ['', [Validators.required, Validators.maxLength(50)]],
      codigoBarras: ['', [Validators.maxLength(50)]],
      codigoFabricante: ['', [Validators.maxLength(50)]],
      nome: ['', [Validators.required, Validators.maxLength(255)]],
      descricao: [''], // Aplicacao
      marca: [''],
      modelo: [''],

      // Relacionamentos
      categoriaId: [null],
      unidadeMedidaId: [null],

      // Precos
      precoCusto: [0, Validators.required],
      precoVenda: [0, Validators.required],
      margemLucroPercentual: [0],

      // Estoque
      quantidadeEstoque: [0],
      estoqueMinimo: [0],
      estoqueMaximo: [0],
      controlaLote: [false],
      controlaValidade: [false],

      // Fiscal
      ncm: ['', [Validators.maxLength(8)]],
      cest: ['', [Validators.maxLength(7)]],
      origemProduto: [null], // String enum

      // Outros
      observacoes: [''],
      ativo: [true]
    });
  }

  ngOnInit(): void {
    this.loadDependencies();

    this.route.paramMap.subscribe(pm => {
      const idParam = pm.get('id');
      if (idParam) {
        const id = Number(idParam);
        if (!Number.isNaN(id)) {
          this.editingId = id;
          this.loadProduto(id);
        }
      }
    });
  }

  loadDependencies() {
    this.produtoService.listCategorias().subscribe({
      next: (res: any) => {
        // Backend returns Page or List? Service assumes List of any.
        // Need to check specific response structure. Assuming .content if Page or direct array.
        const list = res['content'] ? res['content'] : res;
        this.categoriaOptions = list.map((c: any) => ({ label: c.nome, value: c.id }));
      }
    });

    this.produtoService.listUnidades().subscribe({
        next: (res: any) => {
          const list = res['content'] ? res['content'] : res;
          this.unidadeOptions = list.map((u: any) => ({ label: `${u.nome} (${u.sigla})`, value: u.id }));
        }
    });
  }

  loadProduto(id: number) {
    this.loading = true;
    this.produtoService.get(id).subscribe({
      next: (p) => {
        this.form.patchValue(p);
        this.quantidadeEstoqueOriginal = p.quantidadeEstoque || 0;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.messageService.add({severity:'error', summary:'Erro', detail:'Erro ao carregar produto'});
      }
    });
  }

  cancelar() {
    if (this.modalMode) {
      this.canceled.emit();
    } else {
      this.router.navigate(['/produtos-servicos/estoque']);
    }
  }

  excluirProduto() {
    if (!this.editingId) return;

    this.confirmationService.confirm({
      message: 'Tem certeza que deseja excluir este produto? Esta ação não pode ser desfeita e pode afetar o histórico de estoque.',
      header: 'Confirmar Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim, excluir',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.loading = true;
        this.produtoService.delete(this.editingId!).subscribe({
          next: () => {
            this.loading = false;
            this.messageService.add({severity:'success', summary:'Sucesso', detail:'Produto excluído com sucesso!'});
            setTimeout(() => {
              if (this.modalMode) {
                  this.saved.emit();
              } else {
                  this.router.navigate(['/produtos-servicos/estoque']);
              }
            }, 1000);
          },
          error: (err) => {
            this.loading = false;
            this.messageService.add({severity:'error', summary:'Erro', detail:'Não foi possível excluir o produto. Verifique se ele possui dependências.'});
          }
        });
      }
    });
  }

  salvar() {
    if (this.form.invalid) {
        this.messageService.add({severity:'warn', summary:'Atenção', detail:'Preencha os campos obrigatórios'});
        return;
    }

    const formVal = this.form.value;

    const payload: ProdutoRequest = {
        empresaId: 0, // Injected by service
        codigoInterno: formVal.codigoInterno,
        codigoBarras: formVal.codigoBarras,
        codigoFabricante: formVal.codigoFabricante,
        nome: formVal.nome,
        descricao: formVal.descricao,
        marca: formVal.marca,
        modelo: formVal.modelo,
        categoriaId: formVal.categoriaId,
        unidadeMedidaId: formVal.unidadeMedidaId,
        precoCusto: formVal.precoCusto,
        precoVenda: formVal.precoVenda,
        margemLucroPercentual: formVal.margemLucroPercentual,
        quantidadeEstoque: formVal.quantidadeEstoque,
        estoqueMinimo: formVal.estoqueMinimo,
        estoqueMaximo: formVal.estoqueMaximo,
        controlaLote: formVal.controlaLote,
        controlaValidade: formVal.controlaValidade,
        ncm: formVal.ncm,
        cest: formVal.cest,
        origemProduto: formVal.origemProduto,
        observacoes: formVal.observacoes,
        ativo: formVal.ativo
    };

    this.loading = true;
    const obs = this.editingId
        ? this.produtoService.update(this.editingId, payload)
        : this.produtoService.create(payload);

    obs.subscribe({
        next: (res) => {
            this.loading = false;
            this.messageService.add({severity:'success', summary:'Sucesso', detail:'Produto salvo com sucesso!'});
            setTimeout(() => {
                if (this.modalMode) this.saved.emit(res);
                else this.router.navigate(['/produtos-servicos/estoque']);
            }, 1000);
        },
        error: (err) => {
            this.loading = false;
            this.messageService.add({severity:'error', summary:'Erro', detail:'Falha ao salvar produto.'});
            console.error(err);
        }
    });
  }
}
