import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { DataViewState, NeriTechIcon, NeriTechIconName, PageHeader } from '@shared';

export interface ModuleWorkspaceLink {
  title: string;
  description: string;
  route: string;
  icon: NeriTechIconName;
  label?: string;
}

export interface ModuleWorkspaceData {
  title: string;
  description: string;
  eyebrow?: string;
  statusTitle?: string;
  statusDescription?: string;
  links?: ModuleWorkspaceLink[];
}

/**
 * Canonical landing surface for capabilities whose aggregate read model is not
 * available yet. It exposes real connected journeys and an explicit degraded
 * state instead of fake KPIs, records or enabled actions.
 */
@Component({
  standalone: true,
  selector: 'app-module-workspace',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, PageHeader, NeriTechIcon, DataViewState],
  template: `
    <main class="workspace" [attr.aria-labelledby]="titleId">
      <page-header
        canonical
        [title]="data.title"
        [titleId]="titleId"
        [eyebrow]="data.eyebrow || 'NeriTech Auto'"
        [description]="data.description"
      />

      @if (links.length) {
        <section class="journeys" aria-labelledby="workspace-journeys-title">
          <div class="section-heading">
            <div>
              <h2 id="workspace-journeys-title">Acessos relacionados</h2>
              <p>Continue por uma etapa já disponível sem perder o contexto da operação.</p>
            </div>
          </div>

          <div class="journey-grid">
            @for (link of links; track link.route) {
              <a class="journey-card" [routerLink]="link.route">
                <span class="journey-card__icon"><nt-icon [name]="link.icon" [size]="22" /></span>
                <span class="journey-card__content">
                  <strong>{{ link.title }}</strong>
                  <small>{{ link.description }}</small>
                </span>
                <span class="journey-card__action">
                  {{ link.label || 'Abrir' }}
                  <nt-icon name="chevron-right" [size]="16" />
                </span>
              </a>
            }
          </div>
        </section>
      }

      <section class="availability" aria-labelledby="workspace-availability-title">
        <div class="section-heading">
          <div>
            <h2 id="workspace-availability-title">Disponibilidade desta visão</h2>
            <p>O sistema informa com clareza quando uma consolidação ainda depende da API.</p>
          </div>
        </div>

        <div class="state-panel">
          <nt-data-view-state
            kind="info"
            icon="database"
            [title]="data.statusTitle || 'Visão consolidada indisponível'"
            [description]="data.statusDescription || defaultStatusDescription"
          />
        </div>
      </section>
    </main>
  `,
  styles: `
    :host {
      display: block;
    }

    .workspace {
      display: grid;
      width: min(100%, 1500px);
      margin: 0 auto;
      padding: var(--nt-space-6);
      gap: var(--nt-space-6);
      color: var(--nt-text-primary);
    }

    .section-heading {
      display: flex;
      align-items: flex-end;
      justify-content: space-between;
      gap: var(--nt-space-4);
      margin-bottom: var(--nt-space-3);
    }

    .section-heading h2 {
      margin: 0;
      font-size: 16px;
      font-weight: 650;
      line-height: 24px;
    }

    .section-heading p {
      margin: var(--nt-space-1) 0 0;
      color: var(--nt-text-secondary);
      font-size: 13px;
      line-height: 18px;
    }

    .journey-grid {
      display: grid;
      grid-template-columns: repeat(3, minmax(0, 1fr));
      gap: var(--nt-space-3);
    }

    .journey-card {
      display: grid;
      grid-template-columns: auto minmax(0, 1fr) auto;
      min-height: 104px;
      align-items: center;
      gap: var(--nt-space-3);
      padding: var(--nt-space-4);
      border: 1px solid var(--nt-border-default);
      border-radius: var(--nt-radius-lg);
      background: var(--nt-surface-panel);
      color: inherit;
      text-decoration: none;
      transition:
        border-color 120ms ease,
        box-shadow 120ms ease,
        transform 120ms ease;
    }

    .journey-card:hover {
      border-color: var(--nt-primary-300);
      box-shadow: var(--nt-shadow-panel);
      transform: translateY(-1px);
    }

    .journey-card:focus-visible {
      outline: 0;
      border-color: var(--nt-primary-500);
      box-shadow: var(--nt-focus-ring);
    }

    .journey-card__icon {
      display: inline-flex;
      width: 42px;
      height: 42px;
      align-items: center;
      justify-content: center;
      border-radius: var(--nt-radius-lg);
      background: var(--nt-primary-50);
      color: var(--nt-primary-600);
    }

    .journey-card__content {
      display: grid;
      min-width: 0;
      gap: 4px;
    }

    .journey-card__content strong {
      font-size: 14px;
      line-height: 20px;
    }
    .journey-card__content small {
      color: var(--nt-text-secondary);
      font-size: 12px;
      line-height: 17px;
    }

    .journey-card__action {
      display: inline-flex;
      align-items: center;
      gap: 4px;
      color: var(--nt-primary-600);
      font-size: 12px;
      font-weight: 650;
    }

    .state-panel {
      overflow: hidden;
      border: 1px solid var(--nt-border-default);
      border-radius: var(--nt-radius-lg);
      background: var(--nt-surface-panel);
    }

    @media (max-width: 1100px) {
      .journey-grid {
        grid-template-columns: repeat(2, minmax(0, 1fr));
      }
    }

    @media (max-width: 767px) {
      .workspace {
        padding: var(--nt-space-4);
        gap: var(--nt-space-5);
      }
      .journey-grid {
        grid-template-columns: 1fr;
      }
      .journey-card {
        min-height: 96px;
      }
    }

    @media (max-width: 420px) {
      .journey-card {
        grid-template-columns: auto minmax(0, 1fr);
      }
      .journey-card__action {
        grid-column: 2;
      }
    }
  `,
})
export class ModuleWorkspace {
  private readonly route = inject(ActivatedRoute);

  readonly titleId = 'module-workspace-title';
  readonly defaultStatusDescription =
    'A API agregadora deste módulo ainda não está disponível. Nenhum indicador ou registro foi estimado no navegador. Use os acessos relacionados para continuar em fluxos já conectados.';
  readonly data = this.route.snapshot.data as ModuleWorkspaceData;
  readonly links = this.data.links ?? [];
}
