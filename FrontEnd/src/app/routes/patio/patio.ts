import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DataViewState, NeriTechIcon, PageHeader } from '@shared';

interface ColunaPatio { titulo: string; cor: string }

@Component({
  standalone: true,
  selector: 'app-patio',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, PageHeader, NeriTechIcon, DataViewState],
  template: `
    <main class="patio-page" aria-labelledby="patio-page-title">
      <page-header
        canonical
        title="Gestão de pátio"
        titleId="patio-page-title"
        eyebrow="Operacional"
        description="Acompanhe cada veículo, sua pendência e a próxima ação segura da equipe."
      >
        <a
          pageHeaderActions
          class="nt-action nt-action--primary"
          routerLink="/ordens-servico/cadastro"
        >
          <nt-icon name="plus" [size]="16" />
          Nova ordem de serviço
        </a>
      </page-header>

      <section class="flow" aria-labelledby="patio-flow-title">
        <div class="section-heading">
          <h2 id="patio-flow-title">Etapas do atendimento</h2>
          <p>O quadro preservará o fluxo mesmo quando estiver temporariamente sem dados.</p>
        </div>

        <ol class="flow-list">
          @for (coluna of colunas; track coluna.titulo; let index = $index) {
            <li>
              <span class="flow-index">{{ index + 1 }}</span>
              <span class="flow-dot" [style.backgroundColor]="coluna.cor"></span>
              <strong>{{ coluna.titulo }}</strong>
              @if (!$last) {
                <nt-icon name="chevron-right" [size]="16" />
              }
            </li>
          }
        </ol>
      </section>

      <section class="board-state" aria-label="Estado do quadro do pátio">
        <nt-data-view-state
          kind="info"
          icon="database"
          title="Quadro aguardando integração operacional"
          description="A API do pátio ainda não fornece a fila tenant-safe de veículos e ordens de serviço. Nenhum veículo de demonstração é exibido como se fosse um atendimento real."
        >
          <a class="secondary-action" routerLink="/ordens-servico">
            Ver ordens de serviço
            <nt-icon name="chevron-right" [size]="16" />
          </a>
        </nt-data-view-state>
      </section>
    </main>
  `,
  styles: `
    :host {
      display: block;
    }

    .patio-page {
      display: grid;
      width: min(100%, 1800px);
      margin: 0 auto;
      padding: var(--nt-space-6);
      gap: var(--nt-space-6);
      color: var(--nt-text-primary);
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

    .flow-list {
      display: grid;
      grid-template-columns: repeat(5, minmax(0, 1fr));
      margin: var(--nt-space-3) 0 0;
      padding: 0;
      border: 1px solid var(--nt-border-default);
      border-radius: var(--nt-radius-lg);
      background: var(--nt-surface-panel);
      list-style: none;
      overflow: hidden;
    }

    .flow-list li {
      display: grid;
      grid-template-columns: auto auto minmax(0, 1fr) auto;
      min-height: 68px;
      align-items: center;
      gap: var(--nt-space-2);
      padding: var(--nt-space-3);
      border-right: 1px solid var(--nt-border-default);
      font-size: 13px;
    }

    .flow-list li:last-child {
      border-right: 0;
    }
    .flow-index {
      color: var(--nt-text-muted);
      font-size: 11px;
      font-variant-numeric: tabular-nums;
    }
    .flow-dot {
      width: 8px;
      height: 8px;
      border-radius: 999px;
    }
    .flow-list nt-icon {
      color: var(--nt-text-muted);
    }

    .board-state {
      overflow: hidden;
      border: 1px solid var(--nt-border-default);
      border-radius: var(--nt-radius-lg);
      background: var(--nt-surface-panel);
    }

    .secondary-action {
      display: inline-flex;
      min-height: 36px;
      align-items: center;
      gap: var(--nt-space-1);
      padding: 7px var(--nt-space-3);
      border: 1px solid var(--nt-border-default);
      border-radius: var(--nt-radius-md);
      color: var(--nt-primary-600);
      font-size: 13px;
      font-weight: 650;
      text-decoration: none;
    }

    .secondary-action:focus-visible {
      outline: 0;
      box-shadow: var(--nt-focus-ring);
    }

    @media (max-width: 1100px) {
      .flow-list {
        grid-template-columns: 1fr;
      }
      .flow-list li {
        grid-template-columns: auto auto minmax(0, 1fr);
        border-right: 0;
        border-bottom: 1px solid var(--nt-border-default);
      }
      .flow-list li:last-child {
        border-bottom: 0;
      }
      .flow-list nt-icon {
        display: none;
      }
    }

    @media (max-width: 767px) {
      .patio-page {
        padding: var(--nt-space-4);
        gap: var(--nt-space-5);
      }
    }
  `,
})
export class PatioComponent {
  readonly colunas: ColunaPatio[] = [
    { titulo: 'Entrada', cor: '#64748b' },
    { titulo: 'Diagnóstico', cor: '#8b5cf6' },
    { titulo: 'Aguardando aprovação', cor: '#b45309' },
    { titulo: 'Em execução', cor: '#2563eb' },
    { titulo: 'Pronto', cor: '#15803d' },
  ];
}
