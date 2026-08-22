import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PageHeader } from '@shared';
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

interface AttentionCard {
  label: string;
  icon: string;
  detail: string;
}

@Component({
  selector: 'app-revisoes-veiculo',
  standalone: true,
  imports: [CommonModule, PageHeader],
  templateUrl: './revisoes-veiculo.html',
  styleUrl: './revisoes-veiculo.scss',
})
export class RevisoesVeiculo implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly vehicles = inject(VeiculoService);
  private readonly customers = inject(ClientesService);

  readonly attentionCards: AttentionCard[] = [
    {
      label: 'Vencidas',
      icon: 'pi pi-exclamation-circle',
      detail: 'Exige status calculado por regra autoritativa.',
    },
    {
      label: 'Próximas 30 dias',
      icon: 'pi pi-calendar',
      detail: 'Exige recomendação com dueDate e origem.',
    },
    {
      label: 'Próximas por km',
      icon: 'pi pi-gauge',
      detail: 'Exige baseline real com instante e fonte.',
    },
    {
      label: 'Sem parâmetro suficiente',
      icon: 'pi pi-question-circle',
      detail: 'Exige read model para contagem confiável.',
    },
  ];

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

  get plateLabel() {
    return this.vehicle?.placa || `Veículo #${this.vehicleId || ''}`;
  }

  get vehicleName() {
    const name = [this.vehicle?.marcaNome, this.vehicle?.modeloNome].filter(Boolean).join(' ');
    return name || `Veículo #${this.vehicleId || ''}`;
  }

  get vehicleStatusLabel() {
    const status = this.vehicle?.status;
    return status ? StatusVeiculoLabels[status] || status : 'Não informado';
  }

  get isInactive() {
    return this.vehicle?.status === StatusVeiculo.INATIVO;
  }

  get customerName() {
    return this.currentCustomer?.displayName
      || this.vehicle?.clienteNome
      || 'Sem responsável vigente';
  }

  get hasDateParameter() {
    return Boolean(this.vehicle?.proximaRevisaoData);
  }

  get hasOdometerParameter() {
    return this.vehicle?.proximaRevisaoKm != null;
  }

  get hasAnyParameter() {
    return this.hasDateParameter || this.hasOdometerParameter;
  }

  get odometerLabel() {
    return this.formatOdometer(this.vehicle?.quilometragemAtual);
  }

  backToPassport() {
    if (!this.vehicleId) return this.router.navigate(['/veiculos']);
    return this.router.navigate(['/veiculos', this.vehicleId]);
  }

  backToList() {
    this.router.navigate(['/veiculos']);
  }

  openCustomer() {
    if (!this.vehicle?.clienteId) return;
    this.router.navigate(['/clientes', this.vehicle.clienteId]);
  }

  retry() {
    this.load();
  }

  formatOdometer(value?: number) {
    return value == null ? 'Não informado' : `${new Intl.NumberFormat('pt-BR').format(value)} km`;
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
