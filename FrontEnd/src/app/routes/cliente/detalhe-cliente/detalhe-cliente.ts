import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPermissionsService } from 'ngx-permissions';
import { Observable, catchError, finalize, forkJoin, map, of, switchMap } from 'rxjs';

import { PageHeader } from '@shared';
import { StatusVeiculo, StatusVeiculoLabels } from '../../veiculo/models/veiculo.models';
import {
  OrigemClienteLabels,
  StatusCliente,
  StatusClienteLabels,
  TipoClienteLabels,
  TipoContatoLabels,
} from '../models/cliente.models';
import {
  CustomerAddressSummary,
  CustomerContactSummary,
  CustomerDetailReadService,
  CustomerDetailSummary,
  CustomerVehicleSummary,
} from './detalhe-cliente.service';

type DetailTab = 'resumo' | 'contatos' | 'enderecos' | 'veiculos' | 'historico' | 'preferencias';

interface PartialResource<T> {
  data: T;
  failed: boolean;
}

@Component({
  selector: 'app-detalhe-cliente',
  standalone: true,
  imports: [CommonModule, PageHeader],
  templateUrl: './detalhe-cliente.html',
  styleUrl: './detalhe-cliente.scss',
})
export class DetalheCliente implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly readService = inject(CustomerDetailReadService);
  private readonly permissions = inject(NgxPermissionsService);

  readonly tabs: { key: DetailTab; label: string; icon: string }[] = [
    { key: 'resumo', label: 'Resumo', icon: 'pi pi-id-card' },
    { key: 'contatos', label: 'Contatos', icon: 'pi pi-phone' },
    { key: 'enderecos', label: 'Endereços', icon: 'pi pi-map-marker' },
    { key: 'veiculos', label: 'Veículos', icon: 'pi pi-car' },
    { key: 'historico', label: 'Histórico', icon: 'pi pi-history' },
    { key: 'preferencias', label: 'Preferências', icon: 'pi pi-sliders-h' },
  ];

  activeTab: DetailTab = 'resumo';
  loading = true;
  fatalError = false;
  customer?: CustomerDetailSummary;
  contacts: CustomerContactSummary[] = [];
  addresses: CustomerAddressSummary[] = [];
  vehicles: CustomerVehicleSummary[] = [];
  partialFailures = { contacts: false, addresses: false, vehicles: false };

  ngOnInit() {
    this.load();
  }

  get customerId() {
    return this.customer?.id;
  }

  get displayName() {
    return this.customer?.displayName || 'Cliente';
  }

  get typeLabel() {
    return this.customer ? TipoClienteLabels[this.customer.type] || 'Cliente' : 'Cliente';
  }

  get statusLabel() {
    return this.customer ? StatusClienteLabels[this.customer.status] || this.customer.status : 'Não informado';
  }

  get isInactive() {
    return this.customer?.status === StatusCliente.INATIVO;
  }

  get canEdit() {
    return Boolean(this.permissions.getPermission('CLIENTE_EDITAR'));
  }

  get canCreateVehicle() {
    return !this.isInactive && Boolean(this.permissions.getPermission('VEICULO_CRIAR'));
  }

  get canCreateEstimate() {
    return !this.isInactive && Boolean(this.permissions.getPermission('OS_INCLUIR'));
  }

  get canSchedule() {
    return !this.isInactive && Boolean(this.permissions.getPermission('GERAL_AGENDAMENTO_EDITAR'));
  }

  get canOpenVehicle() {
    return Boolean(this.permissions.getPermission('VEICULO_EDITAR'));
  }

  get maskedTaxId() {
    return this.customer?.maskedTaxId || 'Não informado';
  }

  get primaryContact() {
    const primary = this.contacts.find(contact => contact.principal) || this.contacts[0];
    return primary?.maskedValue || this.customer?.maskedEmail || 'Não informado';
  }

  get activeVehicles() {
    return this.vehicles.filter(vehicle => vehicle.status !== StatusVeiculo.INATIVO);
  }

  get originLabel() {
    const origin = this.customer?.origin;
    return origin ? OrigemClienteLabels[origin] || origin : 'Não informada';
  }

  get hasRelationshipNotes() {
    return Boolean(this.customer?.hasRelationshipNotes);
  }

  selectTab(tab: DetailTab) {
    this.activeTab = tab;
  }

  editCustomer() {
    if (!this.customerId || !this.canEdit) return;
    this.router.navigate(['/clientes', this.customerId, 'editar']);
  }

  createVehicle() {
    if (!this.customerId || !this.canCreateVehicle) return;
    this.router.navigate(['/veiculos/cadastro'], { queryParams: { clienteId: this.customerId } });
  }

  createEstimate() {
    if (!this.customerId || !this.canCreateEstimate) return;
    this.router.navigate(['/orcamentos/novo'], { queryParams: { clienteId: this.customerId } });
  }

  schedule() {
    if (!this.customerId || !this.canSchedule) return;
    this.router.navigate(['/agenda/novo'], { queryParams: { clienteId: this.customerId } });
  }

  openVehicle(vehicle: CustomerVehicleSummary) {
    if (!this.canOpenVehicle) return;
    this.router.navigate(['/veiculos/editar', vehicle.id]);
  }

  backToList() {
    this.router.navigate(['/clientes']);
  }

  retry() {
    this.load();
  }

  contactTypeLabel(contact: CustomerContactSummary) {
    return TipoContatoLabels[contact.tipoContato] || 'Contato';
  }

  vehicleTitle(vehicle: CustomerVehicleSummary) {
    return [vehicle.marcaNome, vehicle.modeloNome].filter(Boolean).join(' ') || `Veículo #${vehicle.id}`;
  }

  vehicleYear(vehicle: CustomerVehicleSummary) {
    if (vehicle.anoFabricacao && vehicle.anoModelo) return `${vehicle.anoFabricacao}/${vehicle.anoModelo}`;
    return vehicle.anoModelo ? String(vehicle.anoModelo) : 'Ano não informado';
  }

  vehicleStatus(vehicle: CustomerVehicleSummary) {
    return vehicle.status ? StatusVeiculoLabels[vehicle.status] || vehicle.status : 'Não informado';
  }

  private load() {
    const id = this.route.snapshot.paramMap.get('uuid');
    if (!id) {
      this.fatalError = true;
      this.loading = false;
      return;
    }

    this.loading = true;
    this.fatalError = false;
    this.partialFailures = { contacts: false, addresses: false, vehicles: false };

    this.readService.getCustomer(id).pipe(
      switchMap(customer => {
        this.customer = customer;
        return forkJoin({
          contacts: this.partial(
            this.readService.getContacts(id).pipe(map(response => response.content || [])),
            [] as CustomerContactSummary[]
          ),
          addresses: this.partial(
            this.readService.getAddresses(id).pipe(map(response => response.content || [])),
            [] as CustomerAddressSummary[]
          ),
          vehicles: this.partial(this.readService.getVehicles(id), [] as CustomerVehicleSummary[]),
        });
      }),
      finalize(() => (this.loading = false))
    ).subscribe({
      next: resources => {
        this.contacts = resources.contacts.data;
        this.addresses = resources.addresses.data;
        this.vehicles = resources.vehicles.data;
        this.partialFailures = {
          contacts: resources.contacts.failed,
          addresses: resources.addresses.failed,
          vehicles: resources.vehicles.failed,
        };
      },
      error: () => {
        this.customer = undefined;
        this.contacts = [];
        this.addresses = [];
        this.vehicles = [];
        this.fatalError = true;
      },
    });
  }

  private partial<T>(source: Observable<T>, fallback: T) {
    return source.pipe(
      map(data => ({ data, failed: false }) as PartialResource<T>),
      catchError(() => of({ data: fallback, failed: true } as PartialResource<T>))
    );
  }
}
