import { Component, OnInit, AfterViewInit, inject } from '@angular/core';
import { PreloaderService, SettingsService } from '@core';
import { RouterOutlet } from '@angular/router';

import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-root',
  template: `
    <p-toast [style]="{top: '110px'}" [baseZIndex]="99999" />
    <router-outlet />
  `,
  imports: [RouterOutlet, ToastModule],
})
export class App implements OnInit, AfterViewInit {
  private readonly preloader = inject(PreloaderService);
  private readonly settings = inject(SettingsService);

  ngOnInit() {
    this.settings.setDirection();
    this.settings.setTheme();
  }

  ngAfterViewInit() {
    this.preloader.hide();
  }
}
