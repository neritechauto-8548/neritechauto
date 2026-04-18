import { Component, EventEmitter, Input, Output, ViewEncapsulation, inject } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { filter, map, mergeMap } from 'rxjs/operators';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import screenfull from 'screenfull';

import { NotificationButton } from '../widgets/notification-button';
import { UserButton } from '../widgets/user-button';
import { SettingsService } from '@core';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  styleUrl: './header.scss',
  host: {
    class: 'matero-header',
  },
  encapsulation: ViewEncapsulation.None,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    NotificationButton,
    UserButton,
  ],
})
export class Header {
  @Input() showToggle = true;
  @Input() showBranding = false;

  @Output() toggleSidenav = new EventEmitter<void>();
  @Output() toggleSidenavNotice = new EventEmitter<void>();

  private readonly settings = inject(SettingsService);
  private readonly router = inject(Router);
  private readonly activatedRoute = inject(ActivatedRoute);

  pageTitle = 'Dashboard';

  constructor() {
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
        this.pageTitle = event['title'] || 'NeriTech';
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
