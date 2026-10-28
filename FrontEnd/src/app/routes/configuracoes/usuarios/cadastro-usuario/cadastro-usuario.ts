import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { HttpErrorResponse } from '@angular/common/http';
import { UsuarioService } from '../../usuario.service';

import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { PasswordModule } from 'primeng/password';
import { MultiSelectModule } from 'primeng/multiselect';

@Component({
  standalone: true,
  selector: 'app-cadastro-usuario',
  templateUrl: './cadastro-usuario.html',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PanelModule,
    ButtonModule,
    InputTextModule,
    CheckboxModule,
    PasswordModule,
    MultiSelectModule
  ]
})
export class CadastroUsuarioComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service = inject(UsuarioService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private toast = inject(HotToastService);

  form: FormGroup;
  id: number | null = null;
  loading = false;
  roles: any[] = [];
  convite = false;
  loadError = '';
  historico: Array<{ operacao: string; campos: string; valores: string; autor: number; data: string }> = [];

  constructor() {
    this.form = this.fb.group({
      nomeCompleto: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      senha: [''], // Required only for new
      ativo: [true],
      bloqueado: [false],
      funcoesIds: [[]],
      cargo: [''],
      departamento: [''],
      telefone: ['']
    });
  }

  ngOnInit() {
    this.convite = this.route.snapshot.queryParamMap.get('convite') === '1';
    this.carregarRoles();
    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      this.carregarUsuario(this.id);
      this.service.history(this.id).subscribe({ next: page => this.historico = page.content, error: () => {} });
    } else if (!this.convite) {
        this.form.get('senha')?.addValidators(Validators.required);
    }
  }

  carregarRoles() {
    this.service.getRoles().subscribe(roles => {
        this.roles = roles.map(r => ({ label: r.nome, value: r.id }));
    });
  }

  carregarUsuario(id: number) {
    this.loading = true;
    this.service.get(id).subscribe({
      next: (user) => {
        this.form.patchValue(user);
        // Note: funcoesIds mapping might be tricky if backend returns strings in 'funcoes'.
        // Check UsuarioResponse: it has 'funcoes: string[]'.
        // We need to map these strings back to IDs if possible, or just ignore for now if mismatch.
        // Strategies:
        // 1. Backend response includes IDs? No.
        // 2. We can try to map names to IDs from our role list.
        if (user.funcoesIds) {
             this.form.patchValue({ funcoesIds: user.funcoesIds });
        } else if (user.funcoes && this.roles.length) {
             const selectedIds = this.roles
                .filter(r => user.funcoes.includes(r.label))
                .map(r => r.value);
             this.form.patchValue({ funcoesIds: selectedIds });
        }
        this.loading = false;
      },
      error: () => {
         this.toast.error('Erro ao carregar usuário');
         this.loadError = 'Não foi possível carregar o usuário. Tente novamente.';
         this.loading = false;
      }
    });
  }

  salvar() {
     if (this.form.invalid) return;

     this.loading = true;
     const req = this.form.value;

     if (this.id) {
        this.service.update(this.id, req).subscribe({
           next: () => {
             this.toast.success('Usuário atualizado!');
             this.router.navigate(['/configuracoes/usuarios']);
           },
           error: (error: HttpErrorResponse) => {
             this.toast.error(this.errorMessage(error, 'Erro ao atualizar usuário.'));
             this.loading = false;
           }
        });
     } else {
        const operation = this.convite ? this.service.invite(req) : this.service.create(req);
        operation.subscribe({
           next: () => {
             this.toast.success(this.convite ? 'Convite enviado!' : 'Usuário criado!');
             this.router.navigate(['/configuracoes/usuarios']);
           },
           error: (error: HttpErrorResponse) => {
             this.toast.error(this.errorMessage(error, this.convite ? 'Erro ao enviar convite.' : 'Erro ao criar usuário.'));
             this.loading = false;
           }
        });
     }
  }

  cancelar() {
      this.router.navigate(['/configuracoes/usuarios']);
  }

  private errorMessage(error: HttpErrorResponse, fallback: string): string {
    if (error.status === 403) return 'Você não tem permissão para administrar usuários.';
    if (error.status === 409) return error.error?.message || 'Conflito: revise os dados e tente novamente.';
    return fallback;
  }
}
