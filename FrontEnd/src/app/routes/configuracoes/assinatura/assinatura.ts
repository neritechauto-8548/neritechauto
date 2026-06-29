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
  badge?: string;
  promoInfo?: string;
  buttonLabel?: string;
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
  private auth = inject(AuthService);

  loading = false;
  portalLoading = false;
  empresaId: number | null = null;

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
    fimTrial: '',
    diasRestantesTrial: 0
  };

  planos: Plano[] = [
    {
      id: 'basic',
      nome: 'Neri Basic',
      preco: '79,90',
      descricao: 'A gestão essencial para sua oficina que busca performance e organização.',
      destaque: true,
      productId: 'prod_UMeRIYKOYS5enM',
      badge: '🎉 SUPER PROMOÇÃO',
      promoInfo: 'Promoção por tempo limitado: os próximos 50 assinantes ganham 6 meses GRÁTIS! E depois, mais 6 meses pagando apenas a metade do valor (R$ 39,95/mês).',
      buttonLabel: 'Aproveitar Promoção',
      features: [
        'Cadastros Ilimitados',
        'Ordens de Serviço e PDV',
        'Financeiro Operacional',
        'Estoque e Insumos',
        'Relatórios Gerenciais',
        'Dashboards Iniciais',
        '❌ Sem Emissão de Notas Fiscais'
      ]
    },
    {
      id: 'pro',
      nome: 'Neri Pro',
      preco: 'Sob Consulta',
      descricao: 'O nível máximo de controle com inteligência de dados, gestão fiscal e emissão de notas.',
      destaque: false,
      productId: 'pro_consult',
      buttonLabel: 'Falar com Consultor',
      features: [
        'Tudo do Neri Basic',
        'Emissão de Notas Fiscais ilimitadas (NF-e, NFS-e e NFC-e)',
        'SAT/MF-e e Importação de XML automática',
        'Módulo Fiscal Avançado (DRE completo)',
        'Suporte especializado em Configuração Fiscal',
        'Portal do Cliente e Auditoria de Acesso',
      ]
    }
  ];

  ngOnInit() {
    this.auth.user().subscribe(user => {
      if (!this.auth.check()) {
        return;
      }
      const u = user as any;
      const id = u.empresaId || u.tenantId || u.idEmpresa || u.id_empresa || u.companyId;
      
      if (id) {
        this.empresaId = Number(id);
        this.loadStatus();
      } else {
        const stored = this.storage.get('tenantId') || this.storage.get('empresaId');
        if (stored) {
          this.empresaId = Number(stored);
          this.loadStatus();
        }
      }
    });
  }

  loadStatus() {
    if (!this.empresaId) return;
    this.loading = true;
    this.service.getStatus(this.empresaId)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => this.assinatura = data,
        error: () => this.toast.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar o status da assinatura.' })
      });
  }

  abrirPortal() {
    if (!this.empresaId) return;
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

  selecionarPlano(plano: Plano) {
    if (!this.empresaId) return;
    if (plano.preco === 'Sob Consulta') {
      const tawk = (window as any).Tawk_API;
      if (tawk && typeof tawk.maximize === 'function') {
        tawk.maximize();
      } else {
        window.open('https://tawk.to/chat/6a42ce3ad118e21d49b241b4/1jsafb5hm', '_blank');
      }
      return;
    }
    if (this.isPlanoAtual(plano)) {
      this.abrirPortal();
      return;
    }

    this.portalLoading = true;
    this.service.getCheckoutUrl(this.empresaId, plano.productId)
      .pipe(finalize(() => this.portalLoading = false))
      .subscribe({
        next: (res) => {
          if (res.url) {
            window.open(res.url, '_blank');
          }
        },
        error: (err) => {
          console.error('Erro ao gerar checkout:', err);
          this.toast.add({ 
            severity: 'error', 
            summary: 'Erro', 
            detail: 'Não foi possível gerar a tela de pagamento. Verifique sua conexão ou tente mais tarde.' 
          });
        }
      });
  }

  openChat(e: Event | null) {
    if (e) {
      e.preventDefault();
    }
    const tawk = (window as any).Tawk_API;
    if (tawk && typeof tawk.maximize === 'function') {
      tawk.maximize();
    } else {
      window.open('https://tawk.to/chat/6a42ce3ad118e21d49b241b4/1jsafb5hm', '_blank');
    }
  }

  isPlanoAtual(plano: Plano): boolean {
    return this.assinatura.stripeProductId === plano.productId;
  }

  getStatusLabel(): string {
    const map: Record<string, string> = {
      'ATIVO': 'Ativo',
      'TESTE': 'Período de Teste',
      'ATRASADO': 'Pagamento Pendente',
      'CANCELADO': 'Cancelado',
      'SUSPENSO': 'Acesso Suspenso',
      'INCOMPLETO': 'Cadastro Incompleto',
      'NAO_PAGO': 'Não Pago',
      'INATIVO': 'Inativo',
      'ERRO': 'Erro',
    };
    return map[this.assinatura.status] || this.assinatura.status || 'Desconhecido';
  }

  getStatusColor(): string {
    const map: Record<string, string> = {
      'ATIVO': 'bg-emerald-100 text-emerald-700 border-emerald-200',
      'TESTE': 'bg-blue-100 text-blue-700 border-blue-200',
      'ATRASADO': 'bg-amber-100 text-amber-700 border-amber-200',
      'CANCELADO': 'bg-red-100 text-red-700 border-red-200',
      'SUSPENSO': 'bg-red-100 text-red-700 border-red-200',
      'INATIVO': 'bg-slate-100 text-slate-500 border-slate-200',
    };
    return map[this.assinatura.status] || 'bg-slate-100 text-slate-500 border-slate-200';
  }
}
