import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

export type NeriTechIconName =
  | 'menu-2'
  | 'building'
  | 'chevron-down'
  | 'chevron-left'
  | 'chevron-right'
  | 'arrow-left'
  | 'external-link'
  | 'search'
  | 'x'
  | 'plus'
  | 'check'
  | 'trash'
  | 'printer'
  | 'refresh'
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
  | 'clipboard-list'
  | 'cash'
  | 'receipt'
  | 'receipt-tax'
  | 'history'
  | 'chart-bar'
  | 'calendar'
  | 'report-analytics'
  | 'car'
  | 'package'
  | 'settings'
  | 'eye'
  | 'eye-off'
  | 'loader-2'
  | 'info-circle'
  | 'link'
  | 'circle-check'
  | 'filter'
  | 'user'
  | 'clock'
  | 'send'
  | 'briefcase'
  | 'calendar-off'
  | 'bell'
  | 'bell-plus'
  | 'dots-vertical'
  | 'edit'
  | 'language'
  | 'mail'
  | 'brand-whatsapp';

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
        @case ('chevron-left') { <path d="m15 6-6 6 6 6" /> }
        @case ('chevron-right') { <path d="m9 6 6 6-6 6" /> }
        @case ('arrow-left') { <path d="M5 12h14" /><path d="m9 16-4-4 4-4" /> }
        @case ('external-link') { <path d="M12 6H6a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-6" /><path d="M11 13 20 4" /><path d="M15 4h5v5" /> }
        @case ('search') { <circle cx="10" cy="10" r="7" /><path d="m21 21-6-6" /> }
        @case ('x') { <path d="M18 6 6 18M6 6l12 12" /> }
        @case ('plus') { <path d="M12 5v14M5 12h14" /> }
        @case ('check') { <path d="m5 12 4 4L19 6" /> }
        @case ('trash') { <path d="M4 7h16" /><path d="M10 11v6M14 11v6" /><path d="M6 7l1 14h10l1-14" /><path d="M9 7V4h6v3" /> }
        @case ('printer') { <path d="M6 9V3h12v6" /><path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2" /><rect x="6" y="14" width="12" height="7" rx="1" /> }
        @case ('refresh') { <path d="M20 11a8 8 0 1 0-2.34 5.66" /><path d="M20 4v7h-7" /> }
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
        @case ('clipboard-list') { <path d="M9 5H6a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-3" /><rect x="9" y="3" width="6" height="4" rx="2" /><path d="M9 12h6M9 16h6" /> }
        @case ('cash') { <rect x="3" y="6" width="18" height="12" rx="2" /><circle cx="12" cy="12" r="2" /><path d="M7 10h.01M17 14h.01" /> }
        @case ('receipt') { <path d="M5 3v18l3-2 3 2 3-2 3 2 2-1.33V3l-3 2-3-2-3 2-3-2z" /><path d="M9 9h6M9 13h6M9 17h3" /> }
        @case ('receipt-tax') { <path d="M5 3v18l3-2 3 2 3-2 3 2 2-1.33V3l-3 2-3-2-3 2-3-2z" /><path d="M9 9h6M9 13h4" /> }
        @case ('history') { <path d="M3 12a9 9 0 1 0 3-6.7L3 8" /><path d="M3 3v5h5" /><path d="M12 7v5l3 2" /> }
        @case ('chart-bar') { <path d="M3 3v18h18" /><rect x="7" y="12" width="3" height="5" rx="1" /><rect x="12" y="8" width="3" height="9" rx="1" /><rect x="17" y="5" width="3" height="12" rx="1" /> }
        @case ('calendar') { <rect x="3" y="5" width="18" height="16" rx="2" /><path d="M16 3v4M8 3v4M3 11h18" /> }
        @case ('report-analytics') { <path d="M4 19V5a2 2 0 0 1 2-2h9l5 5v11a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2" /><path d="M14 3v5h5" /><path d="M8 17v-3M12 17v-5M16 17v-2" /> }
        @case ('car') { <path d="M5 17H3v-5l2-5h14l2 5v5h-2" /><path d="M5 17h14" /><circle cx="7" cy="17" r="2" /><circle cx="17" cy="17" r="2" /><path d="M5 12h14" /> }
        @case ('package') { <path d="m12 3 8 4.5v9L12 21l-8-4.5v-9z" /><path d="m4 7.5 8 4.5 8-4.5M12 12v9" /> }
        @case ('settings') { <circle cx="12" cy="12" r="3" /><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06-2.83 2.83-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21h-4v-.17a1.65 1.65 0 0 0-1-1.51 1.65 1.65 0 0 0-1.82.33l-.06.06-2.83-2.83.06-.06A1.65 1.65 0 0 0 4.6 15a1.65 1.65 0 0 0-1.51-1H3v-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06 2.83-2.83.06.06A1.65 1.65 0 0 0 8.92 4a1.65 1.65 0 0 0 1-1.51V2h4v.49A1.65 1.65 0 0 0 15 4a1.65 1.65 0 0 0 1.82-.33l.06-.06 2.83 2.83-.06.06A1.65 1.65 0 0 0 19.4 9c.12.61.67 1.04 1.29 1.04H21v4h-.31c-.62 0-1.17.43-1.29 1.04z" /> }
        @case ('eye') { <path d="M2 12s3.5-6 10-6 10 6 10 6-3.5 6-10 6S2 12 2 12" /><circle cx="12" cy="12" r="3" /> }
        @case ('eye-off') { <path d="m3 3 18 18" /><path d="M10.6 10.6a2 2 0 0 0 2.8 2.8" /><path d="M9.9 5.2A10.8 10.8 0 0 1 12 5c6.5 0 10 7 10 7a18 18 0 0 1-2 2.8M6.6 6.6C3.7 8.4 2 12 2 12s3.5 7 10 7a10.7 10.7 0 0 0 5.4-1.4" /> }
        @case ('loader-2') { <path d="M12 3a9 9 0 1 0 9 9" /> }
        @case ('info-circle') { <circle cx="12" cy="12" r="9" /><path d="M12 11v5M12 8v.01" /> }
        @case ('link') { <path d="M10 13a5 5 0 0 0 7.5.5l2-2a5 5 0 0 0-7-7l-1.15 1.15" /><path d="M14 11a5 5 0 0 0-7.5-.5l-2 2a5 5 0 0 0 7 7l1.15-1.15" /> }
        @case ('circle-check') { <circle cx="12" cy="12" r="9" /><path d="m9 12 2 2 4-4" /> }
        @case ('filter') { <path d="M4 4h16l-6 7v6l-4 2v-8z" /> }
        @case ('user') { <circle cx="12" cy="8" r="4" /><path d="M6 21v-2a6 6 0 0 1 12 0v2" /> }
        @case ('clock') { <circle cx="12" cy="12" r="9" /><path d="M12 7v5l3 2" /> }
        @case ('send') { <path d="m22 2-7 20-4-9-9-4z" /><path d="M22 2 11 13" /> }
        @case ('briefcase') { <rect x="3" y="7" width="18" height="13" rx="2" /><path d="M8 7V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M3 12h18M10 12v2h4v-2" /> }
        @case ('calendar-off') { <path d="m3 3 18 18" /><path d="M8 3v4M16 3v4M4 11h7M15 11h5" /><path d="M5 5a2 2 0 0 0-1 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 1.7-1" /><path d="M20 15V7a2 2 0 0 0-2-2H9" /> }
        @case ('bell') { <path d="M10 5a2 2 0 0 1 4 0 7 7 0 0 1 4 6v3l2 2H4l2-2v-3a7 7 0 0 1 4-6" /><path d="M9 20h6" /> }
        @case ('bell-plus') { <path d="M10 5a2 2 0 0 1 4 0 7 7 0 0 1 4 6v3l2 2H4l2-2v-3a7 7 0 0 1 4-6" /><path d="M9 20h6M12 8v4M10 10h4" /> }
        @case ('dots-vertical') { <circle cx="12" cy="5" r="1" fill="currentColor" stroke="none" /><circle cx="12" cy="12" r="1" fill="currentColor" stroke="none" /><circle cx="12" cy="19" r="1" fill="currentColor" stroke="none" /> }
        @case ('edit') { <path d="M4 20h4l11-11a2.8 2.8 0 0 0-4-4L4 16z" /><path d="m13.5 6.5 4 4" /> }
        @case ('language') { <circle cx="12" cy="12" r="9" /><path d="M3 12h18M12 3a15 15 0 0 1 0 18M12 3a15 15 0 0 0 0 18" /> }
        @case ('mail') { <rect x="3" y="5" width="18" height="14" rx="2" /><path d="m3 7 9 6 9-6" /> }
        @case ('brand-whatsapp') { <path d="M3 21l1.7-5A9 9 0 1 1 8 19.3z" /><path d="M9 8c.5 2 2 3.5 4 4l1-1c.5-.5 1-.5 1.5-.2l2 1.2c.5.3.6.8.4 1.3-.7 1.7-2 2.7-3.7 2.5-4.4-.6-7.4-3.6-8-8-.2-1.7.8-3 2.5-3.7.5-.2 1 .1 1.3.6l1.1 2c.3.5.3 1-.2 1.5z" /> }
      }
    </svg>
  `,
})
export class NeriTechIcon {
  @Input({ required: true }) name!: NeriTechIconName;
  @Input() size = 20;
  @Input() stroke = 2;
}
