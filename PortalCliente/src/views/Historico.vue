<template>
  <div class="historico-page bg-light-bg min-h-screen font-sans">
    <!-- Sub-header / Navigation (Stripe Dashboard Style) -->
    <header class="bg-white border-b border-border-default sticky top-0 z-30">
      <div class="max-w-[1400px] mx-auto px-4 md:px-8">
        <div class="flex items-center justify-between h-14 md:h-16">
          <div class="flex items-center gap-10">
            <div class="flex items-center gap-3">
              <div class="w-7 h-7 bg-stripe-indigo rounded-lg flex items-center justify-center text-white shadow-sm">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg>
              </div>
              <span class="text-[14px] font-bold text-midnight-navy tracking-tight uppercase">NeriTech Portal</span>
            </div>
            
            <nav class="hidden md:flex items-center gap-8">
              <router-link to="/historico" class="text-[13px] font-bold text-stripe-indigo border-b-2 border-stripe-indigo h-14 md:h-16 flex items-center transition-all">Histórico</router-link>
              <router-link to="/consulta" class="text-[13px] font-semibold text-text-muted hover:text-midnight-navy transition-colors h-14 md:h-16 flex items-center">Consulta Pública</router-link>
            </nav>
          </div>
          
          <div class="flex items-center gap-3">
            <button @click="handleLogout" class="text-[13px] font-bold text-text-muted hover:text-midnight-navy transition-colors px-3 py-1.5 rounded-md hover:bg-gray-50">Sair</button>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-6xl mx-auto px-4 py-12 md:py-20">
      <div class="mb-12 animate-fade-in max-w-2xl">
        <h1 class="text-4xl md:text-5xl font-extrabold text-midnight-navy tracking-tight leading-tight mb-4">Seu histórico de manutenção</h1>
        <p class="text-[17px] text-text-muted font-medium leading-relaxed">
          Acompanhe o status técnico, orçamentos e aprovações de todos os seus veículos em um só lugar.
        </p>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="flex flex-col items-center justify-center py-32 animate-fade-in">
        <svg class="animate-spin h-8 w-8 text-stripe-indigo mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="3"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        <p class="text-[13px] text-text-muted font-medium">Sincronizando registros...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="errorMessage" class="bg-white rounded-xl shadow-premium border border-border-default p-12 text-center animate-fade-in max-w-xl mx-auto">
        <div class="w-14 h-14 bg-red-50 text-accent-red rounded-full flex items-center justify-center mx-auto mb-6">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
        </div>
        <h3 class="text-xl font-bold text-midnight-navy mb-2 tracking-tight">Falha na conexão</h3>
        <p class="text-[14px] text-text-muted font-medium mb-8 leading-relaxed">{{ errorMessage }}</p>
        <button @click="loadOrdens" class="btn-stripe-primary px-8">Tentar novamente</button>
      </div>

      <!-- Empty State -->
      <div v-else-if="ordens.length === 0" class="bg-white rounded-xl shadow-premium border border-border-default p-20 text-center animate-fade-in">
        <div class="w-16 h-16 bg-light-bg text-text-light rounded-2xl flex items-center justify-center mx-auto mb-8">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-8 h-8" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="22 12 16 12 14 15 10 15 8 12 2 12"/><path d="M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"/></svg>
        </div>
        <h3 class="text-2xl font-bold text-midnight-navy mb-3 tracking-tight">Nenhuma O.S. encontrada</h3>
        <p class="text-[15px] text-text-muted font-medium max-w-sm mx-auto leading-relaxed">Seu histórico de atendimentos aparecerá aqui assim que for iniciado na oficina.</p>
      </div>

      <!-- Orders List / Stripe Table Style -->
      <div v-else class="animate-fade-in">
        <div class="bg-white rounded-xl border border-border-default shadow-stripe-sm overflow-hidden overflow-x-auto">
          <div class="min-w-[800px]">
            <div class="grid grid-cols-12 gap-4 px-8 py-4 bg-gray-50/50 border-b border-border-default text-[11px] font-bold text-text-muted uppercase tracking-widest">
              <div class="col-span-2">Número</div>
              <div class="col-span-4">Veículo / Placa</div>
              <div class="col-span-3">Status Atual</div>
              <div class="col-span-2">Data de Entrada</div>
              <div class="col-span-1 text-right">Ficha</div>
            </div>
            
            <div class="divide-y divide-border-default/60">
              <OsCard
                v-for="(ordem, index) in ordens"
                :key="ordem.id"
                :ordem="ordem"
                :show-details="true"
                :is-list-item="true"
                class="hover:bg-light-bg/50 transition-colors"
              />
            </div>
          </div>
        </div>
        
        <div class="mt-8 flex items-center justify-between px-2">
          <p class="text-[12px] font-bold text-text-muted uppercase tracking-widest">{{ ordens.length }} Atendimentos Registrados</p>
          <div class="flex items-center gap-1">
            <button class="w-9 h-9 flex items-center justify-center rounded-md border border-border-default bg-white text-text-muted hover:border-text-light transition-colors disabled:opacity-30" disabled>
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="15 18 9 12 15 6"/></svg>
            </button>
            <button class="w-9 h-9 flex items-center justify-center rounded-md border border-border-default bg-white text-text-muted hover:border-text-light transition-colors disabled:opacity-30" disabled>
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"/></svg>
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import osService, { auth } from '@/api/osService';
import OsCard from '@/components/OsCard.vue';

const router = useRouter();

const ordens = ref([]);
const loading = ref(true);
const errorMessage = ref('');

const loadOrdens = async () => {
  loading.value = true;
  errorMessage.value = '';
  
  try {
    const response = await osService.listarHistorico();
    ordens.value = response.data;
  } catch (error) {
    console.error('Erro ao carregar histórico:', error);
    errorMessage.value = 'Não foi possível carregar seu histórico no momento. Verifique sua conexão.';
  } finally {
    loading.value = false;
  }
};

const handleLogout = () => {
  auth.logout();
  router.push('/login');
};

onMounted(() => {
  loadOrdens();
});
</script>

<style scoped>
</style>
