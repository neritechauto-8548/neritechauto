import { CommonModule, Location } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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

import { ClientesService } from '../../cliente/cliente/cliente.service';
import {
  AnoModeloResponse,
  MarcaVeiculoResponse,
  ModeloVeiculoResponse,
  TipoCombustivelResponse,
  VeiculoRequest,
  VeiculoResponse,
  getStatusVeiculoOptions,
} from '../models/veiculo.models';
import { VeiculoService } from '../veiculo/veiculo.service';

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
  ],
  providers: [MessageService],
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

  loading = false;
  saving = false;
  checkingPlate = false;
  error: string | null = null;
  duplicateVehicleId: number | null = null;

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
    status: undefined,
  };

  marcas: MarcaVeiculoResponse[] = [];
  modelos: ModeloVeiculoResponse[] = [];
  anosModelos: AnoModeloResponse[] = [];
  combustiveis: TipoCombustivelResponse[] = [];
  loadingMarcas = false;
  loadingModelos = false;
  loadingAnosModelos = false;
  loadingCombustiveis = false;
  statusOptions = getStatusVeiculoOptions();

  filteredClientes: any[] = [];
  selectedCliente: any | null = null;

  ngOnInit() {
    this.loadMarcas();
    this.loadCombustiveis();

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
        this.clientesService.getById(Number(cId)).subscribe(c => {
          this.selectedCliente = {
            ...c,
            nome: c.nomeCompleto || c.nomeFantasia || c.razaoSocial || '',
            cpfCnpj: c.cpf || c.cnpj || '',
          };
        });
      });
    });
  }

  loadVeiculo(id: number) {
    this.loading = true;
    this.veiculoService.getById(id).subscribe({
      next: res => {
        this.form = { ...res };
        this.duplicateVehicleId = null;

        if (res.clienteId) {
          this.clientesService.getById(res.clienteId).subscribe(c => {
            this.selectedCliente = {
              ...c,
              nome: c.nomeCompleto || c.nomeFantasia || c.razaoSocial || '',
              cpfCnpj: c.cpf || c.cnpj || '',
            };
          });
        }
        if (res.marcaId) {
          this.loadModelos(res.marcaId);
        }
        if (res.modeloId) {
          this.loadAnosModelos(res.modeloId);
        }
        this.loading = false;
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

  searchCliente(event: any) {
    const query = event.query;
    const isNumeric = /^\d+$/.test(query.replace(/[.-]/g, ''));
    const filter: any = {};

    if (isNumeric) {
      const cleanQuery = query.replace(/\D/g, '');
      if (cleanQuery.length > 11) {
        filter.cnpj = cleanQuery;
      } else {
        filter.cpf = cleanQuery;
      }
    } else {
      filter.nomeCompleto = query;
      filter.nomeFantasia = query;
      filter.razaoSocial = query;
    }

    this.clientesService.list(filter).subscribe({
      next: page => {
        this.filteredClientes = page.content.map(c => ({
          ...c,
          nome: c.nomeCompleto || c.nomeFantasia || c.razaoSocial || '',
          cpfCnpj: c.cpf || c.cnpj || '',
        }));
      },
      error: err => console.error('Erro ao buscar clientes:', err),
    });
  }

  get marcaOptions() {
    return this.marcas.map(m => ({ label: m.nome, value: m.id }));
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

  salvar() {
    const isNew = !this.id;
    const requiredPermission = isNew ? 'VEICULO_CRIAR' : 'VEICULO_EDITAR';

    if (!this.permissionsService.getPermission(requiredPermission)) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Atenção',
        detail: 'Seu perfil não possui permissão para realizar esta operação.',
      });
      return;
    }

    if (isNew && this.duplicateVehicleId) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Cadastro duplicado bloqueado',
        detail: `A placa já pertence ao veículo #${this.duplicateVehicleId}. Abra o registro existente.`,
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

    if (!this.selectedCliente) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione um cliente.' });
      return;
    }
    this.form.clienteId = this.selectedCliente.id;

    this.form.placa = this.normalizePlate(this.form.placa);
    if (!this.form.placa) {
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
    };

    const request = this.id
      ? this.veiculoService.update(this.id, requestBody)
      : this.veiculoService.create(requestBody);

    request.subscribe({
      next: response => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Veículo salvo com sucesso!',
        });

        if (!this.id && response.id) {
          this.router.navigate(['/veiculo/editar', response.id], { replaceUrl: true });
        }

        this.id = response.id;
        this.duplicateVehicleId = null;
        this.saving = false;
      },
      error: err => {
        console.error('Erro ao salvar veículo:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: err.error?.message || 'Erro ao salvar veículo.',
        });
        this.saving = false;
      },
    });
  }

  cancelar() {
    this.location.back();
  }

  excluirVeiculo() {
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

      // DELETE é mantido por compatibilidade, mas o backend executa inativação lógica.
      this.veiculoService.delete(this.id!).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Veículo inativado',
            detail: 'O histórico foi preservado.',
          });
          this.router.navigate(['/veiculo']);
        },
        error: err => {
          console.error(err);
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: err.error?.message || 'Erro ao inativar veículo.',
          });
        },
      });
    });
  }

  private normalizePlate(value?: string): string {
    return (value || '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
  }
}
