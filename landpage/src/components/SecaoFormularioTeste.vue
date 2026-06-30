<template>
  <div class="trial-page">
    <!-- Blue Gradient Hero -->
    <section class="page-hero trial-hero">
      <div class="container">
        <span class="section-label">Teste Grátis</span>
        <h1 class="page-hero__title">Comece 180 dias grátis</h1>
        <p class="page-hero__subtitle">
          Configuramos sua oficina em segundos. Sem cartão de crédito
        </p>
      </div>
    </section>

    <!-- Soft Stripe-style Mesh Gradient Background -->
    <div class="stripe-mesh-bg"></div>

    <div class="container trial-wrapper">
      <div class="trial-grid">
        <!-- Main Form Card (Light Theme) -->
        <div class="stripe-card registration-card">
          <div class="card-glow"></div>
          <h2 class="form-card-title">Dados de Cadastro</h2>
          <p class="form-card-subtitle">Preencha as informações para criar sua conta de teste.</p>
          
          <form @submit.prevent="lidarRegistro" class="stripe-form">
            <div class="form-row">
              <div class="form-group">
                <label>Nome Completo</label>
                <div class="input-wrapper">
                  <span class="input-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                  </span>
                  <InputText type="text" v-model="formulario.name" placeholder="Ex: Alexandre Neri" required />
                </div>
              </div>
              
              <div class="form-group">
                <label>WhatsApp</label>
                <div class="input-wrapper">
                   <span class="input-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="14" height="20" x="5" y="2" rx="2" ry="2"/><path d="M12 18h.01"/></svg>
                  </span>
                  <InputText 
                    type="tel" 
                    :value="formulario.whatsapp" 
                    @input="handlePhoneInput"
                    placeholder="(11) 99999-9999" 
                    maxlength="15"
                    required 
                  />
                </div>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Nome da Oficina/Empresa</label>
                <div class="input-wrapper">
                   <span class="input-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="16" height="20" x="4" y="2" rx="2" ry="2"/><path d="M9 22v-4h6v4"/><path d="M8 6h.01"/><path d="M16 6h.01"/><path d="M8 10h.01"/><path d="M16 10h.01"/><path d="M8 14h.01"/><path d="M16 14h.01"/></svg>
                  </span>
                  <InputText type="text" v-model="formulario.company_name" placeholder="Ex: NeriTech Auto Center" required />
                </div>
              </div>
              
              <div class="form-group">
                <label>CNPJ ou CPF</label>
                <div class="input-wrapper">
                   <span class="input-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="14" x="2" y="5" rx="2"/><line x1="2" x2="22" y1="10" y2="10"/><line x1="7" x2="7" y1="15" y2="15"/><line x1="11" x2="11" y1="15" y2="15"/></svg>
                  </span>
                  <InputText 
                    type="text" 
                    :value="formulario.cnpjOuCpf" 
                    @input="handleCpfCnpjInput"
                    placeholder="00.000.000/0000-00" 
                    maxlength="18"
                    required 
                  />
                </div>
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Segmento</label>
                <div class="input-wrapper">
                   <span class="input-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a2 2 0 0 1-2.83-2.83l-3.94 3.6Z"/><path d="m14.7 6.3 5.4 5.4"/><path d="M21 21v-3.76a2 2 0 0 0-.59-1.41L15.35 11.1c-1.41-1.4-3.7-1.4-5.1 0-1.4 1.41-1.4 3.7 0 5.1l4.74 4.75a2 2 0 0 0 1.41.59H21Z"/><path d="M21 21H3c-1.1 0-2-.9-2-2v-4c0-1.38 1.12-2.5 2.5-2.5 1.1 0 2.01.71 2.36 1.74l3.14-.3"/></svg>
                  </span>
                  <Select 
                    v-model="formulario.segment" 
                    :options="optionsSegmento" 
                    optionLabel="label" 
                    optionValue="value" 
                    placeholder="Selecione o segmento"
                    required 
                    class="prime-select"
                  />
                </div>
              </div>

              <div class="form-group">
                <label>E-mail Profissional</label>
                <div class="input-wrapper">
                   <span class="input-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
                  </span>
                  <InputText type="email" v-model="formulario.email" placeholder="nome@oficina.com.br" required />
                </div>
              </div>
            </div>

            <div class="form-group">
              <label>Confirme seu E-mail</label>
              <div class="input-wrapper">
                   <span class="input-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10"/><path d="m9 12 2 2 4-4"/></svg>
                  </span>
                <InputText type="email" v-model="formulario.email_confirm" placeholder="Repita seu e-mail" required />
              </div>
            </div>

            <Button type="submit" class="btn-block" :disabled="loading" :label="loading ? 'Processando...' : 'Obter Minha Chave de Teste'" icon="pi pi-key" iconPos="right" />
            <p class="form-footer">
              Ao clicar, você concorda com nossos termos de uso e política de privacidade.
            </p>
          </form>
        </div>

        <!-- Info Column (Light Theme) -->
        <div class="info-content">
          <div class="info-card-mini stripe-card">
            <div class="icon-abstract stripe-icon-1">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="m13 2-2 10h8L11 22l2-10H5Z"/></svg>
            </div>
            <h3>Acesso Imediato</h3>
            <p>Sua chave de acesso de avaliação é gerada e enviada automaticamente para seu e-mail em segundos.</p>
          </div>

          <div class="info-card-mini stripe-card">
            <div class="icon-abstract stripe-icon-2">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M3 18v-2a1 1 0 0 1 1-1h5l3 3 3-3h5a1 1 0 0 1 1 1v2"/><path d="M15 13a3 3 0 1 0-6 0"/><rect width="20" height="12" x="2" y="4" rx="2"/></svg>
            </div>
            <h3>Suporte Dedicado</h3>
            <p>Nossa equipe técnica atende diretamente no WhatsApp para ajudar você a cadastrar seus primeiros veículos no pátio.</p>
          </div>

          <div class="info-card-mini stripe-card">
            <div class="icon-abstract stripe-icon-3">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10"/></svg>
            </div>
            <h3>Sem Compromisso</h3>
            <p>Teste todas as funcionalidades Master por 180 dias sem precisar de cartão de crédito.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useToast } from '../composables/useToast';
import InputText from 'primevue/inputtext';
import Select from 'primevue/select';
import Button from 'primevue/button';

const toast = useToast();

const optionsSegmento = ref([
  { label: 'Oficina Mecânica', value: 'oficina' },
  { label: 'Auto Center', value: 'autocenter' },
  { label: 'Funilaria e Pintura', value: 'funilaria' }
]);

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

const handlePhoneInput = (e) => {
  let v = e.target.value.replace(/\D/g, "");
  if (v.length > 11) v = v.substring(0, 11);
  
  let x = v.match(/(\d{0,2})(\d{0,5})(\d{0,4})/);
  if (v.length <= 10) {
    x = v.match(/(\d{0,2})(\d{0,4})(\d{0,4})/);
  }
  
  v = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
  formulario.value.whatsapp = v;
};

const handleCpfCnpjInput = (e) => {
  let v = e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, "");
  if (v.length > 14) v = v.substring(0, 14);
  
  const temLetras = /[A-Z]/.test(v);
  
  if (v.length <= 11 && !temLetras) {
    let x = v.match(/([A-Z0-9]{0,3})([A-Z0-9]{0,3})([A-Z0-9]{0,3})([A-Z0-9]{0,2})/);
    v = !x[2] ? x[1] : x[1] + '.' + x[2] + (x[3] ? '.' + x[3] : '') + (x[4] ? '-' + x[4] : '');
  } else {
    let x = v.match(/([A-Z0-9]{0,2})([A-Z0-9]{0,3})([A-Z0-9]{0,3})([A-Z0-9]{0,4})([A-Z0-9]{0,2})/);
    v = !x[2] ? x[1] : x[1] + '.' + x[2] + (x[3] ? '.' + x[3] : '') + (x[4] ? '/' + x[4] : '') + (x[5] ? '-' + x[5] : '');
  }
  
  formulario.value.cnpjOuCpf = v;
};

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
.trial-page {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  background-color: var(--surface-base);
  padding-bottom: 80px;
}

.trial-hero {
  padding-bottom: 140px;
}

.stripe-mesh-bg {
  position: absolute;
  top: 360px;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
  background: 
    radial-gradient(circle at 10% 20%, rgba(99, 91, 255, 0.04) 0%, transparent 45%),
    radial-gradient(circle at 90% 10%, rgba(0, 216, 255, 0.04) 0%, transparent 40%),
    var(--surface-base);
}

.trial-wrapper {
  position: relative;
  z-index: 10;
  margin-top: -90px;
}

.trial-grid {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 4rem;
  align-items: flex-start;
}

.registration-card {
  padding: 3rem !important;
  background: #ffffff;
  border-radius: var(--radius-xl);
  box-shadow: 0 40px 100px -20px rgba(10, 37, 64, 0.08);
  border: 1px solid #edf2f7;
  position: relative;
}

.form-card-title {
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--midnight-navy);
  margin-bottom: 0.35rem;
  letter-spacing: -0.02em;
}

.form-card-subtitle {
  font-size: 0.875rem;
  color: var(--text-muted);
  margin-bottom: 2rem;
  line-height: 1.45;
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
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  pointer-events: none;
}

.input-wrapper :deep(input),
.prime-select {
  width: 100% !important;
  padding: 12px 12px 12px 42px !important;
  background: var(--surface-base) !important;
  border: 1.5px solid var(--border) !important;
  border-radius: 8px !important;
  font-family: var(--font-body) !important;
  font-size: 0.9375rem !important;
  transition: all 0.2s !important;
  color: var(--midnight-navy) !important;
}

.input-wrapper :deep(input:focus),
.prime-select:focus-within {
  background: var(--white) !important;
  border-color: var(--primary) !important;
  box-shadow: 0 0 0 4px var(--primary-shadow) !important;
  outline: none !important;
}

.prime-select {
  display: inline-flex !important;
  align-items: center !important;
  height: 48px !important;
}

:deep(.p-select-label) {
  padding: 0 !important;
  color: var(--midnight-navy) !important;
  font-weight: 500 !important;
}

:deep(.p-select-dropdown) {
  margin-right: -12px !important;
}

.btn-block {
  margin-top: 1rem;
  padding: 1rem !important;
  font-size: 1rem !important;
  font-weight: 700 !important;
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
  background: var(--white);
  border: 1px solid var(--border);
  padding: 1.5rem !important;
  border-radius: var(--radius-lg);
  color: var(--midnight-navy);
  box-shadow: 0 4px 12px rgba(10, 37, 64, 0.02);
}

.info-card-mini h3 {
  font-size: 1.125rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  color: var(--midnight-navy);
}

.info-card-mini p {
  font-size: 0.875rem;
  color: var(--text-main);
  line-height: 1.6;
}

.icon-abstract {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  margin-bottom: 1.25rem;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(10, 37, 64, 0.04);
}

.stripe-icon-1 { background: rgba(99, 91, 255, 0.08); color: var(--primary-indigo); }
.stripe-icon-2 { background: rgba(0, 200, 83, 0.08); color: #00c853; }
.stripe-icon-3 { background: rgba(0, 216, 255, 0.08); color: #00d8ff; }

@media (max-width: 968px) {
  .trial-grid { grid-template-columns: 1fr; gap: 3rem; }
  .registration-card { padding: 2rem !important; }
}

@media (max-width: 640px) {
  .form-row { grid-template-columns: 1fr; }
  .form-card-title { font-size: 1.75rem; }
}

/* ── Dark Mode trial specific styles ── */
:global(.p-dark) .stripe-mesh-bg {
  background: 
    radial-gradient(circle at 10% 20%, rgba(99, 91, 255, 0.1) 0%, transparent 45%),
    radial-gradient(circle at 90% 10%, rgba(0, 216, 255, 0.08) 0%, transparent 40%),
    var(--p-surface-950) !important;
}

:global(.p-dark) .registration-card {
  border-color: var(--p-surface-800) !important;
  box-shadow: 0 40px 100px -20px rgba(0, 0, 0, 0.3) !important;
}

:global(.p-dark) .info-card-mini {
  border-color: var(--p-surface-800) !important;
}

:global(.p-dark) .stripe-icon-1 { background: rgba(99, 91, 255, 0.2); }
:global(.p-dark) .stripe-icon-2 { background: rgba(0, 200, 83, 0.2); }
:global(.p-dark) .stripe-icon-3 { background: rgba(0, 216, 255, 0.2); }
</style>
