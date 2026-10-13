import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { NgxPermissionsService } from 'ngx-permissions';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { ConfirmationDialogComponent, DataViewState, NeriTechIcon } from '@shared/components';

import { QuestionamentoItemDialog } from './questionamento-item-dialog';
import { QuestionamentoModeloDialog } from './questionamento-modelo-dialog';
import { QuestionarioService, QuestionarioResponse } from './questionario.service';
import { ItQuestionarioService, ItQuestionarioResponse, TipoItem } from './it-questionario.service';

export type TipoPergunta = 'avaliacao' | 'simnao' | 'aberta';

export interface PerguntaItem {
  id?: number;
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
  erroPerguntas?: boolean;
}

@Component({
  selector: 'app-questionamento',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DynamicDialogModule,
    ToastModule,
    ConfirmationDialogComponent,
    DataViewState,
    NeriTechIcon,
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
  private readonly permissionsService = inject(NgxPermissionsService);

  get canManageResearch(): boolean {
    return !!this.permissionsService.getPermission('GERAL_CONFIG_SISTEMA')
      || !!this.permissionsService.getPermission('ADMIN');
  }
  private readonly questionarioService = inject(QuestionarioService);
  private readonly itQuestionarioService = inject(ItQuestionarioService);

  modelos: ModeloQuestionamento[] = [];
  searchTerm = '';
  loading = true;
  loadError = '';

  get filteredModelos() {
    const term = this.searchTerm.trim().toLocaleLowerCase('pt-BR');
    if (!term) return this.modelos;
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

  get tenantId(): number {
    // A API recusará o cadastro sem uma empresa autenticada; não utilizar ID fixo.
    return empresaIdAtual(this.storage) ?? 0;
  }

  ngOnInit() {
    this.load();
  }

  private mapTipoItemToPergunta(tp: TipoItem): TipoPergunta {
    if (tp === 'SN') return 'simnao';
    if (tp === 'AB') return 'aberta';
    return 'avaliacao';
  }

  private mapPerguntaToTipoItem(tp: TipoPergunta): TipoItem {
    if (tp === 'simnao') return 'SN';
    if (tp === 'aberta') return 'AB';
    return 'AV';
  }

  load() {
    this.loading = true;
    this.loadError = '';
    this.questionarioService.list({ page: 0, size: 1000, sort: 'dsQuestionario,asc' }).subscribe({
      next: (resp) => {
        this.modelos = (resp.content || []).map((q: QuestionarioResponse) => ({
          id: q.id,
          titulo: q.dsQuestionario,
          perguntas: [],
          expanded: false,
          ativo: !!q.snAtivo,
          loading: true,
        }));
        this.loading = false;

        // Carrega imediatamente as perguntas para cada questionário
        this.modelos.forEach(modelo => {
          this.itQuestionarioService.listPorQuestionario(modelo.id).subscribe({
            next: (items: ItQuestionarioResponse[]) => {
              modelo.perguntas = items.map(it => ({ id: it.id, texto: it.dsItQuestionario, tipo: this.mapTipoItemToPergunta(it.tpItQuestionario) }));
              modelo.loading = false;
              this.modelos = [...this.modelos];
            },
            error: (err) => {
              console.error(`Erro ao carregar itens para o questionário ${modelo.id}:`, err);
              modelo.erroPerguntas = true;
              modelo.loading = false;
              this.modelos = [...this.modelos];
            }
          });
        });
      },
      error: (err) => {
        console.error('Erro ao listar questionários', err);
        this.loading = false;
        this.loadError = err.status === 403
          ? 'Seu perfil não tem permissão para consultar esta pesquisa.'
          : 'Verifique a conexão e tente carregar os dados novamente.';
      }
    });
  }

  toggle(modelo: ModeloQuestionamento): void {
    modelo.expanded = !modelo.expanded;
    if (modelo.expanded && modelo.perguntas.length === 0 && !modelo.loading) {
      modelo.loading = true;
      modelo.erroPerguntas = false;
      this.itQuestionarioService.listPorQuestionario(modelo.id).subscribe({
        next: (items: ItQuestionarioResponse[]) => {
          modelo.perguntas = items.map(it => ({ id: it.id, texto: it.dsItQuestionario, tipo: this.mapTipoItemToPergunta(it.tpItQuestionario) }));
          modelo.loading = false;
        },
        error: (err) => {
          console.error('Erro ao listar itens de questionário', err);
          modelo.erroPerguntas = true;
          modelo.loading = false;
        }
      });
    }
  }

  incluirPergunta(modelo: ModeloQuestionamento): void {
    if (!this.canManageResearch) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito',
        detail: 'Seu perfil pode consultar, mas não gerenciar pesquisas.' });
      return;
    }
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
          }
        });
      }
    });
  }

  private refreshPerguntas(modelo: ModeloQuestionamento) {
    modelo.loading = true;
    modelo.erroPerguntas = false;
    this.itQuestionarioService.listPorQuestionario(modelo.id).subscribe({
      next: items => {
        modelo.perguntas = items.map(it => ({ id: it.id, texto: it.dsItQuestionario, tipo: this.mapTipoItemToPergunta(it.tpItQuestionario) }));
        modelo.erroPerguntas = false;
        modelo.loading = false;
      },
      error: () => {
        modelo.erroPerguntas = true;
        modelo.loading = false;
      }
    });
  }

  editarPergunta(modelo: ModeloQuestionamento, index: number): void {
    if (!this.canManageResearch) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito',
        detail: 'Seu perfil pode consultar, mas não gerenciar pesquisas.' });
      return;
    }
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
      if (result && result.texto?.trim() && pergunta?.id != null) {
        const payload = {
          questionarioId: modelo.id,
          dsItQuestionario: result.texto,
          tpItQuestionario: this.mapPerguntaToTipoItem(result.tipo),
        };
        this.itQuestionarioService.update(pergunta.id, payload).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Pergunta atualizada com sucesso' });
            this.refreshPerguntas(modelo);
          },
          error: (err) => console.error('Erro ao atualizar pergunta', err)
        });
      }
    });
  }

  removerPergunta(modelo: ModeloQuestionamento, index: number): void {
    if (!this.canManageResearch) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito',
        detail: 'Seu perfil pode consultar, mas não gerenciar pesquisas.' });
      return;
    }
    this.confirmationService.confirm({
      title: 'Excluir Pergunta',
      message: 'Tem certeza que deseja excluir esta pergunta? Esta ação não pode ser desfeita.',
      confirmText: 'Excluir Pergunta',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'delete'
    }).subscribe(confirmed => {
      const perguntaId = modelo.perguntas[index]?.id;
      if (confirmed && perguntaId != null) {
        this.itQuestionarioService.delete(perguntaId).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Pergunta apagada com sucesso' });
            this.refreshPerguntas(modelo);
          },
          error: (err) => console.error('Erro ao excluir pergunta', err)
        });
      }
    });
  }

  duplicarModelo(modelo: ModeloQuestionamento): void {
    if (!this.canManageResearch) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito',
        detail: 'Seu perfil pode consultar, mas não gerenciar pesquisas.' });
      return;
    }
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
    if (!this.canManageResearch) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito',
        detail: 'Seu perfil pode consultar, mas não gerenciar pesquisas.' });
      return;
    }
    const ref = this.dialogService.open(QuestionamentoModeloDialog, {
      header: 'Editar Questionário',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: { titulo: modelo.titulo, ativo: modelo.ativo },
    });

    ref?.onClose?.subscribe((result: { titulo: string; ativo: boolean } | undefined) => {
      const titulo = result?.titulo?.trim();
      if (result && titulo) {
        const payload = { empresaId: Number(this.tenantId), dsQuestionario: titulo, snAtivo: result.ativo };
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
    if (!this.canManageResearch) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito',
        detail: 'Seu perfil pode consultar, mas não gerenciar pesquisas.' });
      return;
    }
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
    if (!this.canManageResearch) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito',
        detail: 'Seu perfil pode consultar, mas não gerenciar pesquisas.' });
      return;
    }
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
    if (!this.canManageResearch) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito',
        detail: 'Seu perfil pode consultar, mas não gerenciar pesquisas.' });
      return;
    }
    const ref = this.dialogService.open(QuestionamentoModeloDialog, {
      header: 'Novo Questionário',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
    });

    ref?.onClose?.subscribe((result: { titulo: string; ativo?: boolean } | undefined) => {
      const titulo = result?.titulo?.trim();
      if (result && titulo) {
        const ativo = result.ativo !== false;
        this.questionarioService.create({ empresaId: Number(this.tenantId), dsQuestionario: titulo, snAtivo: ativo }).subscribe({
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
