import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPermissionsService } from 'ngx-permissions';
import { catchError, finalize, forkJoin, map, of, switchMap } from 'rxjs';

import { PageHeader } from '@shared';
import { VeiculoResponse, StatusVeiculoLabels } from '../../veiculo/models/veiculo.models';
import { VeiculoService } from '../../veiculo/veiculo/veiculo.service';
import { ClientesService } from '../cliente/cliente.service';
import {
  ClienteResponse,
  ContatoClienteResponse,
  EnderecoClienteResponse,
  OrigemClienteLabels,
  StatusCliente,
  StatusClienteLabels,
  TipoCliente,
  TipoClienteLabels,
  TipoContatoLabels,
} from '../models/cliente.models';

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
  private readonly clientesService = inject(ClientesService);
  private readonly veiculoService = inject(VeiculoService);
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
  customer?: ClienteResponse;
  contacts: ContatoClienteResponse[] = [];
  addresses: EnderecoClienteResponse[] = [];
  vehicles: VeiculoResponse[] = [];
  partialFailures = {
    contacts: false,
    addresses: false,
    vehicles: false,
  };

  ngOnInit() {
    this.load();
  }

  get customerId() {
    return this.customer?.id;
  }

  get displayName() {
    if (!this.customer) return 'Cliente';
    return this.customer.tipoCliente === TipoCliente.PESSOA_JURIDICA
      ? this.customer.razaoSocial || this.customer.nomeFantasia || this.customer.nomeCompleto || `Cliente #${this.customer.id}`
      : this.customer.nomeCompleto || `Cliente #${this.customer.id}`;
  }

  get typeLabel() {
    return this.customer ? TipoClienteLabels[this.customer.tipoCliente] || 'Cliente' : 'Cliente';
  }

  get statusLabel() {
    return this.customer?.status ? StatusClienteLabels[this.customer.status] || this.customer.status : 'Não informado';
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

  get maskedTaxId() {
    const value = this.customer?.tipoCliente === TipoCliente.PESSOA_JURIDICA
      ? this.customer?.cnpj
      : this.customer?.cpf;
    return this.maskDocument(value || '');
  }

  get primaryContact() {
    const primary = this.contacts.find(contact => contact.principal) || this.contacts[0];
    return primary ? this.maskContact(this.contactValue(primary)) : 'Não informado';
  }

  get activeVehicles() {
    return this.vehicles.filter(vehicle => vehicle.status !== 'INATIVO');
  }

  get originLabel() {
    const origin = this.customer?.origemCliente;
    return origin ? OrigemClienteLabels[origin] || origin : 'Não informada';
  }

  get relationshipNotes() {
    return this.customer?.observacoesGerais?.trim() || '';
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

  openVehicle(vehicle: VeiculoResponse) {
    this.router.navigate(['/veiculos/editar', vehicle.id]);
  }

  backToList() {
    this.router.navigate(['/clientes']);
  }

  retry() {
    this.load();
  }

  contactTypeLabel(contact: ContatoClienteResponse) {
    return TipoContatoLabels[contact.tipoContato] || 'Contato';
  }

  maskedContact(contact: ContatoClienteResponse) {
    return this.maskContact(this.contactValue(contact));
  }

  addressSummary(address: EnderecoClienteResponse) {
    const parts = [
      address.logradouro,
      address.numero ? 'nº •••' : '',
      address.bairro,
      address.cidade,
      address.estado?.toUpperCase(),
    ].filter(Boolean);
    return parts.join(', ');
  }

  maskedCep(address: EnderecoClienteResponse) {
    const digits = (address.cep || '').replace(/\D/g, '');
    if (digits.length !== 8) return 'CEP protegido';
    return `${digits.slice(0, 2)}***-${digits.slice(-3)}`;
  }

  vehicleTitle(vehicle: VeiculoResponse) {
    return [vehicle.marcaNome, vehicle.modeloNome].filter(Boolean).join(' ') || `Veículo #${vehicle.id}`;
  }

  vehicleYear(vehicle: VeiculoResponse) {
    if (vehicle.anoFabricacao && vehicle.anoModelo) {
      return `${vehicle.anoFabricacao}/${vehicle.anoModelo}`;
    }
    return vehicle.anoModelo ? String(vehicle.anoModelo) : 'Ano não informado';
  }

  vehicleStatus(vehicle: VeiculoResponse) {
    return vehicle.status ? StatusVeiculoLabels[vehicle.status] || vehicle.status : 'Não informado';
  }

  maskedPlate(vehicle: VeiculoResponse) {
    const plate = (vehicle.placa || '').replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    if (plate.length < 4) return 'Placa protegida';
    return `${plate.slice(0, 3)}••${plate.slice(-2)}`;
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

    this.clientesService.getById(id).pipe(
      switchMap(customer => {
        this.customer = customer;
        return forkJoin({
          contacts: this.partial(
            this.clientesService.listarContatos(id).pipe(map(response => response.content || [])),
            [] as ContatoClienteResponse[]
          ),
          addresses: this.partial(
            this.clientesService.listarEnderecos(id).pipe(map(response => response.content || [])),
            [] as EnderecoClienteResponse[]
          ),
          vehicles: this.partial(this.veiculoService.list(Number(id)), [] as VeiculoResponse[]),
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

  private partial<T>(source: import('rxjs').Observable<T>, fallback: T) {
    return source.pipe(
      map(data => ({ data, failed: false }) as PartialResource<T>),
      catchError(() => of({ data: fallback, failed: true } as PartialResource<T>))
    );
  }

  private contactValue(contact: ContatoClienteResponse) {
    return contact.contato ?? contact.valor ?? '';
  }

  private maskDocument(value: string) {
    const clean = value.replace(/[^a-zA-Z0-9]/g, '');
    if (!clean) return 'Não informado';
    if (clean.length === 11) return `***.${clean.slice(3, 6)}.${clean.slice(6, 9)}-**`;
    if (clean.length === 14) return `**.${clean.slice(2, 5)}.${clean.slice(5, 8)}/****-**`;
    return `${clean.slice(0, 2)}••••${clean.slice(-2)}`;
  }

  private maskContact(value: string) {
    const trimmed = (value || '').trim();
    if (!trimmed) return 'Não informado';

    if (trimmed.includes('@')) {
      const [local, domain = ''] = trimmed.split('@');
      const maskedLocal = local.length <= 1 ? '*' : `${local[0]}***`;
      const [host, ...rest] = domain.split('.');
      const maskedHost = host ? `${host[0] || '*'}***` : '***';
      return `${maskedLocal}@${maskedHost}${rest.length ? `.${rest.join('.')}` : ''}`;
    }

    const digits = trimmed.replace(/\D/g, '');
    if (digits.length >= 8) {
      return `(**) *****-${digits.slice(-4)}`;
    }
    return `${trimmed.slice(0, 1)}•••${trimmed.slice(-1)}`;
  }
}
