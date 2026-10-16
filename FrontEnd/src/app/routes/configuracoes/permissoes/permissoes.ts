import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { PermissaoService, FuncaoResponse, PermissaoResponse } from './permissao.service';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { HttpErrorResponse } from '@angular/common/http';



@Component({
  selector: 'permissoes',
  standalone: true,
  templateUrl: './permissoes.html',
  styleUrls: ['./permissoes.scss'],
  imports: [CommonModule, FormsModule, ToastModule],
})
export class Permissoes implements OnInit {
  private readonly messageService = inject(MessageService);
  private readonly service = inject(PermissaoService);

  private readonly router = inject(Router);
  private readonly confirmationService = inject(ConfirmationService);

  funcoes:  FuncaoResponse[] = [];
  loading = false;
  loadError = '';
  deletingId: number | null = null;
  busca = '';
  filtroAtivo = '';
  pagina = 0;
  totalPaginas = 1;

  ngOnInit() { this.carregar(); }

  carregar() {
    this.loading = true;
    this.loadError = '';
    this.service.listFuncoes({ page: this.pagina, size: 20, q: this.busca.trim() || undefined,
      ativo: this.filtroAtivo === '' ? undefined : this.filtroAtivo === 'true' }).subscribe({
      next: (page) => {
        this.funcoes = page.content ?? [];
        this.totalPaginas = page.totalPages || 1;
        this.loading = false;
      },
      error: (error: HttpErrorResponse) => { this.loading = false; this.funcoes = []; this.loadError = error.status === 403
        ? 'Você não tem permissão para administrar perfis.' : error.status === 0
          ? 'Sem conexão. Verifique sua rede e tente novamente.' : 'Não foi possível carregar os perfis. Tente novamente.'; },
    });
  }



  abrirCadastro(funcao?: FuncaoResponse) {
    const url = funcao 
      ? `/configuracoes/permissoes/editar/${funcao.id}` 
      : '/configuracoes/permissoes/cadastro';

    this.router.navigate([url], {
      state: funcao ? { 
        id: funcao.id, 
        nome: funcao.nome,
        descricao: funcao.descricao,
        ativo: funcao.ativo,
        sistema: funcao.sistema,
        versao: funcao.versao,
        permissoes: funcao.permissoes
      } : {}
    });
  }

  filtrar() { this.pagina = 0; this.carregar(); }
  mudarPagina(delta: number) { const next = this.pagina + delta; if (next >= 0 && next < this.totalPaginas) { this.pagina = next; this.carregar(); } }

  excluirFuncao(id: number) {
    if (this.deletingId !== null) return;
    this.confirmationService.confirm({
      title: 'Confirmar inativação',
      message: 'Inativar este perfil? Usuários e histórico permanecerão vinculados.',
      confirmText: 'Sim, inativar',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'delete_forever'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.deletingId = id;
        this.service.deleteFuncao(id).subscribe({
          next: () => {
            this.deletingId = null;
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Perfil inativado; vínculos preservados' });
            this.carregar();
          },
          error: (error: HttpErrorResponse) => { this.deletingId = null; this.messageService.add({ severity: 'error', summary: 'Erro', detail:
            error.status === 409 ? (error.error?.message || 'Conflito: o último administrador não pode ser removido.')
              : error.status === 403 ? 'Operação não autorizada.' : 'Erro ao inativar perfil' }); }
        });
      }
    });
  }
}
