import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

/** SVG icons aligned to UI-MASTER-001 / Tabler stroke convention.
 * New names must be approved in the shared icon inventory.
 */
export type NeriTechIconName =
  | 'home' | 'users' | 'settings' | 'search' | 'alert-circle'
  | 'check-circle' | 'info-circle' | 'wifi-off' | 'file-search'
  | 'lock' | 'loader' | 'eye' | 'eye-off' | 'calendar' | 'arrow-up-right'
  | 'trend-up' | 'trend-down' | 'currency-dollar' | 'plus';

@Component({
  selector: 'nt-icon',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
      [attr.width]="size" [attr.height]="size" [attr.aria-hidden]="label ? null : 'true'"
      [attr.role]="label ? 'img' : null" [attr.aria-label]="label || null"
      fill="none" stroke="currentColor" stroke-width="1.75"
      stroke-linecap="round" stroke-linejoin="round">
      @switch (name) {
        @case ('home') {
          <path d="m3 10 9-7 9 7v10a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1z"/><path d="M9 21v-7h6v7"/>
        }
        @case ('users') {
          <circle cx="9" cy="7" r="3"/><path d="M3 21v-2a6 6 0 0 1 12 0v2M16 4a3 3 0 0 1 0 6m2 11v-2a6 6 0 0 0-3-5.2"/>
        }
        @case ('settings') {
          <circle cx="12" cy="12" r="3"/><path d="M10 2h4l.8 2.3 2 .9 2.2-1 2.8 2.8-1 2.2.9 2L24 12l-2.3.8-.9 2 1 2.2-2.8 2.8-2.2-1-2 .9L14 22h-4l-.8-2.3-2-.9-2.2 1-2.8-2.8 1-2.2-.9-2L0 12l2.3-.8.9-2-1-2.2L5 4.2l2.2 1 2-.9z" transform="translate(1 1) scale(.9167)"/>
        }
        @case ('search') {
          <circle cx="10.5" cy="10.5" r="7.5"/><path d="m16 16 5 5"/>
        }
        @case ('alert-circle') {
          <circle cx="12" cy="12" r="9"/><path d="M12 7.5v5.5m0 3v.5"/>
        }
        @case ('check-circle') {
          <circle cx="12" cy="12" r="9"/><path d="m8 12 3 3 5-6"/>
        }
        @case ('info-circle') {
          <circle cx="12" cy="12" r="9"/><path d="M12 11v5m0-8v.2"/>
        }
        @case ('wifi-off') {
          <path d="m3 3 18 18M2 9a16 16 0 0 1 5-2.1m4-1a16 16 0 0 1 11 3M5 12a11 11 0 0 1 5-2m3-.1a11 11 0 0 1 6 2M9 16a5 5 0 0 1 6 0M12 20h.01"/>
        }
        @case ('file-search') {
          <path d="M13 3H6a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h5M13 3l5 5h-5V3M18 8v4"/><circle cx="16" cy="17" r="3"/><path d="m18 19 3 3"/>
        }
        @case ('lock') {
          <rect x="5" y="11" width="14" height="10" rx="2"/><path d="M8 11V8a4 4 0 0 1 8 0v3"/>
        }
        @case ('loader') {
          <circle cx="12" cy="12" r="9" opacity=".22"/><path d="M12 3a9 9 0 0 1 9 9"/>
        }
        @case ('eye') {
          <path d="M2 12s3.6-6 10-6s10 6 10 6s-3.6 6-10 6s-10-6-10-6Z"/><circle cx="12" cy="12" r="2.5"/>
        }
        @case ('eye-off') {
          <path d="m3 3 18 18M10.6 10.6a2 2 0 0 0 2.8 2.8M9.9 5.2A10.8 10.8 0 0 1 12 5c6.4 0 10 7 10 7a17 17 0 0 1-3.2 4.1M6.2 6.2C3.5 8 2 12 2 12s3.6 7 10 7a10.8 10.8 0 0 0 3.7-.7"/>
        }
        @case ('calendar') {
          <rect x="3" y="5" width="18" height="16" rx="2"/><path d="M16 3v4M8 3v4M3 11h18M8 15h2m4 0h2"/>
        }
        @case ('arrow-up-right') {
          <path d="M7 17 17 7M7 7h10v10"/>
        }
        @case ('trend-up') {
          <path d="m3 17 6-6 4 4 8-8M15 7h6v6"/>
        }
        @case ('trend-down') {
          <path d="m3 7 6 6 4-4 8 8m-6 0h6v-6"/>
        }
        @case ('currency-dollar') {
          <path d="M12 2v20m5-15.5C16 5 14.5 4 12 4S7 5.5 7 8s2 3.5 5 4 5 1.5 5 4-2 4-5 4-5-1-5-2.5"/>
        }
        @case ('plus') {
          <path d="M12 5v14M5 12h14"/>
        }
      }
    </svg>
  `,
  styles: [`:host{display:inline-flex;align-items:center;justify-content:center;flex:none;line-height:0}
svg{display:block;flex:none}
`],
})
export class NeriTechIcon {
  @Input() name: NeriTechIconName = 'info-circle';
  @Input() size = 20;
  /** Label only for meaningful icons; decorative icons are hidden from AT. */
  @Input() label = '';
}
