import { Component, EventEmitter, Input, Output, inject, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { ProdutoService } from '../produto.service';
import { ProdutoRequest, OrigemProduto } from '../models/produto.models';
import { SetorService } from '../../configuracoes/setores/setor.service';
import { FornecedorService } from '../../fornecedor/fornecedor.service';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { TooltipModule } from 'primeng/tooltip';
import { TabsModule } from 'primeng/tabs';
import { DatePickerModule } from 'primeng/datepicker';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
import { MatIconModule } from '@angular/material/icon';
import { TableModule } from 'primeng/table';
import { FileUploadModule } from 'primeng/fileupload';
import { forkJoin, of, finalize, catchError } from 'rxjs';

@Component({
  selector: 'app-cadastro-produto',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
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
    MatIconModule,
    TabsModule,
    TableModule,
    FileUploadModule
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

  // Foto / Upload
  fotoUrl: string = '';
  fotoPreview: string = '';
  uploadingFoto = false;

  // Dropdown Options
  categoriaOptions: any[] = [];
  unidadeOptions: any[] = [];
  setorOptions: any[] = [];
  fornecedorOptions: any[] = [];

  // Cotações / Fornecedores do Produto
  cotacoes: any[] = [];
  cotacaoForm: FormGroup;
  displayCotacaoDialog = false;
  editingCotacaoId?: number;
  savingCotacao = false;

  origemOptions = [
    { label: '0 - Nacional', value: OrigemProduto.NACIONAL },
    { label: '1 - Estrangeira (Imp. Direta)', value: OrigemProduto.ESTRANGEIRA_IMPORTACAO_DIRETA },
    { label: '2 - Estrangeira (Merc. Interno)', value: OrigemProduto.ESTRANGEIRA_ADQUIRIDA_NO_MERCADO_INTERNO }
  ];

  private readonly produtoService = inject(ProdutoService);
  private readonly setorService = inject(SetorService);
  private readonly fornecedorService = inject(FornecedorService);
  private readonly route = inject(ActivatedRoute);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly cdr = inject(ChangeDetectorRef);

  constructor(private router: Router, private fb: FormBuilder) {
    this.cotacaoForm = this.fb.group({
      id: [null],
      fornecedorId: [null, [Validators.required]],
      dataUltimoPreco: [new Date(), [Validators.required]],
      precoCusto: [0, [Validators.required, Validators.min(0.01)]],
      quantidadeMinima: [1],
      prazoEntregaDias: [0],
      principal: [false],
      ativo: [true],
      observacoes: ['']
    });

    this.form = this.fb.group({
      // Identificacao
      codigoInterno: ['', [Validators.required, Validators.maxLength(50)]],
      codigoBarras: ['', [Validators.maxLength(50)]],
      codigoFabricante: ['', [Validators.maxLength(50)]],
      nome: ['', [Validators.required, Validators.maxLength(255)]],
      descricao: [''],
      marca: [''],
      modelo: [''],

      // Relacionamentos
      categoriaId: [null],
      unidadeMedidaId: [null],

      // Precos
      precoCompra: [0, Validators.required],
      precoCusto: [0, Validators.required],
      precoVenda: [0, Validators.required],
      margemLucroPercentual: [0],

      // Estoque
      quantidadeEstoque: [0],
      estoqueMinimo: [0],
      estoqueMaximo: [0],
      controlaLote: [false],
      controlaValidade: [false],

      // Outros
      observacoes: [''],
      ativo: [true],
      enderecoEstoque: ['', [Validators.maxLength(100)]],
      setor: ['', [Validators.maxLength(100)]],
      dataVencimento: [null],
      codigoSubstituto1: ['', [Validators.maxLength(50)]],
      codigoSubstituto2: ['', [Validators.maxLength(50)]],
      descontoFornecedorPercentual: [0],

      dadosFiscais: this.fb.group({
        ceanTrib: [''],
        cfop: [''],
        ncm: [''],
        cest: [''],
        unComercial: [''],
        qtdComercial: [0],
        valorUnitComercial: [0],
        unTrib: [''],
        qtdTrib: [0],
        valorUnitTributavel: [0],
        totalSeguro: [0],
        desconto: [0],
        totalFrete: [0],
        outrasDespesas: [0],
        valorTotalBruto: [0],
        exTipi: [''],
        indicadorEscalaRelevante: [''],
        cnpjFabricante: [''],
        codigoBeneficioFiscal: [''],
        valorBrutoCompoeTotal: [false],
        pedidoCompra: [''],
        itemPedidoCompra: [0],
        numeroFci: [''],
        impostoFederalAprox: [0],
        impostoEstadualAprox: [0],

        ipiSitTrib: [''],
        ipiClasseEnquadramento: [''],
        ipiCodEnquadramento: [''],
        ipiCnpjProdutor: [''],
        ipiCodSeloControle: [''],
        ipiQtdSelo: [0],
        ipiTipoCalculo: [''],
        ipiBaseCalc: [0],
        ipiAliquota: [0],
        ipiValorUnidTrib: [0],
        ipiValor: [0],

        icmsSitTrib: [''],
        icmsOrigem: [''],
        icmsModBc: [''],
        icmsRedBc: [0],
        icmsBaseCalc: [0],
        icmsAliquota: [0],
        icmsMotivoDesoneracao: [''],
        icmsValorDesoneracao: [0],
        icmsValor: [0],
        icmsBcStUfOrigem: [0],
        icmsRetidoAntes: [0],
        icmsBcStUfDest: [0],
        icmsStUfDestino: [0],
        icmsAliqCalcCredito: [0],
        icmsValorCredito: [0],
        icmsModBcSt: [''],
        icmsMargemValorAdicionado: [0],
        icmsPrecoUnitPautaSt: [0],
        icmsRedBcSt: [0],
        icmsBaseCalcSt: [0],
        icmsAliquotaSt: [0],
        icmsValorSt: [0],
        icmsPercBcOpPropria: [0],
        icmsUfPgtoSt: [''],
        icmsPercFcp: [0],
        icmsValorFcp: [0],
        icmsBcFcp: [0],
        icmsValorBcRetSt: [0],
        icmsPercFcpRetSt: [0],
        icmsVbCalcFcpRetAntSt: [0],
        icmsPercFcpRetAntSt: [0],
        icmsValorBcFcpUfDest: [0],

        pisSitTrib: [''],
        pisTipoCalculo: [''],
        pisBaseCalc: [0],
        pisAliquota: [0],
        pisQv: [0],
        pisRv: [0],
        pisRvbc: [0],
        pisValorUnidTrib: [0],
        pisTipoCalculoSt: [''],
        pisAliquotaSt: [0],
        pisValorUnidTribSt: [0],

        cofinsSitTrib: [''],
        cofinsBaseCalc: [0],
        cofinsAliquota: [0],
        cofinsValorUnidTrib: [0],
        cofinsValor: [0],
        cofinsTipoCalculoSt: [''],
        cofinsAliquotaSt: [0],
        cofinsValorUnidTribSt: [0],

        importValorBc: [0],
        importValorDespAduaneiras: [0],
        importValorImposto: [0],
        importValorIof: [0],

        combCodAnp: [''],
        combPercGlp: [0],
        combCodAutorizacaoCodif: [''],
        combUfConsumo: [''],
        combBcCide: [0],
        combAliquotaCide: [0],
        combValorCide: [0],
        combDescAnp: [''],
        combPercGlpDerivPetroleo: [0],
        combPercGasNac: [0],
        combPercGasImp: [0],
        combValorPartida: [0],

        ibsSitTrib: [''],
        ibsClassificacaoTrib: [''],
        ibsAliquotaUf: [0],
        ibsPercDiferimentoUf: [0],
        ibsPercRedAliquotaUf: [0],
        ibsAliquotaMunicipio: [0],
        cbsAliquota: [0],
        cbsPercDiferimento: [0],
        cbsPercRedAliquota: [0]
      })
    });
    this.setupPricingCalculations();
  }

  setupPricingCalculations() {
    // Escuta mudanças no Preço de Compra
    this.form.get('precoCompra')?.valueChanges.subscribe(compra => {
      const desc = this.form.get('descontoFornecedorPercentual')?.value || 0;
      const margem = this.form.get('margemLucroPercentual')?.value || 0;
      
      const custo = (compra || 0) * (1 - desc / 100);
      const venda = custo * (1 + margem / 100);
      
      this.form.patchValue({
        precoCusto: Number(custo.toFixed(4)),
        precoVenda: Number(venda.toFixed(2))
      }, { emitEvent: false });
      this.cdr.markForCheck();
    });

    // Escuta mudanças no Desconto do Fornecedor
    this.form.get('descontoFornecedorPercentual')?.valueChanges.subscribe(desc => {
      const compra = this.form.get('precoCompra')?.value || 0;
      const margem = this.form.get('margemLucroPercentual')?.value || 0;
      
      const custo = (compra || 0) * (1 - (desc || 0) / 100);
      const venda = custo * (1 + margem / 100);
      
      this.form.patchValue({
        precoCusto: Number(custo.toFixed(4)),
        precoVenda: Number(venda.toFixed(2))
      }, { emitEvent: false });
      this.cdr.markForCheck();
    });

    // Escuta mudanças no Preço de Custo
    this.form.get('precoCusto')?.valueChanges.subscribe(custo => {
      const margem = this.form.get('margemLucroPercentual')?.value || 0;
      const venda = (custo || 0) * (1 + margem / 100);
      
      this.form.patchValue({
        precoVenda: Number(venda.toFixed(2))
      }, { emitEvent: false });
      this.cdr.markForCheck();
    });

    // Escuta mudanças na Margem de Lucro
    this.form.get('margemLucroPercentual')?.valueChanges.subscribe(margem => {
      const custo = this.form.get('precoCusto')?.value || 0;
      const venda = (custo || 0) * (1 + (margem || 0) / 100);
      
      this.form.patchValue({
        precoVenda: Number(venda.toFixed(2))
      }, { emitEvent: false });
      this.cdr.markForCheck();
    });

    // Escuta mudanças no Preço de Venda (recalcula a Margem)
    this.form.get('precoVenda')?.valueChanges.subscribe(venda => {
      const custo = this.form.get('precoCusto')?.value || 0;
      if (custo > 0) {
        const margem = (((venda || 0) - custo) / custo) * 100;
        this.form.patchValue({
          margemLucroPercentual: Number(margem.toFixed(2))
        }, { emitEvent: false });
      }
      this.cdr.markForCheck();
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(pm => {
      const idParam = pm.get('id');
      const id = idParam ? Number(idParam) : null;
      if (id && !Number.isNaN(id)) {
        this.editingId = id;
      }
      // Carregar dependências e produto em paralelo
      this.loadDependencies(id ?? undefined);
    });
  }

  loadDependencies(produtoId?: number) {
    forkJoin({
      categorias: this.produtoService.listCategorias().pipe(catchError(() => of([]))),
      unidades:   this.produtoService.listUnidades().pipe(catchError(() => of([]))),
      setores:    this.setorService.list({ size: 200 }).pipe(catchError(() => of({ content: [] }))),
      fornecedores: this.fornecedorService.list({ size: 500 }).pipe(catchError(() => of({ content: [] })))
    }).subscribe(({ categorias, unidades, setores, fornecedores }) => {
      const catList = (categorias as any)?.content ?? (Array.isArray(categorias) ? categorias : []);
      const unList  = (unidades as any)?.content  ?? (Array.isArray(unidades)  ? unidades  : []);
      const setList = (setores  as any)?.content  ?? (Array.isArray(setores)   ? setores   : []);
      const fornList = (fornecedores as any)?.content ?? (Array.isArray(fornecedores) ? fornecedores : []);

      this.categoriaOptions = catList.map((c: any) => ({ label: c.nome, value: c.id }));
      this.unidadeOptions   = unList.map((u: any)  => ({ label: `${u.nome} (${u.sigla})`, value: u.id }));
      this.setorOptions     = setList.map((s: any) => ({ label: s.nome, value: s.nome }));
      this.fornecedorOptions = fornList.map((f: any) => ({ label: f.nomeFantasia || f.razaoSocial || 'Sem Nome', value: f.id }));
      this.cdr.markForCheck();

      // Só carrega o produto depois que os selects já estão prontos (evita flickering)
      if (produtoId) {
        this.loadProduto(produtoId);
      }
    });
  }

  loadProduto(id: number) {
    this.loading = true;
    this.cdr.markForCheck();
    forkJoin({
      produto: this.produtoService.get(id),
      cotacoes: this.produtoService.listProdutoFornecedores(id).pipe(catchError(() => of({ content: [] })))
    }).subscribe({
      next: ({ produto, cotacoes }) => {
        this.form.patchValue(produto, { emitEvent: false });
        this.quantidadeEstoqueOriginal = produto.quantidadeEstoque || 0;
        if ((produto as any).fotoPrincipalUrl) {
          this.fotoUrl = this.produtoService.getFotoUrl(id);
        }
        this.cotacoes = cotacoes?.content ?? (Array.isArray(cotacoes) ? cotacoes : []);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.cdr.markForCheck();
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar produto' });
      }
    });
  }

  // ── CNPJ Alfanumérico ──────────────────────────────────────────────
  /** Aplica máscara de CNPJ alfanumérico (padrão jul/2026): AA.AAA.AAA/AAAA-DD */
  formatarCnpjFabricante(event: Event): void {
    const input = event.target as HTMLInputElement;
    let raw = input.value.replace(/[^A-Z0-9]/gi, '').toUpperCase();
    if (raw.length > 14) raw = raw.substring(0, 14);

    let fmt = raw;
    if (raw.length > 2)  fmt = raw.substring(0, 2) + '.' + raw.substring(2);
    if (raw.length > 5)  fmt = fmt.substring(0, 6) + '.' + raw.substring(5);
    if (raw.length > 8)  fmt = fmt.substring(0, 10) + '/' + raw.substring(8);
    if (raw.length > 12) fmt = fmt.substring(0, 15) + '-' + raw.substring(12);

    const ctrl = this.form.get('dadosFiscais.cnpjFabricante');
    ctrl?.setValue(fmt, { emitEvent: false });
    input.value = fmt;
  }

  /** Aplica máscara de NCM: 9999.99.99 */
  formatarNcm(event: Event): void {
    const input = event.target as HTMLInputElement;
    let raw = input.value.replace(/\D/g, '');
    if (raw.length > 8) raw = raw.substring(0, 8);
    let fmt = raw;
    if (raw.length > 4) fmt = raw.substring(0, 4) + '.' + raw.substring(4);
    if (raw.length > 6) fmt = fmt.substring(0, 7) + '.' + raw.substring(6);
    const ctrl = this.form.get('dadosFiscais.ncm');
    ctrl?.setValue(fmt, { emitEvent: false });
    input.value = fmt;
  }

  /** Aplica máscara de CEST: 99.999.99 */
  formatarCest(event: Event): void {
    const input = event.target as HTMLInputElement;
    let raw = input.value.replace(/\D/g, '');
    if (raw.length > 7) raw = raw.substring(0, 7);
    let fmt = raw;
    if (raw.length > 2) fmt = raw.substring(0, 2) + '.' + raw.substring(2);
    if (raw.length > 5) fmt = fmt.substring(0, 6) + '.' + raw.substring(5);
    const ctrl = this.form.get('dadosFiscais.cest');
    ctrl?.setValue(fmt, { emitEvent: false });
    input.value = fmt;
  }

  /** Aplica máscara de CFOP: 9999 */
  formatarCfop(event: Event): void {
    const input = event.target as HTMLInputElement;
    let raw = input.value.replace(/\D/g, '');
    if (raw.length > 4) raw = raw.substring(0, 4);
    const ctrl = this.form.get('dadosFiscais.cfop');
    ctrl?.setValue(raw, { emitEvent: false });
    input.value = raw;
  }

  /** Aplica máscara de EAN: somente dígitos, até 14 */
  formatarEan(event: Event): void {
    const input = event.target as HTMLInputElement;
    let raw = input.value.replace(/\D/g, '');
    if (raw.length > 14) raw = raw.substring(0, 14);
    this.form.get('codigoBarras')?.setValue(raw, { emitEvent: false });
    input.value = raw;
  }

  // ── Upload de Imagem ──────────────────────────────────────────────
  onFotoSelected(event: any): void {
    const file: File = event.target.files[0];
    if (!file) return;

    const tiposPermitidos = ['image/png', 'image/jpeg', 'image/jpg', 'image/webp'];
    if (!tiposPermitidos.includes(file.type)) {
      this.messageService.add({ severity: 'error', summary: 'Formato inválido', detail: 'Somente PNG, JPG ou WebP são aceitos.' });
      return;
    }
    if (file.size > 2 * 1024 * 1024) {
      this.messageService.add({ severity: 'error', summary: 'Arquivo muito grande', detail: 'A imagem deve ter no máximo 2MB.' });
      return;
    }

    // Preview local imediato
    const reader = new FileReader();
    reader.onload = (e: any) => { this.fotoPreview = e.target.result; };
    reader.readAsDataURL(file);

    if (!this.editingId) return;

    this.uploadingFoto = true;
    this.produtoService.uploadFoto(this.editingId, file)
      .pipe(finalize(() => this.uploadingFoto = false))
      .subscribe({
        next: () => {
          this.fotoUrl = this.produtoService.getFotoUrl(this.editingId!);
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Imagem do produto atualizada!' });
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao enviar imagem. Tente novamente.' });
          this.fotoPreview = '';
        }
      });
  }

  removerFoto(): void {
    if (!this.editingId) return;
    this.produtoService.removeFoto(this.editingId).subscribe({
      next: () => {
        this.fotoUrl = '';
        this.fotoPreview = '';
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Imagem removida.' });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao remover imagem.' });
      }
    });
  }

  get currentFoto(): string {
    return this.fotoPreview || this.fotoUrl;
  }

  get melhorPreco(): any {
    if (!this.cotacoes || this.cotacoes.length === 0) return null;
    const ativas = this.cotacoes.filter(c => c.ativo !== false && c.precoCusto > 0);
    if (ativas.length === 0) return null;
    return ativas.reduce((min, c) => c.precoCusto < min.precoCusto ? c : min, ativas[0]);
  }

  get fornecedorPrincipal(): any {
    if (!this.cotacoes || this.cotacoes.length === 0) return null;
    return this.cotacoes.find(c => c.principal);
  }

  // ── Cotações e Fornecedores ──────────────────────────────────────
  formatDateToLocalDateString(date: any): string | null {
    if (!date) return null;
    if (typeof date === 'string') return date.substring(0, 10);
    if (date instanceof Date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
    return null;
  }

  abrirNovaCotacao() {
    this.editingCotacaoId = undefined;
    this.cotacaoForm.reset({
      id: null,
      fornecedorId: null,
      dataUltimoPreco: new Date(),
      precoCusto: 0,
      quantidadeMinima: 1,
      prazoEntregaDias: 0,
      principal: false,
      ativo: true,
      observacoes: ''
    });
    this.displayCotacaoDialog = true;
    this.cdr.markForCheck();
  }

  editarCotacao(cotacao: any) {
    this.editingCotacaoId = cotacao.id;
    this.cotacaoForm.patchValue({
      id: cotacao.id,
      fornecedorId: cotacao.fornecedorId,
      dataUltimoPreco: cotacao.dataUltimoPreco ? new Date(cotacao.dataUltimoPreco + 'T00:00:00') : new Date(),
      precoCusto: cotacao.precoCusto || 0,
      quantidadeMinima: cotacao.quantidadeMinima || 1,
      prazoEntregaDias: cotacao.prazoEntregaDias || 0,
      principal: cotacao.principal || false,
      ativo: cotacao.ativo !== false,
      observacoes: cotacao.observacoes || ''
    });
    this.displayCotacaoDialog = true;
    this.cdr.markForCheck();
  }

  salvarCotacao() {
    if (this.cotacaoForm.invalid || !this.editingId) return;

    this.savingCotacao = true;
    this.cdr.markForCheck();

    const formVal = this.cotacaoForm.value;
    const dto = {
      produtoId: this.editingId,
      fornecedorId: formVal.fornecedorId,
      precoCusto: formVal.precoCusto,
      dataUltimoPreco: this.formatDateToLocalDateString(formVal.dataUltimoPreco),
      quantidadeMinima: formVal.quantidadeMinima,
      prazoEntregaDias: formVal.prazoEntregaDias,
      principal: formVal.principal,
      ativo: formVal.ativo,
      observacoes: formVal.observacoes
    };

    const request = this.editingCotacaoId
      ? this.produtoService.updateProdutoFornecedor(this.editingCotacaoId, dto)
      : this.produtoService.createProdutoFornecedor(dto);

    request.pipe(finalize(() => {
      this.savingCotacao = false;
      this.cdr.markForCheck();
    })).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cotação salva com sucesso!' });
        this.displayCotacaoDialog = false;
        this.recarregarCotacoes();
      },
      error: (err) => {
        const detail = err.error?.message || 'Erro ao salvar cotação';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail });
      }
    });
  }

  excluirCotacao(id: number) {
    this.confirmationService.confirm({
      message: 'Tem certeza que deseja excluir esta cotação?',
      header: 'Confirmar Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim, Excluir',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.produtoService.deleteProdutoFornecedor(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cotação excluída com sucesso!' });
            this.recarregarCotacoes();
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir cotação' });
          }
        });
      }
    });
  }

  recarregarCotacoes() {
    if (!this.editingId) return;
    this.produtoService.listProdutoFornecedores(this.editingId).subscribe({
      next: (cotacoes) => {
        this.cotacoes = cotacoes?.content ?? (Array.isArray(cotacoes) ? cotacoes : []);
        this.cdr.markForCheck();
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
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto excluído com sucesso!' });
            setTimeout(() => {
              if (this.modalMode) this.saved.emit();
              else this.router.navigate(['/produtos-servicos/estoque']);
            }, 1000);
          },
          error: () => {
            this.loading = false;
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível excluir o produto. Verifique se ele possui dependências.' });
          }
        });
      }
    });
  }

  salvar() {
    if (this.form.invalid) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha os campos obrigatórios' });
      return;
    }

    const formVal = this.form.value;

    const payload: ProdutoRequest = {
      empresaId: 0,
      codigoInterno: formVal.codigoInterno,
      codigoBarras: formVal.codigoBarras,
      codigoFabricante: formVal.codigoFabricante,
      nome: formVal.nome,
      descricao: formVal.descricao,
      marca: formVal.marca,
      modelo: formVal.modelo,
      categoriaId: formVal.categoriaId,
      unidadeMedidaId: formVal.unidadeMedidaId,
      precoCompra: formVal.precoCompra,
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
      ativo: formVal.ativo,
      enderecoEstoque: formVal.enderecoEstoque,
      setor: formVal.setor,
      dataVencimento: formVal.dataVencimento,
      codigoSubstituto1: formVal.codigoSubstituto1,
      codigoSubstituto2: formVal.codigoSubstituto2,
      descontoFornecedorPercentual: formVal.descontoFornecedorPercentual,
      dadosFiscais: formVal.dadosFiscais
    };

    this.loading = true;
    const obs = this.editingId
      ? this.produtoService.update(this.editingId, payload)
      : this.produtoService.create(payload);

    obs.subscribe({
      next: (res) => {
        this.loading = false;
        if (!this.editingId && res?.id) {
          this.editingId = res.id;
        }
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto salvo com sucesso!' });
        setTimeout(() => {
          if (this.modalMode) this.saved.emit(res);
          else this.router.navigate(['/produtos-servicos/estoque']);
        }, 1000);
      },
      error: (err) => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao salvar produto.' });
        console.error(err);
      }
    });
  }
}
