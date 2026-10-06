import { AsyncPipe, NgTemplateOutlet } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  ViewEncapsulation,
  inject,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { NgxPermissionsModule } from 'ngx-permissions';
import { Subscription, filter } from 'rxjs';

import { Menu, MenuService } from '@core';
import { TopmenuPanel } from './topmenu-panel';

export interface TopmenuState {
  active: boolean;
  route: string;
}

@Component({
  selector: 'app-topmenu',
  templateUrl: './topmenu.html',
  styleUrl: './topmenu.scss',
  host: {
    class: 'matero-topmenu',
  },
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    AsyncPipe,
    NgTemplateOutlet,
    RouterLink,
    RouterLinkActive,
    MatButtonModule,
    MatMenuModule,
    MatTabsModule,
    NgxPermissionsModule,
    TranslateModule,
    TopmenuPanel,
  ],
})
export class Topmenu implements OnDestroy {
  private readonly menu = inject(MenuService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  menu$ = this.menu.getAll();

  buildRoute = this.menu.buildRoute;

  menuList: Menu[] = [];
  menuStates: TopmenuState[] = [];

  private menuSubscription = Subscription.EMPTY;
  private routerSubscription = Subscription.EMPTY;

  constructor() {
    this.menuSubscription = this.menu$.subscribe(res => {
      this.menuList = res;
      this.menuList.forEach(item => {
        this.menuStates.push({
          active: this.router.url.split('/').includes(item.route),
          route: item.route,
        });
      });
    });

    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(e => {
        this.menuStates.forEach(item => (item.active = false));
      });
  }

  ngOnDestroy() {
    this.menuSubscription.unsubscribe();
    this.routerSubscription.unsubscribe();
  }

  iconPaths(item: { icon?: string; name?: string }): string[] {
    const key = `${item.icon ?? ''} ${item.name ?? ''}`.toLowerCase();
    if (key.includes('home') || key.includes('início') || key.includes('dashboard')) return ['M3 10.5 12 3l9 7.5', 'M5 9.5V21h14V9.5', 'M9 21v-6h6v6'];
    if (key.includes('cliente') || key.includes('users') || key.includes('people')) return ['M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2', 'M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8'];
    if (key.includes('oficina') || key.includes('ordem') || key.includes('serviço')) return ['M14.7 6.3a4.8 4.8 0 0 0-6.4 6.4L3 18l3 3 5.3-5.3a4.8 4.8 0 0 0 6.4-6.4l-3 3-3-3 3-3Z'];
    if (key.includes('estoque') || key.includes('produto') || key.includes('compr')) return ['M3 7.5 12 3l9 4.5-9 4.5-9-4.5Z', 'M3 7.5V17l9 4 9-4V7.5', 'M12 12v9'];
    if (key.includes('finance')) return ['M3 6h18v14H3z', 'M3 10h18', 'M16 16h2'];
    if (key.includes('fiscal') || key.includes('nota')) return ['M6 2h9l3 3v17H6z', 'M9 13h6', 'M9 17h6'];
    if (key.includes('ia') || key.includes('inteligência')) return ['M12 3v3', 'M12 18v3', 'M3 12h3', 'M18 12h3', 'M9 9h6v6H9z'];
    if (key.includes('relatório') || key.includes('gráfico')) return ['M4 19V5', 'M4 19h16', 'M8 16v-5', 'M12 16V7', 'M16 16v-8'];
    if (key.includes('admin') || key.includes('config')) return ['M12 3a2 2 0 1 0 0 4 2 2 0 0 0 0-4', 'M5 21v-2a4 4 0 0 1 4-4h6a4 4 0 0 1 4 4v2'];
    return ['M12 3v18', 'M3 12h18'];
  }

  onRouteChange(rla: RouterLinkActive, index: number) {
    this.routerSubscription.unsubscribe();
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(e => {
        this.menuStates.forEach(item => (item.active = false));
        setTimeout(() => {
          this.menuStates[index].active = rla.isActive;
          this.cdr.markForCheck();
        });
      });
  }
}
