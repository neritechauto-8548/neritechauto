import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

/** Shared navigation for the Cliente workspace, independent of the global sidebar. */
@Component({
  selector: 'nt-clientes-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="nt-clientes-tabs" aria-label="Áreas de Clientes">
      <a routerLink="/cliente/listar" routerLinkActive="active" [routerLinkActiveOptions]="{exact:true}">▣ &nbsp; Clientes</a>
      <a routerLink="/veiculo" routerLinkActive="active" [routerLinkActiveOptions]="{exact:true}">♧ &nbsp; Veículos</a>
      <a routerLink="/cliente/indicadores" routerLinkActive="active" [routerLinkActiveOptions]="{exact:true}">⌁ &nbsp; Indicadores</a>
    </nav>
  `,
  styles: [`
    :host{display:block}
    .nt-clientes-tabs{display:flex;align-items:center;gap:4px;padding:5px 6px;border:1px solid #e2e6eb;border-radius:9px;background:#fff;overflow-x:auto}
    .nt-clientes-tabs a{display:flex;align-items:center;justify-content:center;min-height:28px;padding:5px 13px;white-space:nowrap;border-radius:6px;color:#344357;font-size:12px;font-weight:550;text-decoration:none}
    .nt-clientes-tabs a.active{background:#dce9ff;color:#153d85;font-weight:650}
    .nt-clientes-tabs a:hover:not(.active){background:#f0f4f8}
    .nt-clientes-tabs a:focus-visible{outline:2px solid #2252a5;outline-offset:1px}
  `],
})
export class ClientesNav {}
