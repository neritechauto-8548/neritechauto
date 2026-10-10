import { Component, ElementRef, EventEmitter, HostListener, Input, Output, ViewChild, ViewEncapsulation, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { filter, map, mergeMap } from 'rxjs/operators';

import { MatTooltipModule } from '@angular/material/tooltip';

import screenfull from 'screenfull';

import { Branding } from '../widgets/branding';
import { NotificationButton } from '../widgets/notification-button';
import { UserButton } from '../widgets/user-button';
import { Menu, MenuChildrenItem, MenuService, SettingsService } from '@core';
import { FormsModule } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  styleUrl: './header.scss',
  host: {
    class: 'matero-header',
  },
  encapsulation: ViewEncapsulation.None,
  imports: [
    CommonModule,
    FormsModule,
    MatTooltipModule,
    Branding,
    NotificationButton,
    UserButton,
  ],
})
export class Header {
  @Input() showToggle = true;
  @Input() showBranding = false;

  @Output() toggleSidenav = new EventEmitter<void>();
  @Output() toggleSidenavNotice = new EventEmitter<void>();

  public readonly settings = inject(SettingsService);
  private readonly router = inject(Router);
  private readonly menu = inject(MenuService);
  private readonly translate = inject(TranslateService);
  private readonly activatedRoute = inject(ActivatedRoute);

  @ViewChild('globalSearchInput') globalSearchInput?: ElementRef<HTMLInputElement>;
  mobileSearchOpen = false;
  pageTitle = 'Dashboard';
  searchTerm = '';
  searchResults: Array<{ name: string; route: string }> = [];
  private menuItems: Menu[] = [];

  /** Busca global com nomes traduzidos e rotas completas de módulos e submódulos. */
  toggleMobileSearch(): void {
    this.mobileSearchOpen = !this.mobileSearchOpen;
    if (!this.mobileSearchOpen) {
      this.searchResults = [];
      this.searchTerm = '';
    } else {
      queueMicrotask(() => this.globalSearchInput?.nativeElement.focus());
    }
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    const normalized = term.trim().toLocaleLowerCase('pt-BR');
    if (!normalized) { this.searchResults = []; return; }

    const items: Array<{ name: string; route: string }> = [];
    const walk = (
      list: Array<Menu | MenuChildrenItem>,
      parentRoutes: string[] = [],
      parentLabels: string[] = []
    ) => {
      list.forEach(item => {
        const key = String(item.name ?? '');
        const translated = this.translate.instant(key);
        const name = translated && translated !== key
          ? translated
          : (key.replace(/^menu\./, '').split('.').pop() || key).replace(/[_-]/g, ' ');
        const routes = item.route ? [...parentRoutes, item.route] : parentRoutes;
        if (item.route && (item.type === 'link' || item.type === 'extLink' || item.type === 'extTabLink')) {
          const fullLabel = [...parentLabels, name].join(' / ');
          if (fullLabel.toLocaleLowerCase('pt-BR').includes(normalized)) {
            const route = item.type === 'link'
              ? this.menu.buildRoute(routes)
              : item.route;
            if (!items.some(existing => existing.route === route)) {
              items.push({ name: fullLabel, route });
            }
          }
        }
        if (item.children?.length) walk(item.children, routes, [...parentLabels, name]);
      });
    };
    walk(this.menuItems);
    this.searchResults = items.slice(0, 6);
  }

  openFirstSearchResult(): void {
    if (this.searchResults.length) this.navigateSearch(this.searchResults[0].route);
  }

  navigateSearch(route: string): void {
    this.searchTerm = '';
    this.searchResults = [];
    this.mobileSearchOpen = false;
    if (route.startsWith('http')) window.open(route, '_blank', 'noopener,noreferrer');
    else this.router.navigateByUrl(route);
  }

  @HostListener('document:keydown', ['$event'])
  onGlobalKeydown(event: KeyboardEvent): void {
    if ((event.ctrlKey || event.metaKey) && event.key.toLowerCase() === 'k') {
      event.preventDefault();
      this.mobileSearchOpen = true;
      queueMicrotask(() => this.globalSearchInput?.nativeElement.focus());
    }
    if (event.key === 'Escape') {
      this.searchTerm = '';
      this.searchResults = [];
      this.mobileSearchOpen = false;
    }
  }

  constructor() {
    this.menu.getAll().subscribe(menu => this.menuItems = menu);
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        map(() => this.activatedRoute),
        map(route => {
          while (route.firstChild) {
            route = route.firstChild;
          }
          return route;
        }),
        mergeMap(route => route.data)
      )
      .subscribe(event => {
        this.pageTitle = event['title'] || 'NeriTechAuto';
      });
  }

  toggleTheme() {
    this.settings.setTheme(this.settings.getThemeColor() === 'dark' ? 'light' : 'dark');
  }

  toggleFullscreen() {
    if (screenfull.isEnabled) {
      screenfull.toggle();
    }
  }
}
