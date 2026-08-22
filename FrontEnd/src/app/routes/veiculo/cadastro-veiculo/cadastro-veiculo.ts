import { CommonModule, Location } from '@angular/common';
import { Component, HostListener, OnInit, ViewChild, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, CanDeactivateFn, Router } from '@angular/router';
import { PageHeader } from '@shared';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { NgxPermissionsService } from 'ngx-permissions';
import { MessageService } from 'primeng/api';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { SkeletonModule } from 'primeng/skeleton';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { Observable } from 'rxjs';

import {
  ClienteDetailResponseDTO,
  ClienteListResponseDTO,
  ClientesService,
} from '../../cliente/cliente/cliente.service';
import { StatusCliente } from '../../cliente/models/cliente.models';
import {
  AnoModeloResponse,
  MarcaVeiculoResponse,
  ModeloVeiculoResponse,
  StatusVeiculo,
  TipoCombustivelResponse,
  VeiculoRequest,
  VeiculoResponse,
  getStatusVeiculoOptions,
} from '../models/veiculo.models';
import { VeiculoService } from '../veiculo/veiculo.service';

interface CustomerOption {
  id: number;
  nome: string;
  cpfCnpj: string;
  status: StatusCliente;
}

type SaveDestination = 'stay' | 'budget' | 'schedule';
type QuickOrigin = 'orcamento' | 'agenda';

export const pendingVehicleChangesGuard: CanDeactivateFn<CadastroVeiculo> = component =>
  component.canLeave();

@Component({
  selector: 'cadastro-veiculo',
  standalone: true,
  templateUrl: './cadastro-veiculo.html',
  styleUrls: ['./cadastro-veiculo.scss'],
  imports: [
    CommonModule,
    FormsModule,
    PageHeader,
    InputTextModule,
    SelectModule,
    ButtonModule,
    TextareaModule,
    AutoCompleteModule,
    ToastModule,
    TagModule,
    SkeletonModule,
  ],
  providers: [MessageService],
})
export class CadastroVeiculo implements OnInit {
  @ViewChild('vehicleForm') vehicleForm?: NgForm;
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly location = inject(Location);
  private readonly veiculoService = inject(VeiculoService);
  private readonly clientesService = inject(ClientesService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly permissionsService = inject(NgxPermissionsService);

  loading = false;
  saving = false;
  checkingPlate = false;
  error: string | null = null;
  duplicateVehicleId: number | null = null;
  validationErrors: string[] = [];
  saveError: { title: string; detail: string } | null = null;
  origin: QuickOrigin | null = null;
  private originalOdometer: number | null = null;
  private navigationApproved = false;

  id: number | null = null;
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
    status: StatusVeiculo.ATIVO,
  };

  marcas: MarcaVeiculoResponse[] = [];
  modelos: ModeloVeiculoResponse[] = [];
  anosModelos: AnoModeloResponse[] = [];
  combustiveis: TipoCombustivelResponse[] = [];
  loadingMarcas = false;
  loadingModelos = false;
  loadingAnosModelos = false;
  loadingCombustiveis = false;
  filteredClientes: CustomerOption[] = [];
  selectedCliente: CustomerOption | null = null;

  get statusOptions() {
    const options = getStatusVeiculoOptions();

    return this.form.status === StatusVeiculo.INATIVO
      ? options
      : options.filter(option => option.value !== StatusVeiculo.INATIVO);
  }

  ngOnInit() {
    this.loadMarcas();
    this.loadCombustiveis();

    const requestedOrigin = this.route.snapshot.queryParamMap.get('origin');
    this.origin = requestedOrigin === 'orcamento' || requestedOrigin === 'agenda'
      ? requestedOrigin
      : null;

    this.route.paramMap.subscribe(params => {
      const idStr = params.get('id');
      if (idStr) {
        this.id = Number(idStr);
        this.loadVeiculo(this.id);
        return;
      }

      this.route.queryParamMap.subscribe(qParams => {
        const cId = qParams.get('clienteId');
        if (!cId) {
          return;
        }

        this.form.clienteId = Number(cId);
        this.clientesService.getSummary(Number(cId)).subscribe(c => {
          this.selectedCliente = this.toCustomerOption(c);
          this.markPristineAfterRender();
        });
      });
    });
  }

  loadVeiculo(id: number) {
    this.loading = true;
    this.veiculoService.getById(id).subscribe({
      next: res => {
        this.form = { ...res };
        this.originalOdometer = res.quilometragemAtual ?? null;
        this.duplicateVehicleId = null;

        if (res.clienteId) {
          this.clientesService.getSummary(res.clienteId).subscribe(c => {
            this.selectedCliente = this.toCustomerOption(c);
          });
        }
        if (res.marcaId) {
          this.loadModelos(res.marcaId);
        }
        if (res.modeloId) {
          this.loadAnosModelos(res.modeloId);
        }
        this.loading = false;
        this.markPristineAfterRender();
      },
      error: err => {
        console.error('Erro ao carregar veículo', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: err.error?.message || 'Erro ao carregar veículo.',
        });
        this.loading = false;
      },
    });
  }

  loadMarcas() {
    this.loadingMarcas = true;
    this.veiculoService.listMarcas({ ativo: true, size: 1000 }).subscribe({
      next: page => {
        this.marcas = page.content;
        this.loadingMarcas = false;
      },
      error: err => {
        console.error('Erro ao carregar marcas:', err);
        this.loadingMarcas = false;
      },
    });
  }

  loadCombustiveis() {
    this.loadingCombustiveis = true;
    this.veiculoService.listTiposCombustivel().subscribe({
      next: res => {
        this.combustiveis = res;
        this.loadingCombustiveis = false;
      },
      error: err => {
        console.error('Erro ao carregar tipos de combustível:', err);
        this.loadingCombustiveis = false;
      },
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
  }

  onPlacaBlur() {
    if (this.id) {
      return;
    }

    const placa = this.normalizePlate(this.form.placa);
    this.form.placa = placa;
    this.duplicateVehicleId = null;

    if (placa.length < 7) {
      return;
    }

    this.checkingPlate = true;
    this.veiculoService.getByPlaca(placa).subscribe({
      next: existing => {
        this.checkingPlate = false;
        this.duplicateVehicleId = existing.id;
        this.messageService.add({
          severity: 'warn',
          summary: 'Veículo já cadastrado',
          detail: `A placa ${placa} já pertence ao veículo #${existing.id}. Abra o cadastro existente em vez de criar uma duplicidade.`,
          life: 7000,
        });
      },
      error: err => {
        if (err.status === 404) {
          this.loadExternalPlateSuggestion(placa);
          return;
        }

        this.checkingPlate = false;
        console.error('Erro ao verificar placa no cadastro canônico', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Não foi possível verificar a placa',
          detail: 'Tente novamente antes de salvar o veículo.',
        });
      },
    });
  }

  private loadExternalPlateSuggestion(placa: string) {
    this.veiculoService.lookupExternalByPlaca(placa).subscribe({
      next: suggestion => {
        this.checkingPlate = false;
        this.aplicarSugestaoExterna(suggestion);
      },
      error: err => {
        this.checkingPlate = false;
        // Enriquecimento externo é opcional; 404/indisponibilidade não bloqueia cadastro manual.
        if (err.status !== 404) {
          console.warn('Consulta externa indisponível; cadastro manual permanece habilitado', err);
        }
      },
    });
  }

  private aplicarSugestaoExterna(res: VeiculoResponse) {
    this.form.marcaId = res.marcaId;
    this.form.modeloId = res.modeloId;
    this.form.anoModeloId = res.anoModeloId;
    this.form.combustivelId = res.combustivelId;
    this.form.corExterna = res.corExterna || this.form.corExterna;

    // Defesa em profundidade: sugestões externas nunca preenchem automaticamente
    // VIN/chassi, RENAVAM ou número do motor, mesmo que um provider os devolva.
    if (res.marcaId) {
      this.loadModelos(res.marcaId);
    }
    if (res.modeloId) {
      this.loadAnosModelos(res.modeloId);
    }

    this.messageService.add({
      severity: 'info',
      summary: 'Sugestão encontrada',
      detail: `Alguns dados de ${res.placa} foram sugeridos por fonte externa. Confirme as informações antes de salvar.`,
    });
  }

  loadModelos(marcaId: number) {
    this.loadingModelos = true;
    this.veiculoService.listModelos(marcaId).subscribe({
      next: data => {
        this.modelos = data;
        this.loadingModelos = false;
      },
      error: err => {
        console.error('Erro ao carregar modelos:', err);
        this.loadingModelos = false;
      },
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
      next: data => {
        this.anosModelos = data;
        this.loadingAnosModelos = false;
      },
      error: err => {
        console.error('Erro ao carregar anos modelo:', err);
        this.loadingAnosModelos = false;
      },
    });
  }

  searchCliente(event: { query?: string }) {
    const query = (event.query || '').trim();
    const isNumeric = /^\d+$/.test(query.replace(/[.-]/g, ''));
    const filter: Record<string, string | number> = { page: 0, size: 10 };

    if (isNumeric) {
      const cleanQuery = query.replace(/\D/g, '');
      if (cleanQuery.length > 11) {
        filter.cnpj = cleanQuery;
      } else {
        filter.cpf = cleanQuery;
      }
    } else {
      filter['nomeCompleto'] = query;
    }

    this.clientesService.list(filter).subscribe({
      next: page => {
        this.filteredClientes = page.content
          .filter(c => Boolean(this.id) || c.status !== StatusCliente.INATIVO)
          .map(c => this.toCustomerOption(c));
      },
      error: err => console.error('Erro ao buscar clientes:', err),
    });
  }

  get marcaOptions() {
    return this.marcas.map(m => ({ label: m.nome, value: m.id }));
  }

  private toCustomerOption(
    customer: ClienteListResponseDTO | ClienteDetailResponseDTO
  ): CustomerOption {
    return {
      id: customer.id,
      nome: customer.displayName || `Cliente #${customer.id}`,
      cpfCnpj: customer.maskedTaxId || 'Documento não informado',
      status: customer.status,
    };
  }

  get modeloOptions() {
    return this.modelos.map(m => ({ label: m.nome, value: m.id }));
  }

  get anoModeloOptions() {
    return this.anosModelos.map(a => ({
      label: `${a.anoModelo} / ${a.anoFabricacao} ${a.descricao ? `- ${a.descricao}` : ''}`,
      value: a.id,
    }));
  }

  get combustivelOptions() {
    return this.combustiveis.map(c => ({ label: c.nome, value: c.id }));
  }

  get isQuickCreate() { return Boolean(this.origin); }
  get hasUnsavedChanges() {
    return Boolean(this.vehicleForm?.dirty && !this.navigationApproved && !this.saving);
  }
  get canCreateBudget() { return Boolean(this.permissionsService.getPermission('OS_INCLUIR')); }
  get canSchedule() {
    return Boolean(this.permissionsService.getPermission('GERAL_AGENDAMENTO_EDITAR'));
  }
  get canDeactivateVehicle() {
    return Boolean(this.id && this.permissionsService.getPermission('VEICULO_EXCLUIR'));
  }
  get canReactivateVehicle() {
    return Boolean(
      this.id
      && this.form.status === StatusVeiculo.INATIVO
      && this.permissionsService.getPermission('VEICULO_EDITAR')
    );
  }

  salvar(destination: SaveDestination = 'stay') {
    const isNew = !this.id;
    const requiredPermission = isNew ? 'VEICULO_CRIAR' : 'VEICULO_EDITAR';
    const resolvedDestination = destination === 'stay' && this.origin
      ? (this.origin === 'orcamento' ? 'budget' : 'schedule')
      : destination;

    if (!this.permissionsService.getPermission(requiredPermission)) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Atenção',
        detail: 'Seu perfil não possui permissão para realizar esta operação.',
      });
      return;
    }

    if (this.checkingPlate) {
      this.messageService.add({
        severity: 'info',
        summary: 'Verificando placa',
        detail: 'Conclua a verificação da placa antes de salvar.',
      });
      return;
    }

    if (!this.validateBeforeSave(isNew, resolvedDestination)) {
      return;
    }

    this.form.clienteId = this.selectedCliente!.id;
    this.saving = true;
    this.saveError = null;
    const requestBody: VeiculoRequest = {
      placa: this.normalizePlate(this.form.placa),
      chassi: this.normalizeVin(this.form.chassi),
      renavam: this.form.renavam?.replace(/\D/g, ''),
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
    };

    const request = this.id
      ? this.veiculoService.update(this.id, requestBody)
      : this.veiculoService.create(requestBody);

    request.subscribe({
      next: response => {
        this.saving = false;
        this.id = response.id;
        this.originalOdometer = response.quilometragemAtual
          ?? requestBody.quilometragemAtual
          ?? null;
        this.duplicateVehicleId = null;
        this.validationErrors = [];
        this.markPristineAfterRender();
        this.messageService.add({
          severity: 'success',
          summary: 'Veículo salvo',
          detail: 'O registro canônico foi atualizado com sucesso.',
        });

        this.navigateAfterSave(response, resolvedDestination, isNew);
      },
      error: err => {
        this.saving = false;
        console.error('Falha ao salvar veículo', { status: err?.status });
        this.saveError = this.mapSaveError(err?.status);
        this.messageService.add({
          severity: 'error',
          summary: this.saveError.title,
          detail: this.saveError.detail,
        });
      },
    });
  }

  cancelar() {
    const decision = this.canLeave();
    if (typeof decision === 'boolean') {
      if (decision) this.leaveCurrentScreen();
      return;
    }
    decision.subscribe(confirmed => {
      if (confirmed) this.leaveCurrentScreen();
    });
  }

  canLeave(): boolean | Observable<boolean> {
    if (!this.hasUnsavedChanges) return true;
    return this.confirmationService.confirm({
      title: 'Descartar alterações?',
      message: 'Existem dados ainda não salvos. Ao sair, essas alterações serão perdidas.',
      confirmText: 'Descartar e sair',
      cancelText: 'Continuar editando',
      type: 'warning',
      icon: 'warning',
    });
  }

  @HostListener('window:beforeunload', ['$event'])
  preventAccidentalUnload(event: BeforeUnloadEvent) {
    if (!this.hasUnsavedChanges) return;
    event.preventDefault();
    event.returnValue = '';
  }

  inativarVeiculo() {
    if (!this.id) {
      return;
    }
    if (!this.permissionsService.getPermission('VEICULO_EXCLUIR')) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Atenção',
        detail: 'Seu perfil não possui permissão para inativar este veículo.',
      });
      return;
    }

    this.confirmationService.confirm({
      title: 'Inativar veículo',
      message: 'O veículo será inativado, mas seu cadastro, vínculos e histórico serão preservados. Deseja continuar?',
      confirmText: 'Inativar veículo',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning',
    }).subscribe(confirmed => {
      if (!confirmed) {
        return;
      }

      this.veiculoService.deactivate(this.id!).subscribe({
        next: vehicle => {
          this.form.status = vehicle.status;
          this.navigationApproved = true;
          this.messageService.add({
            severity: 'success',
            summary: 'Veículo inativado',
            detail: 'O histórico foi preservado.',
          });
          this.router.navigate(['/veiculos']);
        },
        error: err => {
          console.error('Falha ao inativar veículo', { status: err?.status });
          this.messageService.add({
            severity: 'error',
            summary: 'Não foi possível inativar',
            detail: 'Tente novamente. O cadastro e o histórico permanecem inalterados.',
          });
        },
      });
    });
  }

  reativarVeiculo() {
    if (!this.id || !this.canReactivateVehicle) return;
    this.veiculoService.reactivate(this.id).subscribe({
      next: vehicle => {
        this.form.status = vehicle.status;
        this.markPristineAfterRender();
        this.messageService.add({
          severity: 'success',
          summary: 'Veículo reativado',
          detail: 'O registro voltou a aceitar novos fluxos operacionais.',
        });
      },
      error: err => {
        console.error('Falha ao reativar veículo', { status: err?.status });
        this.messageService.add({
          severity: 'error',
          summary: 'Não foi possível reativar',
          detail: 'Tente novamente. Nenhum dado foi alterado.',
        });
      },
    });
  }

  private validateBeforeSave(isNew: boolean, destination: SaveDestination): boolean {
    this.validationErrors = [];
    this.form.placa = this.normalizePlate(this.form.placa);
    this.form.chassi = this.normalizeVin(this.form.chassi);

    if (isNew && this.duplicateVehicleId) {
      this.validationErrors.push(`A placa já pertence ao veículo #${this.duplicateVehicleId}.`);
    }
    if (!this.selectedCliente) this.validationErrors.push('Selecione o cliente responsável.');
    if (this.selectedCliente?.status === StatusCliente.INATIVO) {
      this.validationErrors.push('Reative o cliente antes de vincular um novo atendimento.');
    }
    if (!/^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/.test(this.form.placa)) {
      this.validationErrors.push('Informe uma placa brasileira válida no formato AAA0A00.');
    }
    if (this.form.chassi && !/^[A-HJ-NPR-Z0-9]{17}$/.test(this.form.chassi)) {
      this.validationErrors.push('O VIN/chassi deve ter 17 caracteres válidos.');
    }
    const renavam = this.form.renavam?.replace(/\D/g, '') || '';
    if (renavam && renavam.length !== 11) {
      this.validationErrors.push('O RENAVAM deve conter 11 dígitos quando informado.');
    }
    if ((this.form.quilometragemCadastro ?? 0) < 0 || (this.form.quilometragemAtual ?? 0) < 0) {
      this.validationErrors.push('A quilometragem não pode ser negativa.');
    }
    if (
      this.originalOdometer != null
      && this.form.quilometragemAtual != null
      && this.form.quilometragemAtual < this.originalOdometer
    ) {
      this.validationErrors.push(
        'A regressão do odômetro exige um fluxo auditável de correção, ainda indisponível nesta tela.'
      );
    }
    if (destination === 'budget' && !this.canCreateBudget) {
      this.validationErrors.push('Seu perfil não permite iniciar um orçamento.');
    }
    if (destination === 'schedule' && !this.canSchedule) {
      this.validationErrors.push('Seu perfil não permite iniciar um agendamento.');
    }

    if (this.validationErrors.length === 0) return true;
    this.messageService.add({
      severity: 'warn',
      summary: 'Revise os dados',
      detail: 'Há campos ou regras que precisam de atenção antes de salvar.',
    });
    queueMicrotask(() => document.getElementById('vehicle-validation-summary')?.focus());
    return false;
  }

  private navigateAfterSave(
    response: VeiculoResponse,
    destination: SaveDestination,
    wasNew: boolean
  ) {
    if (destination === 'budget') {
      this.navigationApproved = true;
      this.router.navigate(['/orcamentos/novo'], {
        queryParams: { clienteId: response.clienteId, veiculoId: response.id },
      });
      return;
    }
    if (destination === 'schedule') {
      this.navigationApproved = true;
      this.router.navigate(['/agenda/novo'], {
        queryParams: { clienteId: response.clienteId, veiculoId: response.id },
      });
      return;
    }
    if (wasNew) {
      this.navigationApproved = true;
      this.router.navigate(['/veiculos', response.id, 'editar'], { replaceUrl: true });
    }
  }

  private mapSaveError(status?: number) {
    if (status === 409 || status === 412) {
      return {
        title: 'Conflito de atualização',
        detail: 'O registro mudou ou entrou em conflito. Recarregue os dados antes de tentar novamente.',
      };
    }
    if (status === 422 || status === 400) {
      return {
        title: 'Dados não aceitos',
        detail: 'Revise os campos destacados. Nenhuma alteração foi persistida.',
      };
    }
    return {
      title: 'Não foi possível salvar',
      detail: 'Tente novamente. Se o problema continuar, informe o horário da tentativa ao suporte.',
    };
  }

  private leaveCurrentScreen() {
    this.navigationApproved = true;
    this.location.back();
  }

  private markPristineAfterRender() {
    queueMicrotask(() => this.vehicleForm?.form.markAsPristine());
  }

  private normalizePlate(value?: string): string {
    return (value || '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
  }

  private normalizeVin(value?: string): string {
    return (value || '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
  }
}
