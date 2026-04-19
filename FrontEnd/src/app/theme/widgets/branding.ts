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
      width: 28px;
      height: 28px;
      background: #6366f1;
      border-radius: 7px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .logo-mark {
      font-family: 'Inter', sans-serif;
      font-weight: 900;
      font-size: 0.85rem;
      color: white;
    }

    .logo-text {
      font-family: 'Inter', sans-serif;
      font-size: 1.15rem;
      font-weight: 900;
      color: #1e293b;
      letter-spacing: -0.05em;
    }

    .dark .logo-text {
      color: white;
    }

    .logo-accent {
      color: #00e5ff;
    }
  `,
})
export class Branding {
  @Input() showName = true;
}
