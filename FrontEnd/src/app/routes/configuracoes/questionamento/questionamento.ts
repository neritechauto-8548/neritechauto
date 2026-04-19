import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { ToastModule } from 'primeng/toast';
import { TooltipModule } from 'primeng/tooltip';
import { MessageService } from 'primeng/api';
import { MatIconModule } from '@angular/material/icon';

import { LocalStorageService } from '@shared/services/storage.service';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { PageHeader, ConfirmationDialogComponent } from '@shared/components';

import { QuestionamentoItemDialog } from './questionamento-item-dialog';
import { QuestionamentoModeloDialog } from './questionamento-modelo-dialog';
import { QuestionarioService, QuestionarioResponse } from './questionario.service';
import { ItQuestionarioService, ItQuestionarioResponse, TipoItem } from './it-questionario.service';

export type TipoPergunta = 'avaliacao' | 'simnao' | 'aberta';

export interface PerguntaItem {
  texto: string;
  tipo: TipoPergunta;
}

export interface ModeloQuestionamento {
  id: number;
  titulo: string;
  perguntas: PerguntaItem[];
  expanded?: boolean;
  ativo?: boolean;
  loading?: boolean;
}

@Component({
  selector: 'app-questionamento',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    DynamicDialogModule,
    MatIconModule,
    ToastModule,
    TooltipModule,
    ConfirmationDialogComponent
  ],
  providers: [DialogService, MessageService, ConfirmationService],
  templateUrl: './questionamento.html',
  styleUrls: ['./questionamento.scss'],
})
export class Questionamento implements OnInit {
  private readonly dialogService = inject(DialogService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);
  private readonly storage = inject(LocalStorageService);
  private readonly questionarioService = inject(QuestionarioService);
  private readonly itQuestionarioService = inject(ItQuestionarioService);

  modelos: ModeloQuestionamento[] = [];
  searchTerm = '';
  loading = false;

  get filteredModelos() {
    if (!this.searchTerm) return this.modelos;
    const term = this.searchTerm.toLowerCase();
    return this.modelos.filter(m => m.titulo.toLowerCase().includes(term));
  }

  get totalModelos() {
    return this.modelos.length;
  }

  get totalAtivos() {
    return this.modelos.filter(m => m.ativo).length;
  }

  get totalPerguntas() {
    return this.modelos.reduce((acc, m) => acc + (m.perguntas?.length || 0), 0);
  }

  get tenantId(): string | number {
    let tid = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tid || typeof tid === 'object') tid = '1';
    return tid;
  }

  ngOnInit() {
    this.load();
  }

  private mapTipoItemToPergunta(tp: TipoItem): TipoPergunta {
    return tp === 'SN' ? 'simnao' : 'avaliacao';
  }

  private mapPerguntaToTipoItem(tp: TipoPergunta): TipoItem {
    return tp === 'simnao' ? 'SN' : 'AV';
  }

  load() {
    this.loading = true;
    this.questionarioService.list({ page: 0, size: 1000, sort: 'dsQuestionario,asc' }).subscribe({
      next: (resp) => {
        this.modelos = (resp.content || []).map((q: QuestionarioResponse) => ({
          id: q.id,
          titulo: q.dsQuestionario,
          perguntas: [],
          expanded: false,
          ativo: !!q.snAtivo,
        }));
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao listar questionários', err);
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar questionários' });
      }
    });
  }

  toggle(modelo: ModeloQuestionamento): void {
    modelo.expanded = !modelo.expanded;
    if (modelo.expanded && modelo.perguntas.length === 0) {
      modelo.loading = true;
      this.itQuestionarioService.listPorQuestionario(modelo.id).subscribe({
        next: (items: ItQuestionarioResponse[]) => {
          modelo.perguntas = items.map(it => ({ texto: it.dsItQuestionario, tipo: this.mapTipoItemToPergunta(it.tpItQuestionario) }));
          modelo.loading = false;
        },
        error: (err) => {
          console.error('Erro ao listar itens de questionário', err);
          modelo.loading = false;
        }
      });
    }
  }

  incluirPergunta(modelo: ModeloQuestionamento): void {
    const ref = this.dialogService.open(QuestionamentoItemDialog, {
      header: 'Adicionar Pergunta',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
    });

    ref?.onClose?.subscribe((result: PerguntaItem | undefined) => {
      if (result && result.texto?.trim()) {
        const payload = {
          questionarioId: modelo.id,
          dsItQuestionario: result.texto,
          tpItQuestionario: this.mapPerguntaToTipoItem(result.tipo),
        };
        this.itQuestionarioService.create(payload).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Pergunta criada com sucesso' });
            this.refreshPerguntas(modelo);
          },
          error: (err) => {
            console.error('Erro ao criar pergunta', err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao cadastrar pergunta' });
          }
        });
      }
    });
  }

  private refreshPerguntas(modelo: ModeloQuestionamento) {
    modelo.loading = true;
    this.itQuestionarioService.listPorQuestionario(modelo.id).subscribe({
      next: items => {
        modelo.perguntas = items.map(it => ({ texto: it.dsItQuestionario, tipo: this.mapTipoItemToPergunta(it.tpItQuestionario) }));
        modelo.loading = false;
      },
      error: () => modelo.loading = false
    });
  }

  editarPergunta(modelo: ModeloQuestionamento, index: number): void {
    const pergunta = modelo.perguntas[index];
    const ref = this.dialogService.open(QuestionamentoItemDialog, {
      header: 'Editar Pergunta',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: pergunta,
    });
    ref?.onClose?.subscribe((result: PerguntaItem | undefined) => {
      if (result && result.texto?.trim()) {
        this.itQuestionarioService.listPorQuestionario(modelo.id).subscribe(items => {
          const target = items[index];
          if (target) {
            const payload = {
              questionarioId: modelo.id,
              dsItQuestionario: result.texto,
              tpItQuestionario: this.mapPerguntaToTipoItem(result.tipo),
            };
            this.itQuestionarioService.update(target.id, payload).subscribe({
              next: () => {
                this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Pergunta atualizada com sucesso' });
                this.refreshPerguntas(modelo);
              },
              error: (err) => console.error('Erro ao atualizar pergunta', err)
            });
          }
        });
      }
    });
  }

  removerPergunta(modelo: ModeloQuestionamento, index: number): void {
    this.confirmationService.confirm({
      title: 'Excluir Pergunta',
      message: 'Tem certeza que deseja excluir esta pergunta? Esta ação não pode ser desfeita.',
      confirmText: 'Excluir Pergunta',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'delete'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.itQuestionarioService.listPorQuestionario(modelo.id).subscribe(items => {
          const target = items[index];
          if (target) {
            this.itQuestionarioService.delete(target.id).subscribe({
              next: () => {
                this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Pergunta apagada com sucesso' });
                this.refreshPerguntas(modelo);
              },
              error: (err) => console.error('Erro ao excluir pergunta', err)
            });
          }
        });
      }
    });
  }

  duplicarModelo(modelo: ModeloQuestionamento): void {
    const ref = this.dialogService.open(QuestionamentoModeloDialog, {
      header: 'Duplicar Questionário',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: { titulo: `${modelo.titulo} (cópia)` },
    });
    ref?.onClose?.subscribe((result: { titulo: string } | undefined) => {
      const titulo = result?.titulo?.trim();
      if (titulo) {
        this.questionarioService.create({ empresaId: Number(this.tenantId), dsQuestionario: titulo, snAtivo: true }).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Questionário duplicado com sucesso' });
            this.load();
          },
          error: (err) => console.error('Erro ao duplicar questionário', err)
        });
      }
    });
  }

  editarModelo(modelo: ModeloQuestionamento): void {
    const ref = this.dialogService.open(QuestionamentoModeloDialog, {
      header: 'Editar Questionário',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: { titulo: modelo.titulo },
    });

    ref?.onClose?.subscribe((result: { titulo: string } | undefined) => {
      const titulo = result?.titulo?.trim();
      if (titulo) {
        const payload = { empresaId: Number(this.tenantId), dsQuestionario: titulo, snAtivo: modelo.ativo };
        this.questionarioService.update(modelo.id, payload).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Questionário atualizado com sucesso' });
            this.load();
          },
          error: (err) => console.error('Erro ao atualizar questionário', err)
        });
      }
    });
  }

  toggleAtivo(modelo: ModeloQuestionamento): void {
    const payload = { empresaId: Number(this.tenantId), dsQuestionario: modelo.titulo, snAtivo: !modelo.ativo };
    this.questionarioService.update(modelo.id, payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: !modelo.ativo ? 'Questionário ativado' : 'Questionário desativado' });
        modelo.ativo = !modelo.ativo;
      },
      error: (err) => console.error('Erro ao atualizar status do questionário', err)
    });
  }

  removerModelo(modelo: ModeloQuestionamento): void {
    this.confirmationService.confirm({
      title: 'Excluir Questionário',
      message: `Tem certeza que deseja excluir o questionário <span class="font-semibold text-slate-900">${modelo.titulo}</span> e todos os seus itens?`,
      confirmText: 'Excluir Todo Questionário',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.questionarioService.delete(modelo.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Questionário apagado com sucesso' });
            this.load();
          },
          error: (err) => console.error('Erro ao excluir questionário', err)
        });
      }
    });
  }

  incluirModelo(): void {
    const ref = this.dialogService.open(QuestionamentoModeloDialog, {
      header: 'Novo Questionário',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
    });

    ref?.onClose?.subscribe((result: { titulo: string } | undefined) => {
      const titulo = result?.titulo?.trim();
      if (titulo) {
        this.questionarioService.create({ empresaId: Number(this.tenantId), dsQuestionario: titulo, snAtivo: true }).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Questionário criado com sucesso' });
            this.load();
          },
          error: (err) => console.error('Erro ao criar questionário', err)
        });
      }
    });
  }
}
