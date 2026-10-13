import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NeriTechIcon } from '@shared/components';
import { ComunicacaoEnviada, ComunicacaoService } from './comunicacao.service';

@Component({
  selector: 'app-crm-comunicacao',
  standalone: true,
  imports: [CommonModule, RouterLink, NeriTechIcon],
  templateUrl: './comunicacao.html',
  styleUrls: ['../crm.scss'],
})
export class Comunicacao implements OnInit {
  private readonly service = inject(ComunicacaoService);
  private requestVersion = 0;
  readonly pageSize = 15;
  page = 0;
  total = 0;
  registros: ComunicacaoEnviada[] = [];
  loading = false;
  error = '';

  ngOnInit(): void { this.carregar(); }

  get paginas(): number { return Math.max(1, Math.ceil(this.total / this.pageSize)); }

  carregar(): void {
    const version = ++this.requestVersion;
    this.loading = true;
    this.error = '';
    this.service.listar(this.page, this.pageSize).subscribe({
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
