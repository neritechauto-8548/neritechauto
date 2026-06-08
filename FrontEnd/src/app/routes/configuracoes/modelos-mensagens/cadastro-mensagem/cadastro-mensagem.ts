import { Component, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PageHeader } from '@shared';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { EditorModule } from 'primeng/editor';
import { PanelModule } from 'primeng/panel';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { MatIconModule } from '@angular/material/icon';
import { TipoTemplate, CategoriaTemplate, TemplateComunicacaoService, TemplateComunicacaoRequest } from '../template-comunicacao.service';
import { LocalStorageService } from '@shared/services/storage.service';

@Component({
  selector: 'cadastro-mensagem',
  standalone: true,
  templateUrl: './cadastro-mensagem.html',
  styleUrls: ['./cadastro-mensagem.scss'],
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    SelectModule,
    ButtonModule,
    EditorModule,
    PanelModule,
    MatIconModule,
    ToastModule
  ],
  providers: [MessageService],
})
export class CadastroMensagem {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly location = inject(Location);
  private readonly service = inject(TemplateComunicacaoService);
  private readonly messageService = inject(MessageService);
  private readonly storage = inject(LocalStorageService);

  id: number | null = null;
  isEditing = false;

  // Opções para os selects
  tipoOptions = [
    { label: 'Email', value: 'EMAIL' },
    { label: 'SMS', value: 'SMS' },
    { label: 'WhatsApp', value: 'WHATSAPP' }
  ];

  // Formulário mapeando Request do Backend
  form = {
    nome: '',
    tipoTemplate: 'EMAIL' as TipoTemplate,
    categoria: 'OUTROS' as CategoriaTemplate,
    assunto: '',
    conteudo: '',
    variaveisDisponiveis: '',
    anexosPadrao: '',
    ativo: true,
    padraoCategoria: false,
    personalizavel: false,
    requerAprovacao: false,
    tags: ''
  };

  // Variáveis disponíveis para inserir no template
  variaveis = [
    '#LOGOMARCA#',
    '#PHONE_CLIENTE#',
    '#LINK#',
    '#TENS_PEDIDO#',
    '#TENS_PEDIDO_S#',
    '#TENS_SUBTOTAL#',
    '#TENS_DESCONTO#',
    '#LISTA_NEGOCIACAO#',
    '#TEMPO_DE_GARANTIA#',
    '#PHONE_FUNCIONARIO#'
  ];

  constructor() {
    // Carrega dados se for edição
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      this.id = idParam ? Number(idParam) : null;
      this.isEditing = !!this.id;

      if (this.isEditing) {
        this.carregarDados();
      }
    });
  }

  carregarDados() {
    if (!this.id) return;

    this.service.findById(this.id).subscribe({
      next: (resp) => {
        this.form = {
          nome: resp.nome,
          tipoTemplate: resp.tipoTemplate,
          categoria: resp.categoria,
          assunto: resp.assunto || '',
          conteudo: resp.conteudo,
          variaveisDisponiveis: resp.variaveisDisponiveis || '',
          anexosPadrao: resp.anexosPadrao || '',
          ativo: resp.ativo,
          padraoCategoria: resp.padraoCategoria,
          personalizavel: resp.personalizavel,
          requerAprovacao: resp.requerAprovacao,
          tags: resp.tags || ''
        };
      },
      error: (err) => {
        console.error('Erro ao carregar modelo', err);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao recuperar o modelo de mensagem.' });
      }
    });
  }

  salvar() {
    const nome = this.form.nome ? this.form.nome.trim() : '';
    if (!nome) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O nome do modelo de envio é obrigatório.' });
      return;
    }
    if (nome.length < 2 || nome.length > 100) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O nome deve ter entre 2 e 100 caracteres.' });
      return;
    }

    if (!this.form.categoria) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'A categoria do modelo é obrigatória.' });
      return;
    }

    const conteudo = this.form.conteudo ? this.form.conteudo.trim() : '';
    if (!conteudo) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O conteúdo do modelo é obrigatório.' });
      return;
    }
    if (conteudo.length < 10) {
      this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O conteúdo deve ter pelo menos 10 caracteres.' });
      return;
    }

    if (this.form.tipoTemplate === 'EMAIL') {
      const assunto = this.form.assunto ? this.form.assunto.trim() : '';
      if (!assunto) {
        this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O assunto é obrigatório para modelos do tipo E-mail.' });
        return;
      }
      if (assunto.length < 2 || assunto.length > 255) {
        this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'O assunto deve ter entre 2 e 255 caracteres.' });
        return;
      }
    }

    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';

    const payload: TemplateComunicacaoRequest = {
      empresaId: Number(tenantId),
      nome: this.form.nome,
      tipoTemplate: this.form.tipoTemplate,
      categoria: this.form.categoria,
      assunto: this.form.assunto,
      conteudo: this.form.conteudo,
      variaveisDisponiveis: this.form.variaveisDisponiveis || undefined,
      anexosPadrao: this.form.anexosPadrao || undefined,
      ativo: this.form.ativo,
      padraoCategoria: this.form.padraoCategoria,
      personalizavel: this.form.personalizavel,
      requerAprovacao: this.form.requerAprovacao,
      tags: this.form.tags || undefined
    };

    const req$ = this.isEditing && this.id
      ? this.service.update(this.id, payload)
      : this.service.create(payload);

    req$.subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: `Modelo ${this.isEditing ? 'atualizado' : 'criado'} com sucesso.` });
        setTimeout(() => {
          this.router.navigate(['/configuracoes/opcoes-envio']);
        }, 800);
      },
      error: (err) => {
        console.error('Erro ao salvar', err);
        const msg = err?.error?.details || err?.error?.message || 'Erro ao processar a requisição.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: msg });
      }
    });
  }

  cancelar() {
    this.location.back();
  }

  inserirVariavel(variavel: string) {
    // Insere a variável no conteúdo na posição do cursor
    this.form.conteudo += variavel;
  }
}
