import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { PageHeader } from '@shared';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'permissoes-cadastro',
  standalone: true,
  templateUrl: './permissoes-cadastro.html',
  styleUrls: ['./permissoes-cadastro.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    PageHeader,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    MatIconModule,
  ],
})
export class PermissoesCadastro implements OnInit {
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  form: FormGroup = this.fb.group({
    nomePerfil: [''],
    // Permissões gerais
    geralUsuarios: [true],
    geralConfigSistema: [false],
    geralNovoCadastro: [false],
    geralAtualizarCadastro: [false],
    geralRelatorios: [false],

    // Clientes
    clientesListar: [false],
    clientesEditar: [false],
    clientesExcluir: [false],

    // Veículos
    veiculosListar: [false],
    veiculosEditar: [false],
    veiculosExcluir: [false],

    // Alertas
    alertasCriar: [false],
    alertasEditarSMS: [false],
    alertasEditarEmail: [false],
    alertasExcluir: [false],

    // Ordem de serviço
    osAbrir: [false],
    osLocalizar: [false],
    osBaixar: [false],
    osCancelar: [false],
    osEmitirNota: [false],
    osConsultar: [false],

    // Produtos e serviços
    psListarProdutos: [false],
    psEditarProdutos: [false],
    psCriarProdutos: [false],
    psListarServicos: [false],
    psEditarServicos: [false],
    psCriarServicos: [false],

    // Financeiro
    finAbrirRecebimentos: [false],
    finEditarConta: [false],
    finEstornarPagamento: [false],
    finBaixaConta: [false],
    finGerarBoletos: [false],
    finBaixaTransferencia: [false],

    // Vendas
    vendasListar: [false],
    vendasEmitirNFCe: [false],
    vendasGerarNotaFiscal: [false],
    vendasAlterarValor: [false],

    // Retornos
    retornosListar: [false],
    retornosEditar: [false],
    retornosExcluir: [false],
    retornosImportar: [false],

    // Materiais
    materiaisCadastrar: [false],
    materiaisBaixar: [false],
    materiaisExcluir: [false],
    materiaisDesfazerBaixa: [false],

    // Módulo Consulta Veicular
    moduloVeicularRealizarConsulta: [false],
    moduloVeicularAbrirHistorico: [false],

    // Módulo Consulta CPF/CNPJ
    moduloCpfCnpjRealizarConsulta: [false],
    moduloCpfCnpjAbrirHistorico: [false],
  });

  isAdmin = false;

  ngOnInit() {
    // Recupera estado de navegação (id/nome)
    const nav = this.router.getCurrentNavigation();
    const state = (nav && nav.extras && (nav.extras.state as any)) || {};
    if (state?.nome) {
      this.form.patchValue({ nomePerfil: `${state.id ? state.id + ' - ' : ''}${state.nome}` });
      this.isAdmin = String(state.nome).toUpperCase().includes('ADMIN');
    }
  }

  salvar() {
    // Aqui futuramente enviar para API; por ora apenas navega de volta
    this.router.navigateByUrl('/configuracoes/permissoes');
  }
}