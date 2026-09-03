import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

import { NeriTechIcon, NeriTechIconName } from '../neritech-icon/neritech-icon';

export type DataViewStateKind = 'empty' | 'error' | 'permission' | 'info';

/**
 * Reusable first-use, filtered-empty, permission and error state.
 * Callers own the wording and recovery action so business semantics stay local.
 */
@Component({
  selector: 'nt-data-view-state',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [NeriTechIcon],
  host: { class: 'nt-data-view-state-host' },
  template: `
    <div
      class="nt-data-view-state"
      [class.nt-data-view-state--error]="kind === 'error'"
      [class.nt-data-view-state--permission]="kind === 'permission'"
      [attr.role]="kind === 'error' ? 'alert' : 'status'"
    >
      <div class="nt-data-view-state__icon">
        <nt-icon [name]="resolvedIcon" [size]="22" />
      </div>

      <div class="nt-data-view-state__body">
        <h3>{{ title }}</h3>
        <p>{{ description }}</p>
        <div class="nt-data-view-state__actions">
          <ng-content></ng-content>
        </div>
      </div>
    </div>
  `,
  styles: `
    :host {
      display: block;
    }

    .nt-data-view-state {
      display: flex;
      align-items: flex-start;
      justify-content: center;
      gap: var(--nt-space-4);
      padding: 44px var(--nt-space-5);
      background: var(--nt-surface-panel);
      text-align: left;
    }

    .nt-data-view-state__icon {
      display: inline-flex;
      width: 42px;
      height: 42px;
      flex: 0 0 42px;
      align-items: center;
      justify-content: center;
      border-radius: var(--nt-radius-lg);
      background: var(--nt-surface-subtle);
      color: var(--nt-primary-600);
    }

    .nt-data-view-state--error {
      background: var(--nt-danger-bg);
    }

    .nt-data-view-state--error .nt-data-view-state__icon {
      background: color-mix(in srgb, var(--nt-danger-bg) 76%, var(--nt-danger) 24%);
      color: var(--nt-danger);
    }

    .nt-data-view-state--permission .nt-data-view-state__icon {
      background: var(--nt-warning-bg);
      color: var(--nt-warning);
    }

    .nt-data-view-state__body {
      min-width: 0;
    }

    h3 {
      margin: 0;
      color: var(--nt-text-primary);
      font-size: 15px;
      font-weight: 650;
      line-height: 1.4;
    }

    p {
      max-width: 560px;
      margin: 6px 0 0;
      color: var(--nt-text-secondary);
      font-size: 13px;
      line-height: 1.55;
    }

    .nt-data-view-state__actions {
      display: flex;
      align-items: center;
      gap: var(--nt-space-2);
      margin-top: var(--nt-space-3);
      flex-wrap: wrap;
    }

    .nt-data-view-state__actions:empty {
      display: none;
    }

    @media (max-width: 767px) {
      .nt-data-view-state {
        justify-content: flex-start;
        padding: 32px var(--nt-space-4);
      }
    }
  `,
})
export class DataViewState {
  @Input() kind: DataViewStateKind = 'empty';
  @Input({ required: true }) title = '';
  @Input({ required: true }) description = '';
  @Input() icon?: NeriTechIconName;

  get resolvedIcon(): NeriTechIconName {
    if (this.icon) return this.icon;
    if (this.kind === 'error') return 'alert-triangle';
    if (this.kind === 'permission') return 'lock';
    if (this.kind === 'info') return 'help-circle';
    return 'users';
  }
}
