import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-branding',
  template: `
    <a class="branding" href="/">
      <!-- SVG Logo -->
      <svg width="140" height="36" viewBox="0 0 140 36" fill="none" xmlns="http://www.w3.org/2000/svg" class="branding-logo">
        <!-- Icon/Symbol -->
        <rect x="0" y="0" width="36" height="36" rx="8" fill="url(#gradient)" />
        <path d="M18 10L26 18L18 26L10 18L18 10Z" fill="white" opacity="0.9"/>
        <circle cx="18" cy="18" r="3" fill="white"/>

        <!-- Gradient Definition -->
        <defs>
          <linearGradient id="gradient" x1="0" y1="0" x2="36" y2="36" gradientUnits="userSpaceOnUse">
            <stop offset="0%" stop-color="#2563EB"/>
            <stop offset="100%" stop-color="#1d4ed8"/>
          </linearGradient>
        </defs>

        <!-- Text "NeriTech" -->
        <text x="44" y="24" font-family="Inter, sans-serif" font-size="18" font-weight="700" letter-spacing="-0.02em" class="logo-text">
          Neri<tspan class="logo-accent">Tech</tspan>
        </text>
      </svg>
    </a>
  `,
  styles: `
    .branding {
      display: flex;
      align-items: center;
      margin: 0 0.5rem;
      text-decoration: none;
      transition: all 0.2s ease;
    }

    .branding:hover {
      transform: translateY(-1px);
    }

    .branding-logo {
      height: 36px;
      width: auto;
    }

    .logo-text {
      fill: var(--slate-900);
    }

    .dark .logo-text {
      fill: white;
    }

    .logo-accent {
      fill: #2563EB;
    }
  `,
})
export class Branding {
  @Input() showName = true;
}
