import { AsyncPipe, NgClass, NgTemplateOutlet, SlicePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Input,
  ViewEncapsulation,
  inject,
} from '@angular/core';
import { MatRippleModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
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
    NgClass,
    SlicePipe,
    NgTemplateOutlet,
    RouterLink,
    RouterLinkActive,
    NgxPermissionsModule,
    MatIconModule,
    MatRippleModule,
    MatTooltipModule,
    TranslateModule,
    NavAccordion,
    NavAccordionItem,
    NavAccordionToggle,
  ],
})
export class Sidemenu {
  // The ripple effect makes page flashing on mobile
  @Input() ripple = false;

  private readonly menu = inject(MenuService);
  private readonly settings = inject(SettingsService);

  menu$ = this.menu.getAll();

  buildRoute = this.menu.buildRoute;

  get isCollapsed() {
    return this.settings.options.sidenavCollapsed;
  }
}
