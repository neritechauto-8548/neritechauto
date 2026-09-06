import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-module-workspace',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <section class="workspace"><header><div><span class="eyebrow">NeriTech Auto</span><h1>{{ titulo }}</h1><p>{{ descricao }}</p></div><button class="primary" type="button">+ Nova movimentação</button></header>
      <div class="notice"><strong>Área pronta para operação</strong><span>Os dados serão carregados conforme a API e as permissões da empresa estiverem disponíveis.</span></div>
      <div class="summary"><article><small>Pendentes</small><strong>—</strong><span>Aguardando dados</span></article><article><small>Em andamento</small><strong>—</strong><span>Aguardando dados</span></article><article><small>Concluídos</small><strong>—</strong><span>Aguardando dados</span></article></div>
      <div class="panel"><div class="panel-head"><div><h2>Registros</h2><p>Pesquise e acompanhe as movimentações deste módulo.</p></div><input aria-label="Pesquisar registros" placeholder="Pesquisar por nome, código ou status" /></div><div class="empty"><div class="empty-icon">◎</div><h3>Nenhum registro carregado</h3><p>Quando houver dados disponíveis, eles aparecerão aqui com filtros, status e ações.</p><button type="button">Atualizar dados</button></div></div>
    </section>
  `,
  styles: [`:host{display:block}.workspace{max-width:1500px;margin:auto;padding:24px;color:#172033}header{display:flex;justify-content:space-between;align-items:flex-end;gap:24px;margin-bottom:24px}.eyebrow{font-size:12px;font-weight:700;letter-spacing:.08em;text-transform:uppercase;color:#2563eb}h1{font-size:28px;letter-spacing:-.03em;margin:6px 0}p{color:#667085;margin:0}.primary,.empty button{border:1px solid #2563eb;border-radius:8px;background:#2563eb;color:#fff;padding:10px 14px;font-weight:700}.notice{display:flex;gap:8px;align-items:center;padding:13px 16px;border:1px solid #bfdbfe;background:#eff6ff;border-radius:10px;color:#1e40af;font-size:13px;margin-bottom:16px}.notice span{color:#475467}.summary{display:grid;grid-template-columns:repeat(3,1fr);gap:14px;margin-bottom:16px}.summary article,.panel{border:1px solid #e5eaf1;border-radius:12px;background:#fff}.summary article{padding:16px}.summary small,.summary span{display:block;color:#667085;font-size:12px}.summary strong{display:block;font-size:24px;margin:8px 0}.panel-head{display:flex;justify-content:space-between;align-items:center;gap:16px;padding:18px;border-bottom:1px solid #eef2f6}.panel h2{font-size:16px;margin:0 0 4px}.panel-head input{width:360px;border:1px solid #dbe3ef;border-radius:8px;padding:10px 12px;font:inherit}.empty{text-align:center;padding:64px 20px}.empty-icon{font-size:36px;color:#94a3b8}.empty h3{margin:10px 0 6px}.empty p{margin-bottom:18px}@media(max-width:700px){header,.panel-head,.notice{align-items:flex-start;flex-direction:column}.summary{grid-template-columns:1fr}.panel-head input{width:100%}.primary{width:100%}}`],
})
export class ModuleWorkspace {
  private readonly rota = inject(ActivatedRoute);
  readonly titulo = String(this.rota.snapshot.data['title'] ?? 'Módulo');
  readonly descricao = String(this.rota.snapshot.data['description'] ?? 'Acompanhe esta área da oficina.');
}
