import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { InputTextModule } from 'primeng/inputtext';
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

  // Paginação
  rows = 10;
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
    nome: ['', [Validators.required, Validators.maxLength(255)]],
    precoBase: [null, [Validators.required, Validators.min(0)]],
    custo: [null, [Validators.required, Validators.min(0)]],
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
    const pageIndex = Math.floor(this.first / this.rows) + 1;
    const params: Record<string, any> = { page: pageIndex, size: this.rows, sort: 'nome,asc' };
    if (this.searchTerm.trim()) params['search'] = this.searchTerm.trim();

    this.service.list(params).subscribe({
      next: (res) => {
        this.backendPage = res;
        this.servicos = res.content;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.isLoading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar serviços.' });
      },
    });
  }

  abrirCriar(): void {
    this.modo = 'criar';
    this.servicoEditando = undefined;
    this.form.reset({ nome: '', precoBase: null, custo: null, instrucoesExecucao: '', ativo: true });
    this.showModal = true;
  }

  abrirEditar(s: ServicoResponse): void {
    this.modo = 'editar';
    this.servicoEditando = s;
    this.form.setValue({
      nome: s.nome,
      precoBase: s.precoBase,
      custo: s.custo,
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
    if (this.form.invalid || this.isSaving) return;
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
      error: () => {
        this.isSaving = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao salvar serviço.' });
      },
    });
  }

  private readonly confirmationService = inject(ConfirmationService);

  excluir(s: ServicoResponse): void {
    this.confirmationService.confirm({
      title: 'Excluir Serviço',
      message: `Tem certeza que deseja excluir o serviço "${s.nome}"? Esta ação não pode ser desfeita.`,
      icon: 'warning',
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger'
    }).subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.service.delete(s.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Excluído', detail: `Serviço "${s.nome}" removido.` });
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
