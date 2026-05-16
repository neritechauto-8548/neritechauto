<template>
  <div class="h-screen flex font-sans selection:bg-stripe-indigo/20 overflow-hidden">
    
    <!-- Left Column: Branding & Premium Visuals -->
    <div class="hidden lg:flex lg:w-[60%] relative bg-midnight-navy overflow-hidden border-r border-white/5">
      <!-- High-Quality Local Workshop Image -->
      <img 
        src="/bg-oficina.png" 
        alt="Premium Auto Repair" 
        class="absolute inset-0 w-full h-full object-cover opacity-50 animate-ambient-pan"
        crossorigin="anonymous"
      />
      
      <!-- Sophisticated Gradient Overlay -->
      <div class="absolute inset-0 bg-gradient-to-br from-midnight-navy/90 via-midnight-navy/40 to-transparent"></div>
      
      <div class="relative z-10 flex flex-col justify-between w-full h-full p-16 lg:p-20">
        <!-- Logo & Brand -->
        <div class="flex items-center gap-4 group cursor-default">
          <div class="w-12 h-12 bg-stripe-indigo rounded-2xl flex items-center justify-center text-white shadow-2xl shadow-stripe-indigo/30 transition-transform group-hover:scale-110 duration-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg>
          </div>
          <div>
            <span class="text-2xl font-black text-white tracking-widest uppercase block leading-none">NeriTech</span>
          </div>
        </div>

        <!-- Hero Content with Professional Polish -->
        <div class="max-w-md">
          <div class="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-stripe-indigo/20 backdrop-blur-xl border border-stripe-indigo/30 text-white text-[11px] font-bold uppercase tracking-[0.2em] mb-8 shadow-xl">
            <span class="w-2 h-2 rounded-full bg-accent-emerald animate-pulse"></span>
            Acompanhamento em Tempo Real
          </div>
          
          <h2 class="text-6xl font-extrabold text-white mb-8 leading-[1.05] tracking-tight-stripe drop-shadow-2xl">
            Sua oficina,<br/><span class="text-transparent bg-clip-text bg-gradient-to-r from-stripe-indigo to-accent-blue">Conectada.</span>
          </h2>

          <!-- Feature List (Trust Building) -->
          <ul class="space-y-6">
            <li class="flex items-start gap-4 animate-slide-in" style="animation-delay: 0.1s">
              <div class="mt-1 w-5 h-5 rounded-full bg-accent-emerald/20 flex items-center justify-center shrink-0">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3 text-accent-emerald" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
              </div>
              <p class="text-white/80 font-medium text-[15px] leading-relaxed">Consulte o status da sua O.S. sem burocracia usando apenas seu CPF.</p>
            </li>
            <li class="flex items-start gap-4 animate-slide-in" style="animation-delay: 0.2s">
              <div class="mt-1 w-5 h-5 rounded-full bg-accent-blue/20 flex items-center justify-center shrink-0">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3 text-accent-blue" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
              </div>
              <p class="text-white/80 font-medium text-[15px] leading-relaxed">Aprove ou rejeite orçamentos técnicos com um clique.</p>
            </li>
          </ul>
        </div>

        <!-- Progress/Stats footer for professional look -->
        <div class="flex items-center gap-12 pt-8 border-t border-white/10">
          <div>
            <p class="text-white font-black text-2xl leading-none">100%</p>
            <p class="text-white/40 text-[10px] font-bold uppercase tracking-widest mt-1">Transparência</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Right Column: Consultation UI -->
    <div class="w-full lg:w-[40%] flex items-center justify-center bg-bg-page relative overflow-y-auto">
      <div class="w-full max-w-[480px] px-8 py-10 relative z-10 animate-fade-in">
        <div class="mb-10">
          <h1 class="text-4xl font-extrabold text-midnight-navy tracking-tight-stripe mb-2">Consultar O.S.</h1>
          <p class="text-[16px] text-text-muted font-medium leading-relaxed">
            Localize as informações técnicas do seu veículo.
          </p>
        </div>

        <div class="bg-white rounded-3xl p-6 md:p-8 shadow-premium border border-border-default/50 relative overflow-hidden group/form transition-all duration-500 hover:shadow-2xl">
          <form @submit.prevent="handleSubmit" class="space-y-6">
            <div class="space-y-5">
              <div class="space-y-1.5">
                <label class="block text-[11px] font-bold text-text-main uppercase tracking-widest" for="cpf">CPF do Titular</label>
                <input
                  id="cpf"
                  v-model="form.cpf"
                  type="text"
                  class="w-full px-5 py-4 bg-gray-50/50 border border-border-default rounded-2xl text-midnight-navy font-bold text-[15px] focus:bg-white focus:outline-none focus:ring-4 focus:ring-stripe-indigo/5 focus:border-stripe-indigo transition-all duration-300 placeholder-text-light"
                  placeholder="000.000.000-00"
                  maxlength="14"
                  @input="formatCPF"
                />
                <p v-if="errors.cpf" class="text-accent-red text-[11px] mt-1 font-bold">{{ errors.cpf }}</p>
              </div>

              <div class="space-y-1.5">
                <label class="block text-[11px] font-bold text-text-main uppercase tracking-widest" for="numeroOs">Número da Ordem</label>
                <input
                  id="numeroOs"
                  v-model="form.numeroOs"
                  type="text"
                  class="w-full px-5 py-4 bg-gray-50/50 border border-border-default rounded-2xl text-midnight-navy font-bold text-[15px] focus:bg-white focus:outline-none focus:ring-4 focus:ring-stripe-indigo/5 focus:border-stripe-indigo transition-all duration-300 placeholder-text-light"
                  placeholder="Ex: 10250"
                />
                <p v-if="errors.numeroOs" class="text-accent-red text-[11px] mt-1 font-bold">{{ errors.numeroOs }}</p>
              </div>
            </div>

            <button type="submit" class="w-full py-4 px-6 bg-stripe-indigo hover:bg-[#524bf2] text-white text-[15px] font-black rounded-2xl shadow-xl shadow-stripe-indigo/30 hover:shadow-2xl hover:-translate-y-1 transition-all duration-300" :disabled="loading">
              <span v-if="!loading">Localizar Ordem de Serviço</span>
              <span v-else>Buscando...</span>
            </button>

            <!-- Error Message -->
            <transition name="fade">
              <div v-if="errorMessage" class="p-4 bg-accent-red/10 border border-accent-red/20 rounded-2xl flex items-center gap-3">
                <div class="w-8 h-8 rounded-full bg-accent-red/20 flex items-center justify-center text-accent-red shrink-0">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
                </div>
                <p class="text-[13px] font-bold text-accent-red leading-tight">
                  {{ errorMessage }}
                </p>
              </div>
            </transition>
          </form>
        </div>

        <p class="mt-8 text-center text-[14px] font-semibold text-text-muted">
          <span v-if="!isAuthenticated">Área técnica?</span>
          <span v-else>Bem-vindo novamente!</span>
          
          <router-link :to="isAuthenticated ? '/historico' : '/login'" class="text-midnight-navy font-black hover:text-stripe-indigo transition-colors pb-0.5 border-b-2 border-transparent hover:border-midnight-navy ml-1">
            {{ isAuthenticated ? 'Acessar Meu Histórico' : 'Entrar' }}
          </router-link>
        </p>

        <!-- Resultado da consulta -->
        <transition name="slide-up">
          <div v-if="resultado" class="mt-8 space-y-4">
            <OsCard :ordem="resultado" />
            
            <router-link 
              :to="`/os/${resultado.id}`"
              class="flex items-center justify-between w-full px-6 py-4 bg-white rounded-2xl border border-stripe-indigo/20 shadow-stripe-sm hover:shadow-premium hover:-translate-y-1 transition-all duration-300 group"
            >
              <div class="flex items-center gap-4">
                <div class="w-10 h-10 rounded-xl bg-stripe-indigo/10 flex items-center justify-center text-stripe-indigo group-hover:bg-stripe-indigo group-hover:text-white transition-colors">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
                </div>
                <div class="text-left">
                  <p class="text-[14px] font-bold text-midnight-navy">Visualizar Laudo Técnico</p>
                  <p class="text-[12px] font-medium text-text-muted">Acesse o checklist completo e fotos</p>
                </div>
              </div>
              <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-text-light group-hover:text-stripe-indigo transition-colors" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"/></svg>
            </router-link>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import osService, { auth } from '@/api/osService';
import OsCard from '@/components/OsCard.vue';

const isAuthenticated = computed(() => auth.isAuthenticated());
const form = ref({
  cpf: '',
  numeroOs: '',
});

const errors = ref({
  cpf: '',
  numeroOs: '',
});

const loading = ref(false);
const errorMessage = ref('');
const resultado = ref(null);

const formatCPF = (e) => {
  let value = e.target.value.replace(/\D/g, '');
  
  if (value.length <= 11) {
    value = value.replace(/(\d{3})(\d)/, '$1.$2');
    value = value.replace(/(\d{3})(\d)/, '$1.$2');
    value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
  }
  
  form.value.cpf = value;
};

const validateCPF = (cpf) => {
  const cleanCPF = cpf.replace(/\D/g, '');
  
  if (cleanCPF.length !== 11) {
    return false;
  }
  
  // Validação básica de CPF
  if (/^(\d)\1{10}$/.test(cleanCPF)) {
    return false;
  }
  
  return true;
};

const validate = () => {
  errors.value = {
    cpf: '',
    numeroOs: '',
  };
  
  let isValid = true;
  
  if (!form.value.cpf) {
    errors.value.cpf = 'CPF é obrigatório';
    isValid = false;
  } else if (!validateCPF(form.value.cpf)) {
    errors.value.cpf = 'CPF inválido';
    isValid = false;
  }
  
  if (!form.value.numeroOs) {
    errors.value.numeroOs = 'Número da OS é obrigatório';
    isValid = false;
  }
  
  return isValid;
};

const handleSubmit = async () => {
  errorMessage.value = '';
  resultado.value = null;
  
  if (!validate()) {
    return;
  }
  
  loading.value = true;
  
  try {
    const cpfLimpo = form.value.cpf.replace(/\D/g, '');
    const response = await osService.consultarOS(cpfLimpo, form.value.numeroOs);
    // O backend retorna o objeto dentro de uma propriedade 'data'
    resultado.value = response.data.data || response.data;
  } catch (error) {
    console.error('Erro ao consultar OS:', error);
    
    if (error.response?.status === 404) {
      errorMessage.value = 'Ordem de serviço não encontrada. Verifique os dados e tente novamente.';
    } else {
      errorMessage.value = 'Erro ao consultar ordem de serviço. Tente novamente mais tarde.';
    }
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  if (window.lucide) {
    window.lucide.createIcons();
  }
});
</script>

<style scoped>
.consulta-rapida {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  padding: 3rem 0;
}

.hero {
  padding: 2rem 0;
}

.hero-content {
  text-align: center;
  margin-bottom: 3rem;
}

.hero-content h1 {
  color: var(--white);
  font-size: 2.5rem;
  margin-bottom: 1rem;
  animation-delay: 0.1s;
}

.hero-subtitle {
  color: rgba(255, 255, 255, 0.9);
  font-size: 1.25rem;
  animation-delay: 0.2s;
}

.consulta-form {
  max-width: 500px;
  margin: 0 auto;
  animation-delay: 0.3s;
}

.form-footer {
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--gray-200);
  text-align: center;
}

.resultado {
  margin-top: 2rem;
  animation-delay: 0.4s;
}

.resultado h3 {
  color: var(--white);
  text-align: center;
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2rem;
  }
  
  .hero-subtitle {
    font-size: 1rem;
  }
  
  .consulta-rapida {
    padding: 2rem 0;
  }
}
</style>
