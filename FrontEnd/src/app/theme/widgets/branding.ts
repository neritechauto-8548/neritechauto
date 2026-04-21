import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-branding',
  template: `
    <a class="branding" href="/">
      <div class="logo-box">
        <span class="logo-mark">N</span>
      </div>
      @if (showName) {
        <span class="logo-text">
          Neri<span class="logo-accent">TechAuto</span>
        </span>
      }
    </a>
  `,
  styles: `
    .branding {
      display: flex;
      align-items: center;
      gap: 10px;
      margin: 0 0.5rem;
      text-decoration: none;
      transition: all 0.2s ease;
      transform: scale(0.9);
      transform-origin: left;
    }

    .branding:hover {
      opacity: 0.9;
    }

    .logo-box {
      width: 30px;
      height: 30px;
      background: linear-gradient(135deg, #8b5cf6 0%, #3f5efb 100%);
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 6px -1px rgba(63, 94, 251, 0.3), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    }

    .logo-mark {
      font-family: 'Inter', sans-serif;
      font-weight: 800;
      font-size: 0.9rem;
      color: white;
    }

    .logo-text {
      font-family: 'Inter', sans-serif;
      font-size: 1.25rem;
      font-weight: 800;
      color: #0f172a;
      letter-spacing: -0.04em;
    }

    .dark .logo-text {
      color: white;
    }

    .logo-accent {
      color: #6366f1;
    }
  `,
})
export class Branding {
  @Input() showName = true;
}
