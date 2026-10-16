import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import {
  Transportadora, TransportadoraForm, TransportadoraService,
  HistoricoTransportadora, VinculoCompra
} from './transportadora.service';

@Component({
  selector: 'nt-transportadoras',
  standalone: true,
  imports: [CommonModule, FormsModule, NeriTechIcon],
  templateUrl: './transportadoras.html'
})
export class TransportadorasComponent implements OnInit {
  private readonly api = inject(TransportadoraService);
  rows: Transportadora[] = [];
  total = 0;
  page = 0;
  size = 20;
  busca = '';
  regiao = '';
  status: 'todas' | 'ativas' | 'inativas' = 'todas';
  loading = false;
  error = '';
  notice = '';
  selected: Transportadora | null = null;
  editing = false;
  saving = false;
  formError = '';
  confirmInactivate = false;
  form: TransportadoraForm = this.blank();
  historico: HistoricoTransportadora[] = [];
  historicoPage = 0;
  historicoTotal = 0;
  historicoLoading = false;
  historicoError = '';
  pedidoId: number | null = null;
  documentoRastreio = '';
  vinculo: VinculoCompra | null = null;
  compraBusy = false;
  compraError = '';

  ngOnInit(): void { this.listar(); }

  private blank(): TransportadoraForm {
    return { tipoPessoa: 'JURIDICA', nomeFantasia: '', razaoSocial: '',
      cpf: '', cnpj: '', fornecedorId: null, contato: '', telefone: '',
      email: '', endereco: '', cep: '', cidade: '', estado: '',
      modalidade: 'RODOVIARIO', ativo: true, observacoes: '' };
  }

  listar(): void {
    if (this.loading) return;
    this.loading = true;
    this.error = '';
    const ativo = this.status === 'todas' ? undefined : this.status === 'ativas';
    this.api.listar(this.page, this.size, this.busca, this.regiao, ativo)
      .pipe(finalize(() => this.loading = false)).subscribe({
        next: result => {
          this.rows = result.content || [];
          this.total = result.totalElements ?? 0;
        },
        error: err => {
          this.rows = []; this.total = 0;
          this.error = this.describe(err);
        }
      });
  }

  filtrar(): void { this.page = 0; this.listar(); }
  porPagina(size: number): void {
    if (![20, 50, 100].includes(Number(size))) return;
    this.size = Number(size);
    this.filtrar();
  }
  pagina(delta: number): void {
    const next = this.page + delta;
    if (next < 0 || next * this.size >= this.total) return;
    this.page = next;
    this.listar();
  }

  nova(): void {
    this.selected = null;
    this.form = this.blank();
    this.editing = true;
    this.formError = ''; this.notice = '';
    this.historico = []; this.vinculo = null; this.compraError = '';
    this.confirmInactivate = false;
  }

  editar(item: Transportadora): void {
    this.selected = item;
    this.form = {
      fornecedorId: item.id, tipoPessoa: item.tipoPessoa,
      nomeFantasia: item.nomeFantasia, razaoSocial: item.razaoSocial,
      cpf: item.cpf, cnpj: item.cnpj, contato: item.contato, telefone: item.telefone,
      email: item.email, endereco: item.endereco, cep: item.cep,
      cidade: item.cidade, estado: item.estado, modalidade: item.modalidade,
      ativo: item.ativo, observacoes: item.observacoes
    };
    this.editing = true; this.formError = ''; this.notice = '';
    this.historico = []; this.historicoError = '';
    this.vinculo = null; this.compraError = ''; this.pedidoId = null;
    this.confirmInactivate = false;
    this.carregarHistorico(0);
  }

  fechar(): void { if (!this.saving && !this.compraBusy) this.editing = false; }

  salvar(): void {
    if (this.saving || !this.form.nomeFantasia?.trim()) return;
    this.saving = true; this.formError = ''; this.notice = '';
    const payload = { ...this.form, nomeFantasia: this.form.nomeFantasia.trim() };
    const request = this.selected
      ? this.api.editar(this.selected.id, payload, this.selected.versao)
      : this.api.criar(payload);
    request.pipe(finalize(() => this.saving = false)).subscribe({
      next: result => {
        this.selected = result;
        this.editing = false;
        this.notice = 'Cadastro confirmado pelo servidor.';
        this.listar();
      },
      error: err => { this.formError = this.describe(err); }
    });
  }

  inativar(): void {
    if (!this.selected?.ativo || !this.confirmInactivate || this.saving) return;
    this.saving = true; this.formError = '';
    this.api.inativar(this.selected.id).pipe(finalize(() => this.saving = false))
      .subscribe({
        next: () => {
          this.confirmInactivate = false;
          this.editing = false;
          this.notice = 'Papel logístico inativado; fornecedor e documentos anteriores preservados.';
          this.listar();
        },
        error: err => { this.formError = this.describe(err); }
      });
  }

  carregarHistorico(page: number): void {
    if (!this.selected || this.historicoLoading || page < 0) return;
    this.historicoLoading = true; this.historicoError = '';
    this.api.historico(this.selected.id, page)
      .pipe(finalize(() => this.historicoLoading = false))
      .subscribe({
        next: res => {
          this.historico = res.content || [];
          this.historicoTotal = res.totalElements || 0;
          this.historicoPage = page;
        },
        error: err => { this.historicoError = this.describe(err); }
      });
  }

  consultarPedido(): void {
    if (!this.pedidoId || this.pedidoId <= 0 || this.compraBusy) return;
    this.compraBusy = true;
    this.compraError = ''; this.vinculo = null;
    this.api.consultarPedido(this.pedidoId)
      .pipe(finalize(() => this.compraBusy = false)).subscribe({
        next: value => {
          this.vinculo = value;
          this.documentoRastreio = value.documentoRastreio || '';
        },
        error: err => { this.compraError = this.describe(err); }
      });
  }

  vincularPedido(): void {
    if (!this.selected?.ativo || !this.pedidoId || this.compraBusy
        || (this.vinculo?.transportadoraId && this.vinculo.transportadoraId !== this.selected.id)) return;
    this.compraBusy = true; this.compraError = '';
    this.api.vincularPedido(this.selected.id, this.pedidoId, this.documentoRastreio)
      .pipe(finalize(() => this.compraBusy = false)).subscribe({
        next: result => {
          this.vinculo = result;
          this.notice = 'Vínculo logístico salvo no pedido existente.';
          this.listar(); this.carregarHistorico(0);
        },
        error: err => { this.compraError = this.describe(err); }
      });
  }

  removerPedido(): void {
    if (!this.selected || !this.pedidoId || this.compraBusy
        || this.vinculo?.transportadoraId !== this.selected.id) return;
    this.compraBusy = true; this.compraError = '';
    this.api.desvincularPedido(this.selected.id, this.pedidoId)
      .pipe(finalize(() => this.compraBusy = false)).subscribe({
        next: () => {
          this.vinculo = null; this.documentoRastreio = '';
          this.notice = 'Referência logística removida; pedido e nota fiscal preservados.';
          this.listar(); this.carregarHistorico(0);
        },
        error: err => { this.compraError = this.describe(err); }
      });
  }

  private describe(err: any): string {
    if (err?.status === 403) return 'Acesso negado ou dados de outra empresa (403).';
    if (err?.status === 409) return 'Conflito de documento, vínculo ou versão (409). Se já é fornecedor, informe seu ID para reaproveitar o cadastro.';
    if (err?.status === 422 || err?.status === 400) return 'Dados inválidos. Confira CPF/CNPJ, telefone, CEP e modalidade.';
    if (err?.status === 0) return 'Sem conexão com o servidor. Confira a rede e tente novamente.';
    if (err?.status === 404) return 'Registro não encontrado.';
    return 'Não foi possível concluir a operação. Tente novamente.';
  }
}
