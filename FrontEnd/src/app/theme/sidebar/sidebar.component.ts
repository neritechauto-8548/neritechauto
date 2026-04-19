import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { UsuarioService } from '../../routes/configuracoes/usuario.service';

interface MenuItem {
    label: string;
    icon: string;
    route?: string;
    children?: MenuItem[];
    expanded?: boolean;
}

@Component({
  standalone: true,
  selector: 'app-sidebar-menu',
  template: `
    <div class="flex flex-col h-full bg-slate-900 text-white w-64">
        <div class="p-4 flex items-center justify-center gap-2.5 border-b border-gray-800">
            <div class="w-7 h-7 rounded-[7px] bg-[#6366f1] flex items-center justify-center text-white font-black text-xs shadow-sm">
                N
            </div>
            <div class="font-black text-[18px] tracking-tighter text-white leading-none mt-0.5">Neri<span class="text-[#00e5ff]">TechAuto</span></div>
        </div>
        <div class="flex-1 overflow-y-auto py-4">
             <nav class="space-y-1">
                 <ng-container *ngFor="let item of menu">
                     <!-- Item Simples -->
                     <a *ngIf="!item.children" [routerLink]="item.route" routerLinkActive="bg-blue-600 text-white" class="flex items-center px-4 py-3 hover:bg-slate-800 transition-colors cursor-pointer text-gray-300">
                         <i [class]="item.icon + ' w-6'"></i>
                         <span>{{ item.label }}</span>
                     </a>

                     <!-- Item com Submenu -->
                     <div *ngIf="item.children">
                         <div (click)="item.expanded = !item.expanded" class="flex items-center justify-between px-4 py-3 hover:bg-slate-800 transition-colors cursor-pointer text-gray-300">
                             <div class="flex items-center">
                                 <i [class]="item.icon + ' w-6'"></i>
                                 <span>{{ item.label }}</span>
                             </div>
                             <i class="pi" [ngClass]="{'pi-chevron-down': !item.expanded, 'pi-chevron-up': item.expanded}"></i>
                         </div>
                         <div *ngIf="item.expanded" class="bg-slate-950">
                             <a *ngFor="let sub of item.children" [routerLink]="sub.route" routerLinkActive="text-blue-400" class="flex items-center pl-12 pr-4 py-2 hover:bg-slate-900 transition-colors text-sm text-gray-400">
                                 <i [class]="sub.icon + ' w-5 mr-1 text-xs'"></i>
                                 <span>{{ sub.label }}</span>
                             </a>
                         </div>
                     </div>
                 </ng-container>
             </nav>
        </div>
        <div class="p-4 border-t border-gray-700 text-xs text-center text-gray-500">
            v1.0.0
        </div>
    </div>
  `,
  imports: [CommonModule, RouterModule]
})
export class SidebarMenuComponent {
   menu: MenuItem[] = [
       { label: 'Dashboard', icon: 'pi pi-home', route: '/dashboard' },
       {
           label: 'Ordens de Serviço', icon: 'pi pi-wrench', expanded: false,
           children: [
               { label: 'Nova OS', icon: 'pi pi-plus', route: '/os/cadastro' },
               { label: 'Pesquisar OS', icon: 'pi pi-search', route: '/os' },
               { label: 'Agenda', icon: 'pi pi-calendar', route: '/agendamento' },
               // { label: 'Orçamentos', icon: 'pi pi-file', route: '/orcamento' } // Se houver rota separada
           ]
       },
       { label: 'PDV (Vendas)', icon: 'pi pi-shopping-cart', route: '/pdv' },
       {
           label: 'Cadastros', icon: 'pi pi-folder', expanded: false,
           children: [
               { label: 'Clientes', icon: 'pi pi-users', route: '/cliente' },
               { label: 'Veículos', icon: 'pi pi-car', route: '/veiculo' },
               { label: 'Produtos', icon: 'pi pi-box', route: '/produtos-servicos/produtos' },
               { label: 'Serviços', icon: 'pi pi-cog', route: '/produtos-servicos/servicos' },
           ]
       },
       {
           label: 'Financeiro', icon: 'pi pi-dollar', expanded: true,
           children: [
               { label: 'Contas a Pagar', icon: 'pi pi-minus-circle', route: '/financeiro/pagar' },
               { label: 'Contas a Receber', icon: 'pi pi-plus-circle', route: '/financeiro/receber' },
               { label: 'Transações', icon: 'pi pi-list', route: '/financeiro/transacoes' }
           ]
       },
       {
           label: 'Fornecedores', icon: 'pi pi-truck', expanded: false,
           children: [
               { label: 'Gestão', icon: 'pi pi-list', route: '/fornecedor' },
               { label: 'Pedidos', icon: 'pi pi-file-o', route: '/fornecedor/pedidos' },
               { label: 'Nova Compra', icon: 'pi pi-cart-plus', route: '/fornecedor/compras' }
           ]
       },
       {
            label: 'Fiscal', icon: 'pi pi-file', expanded: false,
            children: [
                { label: 'Gerenciar Notas (NFe)', icon: 'pi pi-list', route: '/financeiro/nfe' }
            ]
       },
       {
           label: 'Configurações', icon: 'pi pi-cog', expanded: false,
           children: [
               { label: 'Empresa', icon: 'pi pi-building', route: '/configuracoes/empresa' },
               { label: 'Assinatura', icon: 'pi pi-credit-card', route: '/configuracoes/assinatura' },
               { label: 'Usuários', icon: 'pi pi-user', route: '/configuracoes/usuarios' },
               { label: 'Permissões', icon: 'pi pi-lock', route: '/configuracoes/permissoes' },
               { label: 'Administrador', icon: 'pi pi-shield', route: '/configuracoes/administrador' }
           ]
       }
   ];
}
