import { AsyncPipe, NgTemplateOutlet } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Input,
  ViewEncapsulation,
  inject,
} from '@angular/core';
import { MatRippleModule } from '@angular/material/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { NgxPermissionsModule } from 'ngx-permissions';

import { MenuService, SettingsService } from '@core';
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
    MatRippleModule,
    MatTooltipModule,
    TranslateModule,
    NavAccordion,
    NavAccordionItem,
    NavAccordionToggle,
  ],
})
export class Sidemenu {
  @Input() ripple = false;

  private readonly menu = inject(MenuService);
  private readonly settings = inject(SettingsService);

  menu$ = this.menu.getAll();
  buildRoute = this.menu.buildRoute;

  get isCollapsed() {
    return this.settings.options.sidenavCollapsed;
  }

  iconPaths(item: { icon?: string; name?: string }): string[] {
    const key = `${item.icon ?? ''} ${item.name ?? ''}`.toLowerCase();

    if (key.includes('home') || key.includes('início') || key.includes('dashboard')) {
      return ['M3 10.5 12 3l9 7.5', 'M5 9.5V21h14V9.5', 'M9 21v-6h6v6'];
    }
    if (key.includes('cliente') || key.includes('users') || key.includes('people')) {
      return ['M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2', 'M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8', 'M22 21v-2a4 4 0 0 0-3-3.87', 'M16 3.13a4 4 0 0 1 0 7.75'];
    }
    if (key.includes('oficina') || key.includes('ordem') || key.includes('serviço') || key.includes('service')) {
      return ['M14.7 6.3a4.8 4.8 0 0 0-6.4 6.4L3 18l3 3 5.3-5.3a4.8 4.8 0 0 0 6.4-6.4l-3 3-3-3 3-3Z'];
    }
    if (key.includes('estoque') || key.includes('produto') || key.includes('compr') || key.includes('invent')) {
      return ['M3 7.5 12 3l9 4.5-9 4.5-9-4.5Z', 'M3 7.5V17l9 4 9-4V7.5', 'M12 12v9'];
    }
    if (key.includes('finance') || key.includes('conta') || key.includes('caixa') || key.includes('banc')) {
      return ['M3 6h18v14H3z', 'M3 10h18', 'M16 16h2'];
    }
    if (key.includes('fiscal') || key.includes('nota') || key.includes('nfe') || key.includes('nfce')) {
      return ['M6 2h9l3 3v17H6z', 'M9 13h6', 'M9 17h6', 'M15 2v4h4'];
    }
    if (key.includes('ia') || key.includes('inteligência') || key.includes('assistente') || key.includes('automação')) {
      return ['M12 3v3', 'M12 18v3', 'M3 12h3', 'M18 12h3', 'M5.6 5.6l2.1 2.1', 'M16.3 16.3l2.1 2.1', 'M18.4 5.6l-2.1 2.1', 'M7.7 16.3l-2.1 2.1', 'M9 9h6v6H9z'];
    }
    if (key.includes('relatório') || key.includes('gráfico') || key.includes('analytics')) {
      return ['M4 19V5', 'M4 19h16', 'M8 16v-5', 'M12 16V7', 'M16 16v-8'];
    }
    if (key.includes('admin') || key.includes('config') || key.includes('usuário') || key.includes('empresa')) {
      return ['M12 3a2 2 0 1 0 0 4 2 2 0 0 0 0-4', 'M5 21v-2a4 4 0 0 1 4-4h6a4 4 0 0 1 4 4v2', 'M3 10h18'];
    }
    if (key.includes('pátio') || key.includes('patio')) {
      return ['M5 20V9', 'M9 20V5', 'M13 20v-7', 'M17 20V3', 'M21 20H3'];
    }
    return ['M12 3v18', 'M3 12h18'];
  }
}
