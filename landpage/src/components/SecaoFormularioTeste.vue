<template>
  <div class="trial-page">
    <!-- Stripe Mesh Gradient Engine -->
    <div class="stripe-mesh-bg">
      <div class="mesh-vibrant">
        <div class="mesh-cloud-inner"></div>
      </div>
    </div>

    <div class="container trial-wrapper">
      <!-- Spacer for focus -->
      <div style="height: 10px;"></div>

      <div class="trial-grid">
        <!-- Main Form Card -->
        <div class="stripe-card registration-card">
          <div class="card-glow"></div>
          <h2 class="form-title">Comece 7 dias grátis na <span class="text-gradient">neritechauto.</span></h2>
          <p class="form-subtitle">Configuramos sua oficina em segundos. Sem cartão, sem compromisso.</p>
          
          <form @submit.prevent="lidarRegistro" class="stripe-form">
            <div class="form-row">
              <div class="form-group">
                <label>Nome Completo</label>
                <div class="input-wrapper">
                  <span class="input-icon">👤</span>
                  <input type="text" v-model="formulario.name" placeholder="Ex: Alexandre Neri" required />
                </div>
              </div>
              
              <div class="form-group">
                <label>WhatsApp</label>
                <div class="input-wrapper">
                  <span class="input-icon">📱</span>
                  <input type="tel" v-model="formulario.whatsapp" placeholder="(11) 99999-9999" required />
                </div>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Nome da Oficina/Empresa</label>
                <div class="input-wrapper">
                  <span class="input-icon">🏢</span>
                  <input type="text" v-model="formulario.company_name" placeholder="Ex: NeriTech Auto Center" required />
                </div>
              </div>
              
              <div class="form-group">
                <label>CNPJ ou CPF</label>
                <div class="input-wrapper">
                  <span class="input-icon">🆔</span>
                  <input type="text" v-model="formulario.cnpjOuCpf" placeholder="00.000.000/0000-00" required />
                </div>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Segmento</label>
                <div class="input-wrapper">
                  <span class="input-icon">🔧</span>
                  <select v-model="formulario.segment" required>
                    <option value="oficina">Oficina Mecânica</option>
                    <option value="autocenter">Auto Center</option>
                    <option value="funilaria">Funilaria e Pintura</option>
                    <option value="moto">Oficina de Motos</option>
                  </select>
                </div>
              </div>

              <div class="form-group">
                <label>E-mail Profissional</label>
                <div class="input-wrapper">
                  <span class="input-icon">✉️</span>
                  <input type="email" v-model="formulario.email" placeholder="nome@oficina.com.br" required />
                </div>
              </div>
            </div>

            <div class="form-group">
              <label>Confirme seu E-mail</label>
              <div class="input-wrapper">
                <span class="input-icon">🔒</span>
                <input type="email" v-model="formulario.email_confirm" placeholder="Repita seu e-mail" required />
              </div>
            </div>

            <button type="submit" class="btn btn-primary btn-lg btn-block" :disabled="loading">
              {{ loading ? 'Processando...' : 'Obter Minha Chave de Teste' }}
              <span class="arrow" v-if="!loading">→</span>
            </button>
            <p class="form-footer">
              Ao clicar, você concorda com nossos termos de uso e política de privacidade.
            </p>
          </form>
        </div>

        <!-- Info Column -->
        <div class="info-content">
          <div class="info-card-mini stripe-card color-1">
            <div class="icon-abstract stripe-icon-1"></div>
            <h3>Acesso Imediato</h3>
            <p>Sua chave é gerada e enviada automaticamente para seu e-mail em segundos.</p>
          </div>

          <div class="info-card-mini stripe-card color-2">
            <div class="icon-abstract stripe-icon-3"></div>
            <h3>Suporte Dedicado</h3>
            <p>Nossa equipe está disponível de Seg. a Sex. das 8:30 às 18:00 para ajudar na sua primeira configuração.</p>
          </div>

          <div class="info-card-mini stripe-card color-3">
            <div class="icon-abstract stripe-icon-4"></div>
            <h3>Sem Compromisso</h3>
            <p>Teste todas as funcionalidades Master por 7 dias sem precisar de cartão de crédito.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useToast } from '../composables/useToast';

const toast = useToast();

const formulario = ref({
  name: '',
  whatsapp: '',
  company_name: '',
  cnpjOuCpf: '',
  email: '',
  email_confirm: '',
  segment: 'oficina'
});

const loading = ref(false);

const lidarRegistro = async () => {
  if (formulario.value.email !== formulario.value.email_confirm) {
    toast.error('Os e-mails não coincidem.');
    return;
  }
  
  loading.value = true;
  
  try {
    const response = await fetch(`${import.meta.env.VITE_URL_API_BACKEND}/api/public/trial/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nomeCompleto: formulario.value.name,
        email: formulario.value.email,
        telefone: formulario.value.whatsapp,
        nomeEmpresa: formulario.value.company_name,
        cnpjOuCpf: formulario.value.cnpjOuCpf,
        segmento: formulario.value.segment
      })
    });
    
    const result = await response.json();
    
    // O backend retorna um padrão ApiResponse { data, message, errors, ... }
    const success = response.ok && result.data?.success;
    const message = result.data?.message || result.message || (result.errors?.[0]) || 'Erro ao realizar cadastro.';

    if (success) {
      toast.success('Cadastro realizado com sucesso! Verifique seu e-mail.');
      formulario.value = {
        name: '', whatsapp: '', company_name: '', cnpjOuCpf: '', email: '', email_confirm: '', segment: 'oficina'
      };
    } else {
      toast.error(message);
    }
  } catch (error) {
    console.error('Erro na requisição:', error);
    toast.error('Ocorreu um erro de comunicação com o servidor. Tente novamente mais tarde.');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* ── Estilo Premium Stripe ── */
.trial-page {
  position: relative;
  min-height: 100vh;
  padding: 120px 0 80px;
  overflow: hidden;
  background-color: var(--slate-navy);
}

.trial-wrapper {
  position: relative;
  z-index: 10;
}

.trial-grid {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 4rem;
  align-items: flex-start;
}

.registration-card {
  padding: 3rem !important;
  background: white;
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-2xl);
  border: 1px solid rgba(255,255,255,0.1);
  position: relative;
}

.form-title {
  font-size: 2.25rem;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 0.5rem;
  letter-spacing: -0.03em;
}

.form-intro {
  font-size: 1.0625rem;
  color: var(--text-muted);
  margin-bottom: 2.5rem;
  line-height: 1.5;
}

.stripe-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.25rem;
}

.form-group label {
  display: block;
  font-size: 0.8125rem;
  font-weight: 700;
  color: var(--text-main);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 8px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 14px;
  font-size: 1rem;
  opacity: 0.7;
}

.input-wrapper input,
.input-wrapper select {
  width: 100%;
  padding: 12px 12px 12px 42px;
  background: #f8fafc;
  border: 1.5px solid var(--border);
  border-radius: 8px;
  font-family: var(--font-body);
  font-size: 0.9375rem;
  transition: all 0.2s;
  color: var(--midnight-navy);
}

.input-wrapper input:focus,
.input-wrapper select:focus {
  background: white;
  border-color: var(--primary-indigo);
  box-shadow: 0 0 0 4px var(--primary-indigo-light);
  outline: none;
}

.btn-block {
  margin-top: 1rem;
  padding: 1rem;
  font-size: 1rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.form-footer {
  text-align: center;
  font-size: 0.75rem;
  color: var(--text-light);
  margin-top: 1rem;
}

/* Info Cards */
.info-content {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.info-card-mini {
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1);
  backdrop-filter: blur(10px);
  padding: 1.5rem !important;
  border-radius: var(--radius-lg);
  color: white;
}

.info-card-mini h3 {
  font-size: 1.125rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  color: white;
}

.info-card-mini p {
  font-size: 0.875rem;
  color: rgba(255,255,255,0.6);
  line-height: 1.6;
}

.icon-abstract {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  margin-bottom: 1rem;
  background: var(--primary-indigo);
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 968px) {
  .trial-grid { grid-template-columns: 1fr; gap: 3rem; }
  .registration-card { padding: 2rem !important; }
}

@media (max-width: 640px) {
  .form-row { grid-template-columns: 1fr; }
  .form-title { font-size: 1.75rem; }
}
</style>
