import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-branding',
  template: `
    <a class="branding" href="/">
      <div class="logo-box">
        <div class="diamond"></div>
      </div>
      @if (showName) {
        <span class="logo-text">
          NeriTech<span class="logo-accent">auto</span>
        </span>
      }
    </a>
  `,
  styles: `
    .branding {
      display: flex;
      align-items: center;
      gap: 12px;
      margin: 0 0.5rem;
      text-decoration: none;
      transition: all 0.2s ease;
    }

    .branding:hover {
      transform: translateY(-1px);
    }

    .logo-box {
      width: 32px;
      height: 32px;
      background: #2563eb;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 6px -1px rgba(37, 99, 235, 0.2);
    }

    .diamond {
      width: 14px;
      height: 14px;
      background: #eff6ff;
      border-radius: 3px;
      transform: rotate(45deg);
    }

    .logo-text {
      font-family: 'Inter', sans-serif;
      font-size: 1.25rem;
      font-weight: 800;
      color: #1e293b;
      letter-spacing: -0.04em;
    }

    .dark .logo-text {
      color: white;
    }

    .logo-accent {
      color: #2563eb;
    }
  `,
})
export class Branding {
  @Input() showName = true;
}
