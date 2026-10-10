import { Component, Input } from '@angular/core';

/**
 * Cabeçalho reutilizável de páginas NERITECH AUTO.
 * Ações e informações contextuais são projetadas pelo módulo proprietário.
 */
@Component({
  selector: 'neri-page-heading',
  standalone: true,
  template: `
    <header class="neri-page-heading">
      <div class="neri-page-heading__content">
        @if (eyebrow) {
          <p class="neri-page-heading__eyebrow">{{ eyebrow }}</p>
        }
        <h1 class="neri-page-heading__title">{{ title }}</h1>
        @if (description) {
          <p class="neri-page-heading__description">{{ description }}</p>
        }
        <ng-content select="[pageMeta]" />
      </div>
      <div class="neri-page-heading__actions">
        <ng-content select="[pageActions]" />
      </div>
    </header>
  `,
  styles: [`
    :host { display: block; min-width: 0; }
    .neri-page-heading {
      display: flex;
      align-items: flex-end;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: 24px;
      padding-bottom: 20px;
      border-bottom: 1px solid #e2e8f0;
    }
    .neri-page-heading__content { min-width: 0; flex: 1 1 320px; }
    .neri-page-heading__eyebrow {
      margin: 0 0 6px;
      color: #475569;
      font: 600 12px/16px Inter, system-ui, sans-serif;
      letter-spacing: .08em;
      text-transform: uppercase;
    }
    .neri-page-heading__title {
      margin: 0;
      color: #0f172a;
      font: 700 28px/36px Inter, system-ui, sans-serif;
      letter-spacing: -.035em;
    }
    .neri-page-heading__description {
      margin: 4px 0 0;
      color: #475569;
      font: 400 14px/20px Inter, system-ui, sans-serif;
    }
    .neri-page-heading__actions {
      display: flex;
      flex: 0 1 auto;
      align-items: center;
      justify-content: flex-end;
      flex-wrap: wrap;
      gap: 12px;
      min-height: 40px;
    }
    @media (max-width: 767px) {
      .neri-page-heading { align-items: stretch; gap: 16px; }
      .neri-page-heading__content { flex-basis: 100%; }
      .neri-page-heading__title { font-size: 24px; line-height: 32px; }
      .neri-page-heading__actions { justify-content: flex-start; width: 100%; }
    }
  `],
})
export class NeriPageHeading {
  @Input() eyebrow = '';
  @Input({ required: true }) title = '';
  @Input() description = '';
}
