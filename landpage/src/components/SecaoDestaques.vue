<template>
  <section id="showcase" class="showcase">
    <div class="container">
      <div class="showcase-header aos-init">
        <span class="section-label">Funcionalidades</span>
        <h2 class="showcase-title">A tecnologia que <span class="text-gradient">impulsiona sua oficina.</span></h2>
        <p class="showcase-subtitle">Ferramentas desenhadas para alto fluxo e máxima lucratividade — sem complexidade desnecessária.</p>
      </div>

      <div class="showcase-grid">
        <!-- Feature 1 -->
        <div class="showcase-item aos-init aos-delay-1">
          <div class="showcase-text">
            <span class="feature-pill pill-purple">✦ Inovação</span>
            <h3>Inspeção digital que <strong>gera confiança</strong></h3>
            <p>Envie fotos e vídeos dos reparos necessários diretamente para o WhatsApp do cliente. Aprovação de orçamento em segundos, mais conversões para você.</p>
            <ul class="feature-list">
              <li>
                <span class="check">✓</span>
                Checklist personalizado por tipo de veículo
              </li>
              <li>
                <span class="check">✓</span>
                Registro fotográfico de avarias
              </li>
              <li>
                <span class="check">✓</span>
                Aprovação via link seguro no WhatsApp
              </li>
            </ul>
          </div>
          <div class="showcase-visual">
            <div class="visual-card">
              <div class="visual-card-header">
                <span class="vc-dot vc-dot-red"></span>
                <span class="vc-dot vc-dot-yellow"></span>
                <span class="vc-dot vc-dot-green"></span>
                <span class="vc-title">Checklist Digital · Honda Civic 2021</span>
              </div>
              <div class="checklist-items">
                <div class="cl-item done"><span class="cl-icon">✓</span><span>Freios dianteiros</span><span class="cl-status ok">OK</span></div>
                <div class="cl-item done"><span class="cl-icon">✓</span><span>Óleo do motor</span><span class="cl-status ok">OK</span></div>
                <div class="cl-item warn"><span class="cl-icon">!</span><span>Pastilha traseira</span><span class="cl-status warning">Trocar</span></div>
                <div class="cl-item done"><span class="cl-icon">✓</span><span>Filtro de ar</span><span class="cl-status ok">OK</span></div>
                <div class="cl-item warn"><span class="cl-icon">!</span><span>Fluido de freio</span><span class="cl-status warning">Trocar</span></div>
              </div>
              <div class="checklist-footer">
                <button class="vc-btn">Enviar para WhatsApp →</button>
              </div>
            </div>
            <!-- Floating approval card -->
            <div class="approval-badge">
              <span>💬</span>
              <span><strong>Cliente aprovou</strong> via WhatsApp</span>
            </div>
          </div>
        </div>

        <!-- Feature 2 -->
        <div class="showcase-item reverse aos-init aos-delay-2">
          <div class="showcase-text">
            <span class="feature-pill pill-blue">⚡ Gestão de Fluxo</span>
            <h3>Elimine gargalos com <strong>visão em tempo real</strong></h3>
            <p>Veja a capacidade de cada baia, o status de cada OS e a performance de cada técnico — direto no dashboard, sem planilhas ou anotações manuais.</p>
            <ul class="feature-list">
              <li><span class="check">✓</span>Monitoramento de lead time por OS</li>
              <li><span class="check">✓</span>Alocação inteligente de técnicos</li>
              <li><span class="check">✓</span>Dashboard de performance operacional</li>
            </ul>
          </div>
          <div class="showcase-visual">
            <div class="visual-card">
              <div class="visual-card-header">
                <span class="vc-dot vc-dot-red"></span>
                <span class="vc-dot vc-dot-yellow"></span>
                <span class="vc-dot vc-dot-green"></span>
                <span class="vc-title">Kanban de Produção</span>
              </div>
              <div class="kanban-cols">
                <div class="kanban-col">
                  <div class="kanban-header">Aguardando</div>
                  <div class="kanban-card k-waiting">Toyota Corolla<br><small>Troca de óleo</small></div>
                  <div class="kanban-card k-waiting">Ford Focus<br><small>Revisão 40k</small></div>
                </div>
                <div class="kanban-col">
                  <div class="kanban-header active-h">Em andamento</div>
                  <div class="kanban-card k-active">Honda Civic<br><small>Freios + Pastilha</small></div>
                </div>
                <div class="kanban-col">
                  <div class="kanban-header done-h">Concluído</div>
                  <div class="kanban-card k-done">VW Gol<br><small>Alinhamento</small></div>
                  <div class="kanban-card k-done">Fiat Uno<br><small>Filtro + Óleo</small></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
</script>

<style scoped>
.showcase {
  padding: var(--spacing-2xl) 0;
  background: var(--light-bg);
  overflow: hidden;
}

.showcase-header {
  text-align: center;
  max-width: 720px;
  margin: 0 auto var(--spacing-xl);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.showcase-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  margin-bottom: 1rem;
}

.showcase-subtitle {
  font-size: 1.125rem;
  color: var(--text-muted);
  line-height: 1.65;
}

/* ── Items ── */
.showcase-grid {
  display: flex;
  flex-direction: column;
  gap: 5rem;
}

.showcase-item {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.showcase-item.reverse .showcase-text { order: 2; }
.showcase-item.reverse .showcase-visual { order: 1; }

/* ── Text ── */
.feature-pill {
  display: inline-block;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin-bottom: 1rem;
}

.pill-purple { background: rgba(139,92,246,0.1); color: #7c3aed; }
.pill-blue   { background: rgba(59,130,246,0.1); color: #2563eb; }

.showcase-text h3 {
  font-size: clamp(1.5rem, 3vw, 2.25rem);
  margin-bottom: 1rem;
  color: var(--midnight-navy);
  line-height: 1.2;
  font-family: var(--font-heading);
}

.showcase-text h3 strong { color: var(--primary-indigo); font-weight: 800; }

.showcase-text p {
  font-size: 1.0625rem;
  color: var(--text-muted);
  margin-bottom: 1.75rem;
  line-height: 1.7;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.feature-list li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.9375rem;
  font-weight: 500;
  color: var(--text-main);
}

.check {
  width: 20px;
  height: 20px;
  background: rgba(99,91,255,0.1);
  color: var(--primary-indigo);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: 800;
  flex-shrink: 0;
}

/* ── Visual Card ── */
.showcase-visual {
  position: relative;
}

.visual-card {
  background: white;
  border-radius: var(--radius-xl);
  border: 1px solid var(--border);
  box-shadow: var(--shadow-xl);
  overflow: hidden;
}

.visual-card-header {
  height: 36px;
  background: #f8fafc;
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  padding: 0 14px;
  gap: 6px;
}

.vc-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}
.vc-dot-red    { background: #ff5f57; }
.vc-dot-yellow { background: #ffbd2e; }
.vc-dot-green  { background: #28c840; }

.vc-title {
  margin-left: 8px;
  font-size: 0.7rem;
  color: var(--text-muted);
  font-weight: 500;
}

/* Checklist */
.checklist-items {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.cl-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  background: #f8fafc;
}

.cl-item.done { border-left: 3px solid #10b981; }
.cl-item.warn { border-left: 3px solid #f59e0b; }

.cl-icon {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: 800;
  flex-shrink: 0;
}

.cl-item.done .cl-icon { background: rgba(16,185,129,0.12); color: #10b981; }
.cl-item.warn .cl-icon { background: rgba(245,158,11,0.12); color: #d97706; }

.cl-item span:nth-child(2) { flex: 1; color: var(--midnight-navy); }

.cl-status {
  font-size: 0.75rem;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 99px;
}
.cl-status.ok { background: rgba(16,185,129,0.1); color: #059669; }
.cl-status.warning { background: rgba(245,158,11,0.1); color: #d97706; }

.checklist-footer {
  padding: 12px 16px;
  border-top: 1px solid var(--border);
}

.vc-btn {
  width: 100%;
  padding: 10px;
  background: var(--primary-indigo);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  font-family: var(--font-body);
  transition: background 0.2s;
}
.vc-btn:hover { background: #5851e6; }

/* Approval badge */
.approval-badge {
  position: absolute;
  top: -16px;
  right: -20px;
  background: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  padding: 10px 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.8125rem;
  box-shadow: var(--shadow-lg);
  animation: float-a 5s ease-in-out infinite;
}

.approval-badge strong { color: #059669; }

/* Kanban */
.kanban-cols {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  padding: 14px;
}

.kanban-col {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.kanban-header {
  font-size: 0.6875rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-muted);
  padding: 0 4px 4px;
  border-bottom: 2px solid var(--border);
}
.active-h { color: var(--primary-indigo); border-color: var(--primary-indigo); }
.done-h   { color: #059669; border-color: #10b981; }

.kanban-card {
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 0.75rem;
  font-weight: 600;
  line-height: 1.4;
  color: var(--midnight-navy);
  border: 1px solid transparent;
}

.kanban-card small { display: block; font-weight: 400; color: var(--text-muted); margin-top: 2px; font-size: 0.7rem; }

.k-waiting { background: #f8fafc; border-color: var(--border); }
.k-active  { background: rgba(99,91,255,0.06); border-color: rgba(99,91,255,0.25); }
.k-done    { background: rgba(16,185,129,0.06); border-color: rgba(16,185,129,0.2); }

@keyframes float-a {
  0%, 100% { transform: translateY(0); }
  50%       { transform: translateY(-8px); }
}

/* ── Responsive ── */
@media (max-width: 900px) {
  .showcase-item,
  .showcase-item.reverse {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 2rem;
  }
  .showcase-item.reverse .showcase-text,
  .showcase-item.reverse .showcase-visual { order: unset; }
  .feature-list { align-items: flex-start; }
  .approval-badge { display: none; }
}
</style>
