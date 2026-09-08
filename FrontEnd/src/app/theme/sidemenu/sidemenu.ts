import { AsyncPipe, NgTemplateOutlet } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ViewEncapsulation,
  inject,
} from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { NgxPermissionsModule } from 'ngx-permissions';

import { MenuService, SettingsService } from '@core';
import {
  NeriTechIcon,
  NeriTechIconName,
} from '@shared/components/neritech-icon/neritech-icon';
import { NavAccordion } from './nav-accordion';
import { NavAccordionItem } from './nav-accordion-item';
import { NavAccordionToggle } from './nav-accordion-toggle';

@Component({
  selector: 'app-sidemenu',
  templateUrl: './sidemenu.html',
  styleUrl: './sidemenu.scss',
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    AsyncPipe,
    NgTemplateOutlet,
    RouterLink,
    RouterLinkActive,
    NgxPermissionsModule,
    MatTooltipModule,
    TranslateModule,
    NeriTechIcon,
    NavAccordion,
    NavAccordionItem,
    NavAccordionToggle,
  ],
})
export class Sidemenu {
  private readonly menu = inject(MenuService);
  private readonly settings = inject(SettingsService);

  readonly menu$ = this.menu.getAll();
  readonly buildRoute = this.menu.buildRoute;

  get isCollapsed() {
    return this.settings.options.sidenavCollapsed;
  }

  /**
   * The API remains authoritative for menu visibility/routes/permissions.
   * This method only translates legacy icon/name hints into the canonical
   * NeriTech Tabler icon vocabulary used by the application shell.
   */
  iconFor(icon?: string, name?: string): NeriTechIconName {
    const hint = `${icon ?? ''} ${name ?? ''}`
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase();

    if (this.hasAny(hint, ['patio', 'veiculo', 'carro', 'automovel'])) return 'car';
    if (this.hasAny(hint, ['home', 'dashboard', 'inicio'])) return 'layout-dashboard';
    if (this.hasAny(hint, ['cliente', 'crm', 'pessoa', 'usuario'])) return 'users';
    if (this.hasAny(hint, ['estoque', 'peca', 'produto', 'insumo'])) return 'package';
    if (this.hasAny(hint, ['orcamento', 'budget', 'estimate'])) return 'file-invoice';
    if (this.hasAny(hint, ['ordem de servico', 'checklist', 'aprovacao', 'inspection'])) {
      return 'clipboard-check';
    }
    if (this.hasAny(hint, ['financeiro', 'caixa', 'receber', 'pagar', 'cash'])) return 'cash';
    if (this.hasAny(hint, ['fiscal', 'nfe', 'nf-e', 'nfce', 'nfs', 'nota fiscal', 'tax'])) {
      return 'receipt-tax';
    }
    if (this.hasAny(hint, ['historico', 'history'])) return 'history';
    if (this.hasAny(hint, ['grafico', 'indicador', 'analytics', 'chart'])) return 'chart-bar';
    if (this.hasAny(hint, ['agenda', 'agendamento', 'calendar'])) return 'calendar';
    if (this.hasAny(hint, ['relatorio', 'report'])) return 'report-analytics';
    if (this.hasAny(hint, ['config', 'parametro', 'setting'])) return 'settings';
    if (this.hasAny(hint, ['moviment', 'transfer', 'fluxo'])) return 'arrows-exchange';
    if (this.hasAny(hint, ['operacional', 'servico', 'kit', 'acessorio', 'oficina', 'tool'])) {
      return 'tool';
    }
    if (this.hasAny(hint, ['cadastro', 'database', 'base'])) return 'database';

    return 'database';
  }

  private hasAny(value: string, terms: string[]): boolean {
    return terms.some(term => value.includes(term));
  }
}
