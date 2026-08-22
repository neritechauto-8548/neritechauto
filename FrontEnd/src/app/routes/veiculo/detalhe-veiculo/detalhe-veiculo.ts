import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PageHeader } from '@shared';
import { NgxPermissionsService } from 'ngx-permissions';
import { catchError, finalize, of, switchMap } from 'rxjs';

import {
  ClienteDetailResponseDTO,
  ClientesService,
} from '../../cliente/cliente/cliente.service';
import {
  StatusVeiculo,
  StatusVeiculoLabels,
  VeiculoResponse,
} from '../models/veiculo.models';
import { VeiculoService } from '../veiculo/veiculo.service';

type PassportTab =
  | 'resumo'
  | 'ficha'
  | 'historico'
  | 'inspecoes'
  | 'servicos'
  | 'quilometragem'
  | 'recomendacoes'
  | 'documentos';

interface PassportTabOption {
  key: PassportTab;
  label: string;
  icon: string;
}

interface PassportAlert {
  tone: 'warning' | 'info';
  title: string;
  detail: string;
}

@Component({
  selector: 'app-detalhe-veiculo',
  standalone: true,
  imports: [CommonModule, PageHeader],
  templateUrl: './detalhe-veiculo.html',
  styleUrl: './detalhe-veiculo.scss',
})
export class DetalheVeiculo implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly vehicles = inject(VeiculoService);
  private readonly customers = inject(ClientesService);
  private readonly permissions = inject(NgxPermissionsService);

  readonly tabs: PassportTabOption[] = [
    { key: 'resumo', label: 'Resumo', icon: 'pi pi-th-large' },
    { key: 'ficha', label: 'Ficha técnica', icon: 'pi pi-car' },
    { key: 'historico', label: 'Histórico', icon: 'pi pi-history' },
    { key: 'inspecoes', label: 'Inspeções', icon: 'pi pi-verified' },
    { key: 'servicos', label: 'Serviços e peças', icon: 'pi pi-wrench' },
    { key: 'quilometragem', label: 'Quilometragem', icon: 'pi pi-chart-line' },
    { key: 'recomendacoes', label: 'Próximas revisões', icon: 'pi pi-calendar-clock' },
    { key: 'documentos', label: 'Documentos', icon: 'pi pi-paperclip' },
  ];

  activeTab: PassportTab = 'resumo';
  loading = true;
  fatalError = false;
  customerFailed = false;
  vehicle?: VeiculoResponse;
  currentCustomer?: ClienteDetailResponseDTO;

  ngOnInit() {
    this.load();
  }

  get vehicleId() {
    return this.vehicle?.id;
  }

  get isInactive() {
    return this.vehicle?.status === StatusVeiculo.INATIVO;
  }

  get canEdit() {
    return Boolean(this.permissions.getPermission('VEICULO_EDITAR'));
  }

  get canCreateEstimate() {
    return !this.isInactive
      && Boolean(this.vehicle?.clienteId)
      && Boolean(this.permissions.getPermission('OS_INCLUIR'));
  }

  get canSchedule() {
    return !this.isInactive
      && Boolean(this.vehicle?.clienteId)
      && Boolean(this.permissions.getPermission('GERAL_AGENDAMENTO_EDITAR'));
  }

  get plateLabel() {
    return this.vehicle?.placa || `Veículo #${this.vehicleId || ''}`;
  }

  get vehicleName() {
    const name = [this.vehicle?.marcaNome, this.vehicle?.modeloNome].filter(Boolean).join(' ');
    return name || `Veículo #${this.vehicleId || ''}`;
  }

  get customerName() {
    return this.currentCustomer?.displayName
      || this.vehicle?.clienteNome
      || 'Sem responsável vigente';
  }

  get statusLabel() {
    const status = this.vehicle?.status;
    return status ? StatusVeiculoLabels[status] || status : 'Não informado';
  }

  get yearLabel() {
    if (this.vehicle?.anoFabricacao && this.vehicle.anoModelo) {
      return `${this.vehicle.anoFabricacao}/${this.vehicle.anoModelo}`;
    }
    return String(this.vehicle?.anoModelo || this.vehicle?.anoFabricacao || 'Não informado');
  }

  get odometerLabel() {
    return this.formatOdometer(this.vehicle?.quilometragemAtual);
  }

  get alerts(): PassportAlert[] {
    const alerts: PassportAlert[] = [];

    if (this.isInactive) {
      alerts.push({
        tone: 'warning',
        title: 'Veículo inativo',
        detail: 'O passaporte permanece consultável, mas novas operações estão bloqueadas.',
      });
    }
    if (!this.vehicle?.marcaNome || !this.vehicle?.modeloNome || this.yearLabel === 'Não informado') {
      alerts.push({
        tone: 'warning',
        title: 'Ficha técnica incompleta',
        detail: 'Marca, modelo ou ano precisam ser confirmados no cadastro canônico.',
      });
    }
    if (this.vehicle?.quilometragemAtual == null) {
      alerts.push({
        tone: 'warning',
        title: 'Quilometragem não informada',
        detail: 'Nenhuma leitura confiável está disponível para orientar a operação.',
      });
    }
    if (this.customerFailed) {
      alerts.push({
        tone: 'info',
        title: 'Cliente parcialmente indisponível',
        detail: 'A ficha do veículo continua disponível sem substituir o vínculo por dados simulados.',
      });
    }

    return alerts;
  }

  selectTab(tab: PassportTab) {
    this.activeTab = tab;
  }

  onTabKeydown(event: KeyboardEvent, index: number) {
    const keys = ['ArrowRight', 'ArrowLeft', 'Home', 'End'];
    if (!keys.includes(event.key)) return;

    event.preventDefault();
    let nextIndex = index;
    if (event.key === 'ArrowRight') nextIndex = (index + 1) % this.tabs.length;
    if (event.key === 'ArrowLeft') nextIndex = (index - 1 + this.tabs.length) % this.tabs.length;
    if (event.key === 'Home') nextIndex = 0;
    if (event.key === 'End') nextIndex = this.tabs.length - 1;

    this.activeTab = this.tabs[nextIndex].key;
    const container = (event.currentTarget as HTMLElement).parentElement;
    const targets = container?.querySelectorAll<HTMLButtonElement>('[role="tab"]');
    targets?.item(nextIndex).focus();
  }

  backToList() {
    this.router.navigate(['/veiculos']);
  }

  editVehicle() {
    if (!this.vehicleId || !this.canEdit) return;
    this.router.navigate(['/veiculos', this.vehicleId, 'editar']);
  }

  openLinks() {
    if (!this.vehicleId) return;
    this.router.navigate(['/veiculos', this.vehicleId, 'vinculos']);
  }

  openCustomer() {
    if (!this.vehicle?.clienteId) return;
    this.router.navigate(['/clientes', this.vehicle.clienteId]);
  }

  createEstimate() {
    if (!this.vehicleId || !this.vehicle?.clienteId || !this.canCreateEstimate) return;
    this.router.navigate(['/orcamentos/novo'], {
      queryParams: { clienteId: this.vehicle.clienteId, veiculoId: this.vehicleId },
    });
  }

  schedule() {
    if (!this.vehicleId || !this.vehicle?.clienteId || !this.canSchedule) return;
    this.router.navigate(['/agenda/novo'], {
      queryParams: { clienteId: this.vehicle.clienteId, veiculoId: this.vehicleId },
    });
  }

  retry() {
    this.load();
  }

  formatOdometer(value?: number) {
    return value == null ? 'Não informado' : `${new Intl.NumberFormat('pt-BR').format(value)} km`;
  }

  protectedIdentifier(value?: string) {
    return value ? 'Registrado · acesso protegido' : 'Não informado';
  }

  private load() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isInteger(id) || id <= 0) {
      this.loading = false;
      this.fatalError = true;
      return;
    }

    this.loading = true;
    this.fatalError = false;
    this.customerFailed = false;
    this.vehicle = undefined;
    this.currentCustomer = undefined;

    this.vehicles.getById(id).pipe(
      switchMap(vehicle => {
        this.vehicle = vehicle;
        if (!vehicle.clienteId) return of(undefined);

        return this.customers.getSummary(vehicle.clienteId).pipe(
          catchError(() => {
            this.customerFailed = true;
            return of(undefined);
          })
        );
      }),
      finalize(() => (this.loading = false))
    ).subscribe({
      next: customer => (this.currentCustomer = customer),
      error: () => {
        this.vehicle = undefined;
        this.currentCustomer = undefined;
        this.fatalError = true;
      },
    });
  }
}
