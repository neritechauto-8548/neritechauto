import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { TextareaModule } from 'primeng/textarea';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from '@shared/services/confirmation.service';

import { ClientesService } from '../cliente/cliente/cliente.service';

@Component({
  standalone: true,
  selector: 'app-cadastro-orcamento',
  templateUrl: './cadastro-orcamento.html',
  styleUrls: ['./cadastro-orcamento.scss'],
  imports: [
    CommonModule, FormsModule,
    InputTextModule, SelectModule, ButtonModule,
    TextareaModule, AutoCompleteModule, ToastModule
  ],
  providers: [MessageService]
})
export class CadastroOrcamentoComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly location = inject(Location);
  private readonly clientesService = inject(ClientesService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);

  // Estado geral
  id: number | null = null;
  loading = false;
  saving = false;

  // Autocomplete de cliente — idêntico ao padrão do veículo
  filteredClientes: any[] = [];
  selectedCliente: any | null = null;

  funcionarios = [
    { label: 'ALEXANDRE ROMULO A', value: 'alexandre' },
  ];

  form = {
    funcionario: 'alexandre',
    veiculo: '',
    email: '',
    celular: '',
    emissao: new Date().toISOString().substring(0, 10),
    validade: new Date(new Date().getTime() + 9 * 24 * 60 * 60 * 1000).toISOString().substring(0, 10),
    descricao: '',
  };

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const idStr = params.get('id');
      if (idStr) {
        this.id = Number(idStr);
        // Aqui carregaria os dados do orçamento via service quando disponível
      }
    });
  }

  // Busca de cliente — mesma lógica do cadastro-veiculo
  searchCliente(event: any) {
    const query = event.query;
    const isNumeric = /^\d+$/.test(query.replace(/[.-]/g, ''));

    const filter: any = {};
    if (isNumeric) {
      const clean = query.replace(/\D/g, '');
      filter[clean.length > 11 ? 'cnpj' : 'cpf'] = clean;
    } else {
      filter.nomeCompleto = query;
      filter.nomeFantasia = query;
      filter.razaoSocial = query;
    }

    this.clientesService.list(filter).subscribe({
      next: (page) => {
        this.filteredClientes = page.content.map(c => ({
          ...c,
          nome: c.nomeCompleto || c.nomeFantasia || c.razaoSocial || '',
          cpfCnpj: c.cpf || c.cnpj || ''
        }));
      },
      error: () => { this.filteredClientes = []; }
    });
  }

  salvar() {
    if (!this.selectedCliente) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione um cliente.' });
      return;
    }

    this.saving = true;
    // Integração com backend de orçamento quando disponível
    setTimeout(() => {
      this.messageService.add({ severity: 'success', summary: 'Orçamento salvo!', detail: 'Orçamento registrado com sucesso.' });
      this.saving = false;
      setTimeout(() => this.router.navigate(['/orcamento/orcamento']), 1200);
    }, 800);
  }

  excluir() {
    if (!this.id) return;
    this.confirmationService.confirm({
      title: 'Excluir Orçamento',
      message: 'Tem certeza que deseja excluir este orçamento? Esta ação não pode ser desfeita.',
      confirmText: 'Excluir Orçamento',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.messageService.add({ severity: 'success', summary: 'Excluído!', detail: 'Orçamento excluído com sucesso.' });
        this.router.navigate(['/orcamento/orcamento']);
      }
    });
  }

  cancelar() {
    this.location.back();
  }
}
