import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

/**
 * Canonical operational data surface for lists and tables.
 *
 * Keeps the Stripe-like visual hierarchy (quiet surface, precise keylines)
 * while leaving workflow-specific filters, rows and pagination to the feature.
 * The shell intentionally does not own data fetching or business state.
 */
@Component({
  selector: 'nt-data-table-shell',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'nt-data-table-shell-host' },
  template: `
    <section class="nt-data-table-shell" [attr.aria-labelledby]="titleId">
      <header class="nt-data-table-shell__header">
        <div class="nt-data-table-shell__heading">
          @if (eyebrow) {
            <span class="nt-data-table-shell__eyebrow">{{ eyebrow }}</span>
          }
          <h2 [id]="titleId">{{ title }}</h2>
          @if (description) {
            <p>{{ description }}</p>
          }
        </div>

        <div class="nt-data-table-shell__meta">
          <ng-content select="[tableMeta]"></ng-content>
        </div>
      </header>

      <div class="nt-data-table-shell__content">
        <ng-content></ng-content>
      </div>

      <ng-content select="[tableFooter]"></ng-content>
    </section>
  `,
  styles: `
    :host {
      display: block;
      min-width: 0;
    }

    .nt-data-table-shell {
      overflow: hidden;
      border: 1px solid var(--nt-border-default);
      border-radius: var(--nt-radius-lg);
      background: var(--nt-surface-panel);
      box-shadow: var(--nt-shadow-panel);
    }

    .nt-data-table-shell__header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: var(--nt-space-4);
      padding: var(--nt-space-4) var(--nt-space-5);
      border-bottom: 1px solid var(--nt-border-default);
    }

    .nt-data-table-shell__heading {
      min-width: 0;
    }

    .nt-data-table-shell__eyebrow {
      color: var(--nt-primary-600);
      font-size: 10px;
      font-weight: 700;
      letter-spacing: 0.08em;
      text-transform: uppercase;
    }

    h2 {
      margin: 2px 0 0;
      color: var(--nt-text-primary);
      font-size: 17px;
      font-weight: 650;
      line-height: 1.35;
    }

    p {
      max-width: 720px;
      margin: var(--nt-space-1) 0 0;
      color: var(--nt-text-secondary);
      font-size: 12px;
      line-height: 1.5;
    }

    .nt-data-table-shell__meta {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      gap: var(--nt-space-2);
      flex-wrap: wrap;
    }

    .nt-data-table-shell__content {
      min-width: 0;
    }

    @media (max-width: 767px) {
      .nt-data-table-shell__header {
        align-items: flex-start;
        flex-direction: column;
        padding: var(--nt-space-4);
      }

      .nt-data-table-shell__meta {
        justify-content: flex-start;
      }
    }
  `,
})
export class DataTableShell {
  @Input() eyebrow = 'Resultado';
  @Input({ required: true }) title = '';
  @Input() titleId = 'data-table-title';
  @Input() description = '';
}
