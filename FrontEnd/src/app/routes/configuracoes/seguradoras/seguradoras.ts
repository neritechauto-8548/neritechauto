import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import {
  HistoricoSeguradora, Seguradora, SeguradoraForm, SeguradoraService, VinculoOS
} from './seguradora.service';

@Component({
  selector: 'nt-seguradoras',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './seguradoras.html'
})
export class SeguradorasComponent implements OnInit {
  private readonly api = inject(SeguradoraService);
  items: Seguradora[] = [];
  total = 0;
  page = 0;
  size = 20;
  busca = '';
  status: 'todos' | 'ativos' | 'inativos' = 'todos';
  loading = false;
  saving = false;
  error = '';
  formError = '';
  notice = '';
  selected: Seguradora | null = null;
  formVisible = false;
  confirmInactivate = false;
  form: SeguradoraForm = this.empty();
  history: HistoricoSeguradora[] = [];
  historyPage = 0;
  historyTotal = 0;
  historyLoading = false;
  historyError = '';
  osInput: number | null = null;
  osTipo: 'CONVENIO' | 'REEMBOLSO' = 'CONVENIO';
  osLink: VinculoOS | null = null;
  linking = false;
  osError = '';

  ngOnInit(): void { this.listar(); }

  private empty(): SeguradoraForm {
    return { nomeFantasia: '', razaoSocial: '', cnpj: '', registroSusep: '',
      nomeContato: '', telefone: '', email: '', endereco: '', tipoConvenio: null,
      notas: '', ativo: true };
  }

  listar(): void {
    if (this.loading) return;
    this.loading = true;
    this.error = '';
    const ativo = this.status === 'todos' ? undefined : this.status === 'ativos';
    this.api.listar(this.page, this.size, this.busca, ativo)
      .pipe(finalize(() => this.loading = false)).subscribe({
        next: res => { this.items = res.content || []; this.total = res.totalElements || 0; },
        error: err => {
          this.items = []; this.total = 0;
          this.error = this.describeError(err);
        }
      });
  }

  filtrar(): void { this.page = 0; this.listar(); }
  mudarTamanho(size: number): void {
    if (![20, 50, 100].includes(Number(size))) return;
    this.size = Number(size);
    this.filtrar();
  }
  proxima(delta: number): void {
    const page = this.page + delta;
    if (page < 0 || page * this.size >= this.total) return;
    this.page = page;
    this.listar();
  }

  novo(): void {
    this.selected = null; this.form = this.empty();
    this.formVisible = true; this.formError = ''; this.notice = '';
    this.history = []; this.osLink = null; this.osError = '';
    this.confirmInactivate = false;
  }

  editar(item: Seguradora): void {
    this.selected = item;
    this.form = { nomeFantasia: item.nomeFantasia, razaoSocial: item.razaoSocial,
      cnpj: item.cnpj, registroSusep: item.registroSusep, nomeContato: item.nomeContato,
      telefone: item.telefone, email: item.email, endereco: item.endereco,
      tipoConvenio: item.tipoConvenio, notas: item.notas, ativo: item.ativo };
    this.formVisible = true; this.formError = ''; this.notice = '';
    this.history = []; this.osLink = null; this.osError = '';
    this.confirmInactivate = false;
    this.carregarHistorico(0);
  }

  fechar(): void { if (!this.saving) this.formVisible = false; }

  salvar(): void {
    if (this.saving || !this.form.nomeFantasia.trim()) return;
    this.saving = true; this.formError = ''; this.notice = '';
    const payload = { ...this.form, nomeFantasia: this.form.nomeFantasia.trim() };
    const req = this.selected
      ? this.api.editar(this.selected.id, payload, this.selected.versao)
      : this.api.criar(payload);
    req.pipe(finalize(() => this.saving = false)).subscribe({
      next: value => {
        this.selected = value;
        this.notice = 'Seguradora salva e confirmada pelo servidor.';
        this.formVisible = false;
        this.listar();
      },
      error: err => { this.formError = this.describeError(err); }
    });
  }

  inativar(): void {
    if (!this.selected || !this.confirmInactivate || this.saving) return;
    this.saving = true; this.formError = '';
    this.api.inativar(this.selected.id).pipe(finalize(() => this.saving = false)).subscribe({
      next: () => {
        this.confirmInactivate = false; this.formVisible = false;
        this.notice = 'Seguradora inativada. OS existentes foram preservadas.';
        this.listar();
      },
      error: err => { this.formError = this.describeError(err); }
    });
  }

  carregarHistorico(page: number): void {
    if (!this.selected || this.historyLoading) return;
    this.historyLoading = true; this.historyError = '';
    this.api.historico(this.selected.id, page)
      .pipe(finalize(() => this.historyLoading = false)).subscribe({
        next: value => {
          this.history = value.content || [];
          this.historyTotal = value.totalElements || 0;
          this.historyPage = page;
        },
        error: err => { this.historyError = this.describeError(err); }
      });
  }

  consultarOS(): void {
    if (!this.osInput || this.osInput <= 0 || this.linking) return;
    this.linking = true; this.osError = ''; this.osLink = null;
    this.api.vinculoOS(this.osInput).pipe(finalize(() => this.linking = false)).subscribe({
      next: value => { this.osLink = value; },
      error: err => { this.osError = this.describeError(err); }
    });
  }

  vincularOS(): void {
    if (!this.selected?.ativo || !this.osInput || this.linking || this.osLink) return;
    this.linking = true; this.osError = '';
    this.api.vincular(this.selected.id, this.osInput, this.osTipo)
      .pipe(finalize(() => this.linking = false)).subscribe({
        next: value => { this.osLink = value; this.notice = 'Vínculo com OS confirmado no servidor.'; this.listar(); },
        error: err => { this.osError = this.describeError(err); }
      });
  }

  desvincularOS(): void {
    if (!this.selected || !this.osInput || this.linking ||
        this.osLink?.seguradoraId !== this.selected.id) return;
    this.linking = true; this.osError = '';
    this.api.desvincular(this.selected.id, this.osInput)
      .pipe(finalize(() => this.linking = false)).subscribe({
        next: () => { this.osLink = null; this.notice = 'Vínculo removido; OS preservada.'; this.listar(); },
        error: err => { this.osError = this.describeError(err); }
      });
  }

  private describeError(err: any): string {
    if (err?.status === 403) return 'Acesso não autorizado a esta empresa ou operação (403).';
    if (err?.status === 409) return 'Conflito: cadastro duplicado, OS já vinculada ou versão desatualizada (409).';
    if (err?.status === 422 || err?.status === 400) return 'Dados inválidos: revise CNPJ, contato e convênio.';
    if (err?.status === 0) return 'Sem conexão com o servidor. Verifique a rede e tente novamente.';
    return 'Não foi possível concluir a operação. Tente novamente.';
  }
}
