<template>
  <section id="showcase" class="showcase">
    <div class="container">
      
      <!-- Beside-style 4-Column Highlights Grid -->
      <div class="highlights-grid aos-init">
        <div class="highlight-card">
          <div class="hl-badge">Migração</div>
          <h3>Fácil de migrar</h3>
          <p>Importe dados de clientes, veículos e histórico de peças de outros sistemas em minutos.</p>
          <router-link to="/teste-gratis" class="hl-link">Começar agora →</router-link>
        </div>

        <div class="highlight-card">
          <div class="hl-badge">Preços</div>
          <h3>Planos flexíveis</h3>
          <p>Assinatura simples sem custos ocultos, taxas de implantação ou multas de fidelidade.</p>
          <a href="#planos" class="hl-link">Comparar planos →</a>
        </div>

        <div class="highlight-card">
          <div class="hl-badge">Operação</div>
          <h3>Equipe integrada</h3>
          <p>Distribua ordens de serviço, controle o fluxo de oficinas e sincronize mecânicos e consultores.</p>
          <router-link to="/teste-gratis" class="hl-link">Testar com equipe →</router-link>
        </div>

        <div class="highlight-card">
          <div class="hl-badge">Produtividade</div>
          <h3>Uso ilimitado</h3>
          <p>Emissão de ordens de serviço, orçamentos e checklists ilimitados em todos os planos.</p>
          <router-link to="/teste-gratis" class="hl-link">Experimentar grátis →</router-link>
        </div>
      </div>

      <!-- Divider line -->
      <div class="section-divider"></div>

      <!-- Detailed Showcase Items -->
      <div class="showcase-header aos-init">
        <span class="section-label">Aceleração Operacional</span>
        <h2 class="showcase-title">A tecnologia que <span class="text-gradient">impulsiona sua oficina.</span></h2>
        <p class="showcase-subtitle">Ferramentas desenhadas para alto fluxo e máxima lucratividade — sem complexidade desnecessária.</p>
      </div>

      <div class="showcase-grid">
        <!-- Feature 1: Checklist Digital -->
        <div class="showcase-item aos-init">
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
                Registro fotográfico de avarias direto do celular
              </li>
              <li>
                <span class="check">✓</span>
                Aprovação digital via link seguro no WhatsApp
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
                <div class="cl-item warn"><span class="cl-icon">!</span><span>Pastilha traseira</span><span class="cl-status warning">Substituir</span></div>
                <div class="cl-item done"><span class="cl-icon">✓</span><span>Filtro de ar</span><span class="cl-status ok">OK</span></div>
                <div class="cl-item warn"><span class="cl-icon">!</span><span>Fluido de freio</span><span class="cl-status warning">Substituir</span></div>
              </div>
              <div class="checklist-footer">
                <button class="vc-btn">Enviar Orçamento para o WhatsApp →</button>
              </div>
            </div>
            <!-- Floating approval card -->
            <div class="approval-badge">
              <span>💬</span>
              <span><strong>Cliente aprovou</strong> via WhatsApp</span>
            </div>
          </div>
        </div>

        <!-- Feature 2: Kanban de Produção -->
        <div class="showcase-item reverse aos-init">
          <div class="showcase-text">
            <span class="feature-pill pill-blue">⚡ Gestão de Fluxo</span>
            <h3>Elimine gargalos com <strong>visão em tempo real</strong></h3>
            <p>Veja a capacidade de cada baia, o status de cada OS e a performance de cada técnico — direto no dashboard, sem planilhas ou anotações manuais.</p>
            <ul class="feature-list">
              <li><span class="check">✓</span>Monitoramento de lead time por OS</li>
              <li><span class="check">✓</span>Alocação inteligente de mecânicos e consultores</li>
              <li><span class="check">✓</span>Dashboard de performance operacional em tempo real</li>
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
  padding: 6rem 0;
  background: #f8fafc;
  overflow: hidden;
}

/* ── Highlights Grid ── */
.highlights-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
  margin-bottom: 5rem;
}

.highlight-card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: var(--radius-lg);
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  transition: all var(--transition-base);
}

.highlight-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.05);
}

.hl-badge {
  font-size: 0.6875rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--primary-indigo);
  background: rgba(99, 91, 255, 0.1);
  padding: 4px 10px;
  border-radius: 99px;
  margin-bottom: 1rem;
}

.highlight-card h3 {
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--midnight-navy);
  margin-bottom: 0.75rem;
}

.highlight-card p {
  font-size: 0.875rem;
  color: var(--text-muted);
  line-height: 1.5;
  margin-bottom: 1.25rem;
  flex-grow: 1;
}

.hl-link {
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--primary-indigo);
  text-decoration: none;
  transition: color 0.2s;
}

.hl-link:hover {
  color: var(--primary-violet);
}

.section-divider {
  height: 1px;
  background: #e2e8f0;
  margin-bottom: 5rem;
}

/* ── Detailed Showcase ── */
.showcase-header {
  text-align: center;
  max-width: 720px;
  margin: 0 auto 4rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.showcase-title {
  font-size: clamp(2rem, 4vw, 2.75rem);
  font-weight: 800;
  margin-bottom: 1rem;
}

.showcase-subtitle {
  font-size: 1.125rem;
  color: var(--text-muted);
  line-height: 1.6;
}

.showcase-grid {
  display: flex;
  flex-direction: column;
  gap: 6rem;
}

.showcase-item {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 5rem;
  align-items: center;
}

.showcase-item.reverse .showcase-text { order: 2; }
.showcase-item.reverse .showcase-visual { order: 1; }

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
  gap: 12px;
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
  border: 1px solid #e2e8f0;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.visual-card-header {
  height: 36px;
  background: #f8fafc;
  border-bottom: 1px solid #e2e8f0;
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

.checklist-items {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.cl-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
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
  padding: 16px 20px;
  border-top: 1px solid #e2e8f0;
}

.vc-btn {
  width: 100%;
  padding: 12px;
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

.approval-badge {
  position: absolute;
  top: -16px;
  right: -20px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: var(--radius-md);
  padding: 10px 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.8125rem;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
  animation: float-badge 5s ease-in-out infinite;
}

.approval-badge strong { color: #059669; }

/* Kanban */
.kanban-cols {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  padding: 20px;
}

.kanban-col {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.kanban-header {
  font-size: 0.6875rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-muted);
  padding: 0 4px 6px;
  border-bottom: 2px solid #e2e8f0;
}
.active-h { color: var(--primary-indigo); border-color: var(--primary-indigo); }
.done-h   { color: #059669; border-color: #10b981; }

.kanban-card {
  padding: 12px;
  border-radius: 8px;
  font-size: 0.75rem;
  font-weight: 600;
  line-height: 1.4;
  color: var(--midnight-navy);
  border: 1px solid transparent;
}

.kanban-card small { display: block; font-weight: 400; color: var(--text-muted); margin-top: 2px; font-size: 0.7rem; }

.k-waiting { background: #f8fafc; border-color: #e2e8f0; }
.k-active  { background: rgba(99,91,255,0.06); border-color: rgba(99,91,255,0.2); }
.k-done    { background: rgba(16,185,129,0.06); border-color: rgba(16,185,129,0.15); }

@keyframes float-badge {
  0%, 100% { transform: translateY(0); }
  50%       { transform: translateY(-6px); }
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .highlights-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 900px) {
  .showcase-item,
  .showcase-item.reverse {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 3rem;
  }
  .showcase-item.reverse .showcase-text,
  .showcase-item.reverse .showcase-visual { order: unset; }
  .feature-list { align-items: center; text-align: left; }
  .approval-badge { display: none; }
}

@media (max-width: 640px) {
  .highlights-grid {
    grid-template-columns: 1fr;
  }
}
</style>
