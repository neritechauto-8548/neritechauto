import { Component, Input } from '@angular/core';

/** Icones SVG locais da Home; nao dependem de PrimeIcons nem de CDN. */
@Component({
  selector:'nt-dashboard-icon',standalone:true,
  template:`
  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"
    fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"
    [attr.width]="size" [attr.height]="size" aria-hidden="true">
    @switch(name){
      @case('clipboard-list'){<rect x="5" y="4" width="14" height="18" rx="2"/><path d="M9 4V2h6v2M9 10h6M9 14h6M9 18h3"/>}
      @case('wallet'){<rect x="3" y="6" width="18" height="15" rx="2"/><path d="M3 10h18M16 15h2M5 6V3h13"/>}
      @case('chart-no-axes-combined'){<path d="M3 3v18h18M6 16l4-5 4 3 6-8M17 6h3v3"/>}
      @case('layout-grid'){<rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/>}
      @case('chart-area'){<path d="M3 3v18h18M6 17l5-6 3 2 5-7v11z"/>}
      @case('circle-dollar-sign'){<circle cx="12" cy="12" r="9"/><path d="M15 8h-4a2 2 0 1 0 0 4h2a2 2 0 0 1 0 4H9M12 6v12"/>}
      @case('chart-column'){<path d="M3 21h18M5 17V9h4v8M11 17V4h4v13M17 17v-6h4v6"/>}
      @case('badge-dollar-sign'){<rect x="3" y="4" width="18" height="16" rx="2"/><path d="M7 9h10M7 15h3m4 0h3M12 6v12"/>}
      @case('scale'){<path d="M12 3v18M4 6h16M6 6l-4 7h8L6 6zm12 0-4 7h8l-4-7zM8 21h8"/>}
      @case('calculator'){<rect x="4" y="2" width="16" height="20" rx="2"/><path d="M8 6h8M8 11h2m4 0h2M8 15h2m4 0h2M8 19h8"/>}
      @case('shield-check'){<path d="M12 2 4 5v6c0 6 4 9 8 11 4-2 8-5 8-11V5zM8 12l3 3 5-6"/>}
      @case('landmark'){<path d="M3 9 12 3l9 6H3zM4 21h16M6 10v9M11 10v9M16 10v9M21 21H3"/>}
      @case('wrench'){<path d="M14 7a5 5 0 0 0-7 7l-5 5 3 3 5-5a5 5 0 0 0 7-7l-3 3-3-3z"/>}
      @case('car-front'){<path d="m5 11 2-6h10l2 6M4 11h16v8H4zM4 15h16M7 19v2m10-2v2"/><circle cx="7" cy="15" r="1"/><circle cx="17" cy="15" r="1"/>}
      @case('users-round'){<circle cx="9" cy="8" r="3"/><path d="M3 21v-2a6 6 0 0 1 12 0v2M17 7a3 3 0 0 1 0 6m1 3a5 5 0 0 1 3 5"/>}
      @case('user-round-x'){<circle cx="9" cy="8" r="4"/><path d="M2 21v-2a7 7 0 0 1 12-5M17 17l5 5m0-5-5 5"/>}
      @case('settings'){<circle cx="12" cy="12" r="3"/><path d="M12 2v3m0 14v3M2 12h3m14 0h3M5 5l2 2m10 10 2 2M19 5l-2 2M7 17l-2 2"/>}
      @case('briefcase-business'){<rect x="3" y="7" width="18" height="14" rx="2"/><path d="M8 7V4h8v3M3 13h18m-11 0v2h4v-2"/>}
      @case('chevron-down'){<path d="m5 9 7 7 7-7"/>}
      @case('chevron-up'){<path d="m5 15 7-7 7 7"/>}
      @default{<circle cx="12" cy="12" r="9"/><path d="M12 8v4m0 4v.1"/>}
    }
  </svg>`
})
export class DashboardUiIcon {
  @Input({required:true}) name='';
  @Input() size=18;
}
