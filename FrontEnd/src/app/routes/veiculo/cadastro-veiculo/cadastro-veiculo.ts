import { Component, inject, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { environment } from '@env/environment';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { TextareaModule } from 'primeng/textarea';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { TabsModule } from 'primeng/tabs'; // Importando TabsModule completo para garantir compatibilidade
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { FileUploadModule } from 'primeng/fileupload';
import { TagModule } from 'primeng/tag';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { NgxPermissionsService } from 'ngx-permissions';
import { SkeletonModule } from 'primeng/skeleton';

import { VeiculoService } from '../veiculo/veiculo.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { CadastroCliente } from '../../cliente/cadastro-cliente/cadastro-cliente';
import {
  VeiculoRequest,
  VeiculoResponse,
  MarcaVeiculoResponse,
  ModeloVeiculoResponse,
  AnoModeloResponse,
  TipoCombustivelResponse,
  getStatusVeiculoOptions
} from '../models/veiculo.models';

@Component({
  selector: 'cadastro-veiculo',
  standalone: true,
  templateUrl: './cadastro-veiculo.html',
  styleUrls: ['./cadastro-veiculo.scss'],
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    SelectModule,
    ButtonModule,
    TextareaModule,
    AutoCompleteModule,
    ToastModule,
    TagModule,
    SkeletonModule,
    CadastroCliente
  ],
  providers: [MessageService]
})
export class CadastroVeiculo implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly location = inject(Location);
  private readonly veiculoService = inject(VeiculoService);
  private readonly clientesService = inject(ClientesService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly permissionsService = inject(NgxPermissionsService);

  @Input() modalMode = false;
  @Output() saved = new EventEmitter<VeiculoResponse>();
  @Output() canceled = new EventEmitter<void>();

  readonly isUxPreview = environment.uxPreview;
  searchMode: 'placa' | 'chassi' = 'placa';
  lookupState: 'idle' | 'loading' | 'found' | 'none' | 'error' | 'unavailable' = 'idle';
  lookupMessage = '';
  selectedFabricationYear:number|null=null;
  get availableFabricationYears():number[] {
    return [...new Set(this.anosModelos.map(x=>x.anoFabricacao))].sort((a,b)=>b-a);
  }
  get availableYearModels():AnoModeloResponse[] {
    return this.anosModelos.filter(x=>this.selectedFabricationYear===null || x.anoFabricacao===this.selectedFabricationYear);
  }
  onFabricationYearChange():void {
    const selected=this.anosModelos.find(x=>x.id===this.form.anoModeloId);
    if(selected && selected.anoFabricacao!==this.selectedFabricationYear) this.form.anoModeloId=undefined;
  }
  customerLookupOpen = false;
  customerQuery = '';
  customerLookupError = '';
  customerLoading = false;
  clientCreationOpen = false;
  foundVehicle: VeiculoResponse | null = null;
  openCustomerLookup(): void {
    this.customerLookupOpen = true;
    this.searchCliente({query:this.customerQuery});
  }
  searchCustomerText(value:string): void {
    this.customerQuery=value;
    this.searchCliente({query:value});
  }
  chooseCustomer(customer:any): void {
    this.selectedCliente=customer;
    this.form.clienteId=customer.id;
    this.customerQuery=customer.nome;
    this.customerLookupOpen=false;
  }
  clearCustomer(): void {
    this.selectedCliente=null;this.form.clienteId=0;this.customerQuery='';this.customerLookupOpen=false;
  }
  onClientCreationSaved(value:any):void {
    if(!value?.id)return;
    this.chooseCustomer({...value,nome:value.nomeCompleto||value.nomeFantasia||value.razaoSocial||'',cpfCnpj:value.cpf||value.cnpj||''});
    this.clientCreationOpen=false;
  }
  existingVehicleId: number | null = null;

  get selectedModelImage(): string | undefined { return this.modelos.find(m => m.id === this.form.modeloId)?.imagemUrl; }

  get canSaveModal(): boolean {
    return !!this.selectedCliente?.id && /^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/.test((this.form.placa || '').trim().toUpperCase())
      && !!this.form.marcaId && !!this.form.modeloId && !!this.form.anoModeloId
      && !!this.form.combustivelId && !!this.form.corExterna?.trim() && !this.existingVehicleId && !this.saving && !this.isUxPreview;
  }

  // Estado Geral
  loading = false;
  saving = false;
  error: string | null = null;

  // Dados do Veículo
  id: number | null = null; // ID do veículo se estiver editando ou após salvar
  form: VeiculoRequest = {
    clienteId: 0,
    placa: '',
    marcaId: undefined,
    modeloId: undefined,
    anoModeloId: undefined,
    combustivelId: undefined,
    chassi: '',
    renavam: '',
    numeroMotor: '',
    corExterna: '',
    quilometragemAtual: undefined,
    quilometragemCadastro: undefined,
    proximaRevisaoKm: undefined,
    proximaRevisaoData: undefined,
    observacoes: '',
    status: undefined
  };

  // Listas de apoio
  marcas: MarcaVeiculoResponse[] = [];
  modelos: ModeloVeiculoResponse[] = [];
  anosModelos: AnoModeloResponse[] = [];
  combustiveis: TipoCombustivelResponse[] = [];
  loadingMarcas = false;
  loadingModelos = false;
  loadingAnosModelos = false;
  loadingCombustiveis = false;
  statusOptions = getStatusVeiculoOptions();

  // Busca de Cliente
  filteredClientes: any[] = [];
  selectedCliente: any | null = null;

  ngOnInit() {
    this.loadMarcas();
    this.loadCombustiveis();

    // Verificar se é edição (tem ID na rota)
    this.route.paramMap.subscribe(params => {
      const idStr = params.get('id');
      if (idStr) {
        this.id = Number(idStr);
        this.loadVeiculo(this.id);
      } else {
        // Modo criação: verifica se há um cliente pré-selecionado na URL (via listagem de clientes)
        this.route.queryParamMap.subscribe(qParams => {
          const cId = qParams.get('clienteId');
          if (cId) {
            this.form.clienteId = Number(cId);
            this.clientesService.getById(Number(cId)).subscribe(c => {
               this.selectedCliente = {
                   ...c,
                   nome: c.nomeCompleto || c.nomeFantasia || c.razaoSocial || '',
                   cpfCnpj: c.cpf || c.cnpj || ''
               };
               this.customerQuery = this.selectedCliente.nome;
            });
          }
        });
      }
    });
  }

  // ========== CARREGAMENTO DADOS ==========

  loadVeiculo(id: number) {
    this.loading = true;
    this.veiculoService.getById(id).subscribe({
      next: (res) => {
        this.form = { ...res };
        // Buscar cliente para preencher o AutoComplete
        if (res.clienteId) {
            this.clientesService.getById(res.clienteId).subscribe(c => {
                this.selectedCliente = {
                    ...c,
                    nome: c.nomeCompleto || c.nomeFantasia || c.razaoSocial || '',
                    cpfCnpj: c.cpf || c.cnpj || ''
                };
                this.customerQuery=this.selectedCliente.nome;
            });
        }
        // Carregar modelos se houver marca
        if (res.marcaId) {
            this.loadModelos(res.marcaId);
        }
        if (res.modeloId) {
            this.loadAnosModelos(res.modeloId);
        }

        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar veículo', err);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar veículo.' });
        this.loading = false;
      }
    });
  }

  loadMarcas() {
    this.loadingMarcas = true;
    this.veiculoService.listMarcas({ ativo: true, size: 1000 }).subscribe({
      next: (page) => {
        this.marcas = page.content;
        this.loadingMarcas = false;
      },
      error: (err) => {
        console.error('Erro ao carregar marcas:', err);
        this.loadingMarcas = false;
      }
    });
  }

  loadCombustiveis() {
    this.loadingCombustiveis = true;
    this.veiculoService.listTiposCombustivel().subscribe({
      next: (res) => {
        this.combustiveis = res;
        this.loadingCombustiveis = false;
      },
      error: (err) => {
        console.error('Erro ao carregar tipos de combustível:', err);
        this.loadingCombustiveis = false;
      }
    });
  }

  onMarcaChange() {
    if (this.form.marcaId) {
      this.loadModelos(this.form.marcaId);
    } else {
      this.modelos = [];
      this.form.modeloId = undefined;
    }
    this.anosModelos = [];
    this.form.anoModeloId = undefined;
    this.selectedFabricationYear=null;
  }

  consultarCadastroInterno(): void {
    this.lookupMessage = '';
    this.foundVehicle=null;
    this.existingVehicleId = null;
    if (this.searchMode === 'chassi') {
      this.lookupState = 'unavailable';
      this.lookupMessage = 'A consulta por chassi não possui endpoint integrado. Preencha manualmente.';
      return;
    }
    const plate = (this.form.placa || '').replace(/[^A-Za-z0-9]/g, '').toUpperCase();
    this.form.placa = plate;
    if (!/^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/.test(plate)) {
      this.lookupState = 'error';
      this.lookupMessage = 'Informe uma placa válida com sete caracteres.';
      return;
    }
    if (this.isUxPreview) {
      this.lookupState = 'unavailable';
      this.lookupMessage = 'Consulta indisponível na demonstração sem login.';
      return;
    }
    this.lookupState = 'loading';
    this.veiculoService.getByPlacaInterna(plate).subscribe({
      next: vehicle => {
        this.lookupState = 'found';
        this.foundVehicle = vehicle;
        this.existingVehicleId = vehicle.id;
        this.lookupMessage = 'Veículo já cadastrado na empresa. Abra o registro existente para editar.';
      },
      error: err => {
        this.lookupState = err.status === 404 ? 'none' : 'error';
        this.lookupMessage = err.status === 404
          ? 'Veículo não encontrado. Continue o cadastro manual.'
          : 'Não foi possível consultar a base da oficina. Nenhum serviço externo foi acionado.';
      }
    });
  }

  onPlacaBlur() {
    if (this.id || this.modalMode) return; // Não busca se já estamos editando um veículo existente
    
    const placa = this.form.placa?.trim();
    if (!placa || placa.length < 7) return;

    this.veiculoService.getByPlaca(placa).subscribe({
      next: (res) => {
        if (res) {
          this.aplicarDadosVeiculo(res);
        }
      },
      error: (err) => {
          // 404 é esperado se for um veículo novo no sistema
          if (err.status !== 404) {
              console.error('Erro ao buscar placa no servidor', err);
          }
      }
    });
  }

  private aplicarDadosVeiculo(res: VeiculoResponse) {
      this.form.marcaId = res.marcaId;
      this.form.modeloId = res.modeloId;
      this.form.anoModeloId = res.anoModeloId;
      this.form.combustivelId = res.combustivelId;
      this.form.chassi = res.chassi || this.form.chassi;
      this.form.renavam = res.renavam || this.form.renavam;
      this.form.corExterna = res.corExterna || this.form.corExterna;
      this.form.numeroMotor = res.numeroMotor || this.form.numeroMotor;
      
      // Carregar dependências (modelos e anos) para popular os selects
      if (res.marcaId) this.loadModelos(res.marcaId);
      if (res.modeloId) this.loadAnosModelos(res.modeloId);
      
      this.messageService.add({ severity: 'success', summary: 'Dados Carregados', detail: `Informações do veículo ${res.placa} foram preenchidas.` });
  }

  loadModelos(marcaId: number) {
    this.loadingModelos = true;
    this.veiculoService.listModelos(marcaId).subscribe({
      next: (data) => {
        this.modelos = data;
        this.loadingModelos = false;
      },
      error: (err) => {
        console.error('Erro ao carregar modelos:', err);
        this.loadingModelos = false;
      }
    });
  }

  onModeloChange() {
    if (this.form.modeloId) {
      this.loadAnosModelos(this.form.modeloId);
    } else {
      this.anosModelos = [];
      this.form.anoModeloId = undefined;
    }
  }

  loadAnosModelos(modeloId: number) {
    this.loadingAnosModelos = true;
    this.veiculoService.listAnosModelo(modeloId).subscribe({
      next: (data) => {
        this.anosModelos = data;
        const current=data.find(x=>x.id===this.form.anoModeloId);
        this.selectedFabricationYear=current?.anoFabricacao ?? null;
        this.loadingAnosModelos = false;
      },
      error: (err) => {
        console.error('Erro ao carregar anos modelo:', err);
        this.loadingAnosModelos = false;
      }
    });
  }

  searchCliente(event: {query:string}) {
    const query=(event.query||'').trim();
    this.customerLoading=true;this.customerLookupError='';
    const filter:any={page:0,size:10,sort:'nomeCompleto,asc'};
    // A busca do backend é OR em nome/razão social/nome fantasia/documento.
    if(query)filter.busca=query;
    this.clientesService.list(filter).subscribe({
      next: page => {
        this.filteredClientes=(page.content||[]).map(c=>({...c,
          nome:c.nomeCompleto||c.nomeFantasia||c.razaoSocial||'',cpfCnpj:c.cpf||c.cnpj||''}));
        this.customerLoading=false;
      },
      error: () => {
        this.filteredClientes=[];this.customerLoading=false;
        this.customerLookupError='Não foi possível consultar os clientes desta empresa.';
      }
    });
  }

  get marcaOptions() {
    return this.marcas.map(m => ({ label: m.nome, value: m.id }));
  }

  get modeloOptions() {
    return this.modelos.map(m => ({ label: m.nome, value: m.id }));
  }

  get anoModeloOptions() {
    return this.anosModelos.map(a => ({ label: `${a.anoModelo} / ${a.anoFabricacao} ${a.descricao ? '- '+a.descricao : ''}`, value: a.id }));
  }

  get combustivelOptions() {
    return this.combustiveis.map(c => ({ label: c.nome, value: c.id }));
  }

  // ========== SALVAR VEÍCULO ==========

  salvar() {
    if (this.modalMode && !this.canSaveModal) {
      this.messageService.add({severity:'warn',summary:'Dados obrigatórios',detail:'Selecione cliente, placa, marca, modelo, ano de fabricação/modelo, combustível e cor válidos. O modo demonstração não grava dados.'});
      return;
    }
    const isNew = !this.id;
    const reqPerm = isNew ? 'VEICULO_CRIAR' : 'VEICULO_EDITAR';
    if (!this.permissionsService.getPermission(reqPerm) && !environment.uxPreview) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Atenção',
        detail: 'Seu perfil não possui permissão para realizar esta operação.'
      });
      return;
    }

    if (!this.selectedCliente) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione um cliente.' });
      return;
    }
    this.form.clienteId = this.selectedCliente.id;

    if (!this.form.placa || this.form.placa.trim() === '') {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'A placa é obrigatória.' });
      return;
    }

    this.saving = true;

    const requestBody: VeiculoRequest = {
      placa: this.form.placa,
      chassi: this.form.chassi,
      renavam: this.form.renavam,
      corExterna: this.form.corExterna,
      numeroMotor: this.form.numeroMotor,
      quilometragemAtual: this.form.quilometragemAtual,
      status: this.form.status,
      clienteId: this.form.clienteId,
      marcaId: this.form.marcaId,
      modeloId: this.form.modeloId,
      anoModeloId: this.form.anoModeloId,
      combustivelId: this.form.combustivelId,
      quilometragemCadastro: this.form.quilometragemCadastro,
      dataUltimaRevisao: this.form.dataUltimaRevisao,
      proximaRevisaoData: this.form.proximaRevisaoData,
      proximaRevisaoKm: this.form.proximaRevisaoKm,
      observacoes: this.form.observacoes,
      geracao: this.form.geracao,
      frota: this.form.frota,
      tipoVeiculo: this.form.tipoVeiculo,
      acessorios: this.form.acessorios,
      cilindrada: this.form.cilindrada,
      quantidadeValvulas: this.form.quantidadeValvulas,
      quantidadeCilindros: this.form.quantidadeCilindros,
      nomeMotor: this.form.nomeMotor,
      codigoMotor: this.form.codigoMotor,
      potenciasCv: this.form.potenciasCv,
      linha: this.form.linha,
      entradaAr: this.form.entradaAr,
      sistemaFreios: this.form.sistemaFreios,
      sistemaTracao: this.form.sistemaTracao,
      sistemaInjecao: this.form.sistemaInjecao,
      configuracaoMotor: this.form.configuracaoMotor,
      freioDianteiro: this.form.freioDianteiro,
      freioTraseiro: this.form.freioTraseiro,
      direcao: this.form.direcao,
      cambio: this.form.cambio,
      arCondicionado: this.form.arCondicionado,
      portas: this.form.portas
    };

    const request = this.id
        ? this.veiculoService.update(this.id, requestBody)
        : this.veiculoService.create(requestBody);

    request.subscribe({
      next: (response) => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Veículo salvo com sucesso!' });
        
        if (this.modalMode) { this.saved.emit(response); }
        else if (!this.id && response.id) {
           this.router.navigate(['/veiculo/editar', response.id], { replaceUrl: true });
        }

        this.id = response.id; 
        this.saving = false;
      },
      error: (err) => {
        console.error('Erro ao salvar veículo:', err);
        const msg = err.error?.message || 'Erro ao salvar veículo.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
        this.saving = false;
      }
    });
  }

  cancelar() {
    if (this.modalMode) this.canceled.emit();
    else this.location.back();
  }

  excluirVeiculo() {
    if (!this.id) return;
    if (!this.permissionsService.getPermission('VEICULO_EXCLUIR')) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Atenção',
        detail: 'Seu perfil não possui permissão para realizar esta operação.'
      });
      return;
    }

    this.confirmationService.confirm({
      title: 'Excluir Veículo',
      message: `Tem certeza que deseja excluir este veículo? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir Veículo',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.veiculoService.delete(this.id!).subscribe({
            next: () => {
                this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Veículo excluído com sucesso!' });
                this.router.navigate(['/veiculo']);
            },
            error: (err) => {
                console.error(err);
                this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir veículo.' });
            }
        });
      }
    });
  }
}
