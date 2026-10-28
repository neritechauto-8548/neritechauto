import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { UsuarioService } from '../usuario.service';
import { UsuarioResponse } from '../permissoes/usuario.models';

@Component({
  standalone: true,
  selector: 'app-usuarios',
  templateUrl: './usuarios.html',
  imports: [CommonModule, RouterModule, FormsModule],
})
export class UsuariosComponent implements OnInit {
  readonly Math = Math;
  private service = inject(UsuarioService);
  private router  = inject(Router);
  private toast   = inject(HotToastService);

  usuarios: UsuarioResponse[] = [];
  loading  = false;
  loadError = '';
  savingId: number | null = null;
  query = '';
  status: 'todos' | 'ativos' | 'inativos' = 'todos';
  page = 0;
  pageSize = 20;
  total = 0;

  ngOnInit() { this.carregar(); }

  carregar() {
    this.loading = true;
    this.loadError = '';
    const ativo = this.status === 'todos' ? null : this.status === 'ativos';
    this.service.search(this.query, ativo, this.page, this.pageSize).subscribe({
      next:  (data) => { this.usuarios = data.content; this.total = data.totalElements; this.loading = false; },
      error: ()     => { this.usuarios = []; this.loadError = 'Não foi possível carregar os usuários. Tente novamente.'; this.loading = false; },
    });
  }

  filtrar() { this.page = 0; this.carregar(); }
  pagina(delta: number) { this.page += delta; this.carregar(); }

  novoUsuario()          { this.router.navigate(['/configuracoes/usuarios/novo']); }
  convidarUsuario()      { this.router.navigate(['/configuracoes/usuarios/novo'], { queryParams: { convite: 1 } }); }
  editarUsuario(id: number) { this.router.navigate(['/configuracoes/usuarios/editar', id]); }

  excluirUsuario(id: number) {
    if (this.savingId !== null) return;
    if (!confirm('Deseja realmente inativar este usuário?')) return;
    this.savingId = id;
    this.service.delete(id).subscribe({
      next:  () => { this.savingId = null; this.toast.success('Usuário inativado'); this.carregar(); },
      error: () => { this.savingId = null; this.toast.error('Erro ao inativar usuário'); },
    });
  }
}
