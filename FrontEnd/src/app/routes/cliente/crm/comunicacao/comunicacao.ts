import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { NeriTechIcon } from '@shared/components';
import { ComunicacaoEnviada, ComunicacaoService, FiltrosComunicacao } from './comunicacao.service';

@Component({
  selector: 'app-crm-comunicacao',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, NeriTechIcon],
  templateUrl: './comunicacao.html',
  styleUrls: ['../crm.scss'],
})
export class Comunicacao implements OnInit {
  private readonly service = inject(ComunicacaoService);
  private readonly route = inject(ActivatedRoute);
  private requestVersion = 0;
  readonly pageSize = 15;
  page = 0;
  total = 0;
  registros: ComunicacaoEnviada[] = [];
  loading = false;
  error = '';
  filterError = '';
  canal = '';
  status = '';
  dataInicio = '';
  dataFim = '';
  clienteId: number | undefined;
  filters: FiltrosComunicacao = {};

  readonly canais = ['EMAIL', 'SMS', 'WHATSAPP', 'PUSH_NOTIFICATION', 'LIGACAO'];
  readonly statuses = ['AGENDADA', 'ENVIADA', 'ENTREGUE', 'LIDA', 'CLICADA', 'FALHOU', 'CANCELADA'];

  private validarClienteDaRota(): boolean {
    const raw = this.route.snapshot.queryParamMap.get('clienteId');
    if (raw !== null && (!/^\\d+$/.test(raw) || !Number.isSafeInteger(Number(raw)) || Number(raw) <= 0)) {
      ++this.requestVersion;
      this.registros = [];
      this.total = 0;
      this.loading = false;
      this.filterError = 'O identificador do cliente é inválido. Volte ao cadastro e acesse o histórico novamente.';
      return false;
    }
    this.clienteId = raw !== null ? Number(raw) : undefined;
    this.filterError = '';
    return true;
  }

  ngOnInit(): void {
    this.aplicarFiltros();
  }

  aplicarFiltros(): void {
    if (!this.validarClienteDaRota()) return;
    if (this.dataInicio && this.dataFim && this.dataInicio > this.dataFim) {
      this.filterError = 'A data inicial não pode ser posterior à data final.';
      return;
    }
    this.filterError = '';
    this.page = 0;
    this.filters = {
      ...(this.clienteId ? { clienteId: this.clienteId } : {}),
      ...(this.canal ? { canal: this.canal } : {}),
      ...(this.status ? { status: this.status } : {}),
      ...(this.dataInicio ? { dataInicio: this.dataInicio } : {}),
      ...(this.dataFim ? { dataFim: this.dataFim } : {}),
    };
    this.carregar();
  }

  limparFiltros(): void {
    this.canal = '';
    this.status = '';
    this.dataInicio = '';
    this.dataFim = '';
    this.aplicarFiltros();
  }

  get paginas(): number { return Math.max(1, Math.ceil(this.total / this.pageSize)); }

  carregar(): void {
    if (!this.validarClienteDaRota()) return;
    this.filters = { ...this.filters, ...(this.clienteId ? { clienteId: this.clienteId } : {}) };
    if (!this.clienteId) delete this.filters.clienteId;
    const version = ++this.requestVersion;
    this.loading = true;
    this.error = '';
    this.service.listar(this.page, this.pageSize, this.filters).subscribe({
      next: response => {
        if (version !== this.requestVersion) return;
        this.registros = response.content || [];
        this.total = response.totalElements || 0;
        this.loading = false;
      },
      error: err => {
        if (version !== this.requestVersion) return;
        this.registros = [];
        this.total = 0;
        this.loading = false;
        this.error = err.status === 403
          ? 'Seu perfil não tem permissão para consultar comunicações.'
          : err.message?.includes('identificar sua empresa') ? err.message
          : 'Não foi possível consultar o histórico. Verifique a conexão e tente novamente.';
      },
    });
  }
  irParaPagina(pagina: number): void {
    if (pagina < 0 || pagina >= this.paginas || pagina === this.page) return;
    this.page = pagina;
    this.carregar();
  }
}
