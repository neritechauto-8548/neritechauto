import { CommonModule, Location } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PageHeader } from '@shared';
import { MessageService } from 'primeng/api';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { finalize } from 'rxjs';

import { ClienteDetailResponseDTO, ClienteListResponseDTO, ClientesService } from '../cliente/cliente/cliente.service';
import { StatusCliente } from '../cliente/models/cliente.models';
import {
  OrcamentoDraftResponse,
  OrcamentoDraftService,
  OrcamentoVehicleSummary,
} from './orcamento-draft.service';

interface CustomerOption {
  id: number;
  nome: string;
  documento: string;
  status: StatusCliente;
}

interface VehicleOption extends OrcamentoVehicleSummary {
  label: string;
}

@Component({
  standalone: true,
  selector: 'app-cadastro-orcamento',
  templateUrl: './cadastro-orcamento.html',
  styleUrls: ['./cadastro-orcamento.scss'],
  imports: [
    CommonModule,
    FormsModule,
    PageHeader,
    InputTextModule,
    SelectModule,
    TextareaModule,
    AutoCompleteModule,
    ToastModule,
  ],
  providers: [MessageService],
})
export class CadastroOrcamentoComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly location = inject(Location);
  private readonly clientesService = inject(ClientesService);
  private readonly draftService = inject(OrcamentoDraftService);
  private readonly messageService = inject(MessageService);

  filteredClientes: CustomerOption[] = [];
  selectedCliente: CustomerOption | null = null;
  vehicleOptions: VehicleOption[] = [];
  loadingVehicles = false;
  saving = false;
  createdDraft: OrcamentoDraftResponse | null = null;

  form = {
    veiculoId: null as number | null,
    quilometragemEntrada: null as number | null,
    relatoCliente: '',
    observacoesCliente: '',
    observacoesInternas: '',
  };

  ngOnInit() {
    const clienteId = Number(this.route.snapshot.queryParamMap.get('clienteId'));
    if (Number.isInteger(clienteId) && clienteId > 0) {
      this.prefillCustomer(clienteId);
    }
  }

  searchCliente(event: { query?: string }) {
    const query = (event.query || '').trim();
    const filters: Record<string, string | number> = { page: 0, size: 10 };
    const digits = query.replace(/\D/g, '');

    if (digits.length === 11) {
      filters['cpf'] = digits;
    } else if (digits.length === 14) {
      filters['cnpj'] = digits;
    } else if (query) {
      filters['nomeCompleto'] = query;
    }

    this.clientesService.list(filters).subscribe({
      next: page => {
        this.filteredClientes = (page.content || [])
          .filter(customer => customer.status !== StatusCliente.INATIVO)
          .map(customer => this.toCustomerOption(customer));
      },
      error: () => {
        this.filteredClientes = [];
      },
    });
  }

  onCustomerSelected(event: { value?: CustomerOption } | CustomerOption | null) {
    const selected = this.isCustomerOption(event)
      ? event
      : this.isCustomerOption(event?.value)
        ? event.value
        : null;
    this.selectedCliente = selected;
    this.form.veiculoId = null;
    this.vehicleOptions = [];

    if (selected?.id) {
      this.loadVehicles(selected.id);
    }
  }

  salvar() {
    if (this.saving || this.createdDraft) return;

    if (!this.selectedCliente) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Cliente obrigatório',
        detail: 'Selecione um cliente antes de criar o orçamento.',
      });
      return;
    }

    if (this.selectedCliente.status === StatusCliente.INATIVO) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Cliente inativo',
        detail: 'Reative o cliente antes de criar um novo orçamento.',
      });
      return;
    }

    if (this.form.quilometragemEntrada !== null && this.form.quilometragemEntrada < 0) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Quilometragem inválida',
        detail: 'A quilometragem não pode ser negativa.',
      });
      return;
    }

    this.saving = true;
    this.draftService.create({
      clienteId: this.selectedCliente.id,
      veiculoId: this.form.veiculoId || undefined,
      quilometragemEntrada: this.form.quilometragemEntrada ?? undefined,
      relatoCliente: this.clean(this.form.relatoCliente),
      observacoesCliente: this.clean(this.form.observacoesCliente),
      observacoesInternas: this.clean(this.form.observacoesInternas),
    }).pipe(finalize(() => (this.saving = false))).subscribe({
      next: response => {
        this.createdDraft = response;
        this.messageService.add({
          severity: 'success',
          summary: 'Rascunho criado',
          detail: `Orçamento ${response.numeroOrcamento} criado pelo servidor.`,
        });
      },
      error: error => {
        this.messageService.add({
          severity: 'error',
          summary: 'Não foi possível criar o orçamento',
          detail: error?.error?.message || 'Revise o cliente, o veículo e tente novamente.',
        });
      },
    });
  }

  cancelar() {
    this.location.back();
  }

  novoRascunho() {
    this.createdDraft = null;
    this.form = {
      veiculoId: null,
      quilometragemEntrada: null,
      relatoCliente: '',
      observacoesCliente: '',
      observacoesInternas: '',
    };
  }

  private prefillCustomer(clienteId: number) {
    this.clientesService.getSummary(clienteId).subscribe({
      next: customer => {
        if (customer.status === StatusCliente.INATIVO) {
          this.messageService.add({
            severity: 'warn',
            summary: 'Cliente inativo',
            detail: 'O cliente informado precisa ser reativado antes de receber um novo orçamento.',
          });
          return;
        }
        this.selectedCliente = this.toCustomerOptionFromSummary(customer);
        this.loadVehicles(clienteId);
      },
      error: () => {
        this.messageService.add({
          severity: 'warn',
          summary: 'Cliente não disponível',
          detail: 'Selecione um cliente autorizado para continuar.',
        });
      },
    });
  }

  private loadVehicles(clienteId: number) {
    this.loadingVehicles = true;
    this.draftService.listVehiclesForCustomer(clienteId)
      .pipe(finalize(() => (this.loadingVehicles = false)))
      .subscribe({
        next: vehicles => {
          this.vehicleOptions = vehicles
            .filter(vehicle => vehicle.status !== 'INATIVO')
            .map(vehicle => ({ ...vehicle, label: this.vehicleLabel(vehicle) }));
        },
        error: () => {
          this.vehicleOptions = [];
          this.messageService.add({
            severity: 'info',
            summary: 'Veículos indisponíveis',
            detail: 'O orçamento pode permanecer sem veículo até o vínculo estar disponível.',
          });
        },
      });
  }

  private toCustomerOption(customer: ClienteListResponseDTO): CustomerOption {
    return {
      id: customer.id,
      nome: customer.displayName || `Cliente #${customer.id}`,
      documento: customer.maskedTaxId || 'Documento não informado',
      status: customer.status,
    };
  }

  private toCustomerOptionFromSummary(customer: ClienteDetailResponseDTO): CustomerOption {
    return {
      id: customer.id,
      nome: customer.displayName || `Cliente #${customer.id}`,
      documento: customer.maskedTaxId || 'Documento não informado',
      status: customer.status,
    };
  }

  private isCustomerOption(value: unknown): value is CustomerOption {
    if (!value || typeof value !== 'object') return false;
    const candidate = value as Partial<CustomerOption>;
    return typeof candidate.id === 'number' && typeof candidate.nome === 'string';
  }

  private vehicleLabel(vehicle: OrcamentoVehicleSummary) {
    const name = [vehicle.marcaNome, vehicle.modeloNome].filter(Boolean).join(' ') || `Veículo #${vehicle.id}`;
    const year = vehicle.anoModelo || vehicle.anoFabricacao;
    return [name, vehicle.maskedPlate, year].filter(Boolean).join(' · ');
  }

  private clean(value: string) {
    const trimmed = (value || '').trim();
    return trimmed || undefined;
  }
}
