<template>
  <section id="contato" class="contact">
    <div class="container">
      <div class="contact-inner">

        <!-- Info -->
        <div class="contact-info aos-init">
          <span class="section-label">Fale Conosco</span>
          <h2 class="contact-title">Dúvidas? <span class="text-gradient">Fale com um especialista.</span></h2>
          <p class="contact-subtitle">Nossa equipe está pronta para ajudar sua oficina a dar o próximo passo na digitalização.</p>

          <div class="contact-channels">
            <a
              href="https://wa.me/5511987654321"
              target="_blank"
              rel="noopener"
              class="channel-card channel-whatsapp"
            >
              <div class="channel-icon">💬</div>
              <div class="channel-text">
                <strong>WhatsApp</strong>
                <span>(11) 98765-4321</span>
              </div>
              <span class="channel-arrow">→</span>
            </a>

            <a href="mailto:contato@neritech.com.br" class="channel-card channel-email">
              <div class="channel-icon">📧</div>
              <div class="channel-text">
                <strong>E-mail</strong>
                <span>contato@neritech.com.br</span>
              </div>
              <span class="channel-arrow">→</span>
            </a>
          </div>

          <div class="office-hours">
            <span class="hours-dot"></span>
            <span>Seg–Sex, das 8:30 às 18:00</span>
          </div>
        </div>

        <!-- Form -->
        <div class="contact-form-wrap aos-init aos-delay-2">
          <form class="contact-form" @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="contact-name">Nome completo</label>
              <input id="contact-name" type="text" placeholder="Ex: João Silva" required v-model="form.name" />
            </div>

            <div class="form-row">
              <div class="form-group">
                <label for="contact-email">E-mail</label>
                <input id="contact-email" type="email" placeholder="joao@oficina.com.br" required v-model="form.email" />
              </div>
              <div class="form-group">
                <label for="contact-phone">WhatsApp</label>
                <input id="contact-phone" type="tel" placeholder="(11) 99999-9999" required v-model="form.phone" />
              </div>
            </div>

            <div class="form-group">
              <label for="contact-message">Como podemos ajudar?</label>
              <textarea id="contact-message" placeholder="Descreva a dúvida ou necessidade da sua oficina..." rows="4" v-model="form.message"></textarea>
            </div>

            <button type="submit" class="btn btn-primary btn-lg btn-submit" :disabled="submitted">
              {{ submitted ? 'Mensagem enviada! ✓' : 'Enviar Mensagem →' }}
            </button>

            <p class="form-note">Respondemos em até 2 horas úteis via WhatsApp ou e-mail.</p>
          </form>
        </div>

      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue';

const form = ref({ name: '', email: '', phone: '', message: '' });
const submitted = ref(false);

const handleSubmit = () => {
  // Simulate submission
  submitted.value = true;
  setTimeout(() => {
    form.value = { name: '', email: '', phone: '', message: '' };
    submitted.value = false;
  }, 4000);
};
</script>

<style scoped>
.contact {
  padding: var(--spacing-2xl) 0;
  background: white;
}

.contact-inner {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 5rem;
  align-items: start;
}

/* ── Info ── */
.contact-title {
  font-size: clamp(1.75rem, 3.5vw, 2.5rem);
  font-weight: 800;
  margin-bottom: 0.75rem;
  line-height: 1.2;
}

.contact-subtitle {
  font-size: 1.0625rem;
  color: var(--text-muted);
  line-height: 1.65;
  margin-bottom: 2rem;
}

.contact-channels {
  display: flex;
  flex-direction: column;
  gap: 0.875rem;
  margin-bottom: 1.5rem;
}

.channel-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.25rem;
  border: 1.5px solid var(--border);
  border-radius: var(--radius-lg);
  text-decoration: none;
  transition: all var(--transition-base);
  background: white;
}

.channel-card:hover {
  border-color: var(--primary-indigo);
  box-shadow: var(--shadow-md);
  transform: translateX(4px);
}

.channel-whatsapp:hover { border-color: #25d366; }
.channel-email:hover    { border-color: var(--primary-indigo); }

.channel-icon {
  font-size: 1.5rem;
  flex-shrink: 0;
}

.channel-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.channel-text strong {
  font-size: 0.9375rem;
  font-weight: 700;
  color: var(--midnight-navy);
}

.channel-text span {
  font-size: 0.875rem;
  color: var(--text-muted);
}

.channel-arrow {
  font-size: 1.125rem;
  color: var(--text-light);
  transition: transform 0.2s;
}

.channel-card:hover .channel-arrow {
  transform: translateX(4px);
  color: var(--primary-indigo);
}

.office-hours {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  color: var(--text-muted);
  font-weight: 500;
}

.hours-dot {
  width: 8px;
  height: 8px;
  background: #10b981;
  border-radius: 50%;
  box-shadow: 0 0 0 3px rgba(16,185,129,0.2);
  flex-shrink: 0;
}

/* ── Form ── */
.contact-form-wrap {
  background: var(--light-bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 2.5rem;
}

.contact-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-main);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.btn-submit {
  width: 100%;
  justify-content: center;
}

.btn-submit:disabled {
  background: #10b981;
  cursor: default;
}

.form-note {
  font-size: 0.8125rem;
  color: var(--text-light);
  text-align: center;
}

/* ── Responsive ── */
@media (max-width: 968px) {
  .contact-inner {
    grid-template-columns: 1fr;
    gap: 3rem;
  }
}

@media (max-width: 640px) {
  .form-row { grid-template-columns: 1fr; }
  .contact-form-wrap { padding: 1.5rem; }
}
</style>
