import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

export type NeriTechIconName =
  | 'menu-2'
  | 'building'
  | 'chevron-down'
  | 'search'
  | 'help-circle';

/**
 * Lightweight Angular 20-compatible wrapper around official Tabler SVG geometry.
 *
 * DESIGN.md requires Tabler as the only icon family. The current official
 * @tabler/icons-angular release targets Angular 21+, so rebuilt Angular 20 UI
 * uses this local wrapper until the framework/package compatibility gate is met.
 *
 * Icons are decorative by default. Accessible names belong to the surrounding
 * button/link/control so icon-only controls remain explicit and localizable.
 */
@Component({
  selector: 'nt-icon',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    class: 'nt-icon',
    'aria-hidden': 'true',
  },
  styles: `
    :host {
      display: inline-flex;
      flex: 0 0 auto;
      align-items: center;
      justify-content: center;
      line-height: 0;
    }

    svg {
      display: block;
    }
  `,
  template: `
    <svg
      xmlns="http://www.w3.org/2000/svg"
      [attr.width]="size"
      [attr.height]="size"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      [attr.stroke-width]="stroke"
      stroke-linecap="round"
      stroke-linejoin="round"
      focusable="false"
    >
      @switch (name) {
        @case ('menu-2') {
          <path d="M4 6l16 0" />
          <path d="M4 12l16 0" />
          <path d="M4 18l16 0" />
        }
        @case ('building') {
          <path d="M3 21l18 0" />
          <path d="M9 8l1 0" />
          <path d="M9 12l1 0" />
          <path d="M9 16l1 0" />
          <path d="M14 8l1 0" />
          <path d="M14 12l1 0" />
          <path d="M14 16l1 0" />
          <path d="M5 21v-16a2 2 0 0 1 2 -2h10a2 2 0 0 1 2 2v16" />
        }
        @case ('chevron-down') {
          <path d="M6 9l6 6l6 -6" />
        }
        @case ('search') {
          <path d="M3 10a7 7 0 1 0 14 0a7 7 0 1 0 -14 0" />
          <path d="M21 21l-6 -6" />
        }
        @case ('help-circle') {
          <path d="M3 12a9 9 0 1 0 18 0a9 9 0 0 0 -18 0" />
          <path d="M12 16v.01" />
          <path d="M12 13a2 2 0 0 0 .914 -3.782a1.98 1.98 0 0 0 -2.414 .483" />
        }
      }
    </svg>
  `,
})
export class NeriTechIcon {
  @Input({ required: true }) name!: NeriTechIconName;
  @Input() size = 20;
  @Input() stroke = 2;
}
