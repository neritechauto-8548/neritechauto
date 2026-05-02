import { MatIconModule } from '@angular/material/icon';
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PanelModule } from 'primeng/panel';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { PaginatorModule } from 'primeng/paginator';
import { MessageService } from 'primeng/api';

import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { TemplateComunicacaoService, TemplateComunicacaoResponse, TipoTemplate } from './template-comunicacao.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { ConfirmationDialogComponent } from '@shared/components/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'modelos-mensagens',
  standalone: true,
  templateUrl: './modelos-mensagens.html',
  styleUrls: ['./modelos-mensagens.scss'],
  imports: [
    CommonModule,
    FormsModule,
    PanelModule,
    SelectModule,
    InputTextModule,
    ButtonModule,
    TableModule,
    PaginatorModule,
    MatIconModule,
    TooltipModule,
    ToastModule,
    ConfirmationDialogComponent
  ],
  providers: [MessageService, ConfirmationService],
})
export class ModelosMensagens {
  private readonly router = inject(Router);
  private readonly service = inject(TemplateComunicacaoService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly storage = inject(LocalStorageService);

  // Filtros
  tipoOptions = [
    { label: 'Todos', value: 'TODOS' },
    { label: 'App/Push', value: 'PUSH_NOTIFICATION' },
    { label: 'Email', value: 'EMAIL' },
    { label: 'SMS', value: 'SMS' },
    { label: 'WhatsApp', value: 'WHATSAPP' }
  ];
  filtroTipo: TipoTemplate | 'TODOS' = 'TODOS';
  termoBusca = '';

  // Dados reais retidos da API
  templates: TemplateComunicacaoResponse[] = [];

  constructor() {
    this.load();
  }

  load() {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';

    this.service.list(Number(tenantId), { page: 0, size: 1000, sort: 'nome,asc' }).subscribe({
      next: (resp) => {
        this.templates = resp.content || [];
      },
      error: (err) => {
        console.error('Erro ao buscar templates', err);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao recuperar os modelos de mensagem.' });
      }
    });
  }

  // Paginação manual para combinar com layout do screenshot
  rows = 5;
  first = 0;

  get filteredTemplates() {
    const tipo = this.filtroTipo;
    const term = this.termoBusca.trim().toLowerCase();
    return this.templates.filter(t => {
      const matchesTipo = tipo === 'TODOS' || t.tipoTemplate === tipo;
      const tTitulo = t.assunto || t.nome || '';
      const matchesSearch = !term || tTitulo.toLowerCase().includes(term) || String(t.id).includes(term);
      return matchesTipo && matchesSearch;
    });
  }

  get paginatedTemplates() {
    return this.filteredTemplates.slice(this.first, this.first + this.rows);
  }

  get totalRecords() {
    return this.filteredTemplates.length;
  }

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd() {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  goPrev() {
    this.first = Math.max(0, this.first - this.rows);
  }

  goNext() {
    if (this.first + this.rows < this.totalRecords) {
      this.first = this.first + this.rows;
    }
  }

  onPage(event: any) {
    this.first = event.first ?? this.first;
    this.rows = event.rows ?? this.rows;
  }

  onBuscar() {
    this.first = 0;
  }

  get whatsappCount() {
    return this.templates.filter(t => t.tipoTemplate === 'PUSH_NOTIFICATION' || t.tipoTemplate === 'SMS' || t.tipoTemplate === 'WHATSAPP').length;
  }

  get emailCount() {
    return this.templates.filter(t => t.tipoTemplate === 'EMAIL').length;
  }

  onCadastrarNovoModelo() {
    this.router.navigate(['/configuracoes/opcoes-envio/cadastro']);
  }

  editar(template: TemplateComunicacaoResponse) {
    this.router.navigate(['/configuracoes/opcoes-envio/editar', template.id]);
  }

  remover(template: TemplateComunicacaoResponse) {
    this.confirmationService.confirm({
      title: 'Excluir Modelo',
      message: `Tem certeza que deseja excluir o modelo <span class="font-semibold text-slate-900">${template.nome}</span>?`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.service.delete(template.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Modelo de mensagem excluído.' });
            this.load();
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir modelo.' })
        });
      }
    });
  }
}
