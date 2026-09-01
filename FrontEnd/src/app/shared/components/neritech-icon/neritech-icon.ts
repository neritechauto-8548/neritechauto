import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

export type NeriTechIconName =
  | 'menu-2'
  | 'building'
  | 'chevron-down'
  | 'external-link'
  | 'search'
  | 'x'
  | 'plus'
  | 'filter-x'
  | 'shield-check'
  | 'alert-triangle'
  | 'lock'
  | 'help-circle'
  | 'layout-dashboard'
  | 'users'
  | 'tool'
  | 'database'
  | 'arrows-exchange'
  | 'file-invoice'
  | 'clipboard-check'
  | 'cash'
  | 'receipt-tax'
  | 'history'
  | 'chart-bar'
  | 'calendar'
  | 'report-analytics'
  | 'car'
  | 'package'
  | 'settings';

/**
 * Lightweight Angular 20-compatible wrapper around Tabler-style SVG geometry.
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
        @case ('menu-2') { <path d="M4 6h16" /><path d="M4 12h16" /><path d="M4 18h16" /> }
        @case ('building') { <path d="M3 21h18" /><path d="M6 21V5a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v16" /><path d="M9 7h1M14 7h1M9 11h1M14 11h1M9 15h1M14 15h1" /> }
        @case ('chevron-down') { <path d="m6 9 6 6 6-6" /> }
        @case ('external-link') { <path d="M12 6H6a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-6" /><path d="M11 13 20 4" /><path d="M15 4h5v5" /> }
        @case ('search') { <circle cx="10" cy="10" r="7" /><path d="m21 21-6-6" /> }
        @case ('x') { <path d="M18 6 6 18M6 6l12 12" /> }
        @case ('plus') { <path d="M12 5v14M5 12h14" /> }
        @case ('filter-x') { <path d="M4 4h16l-6 7v4l-4 2v-6z" /><path d="m16 16 5 5M21 16l-5 5" /> }
        @case ('shield-check') { <path d="M12 3 5 6v5c0 5 3 8 7 10 4-2 7-5 7-10V6z" /><path d="m9 12 2 2 4-4" /> }
        @case ('alert-triangle') { <path d="M12 3 2 20h20z" /><path d="M12 9v4M12 17v.01" /> }
        @case ('lock') { <rect x="5" y="11" width="14" height="10" rx="2" /><path d="M8 11V7a4 4 0 0 1 8 0v4" /> }
        @case ('help-circle') { <circle cx="12" cy="12" r="9" /><path d="M12 17v.01" /><path d="M12 14a2.5 2.5 0 1 0-2.35-3.35" /> }
        @case ('layout-dashboard') { <rect x="3" y="3" width="7" height="9" rx="1" /><rect x="14" y="3" width="7" height="5" rx="1" /><rect x="14" y="12" width="7" height="9" rx="1" /><rect x="3" y="16" width="7" height="5" rx="1" /> }
        @case ('users') { <circle cx="9" cy="7" r="4" /><path d="M3 21v-2a6 6 0 0 1 6-6h1" /><path d="M16 3.13a4 4 0 0 1 0 7.75" /><path d="M17 14a5 5 0 0 1 4 5v2" /> }
        @case ('tool') { <path d="M14.7 6.3a4 4 0 0 0-5.4 5.4l-5.8 5.8a2 2 0 0 0 3 3l5.8-5.8a4 4 0 0 0 5.4-5.4l-2.4 2.4-3-3z" /> }
        @case ('database') { <ellipse cx="12" cy="5" rx="8" ry="3" /><path d="M4 5v6c0 1.66 3.58 3 8 3s8-1.34 8-3V5" /><path d="M4 11v6c0 1.66 3.58 3 8 3s8-1.34 8-3v-6" /> }
        @case ('arrows-exchange') { <path d="M7 10h11l-3-3" /><path d="m18 10-3 3" /><path d="M17 14H6l3 3" /><path d="m6 14 3-3" /> }
        @case ('file-invoice') { <path d="M14 3v4a1 1 0 0 0 1 1h4" /><path d="M5 3h9l5 5v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2" /><path d="M9 13h6M9 17h3" /> }
        @case ('clipboard-check') { <path d="M9 5H6a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-3" /><rect x="9" y="3" width="6" height="4" rx="2" /><path d="m9 14 2 2 4-4" /> }
        @case ('cash') { <rect x="3" y="6" width="18" height="12" rx="2" /><circle cx="12" cy="12" r="2" /><path d="M7 10h.01M17 14h.01" /> }
        @case ('receipt-tax') { <path d="M5 3v18l3-2 3 2 3-2 3 2 2-1.33V3l-3 2-3-2-3 2-3-2z" /><path d="M9 9h6M9 13h4" /> }
        @case ('history') { <path d="M3 12a9 9 0 1 0 3-6.7L3 8" /><path d="M3 3v5h5" /><path d="M12 7v5l3 2" /> }
        @case ('chart-bar') { <path d="M3 3v18h18" /><rect x="7" y="12" width="3" height="5" rx="1" /><rect x="12" y="8" width="3" height="9" rx="1" /><rect x="17" y="5" width="3" height="12" rx="1" /> }
        @case ('calendar') { <rect x="3" y="5" width="18" height="16" rx="2" /><path d="M16 3v4M8 3v4M3 11h18" /> }
        @case ('report-analytics') { <path d="M4 19V5a2 2 0 0 1 2-2h9l5 5v11a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2" /><path d="M14 3v5h5" /><path d="M8 17v-3M12 17v-5M16 17v-2" /> }
        @case ('car') { <path d="M5 17H3v-5l2-5h14l2 5v5h-2" /><path d="M5 17h14" /><circle cx="7" cy="17" r="2" /><circle cx="17" cy="17" r="2" /><path d="M5 12h14" /> }
        @case ('package') { <path d="m12 3 8 4.5v9L12 21l-8-4.5v-9z" /><path d="m4 7.5 8 4.5 8-4.5M12 12v9" /> }
        @case ('settings') { <circle cx="12" cy="12" r="3" /><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06-2.83 2.83-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21h-4v-.17a1.65 1.65 0 0 0-1-1.51 1.65 1.65 0 0 0-1.82.33l-.06.06-2.83-2.83.06-.06A1.65 1.65 0 0 0 4.6 15a1.65 1.65 0 0 0-1.51-1H3v-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06 2.83-2.83.06.06A1.65 1.65 0 0 0 8.92 4a1.65 1.65 0 0 0 1-1.51V2h4v.49A1.65 1.65 0 0 0 15 4a1.65 1.65 0 0 0 1.82-.33l.06-.06 2.83 2.83-.06.06A1.65 1.65 0 0 0 19.4 9c.12.61.67 1.04 1.29 1.04H21v4h-.31c-.62 0-1.17.43-1.29 1.04z" /> }
      }
    </svg>
  `,
})
export class NeriTechIcon {
  @Input({ required: true }) name!: NeriTechIconName;
  @Input() size = 20;
  @Input() stroke = 2;
}
