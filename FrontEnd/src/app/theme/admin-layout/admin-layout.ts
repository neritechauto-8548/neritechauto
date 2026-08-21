import { BidiModule } from '@angular/cdk/bidi';
import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, OnDestroy, ViewChild, ViewEncapsulation, inject } from '@angular/core';
import { MatSidenav, MatSidenavContent, MatSidenavModule } from '@angular/material/sidenav';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { NgProgressbar } from 'ngx-progressbar';
import { NgProgressRouter } from 'ngx-progressbar/router';
import { Subscription, filter } from 'rxjs';
import { ToastModule } from 'primeng/toast';

import { AppSettings, SettingsService } from '@core';
import { Header } from '../header/header';
import { SidebarNotice } from '../sidebar-notice/sidebar-notice';
import { Sidebar } from '../sidebar/sidebar';
import { Topmenu } from '../topmenu/topmenu';
import { BreadcrumbComponent } from '../widgets/breadcrumb';

// UI Master: XS < 768, SM 768–1023, desktop >= 1024.
const MOBILE_MEDIAQUERY = 'screen and (max-width: 767px)';
const TABLET_MEDIAQUERY = 'screen and (min-width: 768px) and (max-width: 1023px)';
const DESKTOP_MEDIAQUERY = 'screen and (min-width: 1024px)';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.scss',
  encapsulation: ViewEncapsulation.None,
  imports: [
    RouterOutlet,
    BidiModule,
    MatSidenavModule,
    NgProgressbar,
    NgProgressRouter,
    Header,
    Topmenu,
    Sidebar,
    SidebarNotice,
    BreadcrumbComponent,
    ToastModule,
  ],
  host: {
    '[class.matero-content-width-fix]': 'contentWidthFix',
    '[class.matero-sidenav-collapsed-fix]': 'collapsedWidthFix',
  },
})
export class AdminLayout implements OnDestroy {
  @ViewChild('sidenav', { static: true }) sidenav!: MatSidenav;
  @ViewChild('content', { static: true }) content!: MatSidenavContent;

  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly router = inject(Router);
  private readonly settings = inject(SettingsService);

  options = this.settings.options;

  get themeColor() {
    return this.settings.getThemeColor();
  }

  get isOver() {
    return this.isMobileScreen;
  }

  private isMobileScreen = false;
  private isContentWidthFixed = true;
  private isCollapsedWidthFixed = false;
  private layoutChangesSubscription = Subscription.EMPTY;
  private hoverTimer: ReturnType<typeof setTimeout> | undefined;
  private isHovering = false;

  get contentWidthFix() {
    return (
      this.isContentWidthFixed &&
      this.options.navPos === 'side' &&
      this.options.sidenavOpened &&
      !this.isOver &&
      !this.options.sidenavCollapsed
    );
  }

  get collapsedWidthFix() {
    return (
      this.isCollapsedWidthFixed &&
      (this.options.navPos === 'top' || (this.options.sidenavOpened && this.isOver))
    );
  }

  constructor() {
    this.settings.notify.subscribe(options => Object.assign(this.options, options));

    this.layoutChangesSubscription = this.breakpointObserver
      .observe([MOBILE_MEDIAQUERY, TABLET_MEDIAQUERY, DESKTOP_MEDIAQUERY])
      .subscribe(state => {
        this.options.sidenavOpened = true;
        this.isMobileScreen = state.breakpoints[MOBILE_MEDIAQUERY];

        // Tablet usa a navegação compacta; desktop preserva a preferência do usuário.
        if (state.breakpoints[TABLET_MEDIAQUERY]) {
          this.options.sidenavCollapsed = true;
        }

        this.isContentWidthFixed = state.breakpoints[DESKTOP_MEDIAQUERY];
      });

    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(() => {
      if (this.isOver) {
        this.sidenav.close();
      }
      this.content.scrollTo({ top: 0 });
    });
  }

  ngOnDestroy() {
    this.layoutChangesSubscription.unsubscribe();
    if (this.hoverTimer) {
      clearTimeout(this.hoverTimer);
    }
  }

  toggleCollapsed() {
    this.isHovering = false;
    this.isContentWidthFixed = false;
    this.options.sidenavCollapsed = !this.options.sidenavCollapsed;
    this.resetCollapsedState();
  }

  onMouseEnter() {
    if (this.options.sidenavCollapsed && !this.isOver) {
      if (this.hoverTimer) {
        clearTimeout(this.hoverTimer);
      }
      this.hoverTimer = setTimeout(() => {
        this.isHovering = true;
        this.options.sidenavCollapsed = false;
        this.settings.setOptions(this.options);
      }, 150);
    }
  }

  onMouseLeave() {
    if (this.isHovering && !this.isOver) {
      if (this.hoverTimer) {
        clearTimeout(this.hoverTimer);
      }
      this.hoverTimer = setTimeout(() => {
        this.isHovering = false;
        this.options.sidenavCollapsed = true;
        this.settings.setOptions(this.options);
      }, 200);
    }
  }

  onSidenavToggle() {
    if (this.isOver) {
      this.sidenav.toggle();
    } else {
      this.toggleCollapsed();
    }
  }

  resetCollapsedState(timer = 400) {
    setTimeout(() => this.settings.setOptions(this.options), timer);
  }

  onSidenavClosedStart() {
    this.isContentWidthFixed = false;
  }

  onSidenavOpenedChange(isOpened: boolean) {
    this.isCollapsedWidthFixed = !this.isOver;
    this.options.sidenavOpened = isOpened;
    this.settings.setOptions(this.options);
  }

  updateOptions(options: AppSettings) {
    this.options = options;
    this.settings.setOptions(options);
    this.settings.setDirection();
    this.settings.setTheme();
  }
}
