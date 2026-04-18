import { Component, ViewEncapsulation, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router, NavigationEnd, ActivatedRoute, RouterLink } from '@angular/router';
import { filter, map, mergeMap } from 'rxjs/operators';
import { AsyncPipe, TitleCasePipe } from '@angular/common';

@Component({
  selector: 'app-breadcrumb',
  template: `
    <nav class="flex items-center text-sm text-slate-500 dark:text-slate-400 py-2 px-6 bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800">
      <a routerLink="/" class="hover:text-primary-500 flex items-center transition-colors">
        <mat-icon class="!w-4 !h-4 !text-[16px] mr-1">home</mat-icon>
        Home
      </a>
      @if (pageTitle) {
        <mat-icon class="!w-4 !h-4 !text-[16px] mx-2 text-slate-300">chevron_right</mat-icon>
        <span class="font-medium text-slate-800 dark:text-slate-200">{{ pageTitle }}</span>
      }
    </nav>
  `,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [MatIconModule, RouterLink],
})
export class BreadcrumbComponent {
  private readonly router = inject(Router);
  private readonly activatedRoute = inject(ActivatedRoute);

  pageTitle = '';

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
        this.pageTitle = event['title'] || '';
      });
  }
}
