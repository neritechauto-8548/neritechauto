import { BidiModule } from '@angular/cdk/bidi';
import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, OnDestroy, ViewChild, ViewEncapsulation, inject } from '@angular/core';
import { MatSidenav, MatSidenavContent, MatSidenavModule } from '@angular/material/sidenav';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { NgProgressbar } from 'ngx-progressbar';
import { NgProgressRouter } from 'ngx-progressbar/router';
import { Subscription, filter } from 'rxjs';

import { AppSettings, SettingsService } from '@core';
import { Header } from '../header/header';
import { SidebarNotice } from '../sidebar-notice/sidebar-notice';
import { Sidebar } from '../sidebar/sidebar';
import { Topmenu } from '../topmenu/topmenu';
import { BreadcrumbComponent } from '../widgets/breadcrumb';
import { ConfirmationDialogComponent } from '@shared/components/confirmation-dialog/confirmation-dialog.component';

const MOBILE_MEDIAQUERY = 'screen and (max-width: 599px)';
const TABLET_MEDIAQUERY = 'screen and (min-width: 600px) and (max-width: 959px)';
const MONITOR_MEDIAQUERY = 'screen and (min-width: 960px)';

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
    ConfirmationDialogComponent,
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

  private isCollapsedWidthFixed = false;

  private layoutChangesSubscription = Subscription.EMPTY;

  private hoverTimer: any;
  private isHovering = false;

  constructor() {
    this.layoutChangesSubscription = this.breakpointObserver
      .observe([MOBILE_MEDIAQUERY, TABLET_MEDIAQUERY, MONITOR_MEDIAQUERY])
      .subscribe(state => {
        // SidenavOpened must be reset true when layout changes
        this.options.sidenavOpened = true;

        this.isMobileScreen = state.breakpoints[MOBILE_MEDIAQUERY];
        // Only force collapse on tablet, otherwise respect default/user setting
        if (state.breakpoints[TABLET_MEDIAQUERY]) {
          this.options.sidenavCollapsed = true;
        }
        this.isContentWidthFixed = state.breakpoints[MONITOR_MEDIAQUERY];
      });

    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(e => {
      if (this.isOver) {
        this.sidenav.close();
      }
      this.content.scrollTo({ top: 0 });
    });
  }

  ngOnDestroy() {
    this.layoutChangesSubscription.unsubscribe();
  }

  toggleCollapsed() {
    this.isHovering = false; // Reset hovering state if user manually toggles
    this.isContentWidthFixed = false;
    this.options.sidenavCollapsed = !this.options.sidenavCollapsed;
    this.resetCollapsedState();
  }

  onMouseEnter() {
    // Only expand on hover if it's currently collapsed and NOT in mobile mode
    if (this.options.sidenavCollapsed && !this.isOver) {
      clearTimeout(this.hoverTimer);
      this.hoverTimer = setTimeout(() => {
        this.isHovering = true;
        this.options.sidenavCollapsed = false;
        this.settings.setOptions(this.options);
      }, 150); // Intent delay to avoid accidental expansion
    }
  }

  onMouseLeave() {
    // Only collapse back if it was expanded via hover
    if (this.isHovering && !this.isOver) {
      clearTimeout(this.hoverTimer);
      this.hoverTimer = setTimeout(() => {
        this.isHovering = false;
        this.options.sidenavCollapsed = true;
        this.settings.setOptions(this.options);
      }, 200); // Leave delay buffer
    }
  }

  onSidenavToggle() {
    if (this.isOver) {
      this.sidenav.toggle();
    } else {
      this.toggleCollapsed();
    }
  }

  // TODO: Trigger when transition end
  resetCollapsedState(timer = 400) {
    setTimeout(() => {
      this.settings.setOptions(this.options);
    }, timer);
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
