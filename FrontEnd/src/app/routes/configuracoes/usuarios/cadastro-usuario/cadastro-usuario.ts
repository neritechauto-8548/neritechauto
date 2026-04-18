import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
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
    this.carregarRoles();
    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      this.carregarUsuario(this.id);
    } else {
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
        if (user.funcoes && this.roles.length) {
             const selectedIds = this.roles
                .filter(r => user.funcoes.includes(r.label))
                .map(r => r.value);
             this.form.patchValue({ funcoesIds: selectedIds });
        }
        this.loading = false;
      },
      error: () => {
         this.toast.error('Erro ao carregar usuário');
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
           error: () => {
             this.toast.error('Erro ao atualizar usuário.');
             this.loading = false;
           }
        });
     } else {
        this.service.create(req).subscribe({
           next: () => {
             this.toast.success('Usuário criado!');
             this.router.navigate(['/configuracoes/usuarios']);
           },
           error: () => {
             this.toast.error('Erro ao criar usuário.');
             this.loading = false;
           }
        });
     }
  }

  cancelar() {
      this.router.navigate(['/configuracoes/usuarios']);
  }
}
