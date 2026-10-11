import { AfterContentInit, Directive, inject } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { MenuService } from '@core';
import { filter } from 'rxjs';
import { NavAccordionItem } from './nav-accordion-item';

@Directive({
  selector: '[navAccordion]',
  exportAs: 'navAccordion',
})
export class NavAccordion implements AfterContentInit {
  private readonly router = inject(Router);
  private readonly menu = inject(MenuService);

  private navItems: NavAccordionItem[] = [];

  constructor() {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => this.checkOpenedItems());

    // Fix opening status for async menu data
    this.menu.change().subscribe(() => {
      setTimeout(() => this.checkOpenedItems());
    });
  }

  ngAfterContentInit(): void {
    // Deep routes can be active before async menus finish rendering.
    queueMicrotask(() => this.checkOpenedItems());
  }

  addItem(item: NavAccordionItem) {
    this.navItems.push(item);
  }

  removeItem(item: NavAccordionItem) {
    const index = this.navItems.indexOf(item);
    if (index !== -1) {
      this.navItems.splice(index, 1);
    }
  }

  closeOtherItems(openedItem: NavAccordionItem) {
    this.navItems.forEach(item => {
      if (item !== openedItem) {
        item.expanded = false;
      }
    });
  }

  checkOpenedItems() {
    const current = this.router.url.split(/[?#]/)[0].replace(/\/+$/, '') || '/';
    this.navItems.forEach(item => {
      if (item.type !== 'sub') return;
      const shouldExpand = item.activeWhen.some(path => current === path || current.startsWith(path + '/'));
      if (item.expanded !== shouldExpand) item.expanded = shouldExpand;
    });
  }
}
