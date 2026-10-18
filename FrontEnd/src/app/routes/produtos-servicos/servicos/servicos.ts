import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { ServicoService } from './servico.service';
import { Page, ServicoResponse } from '../models/servico.models';

@Component({
  selector: 'app-servicos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    InputNumberModule,
    ToastModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
  ],
  templateUrl: './servicos.html',
  styleUrls: ['./servicos.scss'],
  providers: [MessageService],
})
export class Servicos implements OnInit {
  private readonly service = inject(ServicoService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly messageService = inject(MessageService);
  private readonly fb = inject(FormBuilder);

  servicos: ServicoResponse[] = [];
  backendPage?: Page<ServicoResponse>;
  isLoading = false;
  loadError = '';
  statusFilter: '' | 'true' | 'false' = '';
  areaFilter: number | null = null;

  // Paginação
  rows = 20;
  first = 0;
  get totalRecords() { return this.backendPage?.totalElements ?? 0; }
  get rangeStart() { return this.totalRecords === 0 ? 0 : this.first + 1; }
  get rangeEnd() { return Math.min(this.first + this.rows, this.totalRecords); }

  // Busca
  searchTerm = '';

  // Modal de criação/edição
  showModal = false;
  modo: 'criar' | 'editar' = 'criar';
  servicoEditando?: ServicoResponse;
  isSaving = false;

  form: FormGroup = this.fb.group({
    codigo: ['', [Validators.maxLength(50)]],
    nome: ['', [Validators.required, Validators.maxLength(255)]],
    areaId: [null, [Validators.required, Validators.min(1)]],
    unidade: ['HORA', [Validators.required, Validators.maxLength(30)]],
    tempoPrevistoHoras: [null, [Validators.required, Validators.min(0.01)]],
    precoBase: [null, [Validators.required, Validators.min(0)]],
    custo: [null, [Validators.required, Validators.min(0)]],
    aliquotaIss: [null, [Validators.min(0)]],
    codigoServicoMunicipal: ['', [Validators.maxLength(30)]],
    instrucoesExecucao: [''],
    ativo: [true],
  });

  ngOnInit(): void {
    this.fetchPage();
  }

  onSearch(): void {
    this.first = 0;
    this.fetchPage();
  }

  goPrev(): void {
    this.first = Math.max(0, this.first - this.rows);
    this.fetchPage();
  }

  goNext(): void {
    if (this.first + this.rows < this.totalRecords) {
      this.first += this.rows;
      this.fetchPage();
    }
  }

  private fetchPage(): void {
    this.isLoading = true;
    this.loadError = '';
    const pageIndex = Math.floor(this.first / this.rows);
    const params: Record<string, any> = { page: pageIndex, size: this.rows, sort: 'nome,asc' };
    if (this.searchTerm.trim()) params['search'] = this.searchTerm.trim();
    if (this.statusFilter) params['ativo'] = this.statusFilter;
    if (this.areaFilter) params['areaId'] = this.areaFilter;

    this.service.list(params).subscribe({
      next: (res) => {
        this.backendPage = res;
        this.servicos = res.content;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        this.loadError = err?.status === 403 ? 'Você não tem permissão para consultar serviços.' : 'Não foi possível carregar os serviços. Tente novamente.';
      },
    });
  }

  abrirCriar(): void {
    this.modo = 'criar';
    this.servicoEditando = undefined;
    this.form.reset({ codigo: '', nome: '', areaId: null, unidade: 'HORA', tempoPrevistoHoras: null, precoBase: null, custo: null, aliquotaIss: null, codigoServicoMunicipal: '', instrucoesExecucao: '', ativo: true });
    this.showModal = true;
  }

  abrirEditar(s: ServicoResponse): void {
    this.modo = 'editar';
    this.servicoEditando = s;
    this.form.setValue({
      codigo: s.codigo || '',
      nome: s.nome,
      areaId: s.areaId,
      unidade: s.unidade,
      tempoPrevistoHoras: s.tempoPrevistoHoras,
      precoBase: s.precoBase,
      custo: s.custo,
      aliquotaIss: s.aliquotaIss ?? null,
      codigoServicoMunicipal: s.codigoServicoMunicipal || '',
      instrucoesExecucao: s.instrucoesExecucao || '',
      ativo: s.ativo,
    });
    this.showModal = true;
  }

  fecharModal(): void {
    this.showModal = false;
    this.servicoEditando = undefined;
  }

  salvar(): void {
    if (this.isSaving) return;

    const nome = this.form.get('nome')?.value ? this.form.get('nome')?.value.trim() : '';
    if (!nome) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O nome do serviço é obrigatório.' });
      return;
    }
    if (nome.length < 2 || nome.length > 255) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O nome do serviço deve ter entre 2 e 255 caracteres.' });
      return;
    }

    const precoBase = this.form.get('precoBase')?.value;
    if (precoBase === null || precoBase === undefined || precoBase === '') {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O preço base é obrigatório.' });
      return;
    }
    if (Number(precoBase) < 0) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O preço base não pode ser negativo.' });
      return;
    }

    const custo = this.form.get('custo')?.value;
    if (custo === null || custo === undefined || custo === '') {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O custo é obrigatório.' });
      return;
    }
    if (Number(custo) < 0) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O custo não pode ser negativo.' });
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.messageService.add({ severity: 'warn', summary: 'Revise os campos', detail: 'Área, unidade e tempo maior que zero são obrigatórios.' });
      return;
    }

    if (Number(precoBase) < Number(custo) && !window.confirm('O preço está abaixo do custo. Deseja salvar mesmo assim?')) return;

    this.isSaving = true;
    const payload = this.form.value;

    const obs = this.modo === 'criar'
      ? this.service.create(payload)
      : this.service.update(this.servicoEditando!.id, payload);

    obs.subscribe({
      next: () => {
        this.isSaving = false;
        this.showModal = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: this.modo === 'criar' ? 'Serviço cadastrado com sucesso.' : 'Serviço atualizado com sucesso.',
        });
        this.fetchPage();
      },
      error: (err) => {
        this.isSaving = false;
        const msg = err?.error?.details || err?.error?.message || 'Falha ao salvar serviço.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
      },
    });
  }

  private readonly confirmationService = inject(ConfirmationService);

  excluir(s: ServicoResponse): void {
    this.confirmationService.confirm({
      title: 'Desativar serviço',
      message: `Desativar "${s.nome}"? O histórico será preservado e ele deixará de estar disponível em novas OS.`,
      icon: 'warning',
      confirmText: 'Desativar',
      cancelText: 'Cancelar',
      type: 'danger'
    }).subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.service.delete(s.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Desativado', detail: `Serviço "${s.nome}" desativado.` });
            this.fetchPage();
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao excluir serviço.' });
          },
        });
      }
    });
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value ?? 0);
  }

  inicialLetter(nome: string): string {
    return (nome || '?').charAt(0).toUpperCase();
  }
}
