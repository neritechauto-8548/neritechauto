import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PageHeader } from '@shared';
import { ConfirmationDialogComponent } from '@shared/components';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { PaginatorModule } from 'primeng/paginator';
import { TooltipModule } from 'primeng/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { MessageService, ConfirmationService as PrimeNGConfirmationService } from 'primeng/api';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { FormasPagamentoItemDialog } from './formas-pagamento-item-dialog';
import { FormasPagamentoTaxasDialog } from './formas-pagamento-taxas-dialog';
import { FormaPagamentoService, FormaPagamentoRequest, FormaPagamentoResponse, TipoFormaPagamento } from './forma-pagamento.service';

type TipoPagamentoLabel = 'PIX' | 'Cartão' | 'Boleto' | 'Cheque' | 'Dinheiro' | 'Outros';
type FormaPagamentoUI = Omit<FormaPagamentoResponse, 'tipo'> & {
  tipoEnum: TipoFormaPagamento;
  tipoPagamento: TipoPagamentoLabel;
  tipo: string;
  lancarAutomatico?: 'SIM' | 'NAO';
  atualizadoEm?: string;
  taxas?: any;
};

@Component({
  selector: 'formas-pagamento',
  standalone: true,
  templateUrl: './formas-pagamento.html',
  styleUrls: ['./formas-pagamento.scss'],
  imports: [
    CommonModule, FormsModule, PanelModule, ButtonModule, TagModule, DynamicDialogModule,
    InputTextModule, PaginatorModule, TooltipModule, MatIconModule, MatMenuModule, MatButtonModule,
    ToastModule, ConfirmationDialogComponent
  ],
  providers: [
    DialogService,
    MessageService,
    ConfirmationService
  ],
})
export class FormasPagamento {
  private readonly dialogService = inject(DialogService);
  private readonly service = inject(FormaPagamentoService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  dialogRef: DynamicDialogRef<FormasPagamentoItemDialog> | null = null;
  dialogTaxasRef: DynamicDialogRef<FormasPagamentoTaxasDialog> | null = null;

  formas: FormaPagamentoUI[] = [];
  termoBusca = '';
  first = 0;
  rows = 10;

  constructor() {
    this.load();
  }

  private tipoLabel(tipo: TipoFormaPagamento): TipoPagamentoLabel {
    if (tipo === 'PIX') return 'PIX';
    if (tipo === 'CARTAO_CREDITO' || tipo === 'CARTAO_DEBITO') return 'Cartão';
    if (tipo === 'BOLETO') return 'Boleto';
    if (tipo === 'CHEQUE') return 'Cheque';
    if (tipo === 'DINHEIRO') return 'Dinheiro';
    return 'Outros';
  }

  private mapResponse(r: FormaPagamentoResponse): FormaPagamentoUI {
    const { tipo: tipoEnum, ...rest } = r;
    return {
      ...rest,
      tipoEnum,
      tipoPagamento: this.tipoLabel(tipoEnum),
      tipo: 'Taxas e Prazos',
    };
  }

  load() {
    this.service.list({ page: 0, size: 500, sort: 'nome,asc' }).subscribe({
      next: page => this.formas = (page.content || []).map(r => this.mapResponse(r)),
      error: () => {},
    });
  }

  // --- Stripe UX Layout Support ---
  get filteredFormas() {
    const term = this.termoBusca.trim().toLowerCase();
    if (!term) return this.formas;
    return this.formas.filter(f =>
      f.nome.toLowerCase().includes(term) ||
      f.tipoPagamento.toLowerCase().includes(term)
    );
  }

  get paginatedFormas() {
    return this.filteredFormas.slice(this.first, this.first + this.rows);
  }

  get totalRecords() {
    return this.filteredFormas.length;
  }

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd() {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  get cartaoCount() {
    return this.formas.filter(f => f.tipoEnum === 'CARTAO_CREDITO' || f.tipoEnum === 'CARTAO_DEBITO').length;
  }

  get boletoCount() {
    return this.formas.filter(f => f.tipoEnum === 'BOLETO').length;
  }

  get pixCount() {
    return this.formas.filter(f => f.tipoEnum === 'PIX' || f.tipoEnum === 'DINHEIRO').length;
  }

  onBuscar() {
    this.first = 0;
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
    this.first = event.first;
    this.rows = event.rows;
  }
  // --- End Stripe UX Layout Support ---

  incluir() {
    const ref = this.dialogService.open(FormasPagamentoItemDialog, {
      header: 'Incluindo formas de pagamento',
      width: '44rem',
      data: { nome: '', tipo: 'Outros' },
      closable: true,
      dismissableMask: true,
    });

    if (ref) {
      this.dialogRef = ref;
      ref.onClose.subscribe(result => {
        const nome = String(result?.nome || '').trim();
        const tipoUi = String(result?.tipo || 'Outros').trim();
        const toEnum = (t: string): TipoFormaPagamento => {
          if (t.toUpperCase().includes('PIX')) return 'PIX';
          if (t.toUpperCase().includes('CART')) return 'CARTAO_CREDITO';
          if (t.toUpperCase().includes('BOLE')) return 'BOLETO';
          if (t.toUpperCase().includes('CHEQ')) return 'CHEQUE';
          if (t.toUpperCase().includes('DINH')) return 'DINHEIRO';
          return 'OUTROS';
        };
        if (!nome) return;
        const dto: FormaPagamentoRequest = {
          nome,
          tipo: toEnum(tipoUi),
          aceitaParcelamento: !!result?.aceitaParcelamento,
          parcelasMaximas: result?.parcelasMaximas != null ? Number(result.parcelasMaximas) : undefined,
          taxaAdministracao: result?.taxaAdministracao != null ? Number(result.taxaAdministracao) : undefined,
          prazoRecebimentoDias: result?.prazoRecebimentoDias != null ? Number(result.prazoRecebimentoDias) : undefined,
          contaBancariaId: result?.contaBancariaId != null ? Number(result.contaBancariaId) : undefined,
          ativo: result?.ativo !== false,
          padrao: !!result?.padrao,
          exigeAutorizacao: !!result?.exigeAutorizacao,
          limiteDiario: result?.limiteDiario != null ? Number(result.limiteDiario) : undefined,
          observacoes: String(result?.observacoes || '').trim() || undefined,
        };
        this.service.create(dto).subscribe({
          next: (created) => {
            this.formas = [...this.formas, this.mapResponse(created)];
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Forma de pagamento adicionada com sucesso.' });
          },
          error: () => {
             this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao adicionar a forma de pagamento.' });
          },
        });
      });
    }
  }

  remover(item: FormaPagamentoUI) {
    this.confirmationService.confirm({
      title: 'Confirmação de Exclusão',
      message: `Tem certeza que deseja excluir a forma de pagamento <span class="font-semibold text-slate-900">${item.nome}</span>? Esta ação não pode ser desfeita.`,
      confirmText: 'Sim, excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.service.delete(item.id).subscribe({
          next: () => {
             this.formas = this.formas.filter(f => f.id !== item.id);
             this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Forma de pagamento excluída com sucesso.' });
          },
          error: () => {
             this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir a forma de pagamento.' });
          }
        });
      }
    });
  }

  editar(item: FormaPagamentoUI) {
    const ref = this.dialogService.open(FormasPagamentoItemDialog, {
      header: 'Editar forma de pagamento',
      width: '44rem',
      data: {
        nome: item.nome,
        tipo: item.tipoPagamento,
        aceitaParcelamento: item.aceitaParcelamento,
        parcelasMaximas: item.parcelasMaximas,
        taxaAdministracao: item.taxaAdministracao,
        prazoRecebimentoDias: item.prazoRecebimentoDias,
        contaBancariaId: item.contaBancariaId,
        ativo: item.ativo,
        padrao: item.padrao,
        exigeAutorizacao: item.exigeAutorizacao,
        limiteDiario: item.limiteDiario,
        observacoes: item.observacoes,
      },
      closable: true,
      dismissableMask: true,
    });

    ref?.onClose?.subscribe(result => {
      if (!result) return;
      const nome = String(result?.nome || '').trim();
      const tipoUi = String(result?.tipo || item.tipoPagamento).trim();
      const toEnum = (t: string): TipoFormaPagamento => {
        if (t.toUpperCase().includes('PIX')) return 'PIX';
        if (t.toUpperCase().includes('CART')) return 'CARTAO_CREDITO';
        if (t.toUpperCase().includes('BOLE')) return 'BOLETO';
        if (t.toUpperCase().includes('CHEQ')) return 'CHEQUE';
        if (t.toUpperCase().includes('DINH')) return 'DINHEIRO';
        return 'OUTROS';
      };
      const dto: FormaPagamentoRequest = {
        nome: nome || item.nome,
        tipo: toEnum(tipoUi),
        aceitaParcelamento: !!result?.aceitaParcelamento,
        parcelasMaximas: result?.parcelasMaximas != null ? Number(result.parcelasMaximas) : undefined,
        taxaAdministracao: result?.taxaAdministracao != null ? Number(result.taxaAdministracao) : undefined,
        prazoRecebimentoDias: result?.prazoRecebimentoDias != null ? Number(result.prazoRecebimentoDias) : undefined,
        contaBancariaId: result?.contaBancariaId != null ? Number(result.contaBancariaId) : undefined,
        ativo: result?.ativo !== false,
        padrao: !!result?.padrao,
        exigeAutorizacao: !!result?.exigeAutorizacao,
        limiteDiario: result?.limiteDiario != null ? Number(result.limiteDiario) : undefined,
        observacoes: String(result?.observacoes || '').trim() || undefined,
      };
      this.service.update(item.id, dto).subscribe({
        next: (updated) => {
          const ui = this.mapResponse(updated);
          this.formas = this.formas.map(f => f.id === ui.id ? { ...f, ...ui } : f);
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Forma de pagamento atualizada com sucesso.' });
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao atualizar a forma de pagamento.' });
        },
      });
    });
  }

  editarTaxas(item: FormaPagamentoUI) {
    const ref = this.dialogService.open(FormasPagamentoTaxasDialog, {
      header: 'Forma de pagamento (TAXAS E PRAZOS)',
      width: '56rem',
      data: {
        nome: item.nome,
        tipoPagamento: item.tipoPagamento,
        lancarAutomatico: item.lancarAutomatico ?? 'NAO',
        atualizadoEm: item.atualizadoEm,
        taxas: item.taxas,
      },
      closable: true,
      dismissableMask: true,
    });

    if (ref) {
      this.dialogTaxasRef = ref;
      ref.onClose.subscribe(result => {
        if (!result) return;
        this.formas = this.formas.map(f => {
          if (f.id !== item.id) return f;
          return {
            ...f,
            nome: (result.nome || f.nome).toUpperCase(),
            tipoPagamento: result.tipoPagamento || f.tipoPagamento,
            lancarAutomatico: result.lancarAutomatico ?? f.lancarAutomatico,
            atualizadoEm: result.atualizadoEm ?? f.atualizadoEm,
            taxas: result.taxas ?? f.taxas,
          };
        });
      });
    }
  }
}
