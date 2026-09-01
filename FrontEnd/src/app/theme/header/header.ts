import {
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  Output,
  ViewChild,
  ViewEncapsulation,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router, RouterLink } from '@angular/router';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslateService } from '@ngx-translate/core';
import { ToolbarModule } from 'primeng/toolbar';

import { MenuChildrenItem, MenuService } from '@core';
import { AuthService, User } from '@core/authentication';
import { NeriTechIcon } from '@shared/components/neritech-icon/neritech-icon';
import { NotificationButton } from '../widgets/notification-button';
import { UserButton } from '../widgets/user-button';

interface HeaderNavigationResult {
  label: string;
  route: string;
  searchText: string;
}

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  styleUrl: './header.scss',
  host: {
    class: 'matero-header',
  },
  encapsulation: ViewEncapsulation.None,
  imports: [RouterLink, ToolbarModule, MatTooltipModule, NeriTechIcon, NotificationButton, UserButton],
})
export class Header {
  @Input() showToggle = true;
  @Input() showBranding = false;

  @Output() toggleSidenav = new EventEmitter<void>();
  @Output() toggleSidenavNotice = new EventEmitter<void>();

  @ViewChild('globalSearchInput') globalSearchInput?: ElementRef<HTMLInputElement>;
  @ViewChild('globalSearchWrap') globalSearchWrap?: ElementRef<HTMLElement>;

  private readonly auth = inject(AuthService);
  private readonly menu = inject(MenuService);
  private readonly router = inject(Router);
  private readonly translate = inject(TranslateService);

  companyLabel = 'Empresa atual';
  globalQuery = '';
  globalSearchOpen = false;
  globalResults: HeaderNavigationResult[] = [];
  private navigationItems: HeaderNavigationResult[] = [];

  constructor() {
    this.auth
      .user()
      .pipe(takeUntilDestroyed())
      .subscribe(user => {
        this.companyLabel = this.resolveCompanyLabel(user);
      });

    // The backend-provided menu is already permission-filtered. The command
    // search only indexes those visible internal routes and never invents access.
    this.menu
      .getAll()
      .pipe(takeUntilDestroyed())
      .subscribe(items => {
        this.navigationItems = this.flattenMenu(items);
        this.refreshGlobalResults();
      });
  }

  onGlobalSearchInput(event: Event) {
    this.globalQuery = (event.target as HTMLInputElement).value;
    this.globalSearchOpen = true;
    this.refreshGlobalResults();
  }

  openGlobalSearch() {
    this.globalSearchOpen = true;
    this.refreshGlobalResults();
  }

  goToGlobalResult(result: HeaderNavigationResult) {
    this.globalQuery = '';
    this.globalResults = [];
    this.globalSearchOpen = false;
    void this.router.navigateByUrl(result.route);
  }

  @HostListener('document:keydown', ['$event'])
  onDocumentKeydown(event: KeyboardEvent) {
    const isCommandSearch = (event.ctrlKey || event.metaKey) && event.key.toLowerCase() === 'k';
    if (isCommandSearch) {
      event.preventDefault();
      this.globalSearchOpen = true;
      queueMicrotask(() => this.globalSearchInput?.nativeElement.focus());
      return;
    }

    if (event.key === 'Escape' && this.globalSearchOpen) {
      this.globalSearchOpen = false;
      this.globalSearchInput?.nativeElement.blur();
    }
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    if (!this.globalSearchOpen) return;
    const target = event.target as Node | null;
    if (target && this.globalSearchWrap?.nativeElement.contains(target)) return;
    this.globalSearchOpen = false;
  }

  private flattenMenu(items: MenuChildrenItem[], parentRoute: string[] = []): HeaderNavigationResult[] {
    return items.flatMap(item => {
      const routeParts = item.route ? [...parentRoute, item.route] : parentRoute;
      const ownResult = item.type === 'link'
        ? [{
            label: this.resolveMenuLabel(item.name),
            route: this.menu.buildRoute(routeParts),
            searchText: this.normalize(`${this.resolveMenuLabel(item.name)} ${item.name} ${routeParts.join(' ')}`),
          }]
        : [];

      const childResults = item.children?.length
        ? this.flattenMenu(item.children, routeParts)
        : [];

      return [...ownResult, ...childResults];
    });
  }

  private refreshGlobalResults() {
    const query = this.normalize(this.globalQuery);
    if (!query) {
      this.globalResults = [];
      return;
    }

    this.globalResults = this.navigationItems
      .filter(item => item.searchText.includes(query))
      .slice(0, 7);
  }

  private resolveMenuLabel(name: string): string {
    const translated = this.translate.instant(name);
    if (translated && translated !== name) return String(translated);
    const fallback = name.split('.').pop() || name;
    return fallback.replace(/[-_]/g, ' ');
  }

  private normalize(value: string): string {
    return value
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase()
      .trim();
  }

  private resolveCompanyLabel(user: User): string {
    const candidate =
      user?.empresaNome ||
      user?.nomeEmpresa ||
      user?.empresa?.nomeFantasia ||
      user?.empresa?.razaoSocial ||
      user?.empresa?.nome;

    return candidate ? String(candidate) : 'Empresa atual';
  }
}
