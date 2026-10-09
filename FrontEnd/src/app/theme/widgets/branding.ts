import { Component, Input } from '@angular/core';

@Component({
  selector:'app-branding',
  template:`
    <a class="branding" href="/">
      <span class="logo-box"><span class="logo-mark">N</span></span>
      @if (showName) { <span class="logo-text">NERITECH <span class="logo-accent">AUTO</span></span> }
    </a>
  `,
  styles:`
    .branding{display:flex;align-items:center;gap:9px;text-decoration:none;transition:opacity .15s ease}
    .branding:hover{opacity:.82}
    .logo-box{display:grid;place-items:center;width:30px;height:30px;border-radius:7px;background:#0f172a}
    .logo-mark{font-family:Inter,sans-serif;font-size:12px;font-weight:800;color:#fff;letter-spacing:-.04em}
    .logo-text{font-family:Inter,sans-serif;font-size:13px;font-weight:750;color:#0f172a;letter-spacing:.055em;line-height:1}
    .logo-accent{color:#2563eb}
    .dark .logo-text{color:#fff}
  `,
})
export class Branding { @Input() showName=true; }