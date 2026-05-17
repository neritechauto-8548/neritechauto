<template>
  <section id="especialistas" class="communication-section">
    <div class="container">
      
      <!-- Premium Stripe Light Header -->
      <div class="comm-header aos-init">
        <span class="section-label">Comunicação & Vistoria</span>
        <h2 class="comm-title">Conecte sua oficina diretamente ao <span class="text-gradient-vibrant">celular do cliente.</span></h2>
        <p class="comm-subtitle">Feito sob medida para o setor automotivo brasileiro. Aumente em até 40% a aprovação de orçamentos com avisos e vistorias digitais direto no WhatsApp.</p>
      </div>

      <!-- Stripe-style Clean Cards Grid -->
      <div class="comm-cards-grid">
        
        <!-- Feature 1: Vistoria Digital -->
        <div class="comm-card">
          <div class="comm-card-header">
            <div class="comm-icon">📸</div>
            <div class="comm-info">
              <h3>Vistoria Digital</h3>
              <p>Evite reclamações de avarias</p>
            </div>
          </div>
          <div class="comm-card-body">
            <p class="comm-desc">Faça o checklist de entrada do veículo registrando fotos de amassados, arranhões e estado dos pneus. Gere um termo digital pronto para ser compartilhado.</p>
            
            <!-- Visual Element: Checklist items -->
            <div class="checklist-preview">
              <div class="check-item checked">
                <span class="indicator">✓</span>
                <span>Lataria e Pintura (Fotos anexadas)</span>
              </div>
              <div class="check-item checked">
                <span class="indicator">✓</span>
                <span>Estado dos Pneus e Estepe</span>
              </div>
              <div class="check-item checked">
                <span class="indicator">✓</span>
                <span>Nível de Combustível & Odômetro</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Feature 2: Orçamentos Interativos -->
        <div class="comm-card active-border">
          <div class="comm-card-header">
            <div class="comm-icon brand-glow">⚡</div>
            <div class="comm-info">
              <h3>Orçamento Interativo</h3>
              <p>Aprovação rápida via WhatsApp</p>
            </div>
          </div>
          <div class="comm-card-body">
            <p class="comm-desc">Envie um link exclusivo pelo WhatsApp. O cliente visualiza a descrição, as peças necessárias, o preço da mão de obra e aprova ou rejeita com apenas um clique.</p>
            
            <!-- Visual Element: Interactive Approved State -->
            <div class="approval-preview">
              <div class="approval-header">
                <strong>Orçamento #1024</strong>
                <span class="status-pill">Aprovado</span>
              </div>
              <div class="approval-total">
                <span>Total dos Serviços:</span>
                <strong>R$ 380,00</strong>
              </div>
            </div>
          </div>
        </div>

        <!-- Feature 3: Pós-Venda Automático -->
        <div class="comm-card">
          <div class="comm-card-header">
            <div class="comm-icon">📅</div>
            <div class="comm-info">
              <h3>Retorno Preventivo</h3>
              <p>Fidelize e traga o cliente de volta</p>
            </div>
          </div>
          <div class="comm-card-body">
            <p class="comm-desc">O sistema identifica serviços recorrentes e dispara alertas de retorno após 3, 6 ou 12 meses (como revisões periódicas, troca de óleo ou freios).</p>
            
            <!-- Visual Element: WhatsApp Message Simulation -->
            <div class="wa-preview">
              <div class="wa-header">WhatsApp Business</div>
              <div class="wa-msg">
                "Olá João, faz 6 meses da sua última troca de óleo do Civic. Que tal agendar sua revisão preventiva?"
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
.communication-section {
  padding: 6rem 0;
  background: #ffffff;
  position: relative;
}

.comm-header {
  text-align: center;
  max-width: 720px;
  margin: 0 auto 4rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.comm-title {
  font-size: clamp(2rem, 4vw, 2.75rem);
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 1rem;
  letter-spacing: -0.03em;
}

.comm-subtitle {
  font-size: 1.1rem;
  color: var(--text-main);
  line-height: 1.6;
}

/* ── Grid ── */
.comm-cards-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.comm-card {
  background: #ffffff;
  border: 1px solid #edf2f7;
  border-radius: var(--radius-xl);
  padding: 2rem;
  transition: all var(--transition-base);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.comm-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 40px rgba(10, 37, 64, 0.05);
  border-color: rgba(99, 91, 255, 0.2);
}

.comm-card.active-border {
  border-color: var(--primary-indigo);
  box-shadow: 0 20px 40px rgba(99, 91, 255, 0.04);
}

.comm-card-header {
  display: flex;
  align-items: center;
  gap: 14px;
}

.comm-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: rgba(99, 91, 255, 0.06);
  color: var(--primary-indigo);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  flex-shrink: 0;
}

.comm-icon.brand-glow {
  background: var(--primary-indigo);
  color: white;
}

.comm-info h3 {
  font-size: 1.125rem;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 2px;
}

.comm-info p {
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.comm-desc {
  font-size: 0.9375rem;
  color: var(--text-main);
  line-height: 1.6;
  margin-bottom: 1.25rem;
}

/* ── Visual Previews ── */
.checklist-preview {
  background: #f8fafc;
  border: 1px solid #edf2f7;
  border-radius: 10px;
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.check-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--text-main);
}

.indicator {
  width: 16px;
  height: 16px;
  background: rgba(0, 200, 83, 0.1);
  color: #00c853;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.625rem;
  font-weight: 800;
}

/* Approval */
.approval-preview {
  background: #f8fafc;
  border: 1px solid #edf2f7;
  border-radius: 10px;
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.approval-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.75rem;
}

.approval-header strong {
  color: var(--midnight-navy);
}

.status-pill {
  background: rgba(0, 200, 83, 0.1);
  color: #00c853;
  font-weight: 700;
  font-size: 0.625rem;
  padding: 2px 8px;
  border-radius: 99px;
}

.approval-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.75rem;
  border-top: 1px solid #edf2f7;
  padding-top: 8px;
}

.approval-total strong {
  color: var(--midnight-navy);
  font-size: 0.875rem;
}

/* WhatsApp */
.wa-preview {
  background: #e5ddd5;
  border: 1px solid #edf2f7;
  border-radius: 10px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.wa-header {
  font-size: 0.625rem;
  font-weight: 700;
  color: #128c7e;
  text-transform: uppercase;
}

.wa-msg {
  background: #ffffff;
  padding: 8px;
  border-radius: 8px;
  font-size: 0.6875rem;
  color: var(--text-main);
  line-height: 1.4;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .comm-cards-grid {
    grid-template-columns: 1fr;
  }
}
</style>
