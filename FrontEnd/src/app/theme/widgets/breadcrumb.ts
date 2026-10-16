import { Component, ViewEncapsulation, inject } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute, RouterLink } from '@angular/router';
import { filter, map, mergeMap } from 'rxjs/operators';
import { AsyncPipe, TitleCasePipe } from '@angular/common';

@Component({
  selector: 'app-breadcrumb',
  template: `
    <nav aria-label="Caminho da página" class="nt-breadcrumb">
      <a routerLink="/home" aria-label="Ir para Início">
        <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M3 10.5 12 3l9 7.5M5 9.5V21h14V9.5M9 21v-6h6v6"></path></svg>
        Início
      </a>
      @if (pageTitle && pageTitle !== 'Início') {
        <svg class="nt-breadcrumb-chevron" viewBox="0 0 24 24" aria-hidden="true"><path d="m9 6 6 6-6 6"></path></svg>
        <span aria-current="page">{{ pageTitle }}</span>
      }
    </nav>
  `,
  encapsulation: ViewEncapsulation.None,
  styles: [`
    .nt-breadcrumb{display:flex;align-items:center;gap:8px;min-height:38px;padding:0 32px;background:var(--nt-surface,#fff);border-bottom:1px solid var(--nt-border,#e2e8f0);font:500 12px/18px Inter,system-ui,sans-serif;color:#64748b}
    .nt-breadcrumb a{display:inline-flex;align-items:center;gap:6px;color:#475569;text-decoration:none}
    .nt-breadcrumb a:hover{color:#2563eb}
    .nt-breadcrumb a:focus-visible{outline:2px solid #2563eb;outline-offset:3px}
    .nt-breadcrumb svg{width:15px;height:15px;fill:none;stroke:currentColor;stroke-width:1.8;stroke-linejoin:round;stroke-linecap:round}
    .nt-breadcrumb .nt-breadcrumb-chevron{width:12px;height:12px;color:#94a3b8}
    .nt-breadcrumb span{color:#334155;font-weight:600}
    @media(max-width:640px){.nt-breadcrumb{padding:0 16px}}
  `],
  standalone: true,
  imports: [RouterLink],
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
