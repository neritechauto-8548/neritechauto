import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { UsuarioService } from '../usuario.service';
import { UsuarioResponse } from '../permissoes/usuario.models';

@Component({
  standalone: true,
  selector: 'app-usuarios',
  templateUrl: './usuarios.html',
  imports: [CommonModule, RouterModule],
})
export class UsuariosComponent implements OnInit {
  private service = inject(UsuarioService);
  private router  = inject(Router);
  private toast   = inject(HotToastService);

  usuarios: UsuarioResponse[] = [];
  loading  = false;

  ngOnInit() { this.carregar(); }

  carregar() {
    this.loading = true;
    this.service.list().subscribe({
      next:  (data) => { this.usuarios = data; this.loading = false; },
      error: ()     => { this.toast.error('Erro ao carregar usuários'); this.loading = false; },
    });
  }

  novoUsuario()          { this.router.navigate(['/configuracoes/usuarios/novo']); }
  editarUsuario(id: number) { this.router.navigate(['/configuracoes/usuarios/editar', id]); }

  excluirUsuario(id: number) {
    if (!confirm('Deseja realmente inativar este usuário?')) return;
    this.service.delete(id).subscribe({
      next:  () => { this.toast.success('Usuário inativado'); this.carregar(); },
      error: () => this.toast.error('Erro ao inativar usuário'),
    });
  }
}
