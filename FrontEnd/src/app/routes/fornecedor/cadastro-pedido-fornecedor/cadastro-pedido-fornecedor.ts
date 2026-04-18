import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { MatIconModule } from '@angular/material/icon';
import { DialogModule } from 'primeng/dialog';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CadastroFornecedor } from '../cadastro-fornecedor/cadastro-fornecedor';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FornecedorService } from '../fornecedor.service';
import { PedidoFornecedorService } from '../pedido-fornecedor.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { PedidoFornecedorRequest } from '../models/compra.models';

@Component({
  selector: 'cadastro-pedido-fornecedor',
  standalone: true,
  templateUrl: './cadastro-pedido-fornecedor.html',
  imports: [CommonModule, FormsModule, RouterModule, ButtonModule, InputTextModule, TextareaModule, SelectModule, DatePickerModule, MatIconModule, DialogModule, CadastroFornecedor, ToastModule, AutoCompleteModule, ConfirmDialogModule],
  providers: [MessageService, ConfirmationService]
})
export class CadastroPedidoFornecedor implements OnInit {
  private readonly location = inject(Location);
  private readonly route = inject(ActivatedRoute);
  private readonly fornecedorService = inject(FornecedorService);
  private readonly pedidoService = inject(PedidoFornecedorService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly storage = inject(LocalStorageService);

  // Modo: 'novo' | 'editar' | 'visualizar'
  modo: 'novo' | 'editar' | 'visualizar' = 'novo';
  pedidoId: number | null = null;
  carregando = false;
  salvando = false;
  excluindo = false;

  // Modelo do formulário
  form = {
    fornecedor: null as any,
    funcionario: 'ALEXANDRE ROMULO',
    previsao: null as Date | null,
    numeroNf: '',
    observacao: '',
    descricaoInterna: '',
  };

  funcionarios = [
    { label: 'ALEXANDRE ROMULO', value: 'ALEXANDRE ROMULO' },
    { label: 'JOSILENE MARIA', value: 'JOSILENE MARIA' },
  ];

  showFornecedorDialog = false;
  fornecedoresFiltrados: any[] = [];

  // Getter/setter que resolve o [object Object] do PrimeNG AutoComplete:
  // quando o PrimeNG escreve o objeto selecionado, o setter extrai nomeFantasia
  // e guarda o objeto em form.fornecedor
  private _fornecedorNome = '';
  get fornecedorNome(): any { return this._fornecedorNome; }
  set fornecedorNome(val: any) {
    if (val && typeof val === 'object') {
      this._fornecedorNome = val.nomeFantasia || val.razaoSocial || '';
      this.form.fornecedor = val;
    } else {
      this._fornecedorNome = val || '';
      if (!val) this.form.fornecedor = null;
    }
  }

  get somenteLeitura(): boolean {
    return this.modo === 'visualizar';
  }

  get modoEdicao(): boolean {
    return this.modo === 'editar';
  }

  get tituloPagina(): string {
    if (this.modo === 'editar') return 'Editar Pedido a Fornecedor';
    if (this.modo === 'visualizar') return 'Visualizar Pedido a Fornecedor';
    return 'Novo Pedido a Fornecedor';
  }

  get subtitulo(): string {
    if (this.modo === 'visualizar') return 'Dados do pedido de compra';
    return 'Preencha os dados do pedido de compra';
  }

  ngOnInit(): void {
    const url = this.route.snapshot.url.map(s => s.path).join('/');
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.pedidoId = +id;
      this.modo = url.includes('visualizar') ? 'visualizar' : 'editar';
      this.carregarPedido(this.pedidoId);
    }
  }

  private carregarPedido(id: number): void {
    this.carregando = true;
    this.pedidoService.get(id).subscribe({
      next: (pedido) => {
        this.form.funcionario = pedido.responsavel;
        this.form.numeroNf = pedido.numeroNf || '';
        this.form.observacao = pedido.observacao || '';
        this.form.descricaoInterna = pedido.descricaoInterna || '';

        // Converter ISO string para Date para o DatePicker
        if (pedido.dataPrevisao) {
          const [year, month, day] = pedido.dataPrevisao.split('-');
          this.form.previsao = new Date(+year, +month - 1, +day);
        }

        if (pedido.fornecedorId) {
          this.fornecedorService.get(pedido.fornecedorId).subscribe({
            next: (f) => {
              this.form.fornecedor = f;
              this.fornecedorNome = f;  // setter extrai nomeFantasia automaticamente
              this.carregando = false;
            },
            error: () => { this.carregando = false; }
          });
        } else {
          this.carregando = false;
        }
      },
      error: () => {
        this.carregando = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Pedido não encontrado.' });
        setTimeout(() => this.location.back(), 1500);
      }
    });
  }

  filtrarFornecedores(event: any) {
    this.fornecedorService.list({ nome: event.query, nomeFantasia: event.query, page: 0, size: 10 }).subscribe(
      page => this.fornecedoresFiltrados = page.content
    );
  }

  onFornecedorSelect(event: any) {
    // O setter de fornecedorNome já extrai nomeFantasia e guarda em form.fornecedor
    this.fornecedorNome = event.value ?? event;
  }

  onFornecedorClear() {
    this.fornecedorNome = '';
  }

  buscarFornecedor() {
    this.showFornecedorDialog = true;
  }

  onFornecedorSaved(model: any) {
    this.form.fornecedor = model;
    this.showFornecedorDialog = false;
  }

  onFornecedorCanceled() {
    this.showFornecedorDialog = false;
  }

  cancelar() {
    this.location.back();
  }

  excluir() {
    if (!this.pedidoId) return;
    this.confirmationService.confirm({
      message: 'Tem certeza que deseja excluir este pedido? Esta ação não pode ser desfeita.',
      header: 'Confirmar Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim, excluir',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.excluindo = true;
        this.pedidoService.delete(this.pedidoId!).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Excluído', detail: 'Pedido excluído com sucesso.' });
            setTimeout(() => this.location.back(), 800);
          },
          error: (err) => {
            this.excluindo = false;
            const msg = err?.error?.message || 'Erro ao excluir pedido.';
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
          }
        });
      }
    });
  }

  private getEmpresaId(): number {
    let tenantId = this.storage.has('tenantId') ? this.storage.get('tenantId') : '1';
    if (tenantId && typeof tenantId === 'object') {
      tenantId = (tenantId as any).id || (tenantId as any).tenantId || (tenantId as any).empresaId || '1';
    }
    const val = String(tenantId);
    return parseInt((val.includes('[object') || val === 'undefined' || val === 'null') ? '1' : val, 10);
  }

  salvar() {
    if (this.somenteLeitura) return;
    if (!this.form.fornecedor || !this.form.funcionario) {
      this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Preencha os campos obrigatórios (*)' });
      return;
    }

    this.salvando = true;

    // Converter Date para ISO YYYY-MM-DD
    let dataPrevisao: string | null = null;
    if (this.form.previsao instanceof Date) {
      const d = this.form.previsao;
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      dataPrevisao = `${d.getFullYear()}-${month}-${day}`;
    }

    const dto: PedidoFornecedorRequest = {
      empresaId: this.getEmpresaId(),
      fornecedorId: this.form.fornecedor.id,
      responsavel: this.form.funcionario,
      dataPrevisao,
      numeroNf: this.form.numeroNf || undefined,
      observacao: this.form.observacao || undefined,
      descricaoInterna: this.form.descricaoInterna || undefined,
    };

    const operacao$ = this.pedidoId
      ? this.pedidoService.update(this.pedidoId, dto)
      : this.pedidoService.create(dto);

    operacao$.subscribe({
      next: () => {
        const msg = this.pedidoId ? 'Pedido atualizado com sucesso' : 'Pedido registrado com sucesso';
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: msg });
        setTimeout(() => this.location.back(), 800);
      },
      error: (err) => {
        this.salvando = false;
        const msg = err?.error?.message || 'Erro ao salvar pedido. Tente novamente.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
      }
    });
  }
}
