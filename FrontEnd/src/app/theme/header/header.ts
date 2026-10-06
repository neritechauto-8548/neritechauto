import { Component, EventEmitter, HostListener, Input, Output, ViewEncapsulation, inject } from '@angular/core';
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
  private readonly activatedRoute = inject(ActivatedRoute);

  pageTitle = 'Dashboard';
  searchTerm = '';
  searchResults: Array<{ name: string; route: string }> = [];
  private menuItems: Menu[] = [];

  onSearch(term: string): void {
    this.searchTerm = term;
    const normalized = term.trim().toLowerCase();
    if (!normalized) { this.searchResults = []; return; }
    const items: Array<{ name: string; route: string }> = [];
    const walk = (list: Array<Menu | MenuChildrenItem>, parents: string[] = []) => {
      list.forEach(item => {
        const name = String(item.name ?? '').replace(/^menu\\./, '').replace(/[._]/g, ' ');
        if (item.route && (item.type === 'link' || item.type === 'extLink' || item.type === 'extTabLink') && name.toLowerCase().includes(normalized)) {
          items.push({ name: [...parents, name].join(' / '), route: this.menu.buildRoute([item.route]) });
        }
        if (item.children?.length) walk(item.children, [...parents, name]);
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
    if (route.startsWith('http')) window.open(route, '_blank', 'noopener,noreferrer');
    else this.router.navigateByUrl(route);
  }

  @HostListener('document:keydown', ['$event'])
  onGlobalKeydown(event: KeyboardEvent): void {
    if ((event.ctrlKey || event.metaKey) && event.key.toLowerCase() === 'k') {
      event.preventDefault();
      document.querySelector<HTMLInputElement>('.nt-global-search input')?.focus();
    }
    if (event.key === 'Escape') { this.searchTerm = ''; this.searchResults = []; }
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
