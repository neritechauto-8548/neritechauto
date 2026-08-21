import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-module-placeholder',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="module-placeholder" aria-labelledby="module-placeholder-title">
      <div class="module-placeholder__card">
        <span class="module-placeholder__status">MAPEAMENTO PENDENTE</span>
        <h1 id="module-placeholder-title">{{ title }}</h1>
        <p>{{ description }}</p>

        <div class="module-placeholder__note">
          <i class="pi pi-info-circle" aria-hidden="true"></i>
          <span>
            Este destino existe para preservar a árvore oficial de navegação. A implementação funcional
            será liberada somente quando o módulo correspondente for desenvolvido e validado.
          </span>
        </div>

        <a routerLink="/home" class="module-placeholder__back">
          <i class="pi pi-arrow-left" aria-hidden="true"></i>
          Voltar para Home
        </a>
      </div>
    </section>
  `,
  styles: [`
    :host {
      display: block;
    }

    .module-placeholder {
      display: grid;
      min-height: calc(100vh - 180px);
      place-items: center;
      padding: 32px;
    }

    .module-placeholder__card {
      width: min(100%, 680px);
      padding: 32px;
      border: 1px solid #e2e8f0;
      border-radius: 14px;
      background: #fff;
      box-shadow: 0 8px 28px rgba(15, 23, 42, 0.05);
    }

    .module-placeholder__status {
      display: inline-flex;
      padding: 5px 9px;
      border-radius: 999px;
      background: #fffbeb;
      color: #b45309;
      font-size: 11px;
      font-weight: 700;
      letter-spacing: .06em;
    }

    h1 {
      margin: 14px 0 8px;
      color: #0f172a;
      font-size: 28px;
      line-height: 1.3;
    }

    p {
      margin: 0;
      color: #475569;
      font-size: 14px;
      line-height: 1.65;
    }

    .module-placeholder__note {
      display: flex;
      gap: 10px;
      margin-top: 24px;
      padding: 14px;
      border-radius: 10px;
      background: #f0f9ff;
      color: #475569;
      font-size: 13px;
      line-height: 1.55;
    }

    .module-placeholder__note i {
      margin-top: 2px;
      color: #0369a1;
    }

    .module-placeholder__back {
      display: inline-flex;
      align-items: center;
      gap: 7px;
      margin-top: 24px;
      color: #2563eb;
      font-size: 13px;
      font-weight: 650;
      text-decoration: none;
    }

    .module-placeholder__back:hover,
    .module-placeholder__back:focus-visible {
      color: #1d4ed8;
      text-decoration: underline;
    }

    @media (max-width: 767px) {
      .module-placeholder {
        min-height: auto;
        padding: 16px;
      }

      .module-placeholder__card {
        padding: 22px;
      }

      h1 {
        font-size: 24px;
      }
    }
  `],
})
export class ModulePlaceholder {
  private readonly route = inject(ActivatedRoute);

  readonly title = this.route.snapshot.data['title'] || 'Módulo em preparação';
  readonly description =
    this.route.snapshot.data['description'] ||
    'Este módulo faz parte da navegação oficial e ainda não possui implementação funcional liberada.';
}
