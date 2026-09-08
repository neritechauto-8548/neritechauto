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
  StatusClienteLabels,
  TipoClienteLabels,
} from '../../cliente/models/cliente.models';
import {
  StatusVeiculo,
  StatusVeiculoLabels,
  VeiculoResponse,
} from '../models/veiculo.models';
import { VeiculoService } from '../veiculo/veiculo.service';

@Component({
  selector: 'app-vinculos-veiculo',
  standalone: true,
  imports: [CommonModule, PageHeader],
  templateUrl: './vinculos-veiculo.html',
  styleUrl: './vinculos-veiculo.scss',
})
export class VinculosVeiculo implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly vehicles = inject(VeiculoService);
  private readonly customers = inject(ClientesService);

  loading = true;
  customerLoading = false;
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

  get hasCurrentLink() {
    return Boolean(this.vehicle?.clienteId);
  }

  get vehicleName() {
    const name = [this.vehicle?.marcaNome, this.vehicle?.modeloNome].filter(Boolean).join(' ');
    return name || `Veículo #${this.vehicleId || ''}`;
  }

  get plateLabel() {
    return this.vehicle?.placa || `Veículo #${this.vehicleId || ''}`;
  }

  get vehicleStatusLabel() {
    const status = this.vehicle?.status;
    return status ? StatusVeiculoLabels[status] || status : 'Não informado';
  }

  get isVehicleInactive() {
    return this.vehicle?.status === StatusVeiculo.INATIVO;
  }

  get customerName() {
    return this.currentCustomer?.displayName
      || this.vehicle?.clienteNome
      || 'Responsável protegido';
  }

  get customerTypeLabel() {
    const type = this.currentCustomer?.type;
    return type ? TipoClienteLabels[type] || type : 'Tipo não disponível';
  }

  get customerStatusLabel() {
    const status = this.currentCustomer?.status;
    return status ? StatusClienteLabels[status] || status : 'Status não disponível';
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

  retryCustomer() {
    if (!this.vehicle?.clienteId || this.customerLoading) return;

    this.customerLoading = true;
    this.customerFailed = false;
    this.customers.getSummary(this.vehicle.clienteId).pipe(
      finalize(() => (this.customerLoading = false))
    ).subscribe({
      next: customer => (this.currentCustomer = customer),
      error: () => {
        this.currentCustomer = undefined;
        this.customerFailed = true;
      },
    });
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
