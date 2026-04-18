<template>
  <div class="h-screen flex font-sans selection:bg-stripe-indigo/20 overflow-hidden">
    
    <!-- Left Column: Branding & Premium Visuals -->
    <div class="hidden lg:flex lg:w-[60%] relative bg-midnight-navy overflow-hidden border-r border-white/5">
      <!-- High-Quality Local Workshop Image -->
      <img 
        src="/bg-oficina.png" 
        alt="Premium Auto Repair" 
        class="absolute inset-0 w-full h-full object-cover opacity-40 animate-ambient-pan"
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
          <div class="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/5 backdrop-blur-xl border border-white/10 text-white/90 text-[11px] font-bold uppercase tracking-[0.2em] mb-8">
            <span class="w-2 h-2 rounded-full bg-stripe-indigo shadow-[0_0_8px_rgba(99,91,255,1)]"></span>
            Gestão Automotiva Inteligente
          </div>
          
          <h2 class="text-6xl font-extrabold text-white mb-8 leading-[1.05] tracking-tight-stripe drop-shadow-2xl">
            Sua oficina,<br/><span class="text-transparent bg-clip-text bg-gradient-to-r from-stripe-indigo to-accent-blue">Conectada.</span>
          </h2>

          <p class="text-xl text-white/70 font-medium leading-relaxed mb-10">
            Acompanhe o status do seu veículo em tempo real e aprove orçamentos com total transparência e segurança.
          </p>
        </div>

        <!-- Progress/Stats footer for professional look -->
        <div class="flex items-center gap-12 pt-8 border-t border-white/10">
          <div>
            <p class="text-white font-black text-2xl leading-none">100%</p>
            <p class="text-white/40 text-[10px] font-bold uppercase tracking-widest mt-1">Transparência</p>
          </div>
          <div>
            <p class="text-white font-black text-2xl leading-none">Aprovado</p>
            <p class="text-white/40 text-[10px] font-bold uppercase tracking-widest mt-1">Garantia Técnica</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Right Column: Login UI -->
    <div class="w-full lg:w-[40%] flex items-center justify-center bg-bg-page relative overflow-y-auto">
      <!-- Subtle Ambient Glow -->
      <div class="absolute top-0 right-0 w-full h-full overflow-hidden pointer-events-none">
        <div class="absolute -top-[5%] -right-[5%] w-[40%] h-[40%] bg-stripe-indigo/10 blur-[120px] rounded-full text-white">.</div>
        <div class="absolute bottom-[10%] left-[10%] w-[30%] h-[30%] bg-accent-blue/5 blur-[100px] rounded-full text-white">.</div>
      </div>

      <div class="w-full max-w-[420px] px-8 py-10 relative z-10 animate-fade-in">
        <div class="mb-10">
          <h1 class="text-4xl font-extrabold text-midnight-navy tracking-tight-stripe mb-2">Login.</h1>
          <p class="text-[16px] text-text-muted font-medium">Acesse o portal restrito NeriTech.</p>
        </div>

        <!-- Modern High-Depth Form Container -->
        <div class="bg-white rounded-3xl p-6 md:p-8 shadow-premium border border-border-default/50 relative overflow-hidden group/form transition-all duration-500 hover:shadow-2xl">
          <form @submit.prevent="handleSubmit" class="space-y-6">
            <div class="space-y-5">
              <div class="space-y-1.5">
                <label class="block text-[11px] font-bold text-text-main uppercase tracking-widest" for="email">E-mail Corporativo</label>
                <input
                  id="email"
                  v-model="form.email"
                  type="email"
                  required
                  class="w-full px-5 py-4 bg-gray-50/50 border border-border-default rounded-2xl text-midnight-navy font-bold text-[15px] focus:bg-white focus:outline-none focus:ring-4 focus:ring-stripe-indigo/5 focus:border-stripe-indigo transition-all duration-300 placeholder-text-light"
                  placeholder="seu@email.com.br"
                />
              </div>

              <div class="space-y-1.5">
                <div class="flex items-center justify-between mb-0.5">
                  <label class="block text-[11px] font-bold text-text-main uppercase tracking-widest" for="password">Senha de Acesso</label>
                  <a href="#" class="text-[11px] font-bold text-stripe-indigo hover:text-midnight-navy transition-colors">Recuperar</a>
                </div>
                <input
                  id="password"
                  v-model="form.senha"
                  type="password"
                  required
                  class="w-full px-5 py-4 bg-gray-50/50 border border-border-default rounded-2xl text-midnight-navy font-bold text-[15px] focus:bg-white focus:outline-none focus:ring-4 focus:ring-stripe-indigo/5 focus:border-stripe-indigo transition-all duration-300 placeholder-text-light"
                  placeholder="••••••••"
                />
              </div>
            </div>

            <transition name="fade">
              <div v-if="errorMessage" class="bg-red-50 border-l-4 border-accent-red p-4 rounded-r-2xl flex items-start gap-4 animate-shake">
                <span class="text-accent-red text-[13px] font-bold leading-tight">{{ errorMessage }}</span>
              </div>
            </transition>

            <button type="submit" class="w-full py-4 px-6 bg-midnight-navy hover:bg-gray-950 text-white text-[15px] font-black rounded-2xl shadow-xl shadow-midnight-navy/30 hover:shadow-2xl hover:-translate-y-1 transition-all duration-300" :disabled="loading">
              <span v-if="!loading">Entrar no Portal</span>
              <span v-else>Autenticando...</span>
            </button>
          </form>
        </div>

        <p class="mt-8 text-center text-[14px] font-semibold text-text-muted">
          Área do Cliente? 
          <router-link to="/consulta" class="text-midnight-navy font-black hover:text-stripe-indigo transition-colors pb-0.5 border-b-2 border-transparent hover:border-midnight-navy ml-1">
            Consultar O.S.
          </router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import osService, { auth } from '@/api/osService';

const router = useRouter();
const route = useRoute();

const form = ref({
  email: '',
  senha: '',
});

const errors = ref({
  email: '',
  senha: '',
});

const loading = ref(false);
const errorMessage = ref('');

const validate = () => {
  errors.value = {
    email: '',
    senha: '',
  };
  
  let isValid = true;
  
  if (!form.value.email) {
    errors.value.email = 'Email ou CPF é obrigatório';
    isValid = false;
  }
  
  if (!form.value.senha) {
    errors.value.senha = 'Senha é obrigatória';
    isValid = false;
  }
  
  return isValid;
};

const handleSubmit = async () => {
  errorMessage.value = '';
  
  if (!validate()) {
    return;
  }
  
  loading.value = true;
  
  try {
    const response = await osService.login(form.value.email, form.value.senha);
    
    // Salvar token e dados do usuário
    auth.setToken(response.data.token, response.data.user);
    
    // Redirecionar para a página solicitada ou para o histórico
    const redirect = route.query.redirect || '/historico';
    router.push(redirect);
  } catch (error) {
    console.error('Erro ao fazer login:', error);
    
    if (error.response?.status === 401) {
      errorMessage.value = 'Email/CPF ou senha incorretos. Tente novamente.';
    } else {
      errorMessage.value = 'Erro ao fazer login. Tente novamente mais tarde.';
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
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem 0;
}

.login-card {
  max-width: 450px;
  width: 100%;
  margin: 0 auto;
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-header h1 {
  color: var(--gray-900);
  margin-bottom: 0.5rem;
}

.login-header p {
  color: var(--gray-600);
  font-size: 0.875rem;
}

.login-footer {
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--gray-200);
  text-align: center;
}

@media (max-width: 768px) {
  .login-page {
    padding: 1rem;
  }
}
</style>
