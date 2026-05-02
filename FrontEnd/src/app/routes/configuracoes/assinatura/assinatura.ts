import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AssinaturaService, AssinaturaStatus } from './assinatura.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { AuthService } from '@core/authentication';
import { finalize } from 'rxjs';

interface Plano {
  id: string;
  nome: string;
  preco: string;
  descricao: string;
  destaque: boolean;
  features: string[];
  productId: string;
}

@Component({
  selector: 'app-assinatura',
  standalone: true,
  templateUrl: './assinatura.html',
  imports: [CommonModule, MatIconModule, ToastModule],
  providers: [MessageService]
})
export class AssinaturaComponent implements OnInit {
  private service = inject(AssinaturaService);
  private toast = inject(MessageService);
  private storage = inject(LocalStorageService);

  loading = false;
  portalLoading = false;
  empresaId = 1;

  assinatura: AssinaturaStatus = {
    plano: '',
    status: '',
    precoFormatado: '',
    proximaCobranca: '',
    stripeCustomerId: '',
    stripeSubscriptionId: '',
    stripeProductId: '',
    trial: false,
    inicioTrial: '',
    fimTrial: ''
  };

  planos: Plano[] = [
    {
      id: 'start',
      nome: 'neri start',
      preco: '90',
      descricao: 'Essencial para organizar o fluxo de entrada e saída.',
      destaque: false,
      productId: 'prod_UMeQq4ASz2Tm5h',
      features: [
        'Ordens de Serviço Digitais',
        'Cadastro de Clientes e Veículos',
        'Financeiro Básico (CP/CR)',
        'Checklist de Entrada',
        'Emissão de NF-e Simples',
        'Suporte via E-mail',
      ]
    },
    {
      id: 'pro',
      nome: 'neri pro',
      preco: '140',
      descricao: 'A gestão completa para oficinas que buscam performance.',
      destaque: true,
      productId: 'prod_UMeRIYKOYS5enM',
      features: [
        'Tudo do neri start',
        'Conciliação Bancária Automática',
        'Emissão de NF-e e NFS-e',
        'Controle de Estoque Inteligente',
        'Relatórios Gerenciais (DRE)',
        'WhatsApp Nativo para Clientes',
        'Suporte prioritário WhatsApp',
      ]
    },
    {
      id: 'elite',
      nome: 'neri elite',
      preco: '230',
      descricao: 'O nível máximo de controle e inteligência de dados.',
      destaque: false,
      productId: 'prod_UMeS5gEJzxET8x',
      features: [
        'Tudo do neri pro',
        'Dashboards BI Customizados',
        'API para Integrações Externas',
        'Consultoria Mensal de Processos',
      ]
    }
  ];

  private auth = inject(AuthService);

  ngOnInit() {
    this.auth.user().subscribe(user => {
      const u = user as any;
      const id = u.empresaId || u.tenantId || u.idEmpresa || u.id_empresa || u.companyId;
      
      if (id) {
        this.empresaId = Number(id);
        this.loadStatus();
      } else {
        // Fallback for LocalStorage if user profile is still loading or incomplete
        const stored = this.storage.get('tenantId') || this.storage.get('empresaId');
        if (stored) {
          this.empresaId = Number(stored);
          this.loadStatus();
        }
      }
    });
  }

  loadStatus() {
    this.loading = true;
    this.service.getStatus(this.empresaId)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => this.assinatura = data,
        error: () => this.toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar o status da assinatura.' })
      });
  }

  abrirPortal() {
    this.portalLoading = true;
    const returnUrl = window.location.href;
    this.service.getPortalUrl(this.empresaId, returnUrl)
      .pipe(finalize(() => this.portalLoading = false))
      .subscribe({
        next: (res) => {
          if (res.url) {
            window.open(res.url, '_blank');
          }
        },
        error: () => this.toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível abrir o portal de faturamento.' })
      });
  }

  isPlanoAtual(plano: Plano): boolean {
    return this.assinatura.stripeProductId === plano.productId;
  }

  getStatusLabel(): string {
    const map: Record<string, string> = {
      'active': 'Ativo',
      'trialing': 'Período de Teste',
      'past_due': 'Pagamento Pendente',
      'canceled': 'Cancelado',
      'unpaid': 'Não Pago',
      'incomplete': 'Incompleto',
      'inactive': 'Inativo',
      'error': 'Erro',
    };
    return map[this.assinatura.status] || this.assinatura.status || 'Desconhecido';
  }

  getStatusColor(): string {
    const map: Record<string, string> = {
      'active': 'bg-emerald-100 text-emerald-700 border-emerald-200',
      'trialing': 'bg-blue-100 text-blue-700 border-blue-200',
      'past_due': 'bg-amber-100 text-amber-700 border-amber-200',
      'canceled': 'bg-red-100 text-red-700 border-red-200',
      'unpaid': 'bg-red-100 text-red-700 border-red-200',
      'inactive': 'bg-slate-100 text-slate-500 border-slate-200',
    };
    return map[this.assinatura.status] || 'bg-slate-100 text-slate-500 border-slate-200';
  }
}
